package com.mobigen.datalake.synonym;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.mobigen.datalake.post.PostMapper;
import com.mobigen.datalake.system.mailTemplate.MailTemplateService;
import com.mobigen.datalake.system.user.AdminService;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SynonymService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final SynonymMapper synonymMapper;
    private final AdminService adminService;
    private final MailTemplateService mailTemplateService;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;
    
    @Value("${properties.activeSendMail.synonym}")
	public boolean activeSendMail;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.synonymsync}")
    private String esSyncUrlSynonymsync;
    

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


    // 검색 시 표기용 동의어 항목 조회
    public Map<String, Object> getSynonymListForSearch(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            // 가공된 동의어 목록
            List<Map> updatedTermList = new ArrayList<>();

            // 검색어 목록
            List<String> searchWords = ((List)params.get("searchWords"));

            if (searchWords != null) {
                // 데이터가 있는 경우
                for(int a=0; a<searchWords.size(); a++) {
                    // 대표어 중복 제거 목록 조회
                    List<Map> termList = synonymMapper.getDistinctTermListForSearchOne(searchWords.get(a).toString());
                    if (!termList.isEmpty()) {
                        // 동의어 목록 조회
                        for (int i = 0; i < termList.size(); i++) {
                            // 대표어 1개에 들어가는 목록 생성
                            Map<String, Object> termMap = new LinkedHashMap<>();
                            String term_1 = termList.get(i).get("term").toString();
                            termMap.put("rownum", termList.size() - i);
                            termMap.put("searchWord", searchWords.get(a));
                            termMap.put("term", term_1);
    
                            List synonyms = new ArrayList<>(); // 동의어 추가할 리스트 부분
    
                            List oneList = synonymMapper.getSynonymListOneForSearch(term_1);
                            if (oneList != null) {
                                for (int j=0; j<oneList.size(); j++) {
                                    synonyms.add((((Map<String, Object>) oneList.get(j)).get("synonym")));
                                }
                            }
                            termMap.put("synonyms", synonyms);
    
                            // 가공된 동의어 목록에 가공한 데이터 추가
                            updatedTermList.add(termMap);
                        }
                    } else {
                        // 앞에 검색어가 동일한 데이터가 있는 경우 제거
                        Map<String, Object> termMap = new LinkedHashMap<>();
                        termMap.put("rownum", a);
                        termMap.put("searchWord", searchWords.get(a));
                        termMap.put("term", "");
                        termMap.put("synonyms", new ArrayList<>());
    
                        // 가공된 동의어 목록에 가공한 데이터 추가
                        updatedTermList.add(termMap);
                    }
                }
            }

            result.put("list", updatedTermList);
            
        } catch(Exception e) {
            log.error("동의어 항목 조회에 실패하였습니다.");
            result.put("list", new ArrayList<>());
        }

        return result;
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

        // 요청일
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String formattedDateTime = now.format(formatterDatetime);
        if (params.get("req_date") == null) params.put("req_date", now);

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
                        params.put("status", "승인");
                        sendDeleteResponseMail(params, request);
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
                    params.put("status", "반려");
                    sendDeleteResponseMail(params, request);
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
                params.put("req_date", req.get("req_date"));
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
                        params.put("status", "반려");
                        sendDeleteResponseMail(params, request);
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
                params.put("req_date", req_info.get("req_date"));
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
                        params.put("status", "승인");
                        sendDeleteResponseMail(params, request);
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
        try {
            // 1. 관리자 이메일 기본값 조회
            List<Map<String, Object>> defaultAdminList = synonymMapper.getDefaultAdminList();
            Map<String, Object> mailDefaultAdminMap = new HashMap<>();
            for (Map default_admin : defaultAdminList) {
                if (!ObjectUtils.isEmpty(default_admin.get("user_email"))) {
                    mailDefaultAdminMap.put("default_admin", default_admin);
                    break;
                }
            }
            
            // 2. 참조 관리자 조회
            List<Map> adminList = synonymMapper.getMailUserList();
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
            mailTemplateParam.put("_title_", "동의어 삭제 요청");
            
            String writer = "";
            String user_nm = user.get("username") != null ? user.get("username").toString() : "-";
            String dept_nm = user.get("dept_nm") != null ? user.get("dept_nm").toString() : "-";
            writer = user_nm + " (" + dept_nm + ")";
            mailTemplateParam.put("_writer_", writer);

            // 요청일
            LocalDateTime req_date = params.get("req_date") != null ? (LocalDateTime) params.get("req_date") : null;
            if (req_date != null) {
                DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                String formattedDateTime = req_date.format(formatterDatetime);
                mailTemplateParam.put("_req_date_", formattedDateTime);
            } else {
                mailTemplateParam.put("_req_date_", "-");
            }
            
            mailTemplateParam.put("_term_", params.get("term") != null ? params.get("term").toString() : "-");
            
            // 동의어 목록
            String synonymsText = "";
            List<Map<String, Object>> synonyms = (List<Map<String, Object>>) params.get("synonyms");
            if (synonyms != null && synonyms.size() > 0) {
                synonymsText = synonyms.stream()
                        .map(map -> (String) map.get("synonym"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(","));
            } else {
                synonymsText = "-";
            }
            mailTemplateParam.put("_synonyms_", synonymsText);
            mailTemplateParam.put("_request_", params.get("request") != null ? params.get("request").toString() : "-");
            mailTemplateParam.put("add_sender", sender);
            mailTemplateParam.put("add_recipients", recipients);
            mailTemplateParam.put("add_cc_recipients", ccRecipients);

            // 안내 메일 발송
            // 메일 템플릿 ID 조회
            Map<String, Object> commonCodeParam = new HashMap<>();
            commonCodeParam.put("type", "email_template");
            commonCodeParam.put("name", "SYNONYM_DELETE_REQUEST");
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
        }

    }

    // 메일 처리 - 동의어 삭제 요청 처리 메일
    public void sendDeleteResponseMail(Map<String, Object> params, HttpServletRequest request) throws Exception {
        try {
            // 1. 관리자 이메일 기본값 조회
            List<Map<String, Object>> defaultAdminList = synonymMapper.getDefaultAdminList();
            Map<String, Object> mailDefaultAdminMap = new HashMap<>();
            for (Map default_admin : defaultAdminList) {
                if (!ObjectUtils.isEmpty(default_admin.get("user_email"))) {
                    mailDefaultAdminMap.put("default_admin", default_admin);
                    break;
                }
            }
            
            // 2. 참조 관리자 조회
            List<Map> adminList = synonymMapper.getMailUserList();
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
            for (String key : mailDefaultAdminMap.keySet()) {
                Map<String, Object> senderMap = new HashMap<>();
                // 기본 관리자 계정 중 하나만 이용
                if (((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_email") != null) {
                    senderMap.put("name", ((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_nm").toString());
                    senderMap.put("email", ((Map<String, String>)mailDefaultAdminMap.get(key)).get("user_email").toString());
                    sender.add(senderMap);
                    break;
                }
            }

            // 수신자
            Map<String, Object> recipientsMap = new HashMap<>();
            recipientsMap.put("name", params.get("writer_user_nm"));
            recipientsMap.put("email", params.get("writer_email"));
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
            mailTemplateParam.put("_title_", "동의어 삭제 요청 결과");
            mailTemplateParam.put("_writer_", params.get("writer_user_nm"));

            // 요청일 (req_date)
            // req_date 문자열을 ZonedDateTime 객체로 파싱
            String req_date = params.get("req_date") != null ? params.get("req_date").toString() : null;
            if (req_date != null) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(req_date);
                DateTimeFormatter formatterDatetime = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                String formattedDateTime = zonedDateTime.format(formatterDatetime);
                mailTemplateParam.put("_req_date_", formattedDateTime);
            } else {
                mailTemplateParam.put("_req_date_", "-");
            }
            
            mailTemplateParam.put("_term_", params.get("term") != null ? params.get("term").toString() : "-");
            
            // 동의어 목록
            String synonymsText = "";
            List<Map<String, Object>> synonyms = (List<Map<String, Object>>) params.get("synonyms");
            if (synonyms != null && synonyms.size() > 0) {
                synonymsText = synonyms.stream()
                        .map(map -> (String) map.get("synonym"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(","));
            } else {
                synonymsText = "-";
            }
            mailTemplateParam.put("_synonyms_", synonymsText);
            mailTemplateParam.put("_status_", params.get("status") != null ? params.get("status").toString() : "-");
            mailTemplateParam.put("_request_", params.get("request") != null ? params.get("request").toString() : "-");
            mailTemplateParam.put("_response_", params.get("response") != null ? params.get("response").toString() : "-");
            mailTemplateParam.put("add_sender", sender);
            mailTemplateParam.put("add_recipients", recipients);
            mailTemplateParam.put("add_cc_recipients", ccRecipients);

            // 안내 메일 발송
            // 메일 템플릿 ID 조회
            Map<String, Object> commonCodeParam = new HashMap<>();
            commonCodeParam.put("type", "email_template");
            commonCodeParam.put("name", "SYNONYM_DELETE_RESPONSE");
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
        }
    }

    // 동의어 동기화 ESSYNC API
    public Boolean esSynonymSyncUpdate() throws Exception {
        boolean esUpdate = false;

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = esSyncBaseUrl + esSyncUrlSynonymsync;
            ResponseEntity<HashMap> restResponse = restTemplate.getForEntity(url, HashMap.class);

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
            log.error("esSynonymSync API 호출 오류");
            esUpdate = false;
        }

        return esUpdate;
    }

}


/* // 동의어 삭제 요청 등록 메일
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
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">요청자</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_writer_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">요청일</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_req_date_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">대표어</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_term_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">삭제요청 목록</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_synonyms_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">삭제요청 사유</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_request_</td>
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

 /* // 동의어 삭제 요청 처리 결과 메일
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
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">요청자</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_writer_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">요청일</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_req_date_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">대표어</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_term_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">삭제요청 목록</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_synonyms_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">삭제요청 사유</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_request_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">결과</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_status_</td>
                            </tr>
                            <tr>
                                <th style="padding: 10px; border-bottom: 1px solid #ddd; text-align: center; background-color: #f4f4f4; font-weight: 700; width: 30%; border-left: none; border-right: none;">반려 사유</th>
                                <td style="padding: 10px; border-bottom: 1px solid #ddd; text-align: left; color: #333; font-size: 14px; width: 70%; border-left: none; border-right: none;">_response_</td>
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