package com.mobigen.datalake.synonym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RequestMapping("/api/synonym")
@Controller
@AllArgsConstructor
public class SynonymController {
    private final SessionManager sessionManager;
    private final SynonymService synonymService;

    // 동의어 목록 조회
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getSynonymList(@RequestParam String searchType,
            @RequestParam String searchWord,
            @RequestParam int pageSize,
            @RequestParam int pnum) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        if (pageSize != -1 && pnum != -1) {
            param.put("limit", String.valueOf(pageSize));
            param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        }

        int total = synonymService.getSynonymListTotal(param);

        List<Map> list = synonymService.getSynonymList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    // 동의어 목록 조회 (통합검색 표기용)
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getSynonymList(@RequestBody Map<String, Object> params) throws Exception {
        return synonymService.getSynonymListForSearch(params);
    }

    /**
     * 동의어 삭제 요청 내역 목록 조회
     * 
     * @param searchType 검색 조건
     * @param searchWord 검색어
     * @param status     동의어 삭제 요청 내역
     * @return 동의어 삭제 요청 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/deleteRequestList")
    public Object getDeleteRequestList(@RequestParam String status,
            @RequestParam String searchType,
            @RequestParam String searchWord,
            @RequestParam Boolean isAdmin,
            HttpServletRequest request) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("status", status);
        params.put("searchType", searchType);
        params.put("searchWord", searchWord);

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("userId", user.get("user_id").toString());

        if (user.get("authority").toString().equals("Super-Admin")
                || user.get("authority").toString().equals("Manager")) {
            if (isAdmin)
                return synonymService.getDeleteRequestListAdmin(params);
            else
                return synonymService.getDeleteRequestList(params);

        } else {
            return synonymService.getDeleteRequestList(params);
        }
    }

    /**
     * 대표어 1건 동의어 목록 조회
     * 
     * @param term 검색 대표어
     * @return 대표어 1건 동의어 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/listOne")
    public Object getSynonymList(@RequestParam String term) throws Exception {
        return synonymService.getSynonymListOne(term);
    }

    /**
     * 대표어 1건 동의어 추가
     * 
     * @param params 동의어 등록 정보
     * @return 동의어 등록 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/addSynonym")
    @ResponseBody
    public Object addSynonym(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("create_user", user.get("user_id").toString());
        return synonymService.addSynonym(params);
    }

    /**
     * 동의어 세트 신규 추가
     * 
     * @param params 동의어 세트 등록 정보
     * @return 동의어 세트 등록 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/addSynonymSet")
    @ResponseBody
    public Object addSynonymSet(@RequestBody List<Map<String, Object>> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("create_user", user.get("user_id").toString());

        List list = new ArrayList();
        for (Map<String, Object> param : params) {
            list.add(param);
        }
        paramsMap.put("addedSynonymList", list);

        return synonymService.addSynonymSet(paramsMap);
    }

    /**
     * 동의어 세트 수정
     * 
     * @param params 동의어 세트 수정 정보
     * @return 동의어 세트 수정 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/modifySynonymSet")
    @ResponseBody
    public Object modifySynonymSet(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("create_user", user.get("user_id").toString());
        params.put("update_user", user.get("user_id").toString());
        return synonymService.modifySynonymSet(params);
    }

    /**
     * 동의어 삭제 요청
     * 
     * @param params 동의어 삭제 요청에 필요한 데이터
     * @return 동의어 삭제 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/requestDeleteSynonym")
    public Object requestDeleteSynonym(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("req_user", user.get("user_id").toString());
        return synonymService.requestDeleteSynonym(params, request);
    }

    /**
     * 동의어 삭제 요청 상태 조회
     * 
     * @id 동의어 삭제 요청 정보
     * @return 동의어 삭제 요청 승인 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/getDeleteRequestOneStatus/{id}")
    @ResponseBody
    public Object getDeleteRequestOneStatus(@PathVariable String id) throws Exception {
        return synonymService.getDeleteRequestOneStatus(id);
    }

    /**
     * 동의어 삭제 요청 내역 1건 요청 취소
     * 
     * @id 동의어 삭제 요청 ID
     * @return 동의어 삭제 요청 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @DeleteMapping("/deleteRequestOne/{id}")
    public Object getDeleteRequestList(@PathVariable long id) throws Exception {
        return synonymService.getDeleteRequestOne(id);
    }

    /**
     * 동의어 삭제 요청 승인
     * 
     * @param params 동의어 삭제 요청 승인 정보
     * @return 동의어 삭제 요청 승인 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/confirmDeleteRequestOne")
    @ResponseBody
    public Object confirmDeleteRequestOne(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("userId", user.get("user_id").toString());
        return synonymService.confirmDeleteRequestOne(params, request);
    }

    /**
     * 동의어 삭제 요청 반려
     * 
     * @param params 동의어 삭제 요청 반려 정보
     * @return 동의어 삭제 요청 반려 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/rejectDeleteRequestOne")
    @ResponseBody
    public Object rejectDeleteRequestOne(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("userId", user.get("user_id").toString());
        return synonymService.rejectDeleteRequestOne(params, request);
    }

    /**
     * 동의어 여러건 삭제
     * 
     * @param params 동의어 삭제 리스트
     * @return 동의어 삭제 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteMySynonym")
    public Object deleteMySynonym(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        return synonymService.deleteMySynonym(params, request);
    }

    /**
     * 동의어 여러건 삭제
     * 
     * @param params 동의어 삭제 리스트
     * @return 동의어 삭제 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteSynonym")
    public Object deleteSynonym(@RequestBody List<Map<String, Object>> params) throws Exception {
        return synonymService.deleteSynonym(params);
    }

    /**
     * 동의어 삭제 요청 일괄 승인
     * 
     * @param params 동의어 삭제 요청 일괄 승인 정보
     * @return 동의어 삭제 요청 일괄 승인 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/confirmDeleteRequestAll")
    @ResponseBody
    public Object confirmDeleteRequestAll(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("res_user", user.get("user_id").toString());
        return synonymService.confirmDeleteRequestAll(params, request);
    }

    /**
     * 동의어 삭제 요청 일괄 반려
     * 
     * @param params 동의어 삭제 요청 일괄 반려 정보
     * @return 동의어 삭제 요청 일괄 반려 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/rejectDeleteRequestAll")
    @ResponseBody
    public Object rejectDeleteRequestAll(@RequestBody Map<String, Object> params, HttpServletRequest request)
            throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("res_user", user.get("user_id").toString());
        return synonymService.rejectDeleteRequestAll(params, request);
    }
}