package com.mobigen.datalake.dataRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.datalake.system.mailTemplate.MailTemplateService;
import com.mobigen.datalake.system.user.AdminService;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
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
    private final AdminService adminService;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;
    private final MailTemplateService mailTemplateService;

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
            
            // 신청일시
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formattedDateTime = now.format(formatterDatetime);
            if (params.get("create_date") == null) params.put("create_date", now);

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

    // 데이터 요청 이메일 전송
    public void sendMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            // 1. 관리자 이메일 기본값 조회
            List<Map<String, Object>> defaultAdminList = dataRequestMapper.getDefaultAdminList();
            Map<String, Object> mailDefaultAdminMap = new HashMap<>();
            for (Map default_admin : defaultAdminList) {
                if (!ObjectUtils.isEmpty(default_admin.get("user_email"))) {
                    mailDefaultAdminMap.put("default_admin", default_admin);
                    break;
                }
            }

            // 2. 참조 관리자 조회
            List<Map> adminList = dataRequestMapper.getMailUserList();
            Map<String, Object> mailAdminMap = new HashMap<>();
            for (Map admin : adminList) {
                if (!ObjectUtils.isEmpty(admin.get("user_email"))) {
                    mailAdminMap.put(admin.get("user_id").toString(), admin);
                }
            }

            // 받는 사람이 없는 경우 이메일 발송 X
            if (mailDefaultAdminMap.size() == 0 && mailAdminMap.size() == 0) {
                log.info("No Email Recipient");
                return;
            }
            
            // 3. 메일 발송 파라미터
            List<Map<String, Object>> sender = new ArrayList<>();
            List<Map<String, Object>> recipients = new ArrayList<>();
            List<Map<String, Object>> ccRecipients = new ArrayList<>();

            // 발신자
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            Map<String, Object> senderMap = new HashMap<>();
            senderMap.put("name", user.get("username"));
            senderMap.put("email", user.get("user_email"));
            sender.add(senderMap);

            // 수신자
            for (String key : mailDefaultAdminMap.keySet()) {
                Map<String, Object> recipientsMap = new HashMap<>();
                // 기본 관리자 계정 중 하나만 이용
                if (((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_email") != null) {
                    recipientsMap.put("name", ((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_nm").toString());
                    recipientsMap.put("email", ((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_email").toString());
                    recipients.add(recipientsMap);
                    break;
                }
            }

            // 참조자
            for (String key : mailAdminMap.keySet()) {
                Map<String, Object> ccRecipientsMap = new HashMap<>();
                ccRecipientsMap.put("name", ((Map<String, String>)mailAdminMap.get(key)).get("user_nm").toString());
                ccRecipientsMap.put("email", ((Map<String, String>)mailAdminMap.get(key)).get("user_email").toString());
                ccRecipients.add(ccRecipientsMap);
            }

            // 메일 전송 파라미터
            Map<String, Object> mailTemplateParam = new HashMap<>();
            mailTemplateParam.put("_title_", params.get("title"));
            
            // 0:수집 1:권한 2:수집, 권한
            String category = "-";
            if (params.get("request_type") != null) {
                Integer request_type = Integer.parseInt(params.get("request_type").toString());
                if (request_type == 0) category = "수집";
                if (request_type == 1) category = "권한";
                if (request_type == 2) category = "수집,권한";
            }
            mailTemplateParam.put("_category_", category);
            mailTemplateParam.put("_data_name_", params.get("data_name") != null ? params.get("data_name").toString() : "-");

            String writer = "";
            String user_nm = user.get("username") != null ? user.get("username").toString() : "-";
            String dept_nm = user.get("dept_nm") != null ? user.get("dept_nm").toString() : "-";
            writer = user_nm + " (" + dept_nm + ")";
            mailTemplateParam.put("_writer_", writer);

            // 신청일시
            LocalDateTime create_date = params.get("create_date") != null ? (LocalDateTime) params.get("create_date") : null;
            if (create_date != null) {
                DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                String formattedDateTime = create_date.format(formatterDatetime);
                mailTemplateParam.put("_create_date_", formattedDateTime);
            } else {
                mailTemplateParam.put("_create_date_", "-");
            }
            
            String request_start = (params.get("request_start") != null && !params.get("request_start").toString().equals("")) ? params.get("request_start").toString().substring(0, 10) : "-";
            mailTemplateParam.put("_request_start_", request_start);
            
            String request_end = (params.get("request_end") != null && !params.get("request_end").toString().equals("")) ? params.get("request_end").toString().substring(0, 10) : "-";
            mailTemplateParam.put("_request_end_", request_end);
            mailTemplateParam.put("_contents_", params.get("contents") != null ? params.get("contents").toString() : "-");
            mailTemplateParam.put("_benefit_content_", params.get("benefit_content") != null ? params.get("benefit_content").toString() : "-");
            mailTemplateParam.put("add_sender", sender);
            mailTemplateParam.put("add_recipients", recipients);
            mailTemplateParam.put("add_cc_recipients", ccRecipients);

            // 안내 메일 발송
            // 메일 템플릿 ID 조회
            Map<String, Object> commonCodeParam = new HashMap<>();
            commonCodeParam.put("type", "email_template");
            commonCodeParam.put("name", "DATA_REQUEST");
            List<Map<String, Object>> commonCodeList = adminService.getCommonCodeList(commonCodeParam);

            String templateId = "";
            if (commonCodeList != null && commonCodeList.size() > 0) {
                templateId = commonCodeList.get(0).get("value").toString();
            }
            if(templateId == null || templateId.isEmpty() || templateId.equals("")) {
                log.error("이메일 발송 실패");
                return;
            }

            // 메일 발송
            Map<String, Object> mailTemplateResult = mailTemplateService.sendMail(templateId, mailTemplateParam);
            if(mailTemplateResult != null && mailTemplateResult.get("result").equals("fail")){
                log.error("이메일 발송 실패");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("이메일 발송에 실패하였습니다.");
        }
    }

    // 데이터 요청 답변 이메일 전송
    public void sendMailReply(Map<String, Object> params, HttpServletRequest request) throws Exception {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            // 데이터 요청 파라미터 설정
            Map<String, Object> dataRequest = (Map<String, Object>) params.get("dataRequest");
            if (dataRequest != null) {
                params.put("title", dataRequest.get("title"));
                params.put("request_type", dataRequest.get("request_type"));
                params.put("data_name", dataRequest.get("data_name"));
                params.put("writer", dataRequest.get("writer"));
                params.put("create_date", dataRequest.get("create_date"));
                params.put("request_start", dataRequest.get("request_start"));
                params.put("request_end", dataRequest.get("request_end"));
                params.put("contents", dataRequest.get("contents"));
                params.put("process_status", dataRequest.get("process_status"));
                params.put("benefit_content", dataRequest.get("benefit_content"));
            } else {
                log.error("데이터 요청 내역 확인 실패");
                return;
            }

            // 답변 파라미터 설정
            Map<String, Object> answer = (Map<String, Object>) params.get("answer");
            if (answer != null) {
                params.put("admin_reply", answer.get("admin_reply"));
            } else {
                log.error("데이터 요청 답변 확인 실패");
                return;
            }

            // 참조 관리자 조회
            List<Map> adminList = dataRequestMapper.getMailUserList();
            Map<String, Object> mailAdminMap = new HashMap<>();
            for (Map admin : adminList) {
                if (!ObjectUtils.isEmpty(admin.get("user_email"))) {
                    mailAdminMap.put(admin.get("user_id").toString(), admin);
                }
            }
            
            // 메일 발송 파라미터
            List<Map<String, Object>> sender = new ArrayList<>();
            List<Map<String, Object>> recipients = new ArrayList<>();
            List<Map<String, Object>> ccRecipients = new ArrayList<>();

            // 발신자
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            Map<String, Object> senderMap = new HashMap<>();
            senderMap.put("name", user.get("username"));
            senderMap.put("email", user.get("user_email"));
            sender.add(senderMap);

            // 수신자
            String writer_email = params.get("writer_email") != null ? params.get("writer_email").toString() : "";
            String writer_user_nm = params.get("writer_user_nm") != null ? params.get("writer_user_nm").toString() : "-";
            if (writer_email.equals("")) { // 수신자 없는 경우 이메일 발송 실패
                log.error("이메일 발송 실패");
                return;
            }
            
            Map<String, Object> recipientsMap = new HashMap<>();
            recipientsMap.put("name", writer_user_nm);
            recipientsMap.put("email", writer_email);
            recipients.add(recipientsMap);

            // 참조자
            for (String key : mailAdminMap.keySet()) {
                Map<String, Object> ccRecipientsMap = new HashMap<>();
                ccRecipientsMap.put("name", ((Map<String, String>)mailAdminMap.get(key)).get("user_nm").toString());
                ccRecipientsMap.put("email", ((Map<String, String>)mailAdminMap.get(key)).get("user_email").toString());
                ccRecipients.add(ccRecipientsMap);
            }

            // 메일 전송 파라미터
            Map<String, Object> mailTemplateParam = new HashMap<>();
            mailTemplateParam.put("_title_", params.get("title"));
            
            // 0:수집 1:권한 2:수집, 권한
            String category = "-";
            if (params.get("request_type") != null) {
                Integer request_type = Integer.parseInt(params.get("request_type").toString());
                if (request_type == 0) category = "수집";
                if (request_type == 1) category = "권한";
                if (request_type == 2) category = "수집,권한";
            }
            mailTemplateParam.put("_category_", category);
            mailTemplateParam.put("_data_name_", params.get("data_name") != null ? params.get("data_name").toString() : "-");

            String writer = "";
            String user_nm = user.get("username") != null ? user.get("username").toString() : "-";
            String dept_nm = user.get("dept_nm") != null ? user.get("dept_nm").toString() : "-";
            writer = user_nm + " (" + dept_nm + ")";
            mailTemplateParam.put("_writer_", writer);

            // 신청일시
            String create_date = params.get("create_date") != null ? params.get("create_date").toString() : "-";
            mailTemplateParam.put("_create_date_", create_date);
            
            String request_start = (params.get("request_start") != null && !params.get("request_start").toString().equals("")) ? params.get("request_start").toString().substring(0, 10) : "-";
            mailTemplateParam.put("_request_start_", request_start);
            
            String request_end = (params.get("request_end") != null && !params.get("request_end").toString().equals("")) ? params.get("request_end").toString().substring(0, 10) : "-";
            mailTemplateParam.put("_request_end_", request_end);
            mailTemplateParam.put("_contents_", params.get("contents") != null ? params.get("contents").toString() : "-");
            mailTemplateParam.put("_benefit_content_", params.get("benefit_content") != null ? params.get("benefit_content").toString() : "-");

            // 진행상태 스타일 지정 0: 신청, 1. 검토중, 2. 진행중, 3. 제공, 4. 미제공, 5. 없음
            String process_status = params.get("process_status") != null ? params.get("process_status").toString() : "";
            String styledStatus = "";
            if (!process_status.equals("")) {
                if (process_status.equals("0")) {
                    styledStatus = "<em style=\"display:inline-flex; align-items:center; justify-content:center; padding: 0 5px 0 5px; height:24px; border-radius:12px; font-size:13px; background-color:#f5edd5; color:#e49217;\">"
                    + "신청" 
                    + "</em>";
                }
                else if (process_status.equals("1")) {
                    styledStatus = "<em style=\"display:inline-flex; align-items:center; justify-content:center; padding: 0 5px 0 5px; height:74px; border-radius:12px; font-size:13px; background-color:#e3e8f6; color:#2855ce;\">"
                    + "검토중" 
                    + "</em>";
                }
                else if (process_status.equals("2")) {
                    styledStatus = "<em style=\"display:inline-flex; align-items:center; justify-content:center; padding: 0 5px 0 5px; height:24px; border-radius:12px; font-size:13px; background-color:#e0e1f6; color:#4e39f1;\">"
                    + "진행중" 
                    + "</em>";
                }
                else if (process_status.equals("3")) {
                    styledStatus = "<em style=\"display:inline-flex; align-items:center; justify-content:center; padding: 0 5px 0 5px; height:24px; border-radius:12px; font-size:13px; background-color:#f9e4e6; color:#e92c3b;\">"
                    + "제공" 
                    + "</em>";
                }
                else if (process_status.equals("4")) {
                    styledStatus = "<em style=\"display:inline-flex; align-items:center; justify-content:center; padding: 0 5px 0 5px; height:24px; border-radius:12px; font-size:13px; background-color:#eaeaee; color:#656d78;\">"
                    + "미제공" 
                    + "</em>";
                } else {
                    styledStatus = "-";
                }
            } else {
                styledStatus = "-";
            }
            
            mailTemplateParam.put("_process_status_", styledStatus);
            mailTemplateParam.put("_admin_reply_", params.get("admin_reply") != null ? params.get("admin_reply").toString() : "-");
            mailTemplateParam.put("add_sender", sender);
            mailTemplateParam.put("add_recipients", recipients);
            mailTemplateParam.put("add_cc_recipients", ccRecipients);

            // 안내 메일 발송
            // 메일 템플릿 ID 조회
            Map<String, Object> commonCodeParam = new HashMap<>();
            commonCodeParam.put("type", "email_template");
            commonCodeParam.put("name", "DATA_REQUEST_RESPONSE");
            List<Map<String, Object>> commonCodeList = adminService.getCommonCodeList(commonCodeParam);

            String templateId = "";
            if (commonCodeList != null && commonCodeList.size() > 0) {
                templateId = commonCodeList.get(0).get("value").toString();
            }
            if(templateId == null || templateId.isEmpty() || templateId.equals("")) {
                log.error("이메일 템플릿 조회 실패");
                return;
            }

            // 메일 발송
            Map<String, Object> mailTemplateResult = mailTemplateService.sendMail(templateId, mailTemplateParam);
            if(mailTemplateResult != null && mailTemplateResult.get("result").equals("fail")){
                log.error("이메일 발송 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("이메일 발송에 실패하였습니다.");
        }
    }

    // 데이터요청 삭제
    public Object deleteDataRequest(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        int deleteCnt = 0;
        int deleteReplyCnt = 0;
        ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("deleteList");
        try {
            for (Integer request_id : deleteList) {
                // 실제 파일 삭제 처리
                deleteRealFileFromRequestId(request_id);
                deleteCnt += dataRequestMapper.deleteDataRequest(request_id);
                deleteReplyCnt += dataRequestMapper.deleteAdminReplyFromDataRequest(request_id);
            }

        } catch (Exception e) {
            log.error("Fail to delete dataRequest..");
        }

        result.put("deleteCnt", deleteCnt);
        result.put("deleteCnt", deleteReplyCnt);

        return result;
    }

    // 데이터요청 수정
    public Object updateDataRequest(Map<String, Object> params, List<String> delete_files) throws Exception {
        if(params.get("contents") != null){     
            params.put("contents", sanitizeCustomHtml(params.get("contents").toString()));
        }

        Integer result = dataRequestMapper.updateDataRequest(params);
        if ( !ObjectUtils.isEmpty(delete_files) ) {
            for (String f_id : delete_files) {
                // 실제 파일 삭제 처리
                deleteRealFileFromFileId(Integer.parseInt(f_id));
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
            try {
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
                            sendMailReply(params, request);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("이메일 발송에 실패하였습니다.");
            }
            
        }

        return result;
    }

    // 데이터 요청 답글 삭제
    public Object deleteAdminReply(Map<String, Object> params) throws Exception {
        Integer result = 0;
        int id = (int) params.get("id");
        result = dataRequestMapper.deleteAdminReply(id); // 답글 삭제 처리

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

   // 실제 파일 삭제
    private void deleteRealFileFromRequestId(Integer request_id) throws Exception {
        List<Map<String, Object>> fileInfoList = new ArrayList<>();
        // 해당 파일의 첨부파일이 있는지 확인하여 있는 경우 첨부파일도 삭제 처리
        try {
            fileInfoList = dataRequestMapper.getDataRequestFileInfoList(request_id);
        } catch (Exception e) {
            log.error("Fail to load FileInfoList");
        }
        if (fileInfoList != null && fileInfoList.size() > 0) {
            try {
                for (int i=0; i<fileInfoList.size(); i++) {
                    String file_id = fileInfoList.get(i).get("file_id").toString();
                    String file_location = fileInfoList.get(i).get("file_location").toString();
                    log.info("deleting real file id : " + file_id);

                    File file = new File(file_location);

                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            log.info("Success to delete file in server..");
                            
                            // DB에서 내역 삭제
                            try {
                                fileMapper.deleteFile(file_id);
                            } catch (Exception e) {
                                log.error("Fail to delete file info..");
                            }
                        } else {
                            log.error("Fail to delete file in server..");
                        }
                    } else {
                        log.info("No Exist file in server..");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Fail to delete file in server..");
            }
        } else {
            log.error("No fileInfo..");
        }
    }

    // 실제 파일 삭제
    private void deleteRealFileFromFileId(Integer f_id) throws Exception {
        Map<String, Object> fileInfo = new HashMap<>();
        // 해당 파일의 첨부파일이 있는지 확인하여 있는 경우 첨부파일도 삭제 처리
        try {
            fileInfo = dataRequestMapper.getFileInfo(f_id);
        } catch (Exception e) {
            log.error("Fail to load FileInfo");
        }
        if (fileInfo != null && fileInfo.size() > 0) {
            try {
                String file_id = f_id.toString();
                String file_location = fileInfo.get("file_location").toString();
                
                log.info("deleting real file id : " + file_id);

                File file = new File(file_location);

                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        log.info("Success to delete file in server..");
                        
                        // DB에서 내역 삭제
                        try {
                            fileMapper.deleteFile(file_id);
                        } catch (Exception e) {
                            log.error("Fail to delete file info..");
                        }
                    } else {
                        log.error("Fail to delete file in server..");
                    }
                } else {
                    log.info("No Exist file in server..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Fail to delete file in server..");
            }
        } else {
            log.error("No fileInfo..");
        }
    }
}


/* 데이터 요청 등록 양식
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MOBIGEN DATA CATALOG</title>
</head>
<body style="font-family:'맑은 고딕'; color: black; background-color: #f4f4f4; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: auto;">
    <div style="width: 600px; background-color: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; border-radius: 8px;">
        <table style="width: 600px; border-collapse: collapse; margin-bottom: 20px; border-top: 2px solid #333;">
            <tbody>
                <tr>
                    <td style="padding: 0; border: none;">
                        <h1 style="text-align: center; font-size: 24px; margin-bottom: 15px; margin-top: 15px;">_title_</h1>
                        <table style="width: 600px; border-collapse: collapse; margin-bottom: 20px; border-top: 2px solid #333;">
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 분류</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_category_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 데이터명</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_data_name_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청자</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_writer_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청일</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_create_date_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 내용</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_contents_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">요청기간</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_request_start_ ~ _request_end_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">활용 목적</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_benefit_content_</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
        <div style="font-size: 14px; color: #555; margin-top: 20px; text-align: center; font-weight: bold;">
            MOBIGEN DATA CATALOG
        </div>
    </div>
</body>
</html>
*/

/* 데이터 요청 답변 양식
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MOBIGEN DATA CATALOG</title>
</head>
<body style="font-family:'맑은 고딕'; color: black; background-color: #f4f4f4; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: auto;">
    <div style="width: 600px; background-color: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; border-radius: 8px;">
        <table style="width: 600px; border-collapse: collapse; margin-bottom: 20px; border-top: 2px solid #333;">
            <tbody>
                <tr>
                    <td style="padding: 0; border: none;">
                        <h1 style="text-align: center; font-size: 24px; margin-bottom: 15px; margin-top: 15px;">_title_</h1>
                        <table style="width: 600px; border-collapse: collapse; margin-bottom: 20px; border-top: 2px solid #333;">
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 분류</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_category_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 데이터명</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_data_name_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청자</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_writer_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청일</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_create_date_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">신청 내용</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_contents_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">요청기간</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_request_start_ ~ _request_end_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">활용 목적</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_benefit_content_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">진행상태</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_process_status_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 20%; border-left: none; border-right: none;">관리자 답변</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 80%; border-left: none; border-right: none;">_admin_reply_</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
        <div style="font-size: 14px; color: #555; margin-top: 20px; text-align: center; font-weight: bold;">
            MOBIGEN DATA CATALOG
        </div>
    </div>
</body>
</html>
*/