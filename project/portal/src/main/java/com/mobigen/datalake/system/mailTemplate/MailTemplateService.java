package com.mobigen.datalake.system.mailTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message.RecipientType;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailTemplateService {
    private final MailTemplateMapper mailTemplateMapper;
    private final JavaMailSender javaMailSender;


    // 메일 템플릿 - 조회
    public Map<String, Object> getMailTemplateList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        totCnt = mailTemplateMapper.getMailTemplateListTotCnt(params);
        resultList = mailTemplateMapper.getMailTemplateList(params);

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    // 메일 템플릿 - 상세 조회
    public Map<String, Object> getMailTemplateInfo(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> mailTemplateInfo = mailTemplateMapper.getMailTemplateInfo(params);

        result.put("list", mailTemplateInfo);

        return result;
    }

    // 메일 템플릿 - 등록/수정
    public Integer saveMailTemplate(Map<String, Object> param) throws Exception {
        String[] fields = {"sender", "receiver", "cc_recipients", "bcc_recipients", "template_variables"};
        
        for (String field : fields) {
            Object fieldValue = param.get(field);
    
            if (fieldValue instanceof String) {
                String json = (String) fieldValue;
    
                if (json == null || json.isEmpty()) {
                    param.put(field, new ArrayList<>());
                } else {
                    param.put(field, parseJsonToList(json));
                }
            } else if (fieldValue instanceof List) {
                param.put(field, fieldValue);
            } else {
                param.put(field, new ArrayList<>());
            }
        }
    
        // 
        param.put("sender", convertToJsonArray(param.get("sender")));
        param.put("receiver", convertToJsonArray(param.get("receiver")));
        param.put("cc_recipients", convertToJsonArray(param.get("cc_recipients")));
        param.put("bcc_recipients", convertToJsonArray(param.get("bcc_recipients")));
        param.put("template_variables", normalConvertToJsonArray(param.get("template_variables")));

        // param.put("template_content", covertToHtml((String) param.get("template_content")));
    
        Integer total = mailTemplateMapper.saveMailTemplate(param);
        return total;
    }
    
    // 메일 템플릿 - 삭제
    public Integer deleteMailTemplate(Map<String, Object> param) throws Exception {
        Integer total = mailTemplateMapper.deleteMailTemplate(param);
        return total;
    }

    //
    private String covertToHtml(String content) {
        if (content == null) return "";

        String convertContent = content.replaceAll("&lt;", "<");
        convertContent = convertContent.replaceAll("&gt;", ">");

        return convertContent;
    }

    // 
    private Object convertToJsonArray(Object value) {
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            
            if (!list.isEmpty() && list.get(0) instanceof Map) {
                return list.stream()
                        .map(item -> {
                            Map<String, Object> map = (Map<String, Object>) item;
                            Object name = map.get("name");
                            Object email = map.get("email");
                            
                            if (name != null && email != null) {
                                return "\"name\":\"" + name + "\"||\"email\":\"" + email + "\"";
                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(", ", "{", "}"));
            }
        }
        return "[]";
    }

    //
    private String normalConvertToJsonArray(Object fieldValue) {
        if (fieldValue instanceof List) {
            List<?> list = (List<?>) fieldValue;

            if(list.size() == 0 || list.get(0) == null) return "[]";

            String jsonArray = list.stream()
                                   .map(Object::toString)
                                   .collect(Collectors.joining(","));
            
            jsonArray = jsonArray.replaceAll("[\\{\\}\\[\\]]", "").trim();
                                   
            if (!jsonArray.startsWith("") && !jsonArray.endsWith("")) {
                return "" + jsonArray + "";
            }
            return jsonArray;
        }
        return "[]";
    }
    
    //
    private List<Map<String, Object>> parseJsonToList(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
    }

    //
    public Map sendMail(String templateId, Map<String, Object> variables) throws Exception {
        Map result = new HashMap();
        MimeMessage message = javaMailSender.createMimeMessage();

        List<Map<String, Object>> mailTemplateInfo = mailTemplateMapper.getMailTemplateInfo(new HashMap<String, Object>() {{
            put("template_id", templateId);
        }});

        if (mailTemplateInfo.isEmpty()){
            result.put("result", "fail");
            result.put("message", "메일 템플릿가 없습니다.");
            return result;
        }

        Map<String, Object> mailTemplate = mailTemplateInfo.get(0);

        if (mailTemplate.get("use_yn") == null || mailTemplate.get("use_yn").equals("N")) {
            result.put("result", "fail");
            result.put("message", "사용 가능한 템플릿이 없습니다.");
            return result;
        }

        // 메일 제목
        String mailTitle = (String) mailTemplate.get("template_title");
        // 메일 본문
        String mailContent = (String) mailTemplate.get("template_content");
        mailContent = covertToHtml(mailContent);

        // 발신자
        List<Map<String, String>> mailSenderUserList = extractUserInfo(mailTemplate.get("sender"));
        // 수신자
        List<Map<String, String>> mailReceiverUserList = extractUserInfo(mailTemplate.get("receiver"));
        // 참조자
        List<Map<String, String>> mailCcUserList = extractUserInfo(mailTemplate.get("cc_recipients"));

        // 강제 발신자 변경
        if (variables.containsKey("add_sender")) {
            Object senderObject = variables.get("add_sender");
            if (senderObject instanceof List) {
                List<Map<String, String>> addSender = addExtractUserInfo(senderObject);
                mailSenderUserList.clear();
                mailSenderUserList.addAll(addSender);
            } else {
                log.error("add_sender는 List 형식이어야 합니다. 현재 형식: " + senderObject.getClass().getName());
            }
        }

        // 추가 수신자(템플릿 수신자 예외)
        if (variables.containsKey("add_recipients")) {
            Object recipientsObject = variables.get("add_recipients");
            if (recipientsObject instanceof List) {
                List<Map<String, String>> addRecipients = addExtractUserInfo(recipientsObject);
                mailReceiverUserList.addAll(addRecipients);
            } else {
                log.error("add_recipients는 List 형식이어야 합니다. 현재 형식: " + recipientsObject.getClass().getName());
            }
        }

        // cc 추가 수신자(템플릿 수신자 예외)
        if (variables.containsKey("add_cc_recipients")) {
            Object ccRecipientsObject = variables.get("add_cc_recipients");
            if (ccRecipientsObject instanceof List) {
                List<Map<String, String>> addCcRecipientsObject = addExtractUserInfo(ccRecipientsObject);
                mailCcUserList.addAll(addCcRecipientsObject);
            } else {
                log.error("add_cc_recipients는 List 형식이어야 합니다. 현재 형식: " + ccRecipientsObject.getClass().getName());
            }
        }

        if (mailSenderUserList.isEmpty() ){
            result.put("result", "fail");
            result.put("message", "발신자가 없습니다.");
            return result;
        }
        // if (mailReceiverUserList.isEmpty() ){
        //     result.put("result", "fail");
        //     result.put("message", "수신자가 없습니다.");
        //     return result;
        // }

        try {
            // 메일 제목
            message.setSubject(mailTitle);

            // 보낸 사람(발신자는 1명만 지원하므로 덮어 씌우는걸로 )
            for (Map<String, String> sender : mailSenderUserList) {
                String name = sender.get("name");
                String email = sender.get("email");
                
                InternetAddress fromAddress = new InternetAddress(email, name, "UTF-8");
                message.setFrom(fromAddress);
            }

            // 받는 사람
            for (Map user : mailReceiverUserList) {
                String email = (String) user.get("email");
                String name = (String) user.get("name");
                addRecipientIfValid(Message.RecipientType.TO, message, email, name);
            }

            // 참조자
            InternetAddress[] toAddress = new InternetAddress[mailCcUserList.size()];
            int i = 0;
            for (Map mailUser : mailCcUserList) {
                String name = mailUser.get("name") != null ? mailUser.get("name").toString() : null;
                String email = mailUser.get("email") != null ? mailUser.get("email").toString() : null;
                
                addRecipientIfValid(Message.RecipientType.CC, message, email, name);
            }

            // 메일 본문 변수 치환
            String htmlBody = replaceVariables(mailContent, variables);

            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");

            // 메일 정보 출력
            log.info("메일 제목: " + message.getSubject());
            log.info("발신자: " + Arrays.toString(message.getFrom()));
            log.info("수신자: " + Arrays.toString(message.getRecipients(Message.RecipientType.TO)));
            log.info("참조자: " + Arrays.toString(message.getRecipients(Message.RecipientType.CC)));
            log.info("내용: " + message.getContent());
            log.info("메일내용: " + htmlBody);

            javaMailSender.send(message);
        } catch (Exception e) {
            // 에러 로깅
            log.error("이메일 발송 중 오류 발생: {}", e.getMessage(), e);
            
            // 실패 응답 반환
            result.put("result", "fail");
            result.put("message", "이메일 발송에 실패하였습니다.");
            return result;
        } 

        result.put("result", "sucess");
        result.put("message", "정상 처리 되었습니다.");
        return result;
    }

    //
    private List<Map<String, String>> extractUserInfo(Object userObject) {
        List<Map<String, String>> userList = new ArrayList<>();
    
        if (userObject instanceof String[]) {
            String[] userArray = (String[]) userObject;
            for (String user : userArray) {
                user = user.replaceAll("[\\{\\}\\[\\]]", "").trim();

                String[] parts = user.split("\\|\\|");
                if (parts.length == 2) {
                    String name = parts[0].split(":")[1].replace("\"", "").trim();
                    String email = parts[1].split(":")[1].replace("\"", "").trim();
                    
                    // 
                    if (isValidEmail(email)) {
                        Map<String, String> userInfo = new HashMap<>();
                        userInfo.put("name", name);
                        userInfo.put("email", email);
                        userList.add(userInfo);
                    } else {
                        log.info("Invalid email format for user: " + name + " with email: " + email);
                    }
                }
            }
        }

        return userList;
    }

    // 
    private List<Map<String, String>> addExtractUserInfo(Object userObject) {
        List<Map<String, String>> userList = new ArrayList<>();
    
        if (userObject instanceof List) {
            List<?> userArray = (List<?>) userObject;
            for (Object user : userArray) {
                if (user instanceof Map) {
                    Map<?, ?> userMap = (Map<?, ?>) user;
                    String name = (String) userMap.get("name");
                    String email = (String) userMap.get("email");
    
                    // 이메일 유효성 검사
                    if (isValidEmail(email)) {
                        Map<String, String> userInfo = new HashMap<>();
                        userInfo.put("name", name);
                        userInfo.put("email", email);
                        userList.add(userInfo);
                    } else {
                        log.info("Invalid email format for user: " + name + " with email: " + email);
                    }
                }
            }
        }
    
        return userList;
    }

    // 
    private String replaceVariables(String template, Map<String, Object> variables) {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String value;
            if (entry.getValue() instanceof List) {
                value = entry.getValue().toString();
            } else if (entry.getValue() instanceof String) {
                value = (String) entry.getValue();
            } else {
                value = entry.getValue().toString();
            }

            template = template.replace(entry.getKey(), value);
        }
        return template;
    }

    // 
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    // 
    private void addRecipientIfValid(RecipientType type, Message message, String email, String name) {
        try {
            if (isValidEmail(email)) {
                try {
                    message.addRecipient(type, new InternetAddress(email, name, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.info("Unsupported encoding for email: " + email + " due to " + e.getMessage());
                }
            } else {
                log.info("Invalid email format: " + email);
            }
        } catch (MessagingException e) {
            log.info("Failed to add recipient: " + email + " due to " + e.getMessage());
        }
    }
}
