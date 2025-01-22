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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.mobigen.datalake.post.PostMapper;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SynonymService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final SynonymMapper synonymMapper;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;
    
    @Value("${properties.activeSendMail.synonym}")
	public boolean activeSendMail;

    @Value("${properties.synonymsync.url}")
    public String synonymsyncUrl;
    

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

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

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

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

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

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

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

        // 메일발송기능 활성화 여부
        if (activeSendMail) {
            sendDeleteRequestMail(params, request);
        }

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

            // 메일발송기능 활성화 여부
            if (activeSendMail) {
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
        }

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

        result.put("confirmResult", confirmResult);
        result.put("deleteSynonym", deleteSynonym);
        return result;
    }

    // 동의어 삭제 요청 반려
    public Object rejectDeleteRequestOne(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int rejectResult = 0; // 반려 결과

        rejectResult = synonymMapper.rejectSynonymRequestOne(params);

        // 메일발송기능 활성화 여부
        if (activeSendMail) {
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

            // 메일발송기능 활성화 여부
            if (activeSendMail) {

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
        }

        result.put("rejectAllResult", rejectAllResult);
        return rejectAllResult;
    }


    // 동의어 여러건 삭제
    public Object deleteMySynonym(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteResult = 0; // 동의어 삭제 결과
        deleteResult = synonymMapper.deleteMySynonym(params);

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

        result.put("deleteResult", deleteResult);
        return result;
    }

    // 동의어 여러건 삭제
    public Object deleteSynonym(List<Map<String, Object>> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        int deleteResult = 0; // 동의어 삭제 결과

        deleteResult = synonymMapper.deleteSynonym(params);

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

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

            // 메일발송기능 활성화 여부
            if (activeSendMail) {
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
        }

        // 동의어 ESSYNC API 호출
        boolean esUpdate = esSynonymSyncUpdate();
        result.put("esUpdate", esUpdate);

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

            message.setSubject("[MOBIGEN DATA CATALOG 동의어 삭제 요청] " + (String) params.get("term")); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
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

            message.setSubject("[MOBIGEN DATA CATALOG 동의어 삭제 요청 결과 안내] " + (String) params.get("term")); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
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

            message.setSubject("[MOBIGEN DATA CATALOG 동의어 삭제 요청 결과 안내] " + (String) params.get("term")); // 메일 제목
            message.setFrom("mobigen_portal@mobigen.com"); // 보낸 사람
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

        String logoDataImage = "data:image/png;base64,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";

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
            MOBIGEN DATA CATALOG
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
            MOBIGEN DATA CATALOG
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

    // 동의어 동기화 ESSYNC API
    public Boolean esSynonymSyncUpdate() throws Exception {
        boolean esUpdate = false;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<HashMap> restResponse = restTemplate.getForEntity(synonymsyncUrl, HashMap.class);

            if (restResponse != null && restResponse.getBody() != null) {
                Map<String, Object> body = restResponse.getBody();
                if (body.get("result") != null) {
                    Map<String, Object> bodyResultMap = (Map<String, Object>) body.get("result");
                    if (bodyResultMap != null) {
                        String result = bodyResultMap.get("result").toString();
                        if (result.equals("success")) {
                            esUpdate = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            esUpdate = false;
        }

        return esUpdate;
    }

}
