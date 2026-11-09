package com.mobigen.datalake.system.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.dataRequest.DataRequestMapper;
import com.mobigen.datalake.system.mailTemplate.MailTemplateService;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSender;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final SessionManager sessionManager;
    private final UserMapper userMapper;

    private final DataRequestMapper dataRequestMapper;
    private final JavaMailSender javaMailSender;

    private final MailTemplateService mailTemplateService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${properties.loginDeactivationDateThreshold}")
    int loginDeactivationDateThreshold;

    /**
     * 사용자 목록 조회
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map> getUserList(Map<String, Object> param) throws Exception {
        param.put("loginDeactivationDateThreshold", loginDeactivationDateThreshold);
        List<Map> user = (List<Map>) adminMapper.getUserList(param);
        return user;
    }

    /**
     * 사용자 목록 개수 조회
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public Integer getUserListTotal(Map<String, Object> param) throws Exception {
        Integer total = adminMapper.getUserListTotal(param);
        return total;
    }

    /**
     * 사용자 목록 조회 (권한 검색 시)
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map> getUserListWithRoleName(Map<String, Object> param) throws Exception {
        param.put("loginDeactivationDateThreshold", loginDeactivationDateThreshold);
        List<Map> user = (List<Map>) adminMapper.getUserListWithRoleName(param);
        return user;
    }

    /**
     * 사용자 목록 개수 조회 (권한 검색 시)
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public Integer getUserListTotalWithRoleName(Map<String, Object> param) throws Exception {
        Integer total = adminMapper.getUserListTotalWithRoleName(param);
        return total;
    }

    /**
     * 사용자 단건 조회
     * 
     * @param user_id
     * @return
     * @throws Exception
     */
    public Object getUser(String user_id) throws Exception {
        Map<String, Object> user = (Map<String, Object>) adminMapper.getUser(user_id);
        return user;
    }

    /**
     * 사용자 정보 수정 (권한)
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public Object updateUserInfo(Map<String, String> param) throws Exception {
        Object result = adminMapper.updateUserInfo(param);
        return result;
    }

    /**
     * 패스워드 초기화
     * 
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    public Object initPassword(Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, Object> result = new HashMap<>();

        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        int passwordLength = 12;

        try {
            SecureRandom random = new SecureRandom(); // SecureRandom 객체 생성
            StringBuilder password = new StringBuilder(passwordLength);

            // 주어진 길이만큼 랜덤하게 문자 선택
            for (int i = 0; i < passwordLength; i++) {
                int index = random.nextInt(CHARACTERS.length()); // 문자 집합에서 랜덤 인덱스 생성
                password.append(CHARACTERS.charAt(index)); // 문자 추가
            }
            log.info("change password>>>>" + password);

            // 비밀번호 암호화
            String encPassword = passwordEncoder.encode(password.toString());

            // Map 객체 생성
            Map<String, String> passwordParam = new HashMap<>();
            passwordParam.put("user_id", param.get("user_id").toString());
            passwordParam.put("user_nm", param.get("user_nm").toString());
            passwordParam.put("password", encPassword.toString());
            passwordParam.put("email", param.get("user_email").toString());
            passwordParam.put("init_password", password.toString());

            // 비밀번호 업데이트
            adminMapper.initPassword(passwordParam);

            // 안내 메일 발송
            // sendMail(passwordParam, request);

            //
            Map<String, Object> mailTemplateParam = new HashMap<>();
            mailTemplateParam.put("_mailTitle_", "패스워드 초기화 안내 메일");
            mailTemplateParam.put("_initPassword_", password.toString());
            mailTemplateParam.put("add_recipients", new ArrayList<Map<String, String>>() {
                {
                    Map<String, String> recipient = new HashMap<>();
                    recipient.put("email", param.get("user_email").toString());
                    recipient.put("name", param.get("user_nm").toString());
                    add(recipient);
                }
            });

            // 메일 템플릿 ID 조회
            Map<String, Object> commonCodeParam = new HashMap<>();
            commonCodeParam.put("type", "email_template");
            commonCodeParam.put("name", "INIT_PASSWORD");

            List<Map<String, Object>> commonCodeList = getCommonCodeList(commonCodeParam);

            String templateId = "";
            if (commonCodeList != null && commonCodeList.size() > 0) {
                templateId = commonCodeList.get(0).get("value").toString();
            }

            if (templateId == null || templateId.isEmpty() || templateId.equals("")) {
                result.put("register_result", "N");
                result.put("register_result_message", "패스워드 초기화 실패하였습니다다. 관리자에게 문의해주시기 바랍니다.");
            }

            // 안내 메일 발송
            Map<String, Object> mailTemplateResult = mailTemplateService.sendMail(templateId, mailTemplateParam);

            if (mailTemplateResult != null && mailTemplateResult.get("result").equals("fail")) {
                result.put("register_result", "N");
                result.put("register_result_message", "패스워드 초기화 실패하였습니다다. 관리자에게 문의해주시기 바랍니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("register_result", "N");
            result.put("register_result_message", "패스워드 초기화 실패하였습니다다. 관리자에게 문의해주시기 바랍니다.");
        }

        return result;
    }

    // 이메일 전송
    public void sendMail(Map<String, String> params, HttpServletRequest request) throws Exception {
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

        if (map.size() == 0)
            return;
        try {
            InternetAddress[] toAddress = new InternetAddress[map.size()];

            int i = 0;
            for (String key : map.keySet()) {
                toAddress[i] = new InternetAddress(((Map<String, String>) map.get(key)).get("user_email").toString(),
                        ((Map<String, String>) map.get(key)).get("user_nm").toString(), "UTF-8");
                i++;
            }

            message.setSubject("[MOBIGEN DATA CATALOG - 패스워드 초기화 안내 메일] "); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(params.get("email").toString(), params.get("user_nm").toString(), "UTF-8")); // 받는
                                                                                                                     // 사람
            message.addRecipients(Message.RecipientType.CC, toAddress); // 참조자

            // 사용자 정보
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            String userId = user.get("user_id").toString();
            Map<String, Object> mailMap = (Map<String, Object>) userMapper
                    .getUserMaster(params.get("user_id").toString());
            mailMap.put("title", "패스워드 초기화 안내 메일");
            mailMap.put("password", params.get("init_password").toString());

            // 메일 본문
            String htmlBody = makeHtmlMailBody(mailMap);

            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("이메일 발송에 실패하였습니다.");
        }
    }

    // 이메일 전송 본문
    public String makeHtmlMailBody(Map<String, Object> params) throws Exception {
        String mailBody = """
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
                                                <th>패스워드</th>
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

        String logoDataImage = "data:image/png;base64,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";

        String resultMailBody = "";
        resultMailBody = String.format(mailBody,
                logoDataImage,
                params.get("title"),
                params.get("password"));

        log.info("mail>>>>" + resultMailBody.toString());

        return resultMailBody;
    }

    // 관리자 이메일 목록 조회
    public Map<String, Object> getEmailManageList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = adminMapper.getEmailManageList();
            if (list != null && list.size() > 0) {
                result.put("emailManageList", list);
            } else {
                result.put("emailManageList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load email manage list");
            result.put("emailManageList", new ArrayList<>());
        }

        return result;
    }

    // 관리자 로고 이미지 목록 조회
    public Map<String, Object> getLogoImageList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = adminMapper.getLogoImageList();
            if (list != null && list.size() > 0) {
                result.put("logoImageList", list);
            } else {
                result.put("logoImageList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load logo image info");
            result.put("logoImageList", new ArrayList<>());
        }

        return result;
    }

    /**
     * 공통코드 리스트 조회
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCommonCodeList(Map<String, Object> param) throws Exception {
        Map<String, String> tParam = new HashMap<>();

        if (param.containsKey("type") && param.get("type") != null && !((String) param.get("type")).isEmpty()) {
            tParam.put("type", (String) param.get("type"));
        }
        if (param.containsKey("name") && param.get("name") != null && !((String) param.get("name")).isEmpty()) {
            tParam.put("name", (String) param.get("name"));
        }

        // 중복 제거
        List<Map<String, Object>> list = adminMapper.getCommonCodeList(tParam);
        List<Map<String, Object>> distinctList = list.stream().distinct().collect(Collectors.toList());

        return distinctList;
    }

    public Map<String, Object> updateLogo(String file_id) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer updateResult = 0;
        try {
            // <table> 테이블에 로고 설정 정보가 있는 경우 추가, 없는경우 업데이트
            Integer existsLogoFileId = adminMapper.isExistsLogoFileId();

            if (existsLogoFileId != null && existsLogoFileId > 0) {
                updateResult = adminMapper.updateLogoFileId(file_id);
            } else {
                updateResult = adminMapper.insertLogoFileId(file_id);
            }
            
        } catch (Exception e) {
            log.error("Fail to updateLogo !!");
            log.error(e.getMessage());
        }

        result.put("updateResult", updateResult);

        return result;
    }

}
