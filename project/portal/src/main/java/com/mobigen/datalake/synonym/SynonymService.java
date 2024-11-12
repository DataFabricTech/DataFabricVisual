package com.mobigen.datalake.synonym;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.PostMapper;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SynonymService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final SynonymMapper synonymMapper;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;

    // 동의어 목록 조회
    public List<Map> getSynonymList(Map params) throws Exception {
        // 대표어 중복 제거 목록 조회
        List<Map> termList = synonymMapper.getDistinctTermList(params);

        // 가공된 동의어 목록
        List<Map> updatedTermList = new ArrayList<>();

        // 데이터가 있는 경우
        if (!termList.isEmpty()) {
            // 동의어 목록 조회
            for (int i = 0; i < termList.size(); i++) {
                // 대표어 1개에 들어가는 목록 생성
                Map<String, Object> termMap = new LinkedHashMap<>();
                String term_1 = termList.get(i).get("term").toString();
                termMap.put("rownum", termList.size() - i);
                termMap.put("term", term_1);
                termMap.put("synonyms", new ArrayList<>()); // 동의어 추가할 리스트 부분

                List oneList = synonymMapper.getSynonymListOne(term_1);
                termMap.put("synonyms", oneList);

                // 가공된 동의어 목록에 가공한 데이터 추가
                updatedTermList.add(termMap);
            }
        }
        return updatedTermList;
    }

    // 동의어 목록 전체개수 조회
    public int getSynonymListTotal(Map params) throws Exception {
        return synonymMapper.getSynonymListTotal(params);
    }

    // 동의어 추가
    public Object addSynonym(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();

        int addResult = 0;
        List<Map> list = (List<Map>) params.get("synonyms");
        for (Map synonyms : list) {
            params.put("synonym", synonyms.get("synonym"));
            addResult += synonymMapper.addSynonym(params);
        }

        result.put("addResult", addResult);
        return result;
    }

    // 동의어 세트 신규 추가
    public Object addSynonymSet(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int addSetResult = 0; // 동의어 세트 추가 결과

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("create_user", params.get("create_user"));
        paramMap.put("addedSynonymList", params.get("addedSynonymList"));

        addSetResult = synonymMapper.addSynonymSet(paramMap);

        result.put("addSetResult", addSetResult);
        return result;
    }

    // 동의어 세트 수정
    public Object modifySynonymSet(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int modifyTermResult = 0; // 대표어 수정 결과
        int deleteSetResult = 0; // 기존 동의어 삭제 결과
        int addSetResult = 0; // 신규 동의어 추가 결과

        // 대표어가 변경된 경우
        if (params.get("newTerm") != null && params.get("originTerm") != null) {
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("originTerm", params.get("originTerm"));
            param.put("newTerm", params.get("newTerm"));
            param.put("update_user", params.get("update_user"));

            modifyTermResult = synonymMapper.modifyTerm(param);
        }

        // 기존 동의어 삭제 목록이 있는 경우
        if (!params.isEmpty()) {
            if (((List<Map<String, Object>>) params.get("deletedSynonymList")).size() > 0)
                deleteSetResult = synonymMapper
                        .deleteSynonymSet((List<Map<String, Object>>) params.get("deletedSynonymList"));
        }

        // 신규 동의어 추가 결과가 있는 경우
        if (((List<Map<String, Object>>) params.get("addedSynonymList")).size() > 0) {
            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("create_user", params.get("create_user"));
            paramMap.put("addedSynonymList", params.get("addedSynonymList"));
            addSetResult = synonymMapper.addSynonymSet(paramMap);
        }

        result.put("modifyTermResult", modifyTermResult);
        result.put("deleteSetResult", deleteSetResult);
        result.put("addSetResult", addSetResult);
        return result;
    }

    // 동의어 삭제 요청
    public Object requestDeleteSynonym(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int addDeleteRequestResult = 0; //
        int addDeleteRequestItemResult = 0;

        // 삭제 요청 정보 등록
        params.put("type", 0); // 요청 타입(0:삭제요청)
        addDeleteRequestResult = synonymMapper.addDeleteRequestInfo(params);

        // 최근 삭제 요청 정보 id 조회
        int req_id = synonymMapper.getRecentDeleteRequestInfoId();

        // 삭제 요청 동의어 등록
        List<Map> list = (List<Map>) params.get("synonyms");
        for (Map<String, Object> data : list) {
            Map<String, Object> req_params = new LinkedHashMap<>();
            req_params.put("req_id", req_id);
            req_params.put("synonym_id", data.get("id"));
            req_params.put("synonym", data.get("synonym"));

            // 요청 동의어 등록
            addDeleteRequestItemResult += synonymMapper.addDeleteRequestItemInfo(req_params);
        }

        sendDeleteRequestMail(params, request);

        result.put("addDeleteRequestResult", addDeleteRequestResult);
        result.put("addDeleteRequestItemResult", addDeleteRequestItemResult);
        return result;
    }


    // 동의어 삭제 요청 내역 목록 조회
    public Object getDeleteRequestList(Map<String, Object> params) throws Exception {
        // req_id에 따라 묶기
        // Map >> { req_id : 0, ......synonyms=[{synonym_id: 0, synonym: '테스트8'}]

        // 요청 내역과 item 추가된 정보 조회
        List<Map> deleteRequestListAndItem = synonymMapper.getDeleteRequestListAndItem(params);

        List<Map> deleteRequestListItem = synonymMapper.getDeleteRequestListItem();

        // 요청 내역 중복 제거 목록
        List<Map> updated_list = new ArrayList<>();
        for (int i = 0; i < deleteRequestListAndItem.size(); i++) {
            if (i == 0) {
                updated_list.add(deleteRequestListAndItem.get(i));
            } else if (i > 0) {
                if (deleteRequestListAndItem.get(i).get("id") != deleteRequestListAndItem.get(i - 1).get("id")) {
                    updated_list.add(deleteRequestListAndItem.get(i));
                }
            }
        }

        // 요청 내역에 동의어 정보 넣기
        for (int j = 0; j < updated_list.size(); j++) {
            // id와 req_id가 같은 경우
            List synonyms = new ArrayList();
            for (int k = 0; k < deleteRequestListItem.size(); k++) {
                if (Integer.parseInt(deleteRequestListItem.get(k).get("req_id").toString()) == Integer
                        .parseInt(updated_list.get(j).get("id").toString())) {
                    // synonyms 목록에 추가
                    Map synonym = new LinkedHashMap();
                    synonym.put("id", deleteRequestListItem.get(k).get("id"));
                    synonym.put("req_id", deleteRequestListItem.get(k).get("req_id"));
                    synonym.put("synonym_id", deleteRequestListItem.get(k).get("synonym_id"));
                    synonym.put("synonym", deleteRequestListItem.get(k).get("synonym"));
                    synonyms.add(synonym);
                }
            }

            // 사용하지 않는 정보 제거
            updated_list.get(j).remove("synonym");
            updated_list.get(j).put("rownum", j + 1);
            updated_list.get(j).put("synonyms", synonyms);
        }
        return updated_list;
    }

    // 동의어 삭제 요청 내역 목록 조회(관리자)
    public Object getDeleteRequestListAdmin(Map<String, Object> params) throws Exception {
        // req_id에 따라 묶기
        // Map >> { req_id : 0, ......synonyms=[{synonym_id: 0, synonym: '테스트8'}]

        // 요청 내역과 item 추가된 정보 조회
        List<Map> deleteRequestListAndItem = synonymMapper.getDeleteRequestListAndItemAdmin(params);

        List<Map> deleteRequestListItem = synonymMapper.getDeleteRequestListItem();

        // 요청 내역 중복 제거 목록
        List<Map> updated_list = new ArrayList<>();
        for (int i = 0; i < deleteRequestListAndItem.size(); i++) {
            if (i == 0) {
                updated_list.add(deleteRequestListAndItem.get(i));
            } else if (i > 0) {
                if (deleteRequestListAndItem.get(i).get("id") != deleteRequestListAndItem.get(i - 1).get("id")) {
                    updated_list.add(deleteRequestListAndItem.get(i));
                }
            }
        }

        // 요청 내역에 동의어 정보 넣기
        for (int j = 0; j < updated_list.size(); j++) {
            // id와 req_id가 같은 경우
            List synonyms = new ArrayList();
            for (int k = 0; k < deleteRequestListItem.size(); k++) {
                if (Integer.parseInt(deleteRequestListItem.get(k).get("req_id").toString()) == Integer
                        .parseInt(updated_list.get(j).get("id").toString())) {
                    // synonyms 목록에 추가
                    Map synonym = new LinkedHashMap();
                    synonym.put("id", deleteRequestListItem.get(k).get("id"));
                    synonym.put("req_id", deleteRequestListItem.get(k).get("req_id"));
                    synonym.put("synonym_id", deleteRequestListItem.get(k).get("synonym_id"));
                    synonym.put("synonym", deleteRequestListItem.get(k).get("synonym"));
                    synonyms.add(synonym);
                }
            }

            // 사용하지 않는 정보 제거
            updated_list.get(j).remove("synonym");
            updated_list.get(j).put("rownum", j + 1);
            updated_list.get(j).put("synonyms", synonyms);
        }

        return updated_list;
    }


    // 대표어 1건 동의어 조회
    public Object getSynonymListOne(String term) throws Exception {
        List<Map> synonymListOne = new ArrayList<>();
        List<Map> synonyms = synonymMapper.getSynonymListOne(term);

        // 토글 정보 추가
        for (Map<String, Object> synonym : synonyms) {
            synonym.put("toggle", false);
            synonymListOne.add(synonym);
        }

        return synonymListOne;
    }

    // 동의어 삭제 요청 상태 조회
    public Object getDeleteRequestOneStatus(String id) throws Exception {
        return synonymMapper.getDeleteRequestOneStatus(id);
    }

    // 동의어 삭제 요청 내역 삭제
    public Object getDeleteRequestOne(long id) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteRequestResult = 0; // 동의어 삭제 요청 내역 삭제 결과
        int deleteRequestItemResult = 0; // 해당 요청의 동의어 목록 삭제 결과

        deleteRequestResult = synonymMapper.deleteRequestOne(id);
        deleteRequestItemResult = synonymMapper.deleteRequestOneItem(id);

        result.put("deleteRequestResult", deleteRequestResult);
        result.put("deleteRequestItemResult", deleteRequestItemResult);

        return result;
    }
    // 동의어 삭제 요청 승인
    public Object confirmDeleteRequestOne(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int confirmResult = 0; // 승인 결과
        int deleteSynonym = 0; // 동의어 삭제 결과

        // 삭제 요청한 동의어 목록이 존재하는지 확인
        // 해당 대표어의 동의어 목록 조회
        List<Map<String, Object>> synonymListOne = synonymMapper.getSynonymListOne(params.get("term").toString());

        // 삭제 요청 동의어
        List<Map<String, Object>> requestSynonyms = (List<Map<String, Object>>) params.get("synonyms");

        for (int i = 0; i < requestSynonyms.size(); i++) {
            for (int j = 0; j < synonymListOne.size(); j++) {
                String originSynonym = synonymListOne.get(j).get("synonym").toString();
                String reqSynonym = requestSynonyms.get(i).get("synonym").toString();
                // 동일한 동의어 삭제 처리
                if (reqSynonym.equals(originSynonym)) {
                    Map<String, Object> deleteParams = new HashMap<>();
                    deleteParams.put("term", params.get("term").toString());
                    deleteParams.put("synonym", reqSynonym);
                    deleteSynonym += synonymMapper.deleteSynonymOne(deleteParams); // 동의어 삭제 처리
                }
            }
        }

        // 삭제한 경우 승인 처리
        if (deleteSynonym >= 0) {
            confirmResult = synonymMapper.confirmSynonymRequestOne(params);

            // 메일 처리
            Map writer = (Map) userMapper.getUserMaster((String) params.get("req_user"));

            String strUserEmail = (String) writer.get("user_email");

            if (strUserEmail != null && !"".equals(strUserEmail)) {
                // String strUserEmailDecode = Crypto.AES_Decode(strUserEmail);
                // if (strUserEmailDecode != null && !"".equals(strUserEmailDecode)) {
                // decode
                // params.put("writer_email", strUserEmailDecode);
                if (strUserEmail != null && !"".equals(strUserEmail)) {
                    params.put("writer_email", strUserEmail);
                    params.put("writer_user_nm", (String) writer.get("user_nm"));
                    sendConfirmMail(params, request);
                }
            }
        }

        result.put("confirmResult", confirmResult);
        result.put("deleteSynonym", deleteSynonym);
        return result;
    }

    // 동의어 삭제 요청 반려
    public Object rejectDeleteRequestOne(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int rejectResult = 0; // 반려 결과

        rejectResult = synonymMapper.rejectSynonymRequestOne(params);

        // 메일 처리
        Map writer = (Map) userMapper.getUserMaster((String) params.get("req_user"));

        String strUserEmail = (String) writer.get("user_email");

        if (strUserEmail != null && !"".equals(strUserEmail)) {
            // String strUserEmailDecode = Crypto.AES_Decode(strUserEmail);
            // if (strUserEmailDecode != null && !"".equals(strUserEmailDecode)) {
            // decode
            // params.put("writer_email", strUserEmailDecode);
            if (strUserEmail != null && !"".equals(strUserEmail)) {
                params.put("writer_email", strUserEmail);
                params.put("writer_user_nm", (String) writer.get("user_nm"));
                sendRejectMail(params, request);
            }
        }

        result.put("rejectResult", rejectResult);
        return result;
    }

    // 동의어 삭제 요청 일괄 반려
    public Object rejectDeleteRequestAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int rejectAllResult = 0; // 일괄 반려 결과

        List<Map<String, Object>> req_list = (List<Map<String, Object>>) params.get("req_list");

        for (Map<String, Object> req : req_list) {
            Map<String, Object> deleteAllParams = new LinkedHashMap<>();
            deleteAllParams.put("res_user", params.get("res_user"));
            deleteAllParams.put("id", req.get("id"));
            deleteAllParams.put("response", params.get("response"));
            rejectAllResult += synonymMapper.rejectSynonymRequestAll(deleteAllParams);

            // 메일 처리
            params.put("req_user", req.get("req_user"));
            params.put("term", req.get("term"));
            params.put("synonyms", req.get("synonyms"));
            params.put("request", req.get("request"));
            
            Map writer = (Map) userMapper.getUserMaster((String) params.get("req_user"));

            String strUserEmail = (String) writer.get("user_email");

            if (strUserEmail != null && !"".equals(strUserEmail)) {
                // String strUserEmailDecode = Crypto.AES_Decode(strUserEmail);
                // if (strUserEmailDecode != null && !"".equals(strUserEmailDecode)) {
                // decode
                // params.put("writer_email", strUserEmailDecode);
                if (strUserEmail != null && !"".equals(strUserEmail)) {
                    params.put("writer_email", strUserEmail);
                    params.put("writer_user_nm", (String) writer.get("user_nm"));
                    sendRejectMail(params, request);
                }
            }
        }

        result.put("rejectAllResult", rejectAllResult);
        return rejectAllResult;
    }


    // 동의어 여러건 삭제
    public Object deleteMySynonym(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteResult = 0; // 동의어 삭제 결과
        deleteResult = synonymMapper.deleteMySynonym(params);

        result.put("deleteResult", deleteResult);
        return result;
    }

    // 동의어 여러건 삭제
    public Object deleteSynonym(List<Map<String, Object>> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteResult = 0; // 동의어 삭제 결과

        deleteResult = synonymMapper.deleteSynonym(params);

        result.put("deleteResult", deleteResult);
        return result;
    }

    // 동의어 삭제 요청 일괄 승인
    public Object confirmDeleteRequestAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteManyResult = 0; // 동의어 삭제 결과
        int confirmAllResult = 0; // 일괄 승인 결과

        // 해당 정보에 있는 대표어와 일치하는 동의어 삭제 처리
        for (Map<String, Object> req_info : (List<Map<String, Object>>) params.get("req_list")) {
            req_info.put("res_user", params.get("res_user"));
            confirmAllResult += synonymMapper.confirmSynonymRequestAll(req_info);
            deleteManyResult += synonymMapper.deleteSynonymAll(req_info);

            // 메일 처리
            params.put("req_user", req_info.get("req_user"));
            params.put("term", req_info.get("term"));
            params.put("synonyms", req_info.get("synonyms"));
            params.put("request", req_info.get("request"));
            
            Map writer = (Map) userMapper.getUserMaster((String) params.get("req_user"));

            String strUserEmail = (String) writer.get("user_email");

            if (strUserEmail != null && !"".equals(strUserEmail)) {
                // String strUserEmailDecode = Crypto.AES_Decode(strUserEmail);
                // if (strUserEmailDecode != null && !"".equals(strUserEmailDecode)) {
                // decode
                // params.put("writer_email", strUserEmailDecode);
                if (strUserEmail != null && !"".equals(strUserEmail)) {
                    params.put("writer_email", strUserEmail);
                    params.put("writer_user_nm", (String) writer.get("user_nm"));
                    sendConfirmMail(params, request);
                }
            }
        }

        result.put("deleteManyResult", deleteManyResult);
        result.put("confirmAllResult", confirmAllResult);
        return result;
    }

    // 메일 처리 - 동의어 삭제 요청
    public void sendDeleteRequestMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
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

            message.setSubject("[BIGS DATA PORTAL 동의어 삭제 요청] " + (String) params.get("term")); // 메일 제목
            message.setFrom("rotem_portal@hyundai-rotem.co.kr"); // 보낸 사람
            message.addRecipients(Message.RecipientType.TO, toAddress); // 받는 사람

            // 동의어 삭제 요청자 조회
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

            // 동의어 삭제요청 목록 조회
            List<Map> synonyms = (List) params.get("synonyms");
            for (Map synonym : synonyms) {
                synonym.get("synonyms") ;
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

    // 메일 처리 - 동의어 삭제 요청 승인
    public void sendConfirmMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
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

            message.setSubject("[BIGS DATA PORTAL 동의어 삭제 요청 결과 안내] " + (String) params.get("term")); // 메일 제목
            message.setFrom("rotem_portal@hyundai-rotem.co.kr"); // 보낸 사람
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(params.get("writer_email").toString(), params.get("writer_user_nm").toString(), "UTF-8")); // 받는 사람
            message.addRecipients(Message.RecipientType.CC, toAddress); // 참조자

            // 동의어 삭제 요청자 조회
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

            // 동의어 삭제요청 목록 조회
            List<Map> synonyms = (List) params.get("synonyms");
            for (Map synonym : synonyms) {
                synonym.get("synonyms") ;
            }
           
            //메일 본문
            String htmlBody = makeHtmlMailBody(params, deptName + " " + userName + " " + jwName, "approval");
            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 메일 처리 - 동의어 삭제 요청 반려
    public void sendRejectMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
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

            message.setSubject("[BIGS DATA PORTAL 동의어 삭제 요청 결과 안내] " + (String) params.get("term")); // 메일 제목
            message.setFrom("rotem_portal@hyundai-rotem.co.kr"); // 보낸 사람
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(params.get("writer_email").toString(), params.get("writer_user_nm").toString(), "UTF-8")); // 받는 사람
            message.addRecipients(Message.RecipientType.CC, toAddress); // 참조자

            // 동의어 삭제 요청자 조회
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

            // 동의어 삭제요청 목록 조회
            List<Map> synonyms = (List) params.get("synonyms");
            for (Map synonym : synonyms) {
                synonym.get("synonyms") ;
            }
            
            //메일 본문
            String htmlBody = makeHtmlMailBody(params, deptName + " " + userName + " " + jwName, "reject");
            // 메일 양식 html로 저장
            message.setContent(htmlBody, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String makeHtmlMailBody(Map<String, Object> params, String request_user_info, String mailType) throws Exception{
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String formattedDateTime = now.format(formatterDatetime);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = now.format(formatterDate);

        String logoDataImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAAsCAMAAACZpYwBAAAAP1BMVEVHcEwbHFcJI2gJI2kJI2kIJGkGIGkKJGgJJGkIJGgII2gIImgJI2iWDDB+DDWlCSrTAhgJImm5BiIKJGnmABI0w7dJAAAAE3RSTlMAD5rx1z8ngMBAYEywrS3R/nDwOs9O9gAAAk1JREFUWMPlmOFygyAMgAkgAWq3ufT9n3UgBNHqppO6H8udhUsKX0kgwQrx/4QofiJZR+RCLzaWkE2URJvwlSTBNJTujdRpsJCkQ0+TFM/goEDS1lpNBJWyCRh0mh7m4GTG8RmVrlKeAndBZAAHLyunor8vAicJ4OBsol7sAacfexJsx9BFMChSsAu8EeP7+29iPDU/gzGIXYDh/vH4PAuWwZW3JTgqOyKzGuOIfZwH59M7BydRYgWcsIfAODrQoa8aj0nMZHbhWVEaKSvsIfB5eSvYi8H3gn28v/0J+FpsATfG4iTdd+Dmq6VK9G0D/PECJ9NMVsn3l8R2Di750YUctjECYn6DpdZ1ZmzN5rhVcLl0uDG/6FzQ/Ji/aKoZk03jNNwKr2IDAmJOVHgELDTPDXpCyQW4tmkoYM8ZWH8TsS2wZbCsfY9z8MxmebhWWdNXlt3gNKcXJq81TaLA27SMcCHz2dZngFlukSLHXW2EZx9r9hqWAA+8np5V+VdiWrPC/jA48foKUyafwPik4upaFPoYOLuzB3Y5rlG2wJYVjrfKsXMsnag32SZYxntgS3A4m7AHXJ/rs+Ds6hjja8G8ucKuvhqct+OwJ8Yui2kCZqDds6tNLBRdG7A8AsaGrsa/Bv+cQORrwGVym6tG1mhR9foXgFMFUkOugFAOkS7VSWabbwouL1a88vD+VM7tzBbfeFuCn24ZoFZvIMqItmABubqm3B0vcH3JVIAZraQRp8ApB8XZORmNKIcD97Oisk19wYPSaODOP/xbT3wBvRl7yPlk+ocAAAAASUVORK5CYII=";

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
                        <h1>동의어 삭제 요청</h1>
                        <table>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 자</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 일</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>대&nbsp;&nbsp; 표&nbsp;&nbsp; 어</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 목록</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 사유</th>
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

        String requestApprovalMailBody = """
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
                        <h1>동의어 삭제 요청 결과</h1>
                        <table>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 자</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 일</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>대&nbsp;&nbsp; 표&nbsp;&nbsp; 어</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 목록</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 사유</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>결&nbsp;&nbsp;&nbsp;&nbsp;과</th>
                                <td>승인</td>
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

        String requestRejectMailBody = """
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
                        <h1>동의어 삭제 요청 결과</h1>
                        <table>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 자</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>요&nbsp;&nbsp; 청&nbsp;&nbsp; 일</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>대&nbsp;&nbsp; 표&nbsp;&nbsp; 어</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 목록</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>삭제요청&nbsp; 사유</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>결&nbsp;&nbsp;&nbsp;&nbsp;과</th>
                                <td>반려</td>
                            </tr>
                            <tr>
                                <th>반려&nbsp; 사유</th>
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

        List<Map> synonyms = (List) params.get("synonyms");
        String synonymListStr = "[";
        for (int x = 0; x < synonyms.size(); x++) {
            synonymListStr +=  (String)synonyms.get(x).get("synonym");
            if (x < synonyms.size() - 1) synonymListStr += ", ";
        }
        synonymListStr += "]";

        String resultMailBody = "";
        if(mailType.equals("request")){     //동의어 삭제 요청
            resultMailBody = String.format(requestMailBody,
                logoDataImage,
                request_user_info,
                formattedDate,
                params.get("term"),
                synonymListStr,
                params.get("request")
                );
        }else if(mailType.equals("approval")){  //동의어 삭제 요청 - 승인
            resultMailBody = String.format(requestApprovalMailBody,
                logoDataImage,
                request_user_info,
                formattedDate,
                params.get("term"),
                synonymListStr,
                params.get("request")
                );
        }else if(mailType.equals("reject")){    //동의어 삭제 요청 - 반려
                resultMailBody = String.format(requestRejectMailBody,
                logoDataImage,
                request_user_info,
                formattedDate,
                params.get("term"),
                synonymListStr,
                params.get("request"),
                params.get("response")                
                );

        }

        System.out.println(resultMailBody);

        return resultMailBody;
    }

}
