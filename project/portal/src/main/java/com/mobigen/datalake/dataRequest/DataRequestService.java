package com.mobigen.datalake.dataRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestMapper dataRequestMapper;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;

    @Value("${properties.activeSendMail.data-request}")
	public boolean activeSendMail;
    
    // 데이터요청 목록 전체개수 조회
    public int getDataRequestListTotal(Map<String, String> param) throws Exception {
        int total = dataRequestMapper.getDataRequestListTotal(param);
        return total;
    }
    
    // 데이터요청 목록 조회
    public List<Map> getDataRequestList(Map<String, String> param) throws Exception {
        List<Map> dataRequestList = (List<Map>) dataRequestMapper.getDataRequestList(param);
        return dataRequestList;
    }

    // 데이터요청 단건 조회
    public Map<String, Object> getDataRequestOne(String id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> dataRequest = dataRequestMapper.getDataRequestOne(id);
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);

        Boolean accessOk = false;
        
        // 작성자인지 확인
        if (dataRequest != null) {
            accessOk = isWriter((String) dataRequest.get("writer"), user);
        }

        // 작성자 또는 관리자 여부 확인
        if (!accessOk) {
            result.put("accessOk", false);
            return result;
        }

        List<Map> fileList = (List<Map>) fileMapper.getFileList(id, "file");
        List<Map> thumbList = (List<Map>) fileMapper.getFileList(id, "thumb");

        List<Map> answerList = (List<Map>) dataRequestMapper.getDataRequestAnswerList(id);
        result.put("answerList", answerList);
           
        result.put("dataRequest", dataRequest);
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

    // 데이터 요청 등록
    public Integer createDataRequest(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Integer result = 0;

        try {
            if(params.get("contents") != null){
                params.put("contents", sanitizeCustomHtml(params.get("contents").toString()));
            }
            
            result = dataRequestMapper.insertDataRequest(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("데이터 요청 등록에 실패하였습니다.");
        }

        // 메일발송기능 활성화 여부
        if (activeSendMail) {
            try {
                sendMail(params, request); // 데이터 요청 메일 발송
            } catch (Exception e) {
                e.printStackTrace();
                log.error("이메일 발송에 실패하였습니다.");
            }
        }

        return result;
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

    // 이메일 전송
    public void sendMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        // 관리자 이메일 조회
        List<Map> mailUserList = dataRequestMapper.getMailUserList();
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

            message.setSubject("[MOBIGEN DATA CATALOG 데이터 요청] " + params.get("data_name")); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
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
            throw new Exception("이메일 발송에 실패하였습니다.");
        }
    }

    // 답변 이메일 전송
    public void sendMailReply(Map<String, Object> params) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        // 게시글 정보
        Map<String, Object> dataRequest = (Map<String, Object>) params.get("dataRequest");

        // 관리자 이메일 조회
        List<Map> mailUserList = dataRequestMapper.getMailUserList();
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

            message.setSubject("[MOBIGEN DATA CATALOG 데이터 요청 답변] " + dataRequest.get("data_name")); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(params.get("writer_email").toString(), params.get("writer_user_nm").toString(), "UTF-8")); // 받는 사람
            message.addRecipients(Message.RecipientType.CC, toAddress); // 참조자

            // 데이터 요청 신청자 조회
            String userId = dataRequest.get("writer").toString();
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
            String htmlBody = makeHtmlMailBody(dataRequest, deptName + " " + userName + " " + jwName, "reply");
            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("이메일 발송에 실패하였습니다.");
        }
    }

    public String makeHtmlMailBody(Map<String, Object> params, String request_user_info, String mailType) throws Exception{
        String request_start = (params.get("request_start") != null && !params.get("request_start").toString().equals("")) ? params.get("request_start").toString().substring(0, 10) : "";
        String request_end = (params.get("request_end") != null && !params.get("request_end").toString().equals("")) ? params.get("request_end").toString().substring(0, 10) : "";

        // 신청일시
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String formattedDateTime = now.format(formatterDatetime);

        String logoDataImage = "data:image/png;base64,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";

        // 신청 분류 가공
        String classificationString = "";
        if (params.get("request_type") != null) {
            String request_type = params.get("request_type").toString();
            if (request_type.equals("0")) classificationString = "수집";
            if (request_type.equals("1")) classificationString = "권한";
            if (request_type.equals("2")) classificationString = "수집, 권한";
        } else {
            classificationString = "";
        }

        String requestMailBody = """
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MOBIGEN DATA CATALOG</title>
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
                                <img src="%s" alt="MOBIGEN" class="logo" style="width: 120px;">
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
            MOBIGEN DATA CATALOG
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
    <title>MOBIGEN DATA CATALOG</title>
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
                                <img src="%s" alt="MOBIGEN" class="logo" style="width: 120px;">
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
            MOBIGEN DATA CATALOG
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
                ((String) params.get("contents")).replaceAll(System.getProperty("line.separator"), "<br>")
                );
        }else if(mailType.equals("reply")){
            // Map<String, String> answer = (Map<String, String>)params.get("answer");

            resultMailBody = String.format(replyMailBody,
                logoDataImage,
                params.get("title"),
                params.get("process_status"),
                classificationString,
                params.get("data_name"),
                request_user_info,
                ((String) params.get("contents")).replaceAll(System.getProperty("line.separator"), "<br>"),
                request_start + " ~ " + request_end,
                ((String) params.get("benefit_content")).replaceAll(System.getProperty("line.separator"), "<br>"),
                "관리자 답변 필요"
                );
        }

        return resultMailBody;
    }

    // 데이터요청 삭제
    public Object deleteDataRequest(Map<String, Object> params) throws Exception {
        Integer result = 0;
        ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("deleteList");
        for (Integer id : deleteList) {
            result += dataRequestMapper.deleteDataRequest(id);
        }

        return result;
    }

    // 데이터요청 수정
    public Object updateDataRequest(Map<String, Object> params, List<String> delete_files) throws Exception {
        if(params.get("contents") != null){     
            params.put("contents", sanitizeCustomHtml(params.get("contents").toString()));
        }

        Integer result = dataRequestMapper.updateDataRequest(params);
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

    // 데이터요청 답변 저장
    public Object updateAdminReply(Map<String, Object> params, HttpServletRequest request) throws Exception {

        Map<String, Object> dataRequest = (Map<String, Object>) params.get("dataRequest");
        Map<String, Object> answer = (Map<String, Object>) params.get("answer");
        dataRequest.put("answer", (Map<String, Object>) params.get("answer"));
        if( "Y".equals((String) answer.get("newFlag")) || "Y".equals((String) answer.get("lastFlag")) ) {
            // 첫 답변이거나 마지막 답변인 경우 마스터 테이블 수정
            Integer result = dataRequestMapper.updateAdminReply(dataRequest);
        }

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);

        answer.put("writer", user.get("user_id"));
        Integer result = 0;
        if( "Y".equals((String) answer.get("newFlag"))) {
            result = dataRequestMapper.insertDataRequestAnswer(answer);
        } else {
            result = dataRequestMapper.updateDataRequestAnswer(answer);
        }

        // 메일발송기능 활성화 여부
        if (activeSendMail) {
            if( "Y".equals((String) answer.get("newFlag")) || "Y".equals((String) answer.get("lastFlag")) ) {
                // 첫 답변이거나 마지막 답변인 경우 이메일 발송
                Map writer = (Map) userMapper.getUserMaster((String) dataRequest.get("writer"));
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
        }

        return result;
    }

    // 데이터 요청 답글 삭제
    public Object deleteAdminReply(Map<String, Object> params) throws Exception {
        Integer result = 0;
        int id = (int) params.get("id");
        result = dataRequestMapper.deleteAdminReply(id);

        // 답글 유무에 따라 분류 설정
        int data_request_id = (int) params.get("data_request_id");
        int replyCount = dataRequestMapper.getDataRequestAnswerListCount(data_request_id);
        int latest_process_status = 5;
        if (replyCount > 0) {
            // 가장 마지막 답글에 대한 상태로 변경
            latest_process_status = dataRequestMapper.getLatestAnswerProcessStatus(data_request_id);
        }
        
        int updateResult = dataRequestMapper.updateDataRequestStatus(data_request_id, latest_process_status);

        return result;
   }
}
