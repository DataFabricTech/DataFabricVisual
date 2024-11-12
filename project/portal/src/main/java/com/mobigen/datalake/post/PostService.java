package com.mobigen.datalake.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;    
    
    public List<Map> getPost(String type, String word, String postId) throws Exception {
        List<Map> post = (List<Map>) postMapper.getPost(type, word, postId);
        return post;
    }

    public List<Map> getAllPostID() throws Exception {
        List<Map> postID = (List<Map>) postMapper.getAllPostID();
        return postID;
    }

    // FAQ 전체 조회
    public List<Map> getAllPost(String postType) throws Exception {
        List<Map> postList = (List<Map>) postMapper.getAllPost(postType);
        return postList;
    }

    // 공지사항, 데이터요청, FAQ 목록 전체개수 조회
    public int getPostListTotal(Map<String, String> param) throws Exception {
        int total = postMapper.getPostListTotal(param);
        return total;
    }
    
    // 공지사항, 데이터요청, FAQ 목록 조회
    public List<Map> getPostList(Map<String, String> param) throws Exception {
        List<Map> post = (List<Map>) postMapper.getPostList(param);
        return post;
    }

    // 공지사항, 데이터요청, FAQ 단건 조회
    public Map<String, Object> getPostOne(String postId, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        postMapper.updateViewCount(postId);
        Map<String, Object> post = postMapper.getPostOne(postId);

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);

        Boolean accessOk = false;
        
        
        
        if (post != null) {
            // 데이터 요청인 경우 권한 확인
            if ("data-request".equals((String) post.get("post_type"))) {
                accessOk = isWriter((String) post.get("writer"), user);
            } else {
                accessOk = true;
            }
        }

        // 작성자 또는 관리자 여부 확인
        if (!accessOk) {
            result.put("accessOk", false);
            return result;
        }

        List<Map> fileList = (List<Map>) fileMapper.getFileList(postId, "file");
        List<Map> thumbList = (List<Map>) fileMapper.getFileList(postId, "thumb");

        if("data-request".equals( (String) post.get("post_type")) ) {//답변목록조회
            List<Map> answerList = (List<Map>) postMapper.getPostAnswerList(postId);
            result.put("answerList", answerList);
        }        
        result.put("post", post);
        result.put("fileList", fileList);
        result.put("thumbList", thumbList);
        return result;

    
    }

    // 데이터 요청 url 접근 시 작성자 여부 확인
    public Boolean isWriter(String writer, Map<String, Object> user) throws Exception {
        String logonId = user.get("user_id").toString();
        String roleName = user.get("authority").toString();

        Boolean result = false;

        // 관리자인 경우, 작성자인 경우 true
        if (roleName.equals("Admin")
         || roleName.equals("Super-Admin")
         || logonId.equals(writer)) {
            result = true;
        }

        return result;
    }

    // 공지사항, 데이터요청, FAQ 저장
    public Integer createPost(Map<String, Object> params, HttpServletRequest request) throws Exception {
        if(params.get("post_content") != null){
            params.put("post_content", sanitizeCustomHtml(params.get("post_content").toString()));
        }
        
        Integer result = postMapper.insertPost(params);
        if("data-request".equals((String) params.get("post_type"))) {//데이터요청인경우 메일발송
            sendMail(params, request);
        }
        return result;
    }

    // 공지사항, 데이터요청, FAQ 수정
    public Object updatePost(Map<String, Object> params, List<String> delete_files) throws Exception {
        if(params.get("post_content") != null){     
            params.put("post_content", sanitizeCustomHtml(params.get("post_content").toString()));
        }

        Integer result = postMapper.updatePost(params);
        if ( !ObjectUtils.isEmpty(delete_files) ) {
            for (String item : delete_files) {
                fileMapper.deleteFile(item);
            }
        }
        return result;
    }

    // 활용사례 썸네일 삭제
    public void deleteThumb(Map<String, Object> params) throws Exception {
        fileMapper.deleteThumb((Integer) params.get("id"));                
        return;
    }

    // 이메일 전송
    public void sendMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        // 관리자 이메일 조회
        List<Map> mailUserList = postMapper.getMailUserList();
        Map<String, Object> map = new HashMap<>();

        // 이메일이 있는 사용자만 발송
        for (Map mailUser : mailUserList) {
            if (!ObjectUtils.isEmpty(mailUser.get("user_email"))) {
                map.put(mailUser.get("user_id").toString(), mailUser);
            }
        }

        if (map.size() == 0) return;
        try {
            InternetAddress[] toAddress = new InternetAddress[map.size()];

            int i = 0;
            for (String key : map.keySet()) {
                toAddress[i] = new InternetAddress(((Map<String, String>)map.get(key)).get("user_email").toString(), ((Map<String, String>)map.get(key)).get("user_nm").toString(), "UTF-8");
                i++;
            }

            message.setSubject("[BIGS DATA PORTAL 데이터 요청] " + params.get("data_name")); // 메일 제목
            message.setFrom("rotem_portal@hyundai-rotem.co.kr"); // 보낸 사람
            message.addRecipients(Message.RecipientType.TO, toAddress); // 받는 사람

            // 데이터 요청 신청자 조회
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            String userId = user.get("user_id").toString();
            Map<String, Object> userinfo = (Map<String, Object>) userMapper.getUserMaster(userId);
            String userName = (String) userinfo.get("user_nm");
            String deptName = (String) userinfo.get("dept_nm");
            String jwName = (String) userinfo.get("jw_nm");

            if (deptName == null) {
                deptName = "부서없음";
            }

            if (jwName == null) {
                jwName = "직급없음";
            }

            //메일 본문
            String htmlBody = makeHtmlMailBody(params, deptName + " " + userName + " " + jwName, "request");

            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    // 공지사항, 데이터요청, FAQ 삭제
    public Object deletePost(Map<String, Object> params) throws Exception {
        Integer result = 0;
        String postType = (String) params.get("post_type");
        ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("deleteList");
        for (Integer id : deleteList) {
            result += postMapper.deletePost(id, postType);
        }

        return result;
    }

    // FAQ 순서 저장
    public Object updatePostFaqOrdNo(Map<String, Object> params) throws Exception {
        Integer result = 0;
        ArrayList<HashMap<String, Object>> postList = (ArrayList<HashMap<String, Object>>) params.get("postList");
        for (HashMap<String, Object> item : postList) {
            if(item.get("post_content") != null){                
                item.put("post_content", sanitizeCustomHtml(item.get("post_content").toString()));
            }

            result += postMapper.updatePostFaqOrdNo(item);
        }

        return result;
    }

    // 데이터요청 답변 저장
    public Object updateAdminReply(Map<String, Object> params, HttpServletRequest request) throws Exception {

        Map<String, Object> post = (Map<String, Object>) params.get("post");
        Map<String, Object> answer = (Map<String, Object>) params.get("answer");
        post.put("answer", (Map<String, Object>) params.get("answer"));
        if( "Y".equals((String) answer.get("newFlag")) || "Y".equals((String) answer.get("lastFlag")) ) {
            // 첫 답변이거나 마지막 답변인 경우 마스터 테이블 수정
            Integer result = postMapper.updateAdminReply(post);
        }

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);

        answer.put("writer", user.get("user_id"));
        Integer result = 0;
        if( "Y".equals((String) answer.get("newFlag"))) {
            result = postMapper.insertPostAnswer(answer);
        } else {
            result = postMapper.updatePostAnswer(answer);
        }
        if( "Y".equals((String) answer.get("newFlag")) || "Y".equals((String) answer.get("lastFlag")) ) {
            // 첫 답변이거나 마지막 답변인 경우 이메일 발송
            Map writer = (Map) userMapper.getUserMaster((String) post.get("writer"));
            String strUserEmail = (String) writer.get("user_email");
            if (strUserEmail != null && !"".equals(strUserEmail)) {
                // String strUserEmailDecode = Crypto.AES_Decode(strUserEmail); // 인코딩 되어있는 경우
                String strUserEmailDecode = strUserEmail;
                if (strUserEmailDecode != null && !"".equals(strUserEmailDecode)) {
                    params.put("writer_email", strUserEmailDecode);
                    params.put("writer_user_nm", (String) writer.get("user_nm"));
                    sendMailReply(params);
                }
            }
        }

        return result;
    }

    // 답변 이메일 전송
    public void sendMailReply(Map<String, Object> params) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        // 게시글 정보
        Map<String, Object> post = (Map<String, Object>) params.get("post");

        // 관리자 이메일 조회
        List<Map> mailUserList = postMapper.getMailUserList();
        Map<String, Object> map = new HashMap<>();

        // 이메일이 있는 사용자만 발송
        for (Map mailUser : mailUserList) {
            if (!ObjectUtils.isEmpty(mailUser.get("user_email"))) {
                map.put(mailUser.get("user_id").toString(), mailUser);
            }
        }

        if (map.size() == 0) return;
        try {
            InternetAddress[] toAddress = new InternetAddress[map.size()];

            int i = 0;
            for (String key : map.keySet()) {
                toAddress[i] = new InternetAddress(((Map<String, String>)map.get(key)).get("user_email").toString(), ((Map<String, String>)map.get(key)).get("user_nm").toString(), "UTF-8");
                i++;
            }

            message.setSubject("[BIGS DATA PORTAL 데이터 요청 답변] " + post.get("data_name")); // 메일 제목
            message.setFrom("rotem_portal@hyundai-rotem.co.kr"); // 보낸 사람
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(params.get("writer_email").toString(), params.get("writer_user_nm").toString(), "UTF-8")); // 받는 사람
            message.addRecipients(Message.RecipientType.CC, toAddress); // 참조자

            // 데이터 요청 신청자 조회
            String userId = post.get("writer").toString();
            Map<String, Object> userinfo = (Map<String, Object>) userMapper.getUserMaster(userId);
            String userName = (String) userinfo.get("user_nm");
            String deptName = (String) userinfo.get("dept_nm");
            String jwName = (String) userinfo.get("jw_nm");

            if (deptName == null) {
                deptName = "부서없음";
            }

            if (jwName == null) {
                jwName = "직급없음";
            }

            //메일 본문
            String htmlBody = makeHtmlMailBody(post, deptName + " " + userName + " " + jwName, "reply");
            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 데이터 요청 답글 삭제
    public Object deleteAdminReply(Map<String, Object> params) throws Exception {
        Integer result = 0;
        int answer_id = (int)params.get("answer_id");
        result = postMapper.deleteAdminReply(answer_id);

        return result;
   }

    public String makeHtmlMailBody(Map<String, Object> params, String request_user_info, String mailType) throws Exception{
        String request_start = (params.get("request_start") != null && !params.get("request_start").toString().equals("")) ? params.get("request_start").toString().substring(0, 10) : "";
        String request_end = (params.get("request_end") != null && !params.get("request_end").toString().equals("")) ? params.get("request_end").toString().substring(0, 10) : "";

        // 신청일시
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String formattedDateTime = now.format(formatterDatetime);

        String logoDataImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAAsCAMAAACZpYwBAAAAP1BMVEVHcEwbHFcJI2gJI2kJI2kIJGkGIGkKJGgJJGkIJGgII2gIImgJI2iWDDB+DDWlCSrTAhgJImm5BiIKJGnmABI0w7dJAAAAE3RSTlMAD5rx1z8ngMBAYEywrS3R/nDwOs9O9gAAAk1JREFUWMPlmOFygyAMgAkgAWq3ufT9n3UgBNHqppO6H8udhUsKX0kgwQrx/4QofiJZR+RCLzaWkE2URJvwlSTBNJTujdRpsJCkQ0+TFM/goEDS1lpNBJWyCRh0mh7m4GTG8RmVrlKeAndBZAAHLyunor8vAicJ4OBsol7sAacfexJsx9BFMChSsAu8EeP7+29iPDU/gzGIXYDh/vH4PAuWwZW3JTgqOyKzGuOIfZwH59M7BydRYgWcsIfAODrQoa8aj0nMZHbhWVEaKSvsIfB5eSvYi8H3gn28v/0J+FpsATfG4iTdd+Dmq6VK9G0D/PECJ9NMVsn3l8R2Di750YUctjECYn6DpdZ1ZmzN5rhVcLl0uDG/6FzQ/Ji/aKoZk03jNNwKr2IDAmJOVHgELDTPDXpCyQW4tmkoYM8ZWH8TsS2wZbCsfY9z8MxmebhWWdNXlt3gNKcXJq81TaLA27SMcCHz2dZngFlukSLHXW2EZx9r9hqWAA+8np5V+VdiWrPC/jA48foKUyafwPik4upaFPoYOLuzB3Y5rlG2wJYVjrfKsXMsnag32SZYxntgS3A4m7AHXJ/rs+Ds6hjja8G8ucKuvhqct+OwJ8Yui2kCZqDds6tNLBRdG7A8AsaGrsa/Bv+cQORrwGVym6tG1mhR9foXgFMFUkOugFAOkS7VSWabbwouL1a88vD+VM7tzBbfeFuCn24ZoFZvIMqItmABubqm3B0vcH3JVIAZraQRp8ApB8XZORmNKIcD97Oisk19wYPSaODOP/xbT3wBvRl7yPlk+ocAAAAASUVORK5CYII=";

        // 신청 분류 가공
        String classificationString = "";
        if (params.get("category") != null) {
            String[] categoryList = params.get("category").toString().split(",");
            for (int i=0; i<categoryList.length; i++) {
                if (categoryList[i].contains("수집")) classificationString += " 데이터 수집 ";
                if (categoryList[i].contains("권한")) classificationString += " 데이터 권한 ";
            }
        } else {
            classificationString = "";
        }

        String requestMailBody = """
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BIGS DATA PORTAL</title>
    <style>
        body {
            font-family:"맑은 고딕";
            color:black;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: auto;
        }
        .container {
            width: 600px; background-color: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; border-radius: 8px;
        }
        h1 {
            text-align: center; font-size: 24px; margin-bottom: 30px;
        }
        table {
            width: 100%%;
            border-collapse: collapse;
            margin-bottom: 20px;
            border-top: 2px solid #333; /* 테이블 상단 굵은 선 */
        }
        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd; /* 하단에만 선 */
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
            font-weight: 700;
            width: 30%%;
            text-align: center;
        }
        td {
            color: #333;
            font-size: 14px;
        }
        th:first-child, td:first-child {
            border-left: none; /* 왼쪽 선 제거 */
        }
        th:last-child, td:last-child {
            border-right: none; /* 오른쪽 선 제거 */
        }
        .highlight {
            color: #007bff;
            font-weight: bold;
            text-decoration: none;
            display: block;
            margin-top: 10px;
        }
        .footer {
            font-size: 14px;
            color: #555;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <table>
            <tbody>
                <tr>
                    <td>
                        <p style="text-align: center;">
                            <span>
                                <img src="%s" alt="HYUNDAI ROTEM" class="logo" style="width: 120px;">
                            </span>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h1>%s</h1>
                        <table>
                            <tr>
                                <th>신청&nbsp; 분류</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청 데이터명</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신&nbsp;&nbsp; 청&nbsp;&nbsp; 자</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청&nbsp; 일시</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요청&nbsp; 기간</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청&nbsp; 내용</th>
                                <td>%s</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="footer" style="text-align: center; margin-top: 10px; color: #0073e6; font-weight: bold;">
            BIGS DATA PORTAL
        </div>
    </div>
</body>
</html>
""";

        String replyMailBody = """
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BIGS DATA PORTAL</title>
    <style>
        body {
            font-family:"맑은 고딕";
            color:black;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: auto;
        }
        .container {
            width: 600px; background-color: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; border-radius: 8px;
        }
        h1 {
            text-align: center; font-size: 24px; margin-bottom: 30px;
        }
        table {
            width: 100%%;
            border-collapse: collapse;
            margin-bottom: 20px;
            border-top: 2px solid #333; /* 테이블 상단 굵은 선 */
        }
        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd; /* 하단에만 선 */
            text-align: left;
        }
        th {
            font-size: 14px;
            background-color: #f4f4f4;
            font-weight: 700;
            width: 30%%;
            text-align: center;
        }
        td {
            color: #333;
            font-size: 14px;
        }
        th:first-child, td:first-child {
            border-left: none; /* 왼쪽 선 제거 */
        }
        th:last-child, td:last-child {
            border-right: none; /* 오른쪽 선 제거 */
        }
        .highlight {
            color: #007bff;
            font-weight: bold;
            text-decoration: none;
            display: block;
            margin-top: 10px;
        }
        .footer {
            font-size: 14px;
            color: #555;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <table>
            <tbody>
                <tr>
                    <td>
                        <p style="text-align: center;">
                            <span>
                                <img src="%s" alt="HYUNDAI ROTEM" class="logo" style="width: 120px;">
                            </span>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h1>%s</h1>
                        <table>
                            <tr>
                                <th>진행&nbsp; 상태</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청&nbsp; 분류</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청 데이터명</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신&nbsp;&nbsp; 청&nbsp;&nbsp; 자</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>신청&nbsp; 내용</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요청&nbsp; 기간</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>활용&nbsp; 목적</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요청&nbsp; 답변</th>
                                <td>%s</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <div class="footer" style="text-align: center; margin-top: 10px; color: #0073e6; font-weight: bold;">
            BIGS DATA PORTAL
        </div>
    </div>
</body>
</html>
    """;                 

    // 신청자 스타일 지정
    String[] ruiArr = request_user_info.split(" ");
    ruiArr[0] = "<span style=\"color: #1F497D\">" + ruiArr[0] + "</span>";
    ruiArr[2] = "<span style=\"color: #1F497D\">" + ruiArr[2] + "</span>";
    request_user_info = String.join(" ", ruiArr);

        String resultMailBody = "";
        if(mailType.equals("request")){
            resultMailBody = String.format(requestMailBody,
                logoDataImage,
                params.get("title"),
                classificationString,
                params.get("data_name"),
                request_user_info,
                formattedDateTime,
                request_start + " ~ " + request_end,
                ((String) params.get("post_content")).replaceAll(System.getProperty("line.separator"), "<br>")
                );
        }else if(mailType.equals("reply")){
            Map<String, String> answer = (Map<String, String>)params.get("answer");

            resultMailBody = String.format(replyMailBody,
                logoDataImage,
                params.get("title"),
                params.get("process_status"),
                classificationString,
                params.get("data_name"),
                request_user_info,
                ((String) params.get("post_content")).replaceAll(System.getProperty("line.separator"), "<br>"),
                request_start + " ~ " + request_end,
                ((String) params.get("benefit_content")).replaceAll(System.getProperty("line.separator"), "<br>"),
                answer.get("admin_reply").replaceAll(System.getProperty("line.separator"), "<br>")
                );
        }

        return resultMailBody;
    }

    /**
     * 커스터마이징한 Safelist 사용 예시
     */
    public String sanitizeCustomHtml(String htmlContent) {
        // 허용할 태그와 속성을 지정한 커스텀 Safelist
        // 기본 Safelist에 태그와 속성 추가
        Safelist safelist = Safelist.basic()
            // 텍스트 스타일링 태그
            .addTags("h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "p", "pre", "code", "ul", "ol", "li", "br", "hr")
            // 인라인 태그
            .addTags("b", "i", "strong", "em", "u", "strike", "a", "span", "small", "sub", "sup", "mark", "abbr")
            // 테이블 태그
            .addTags("table", "thead", "tbody", "tr", "th", "td")
            // 이미지 및 미디어 관련 태그
            // .addTags("img", "figure", "figcaption", "iframe", "embed")
            .addTags("img", "figure", "figcaption")
            // 허용할 속성
            .addAttributes("a", "href", "title")
            .addAttributes("img", "src", "alt", "title", "width", "height")
            .addAttributes("figure", "width", "height")
            .addAttributes(":all", "style", "class") // 모든 태그에 대해 class, style 속성 허용
            // .addAttributes("iframe", "src", "width", "height", "allowfullscreen", "frameborder")
            // .addAttributes("embed", "src", "type", "width", "height", "allowfullscreen", "frameborder")
            // URL 관련 프로토콜 제한 (보안 강화)
            .addProtocols("a", "href", "http", "https", "mailto");
            //   .addProtocols("img", "src", "http", "https");
            // .addProtocols("iframe", "src", "http", "https")
            // .addProtocols("embed", "src", "http", "https");

        // Jsoup을 사용하여 허용된 태그만 남기고 정리
        return Jsoup.clean(htmlContent, safelist);
    }
}
