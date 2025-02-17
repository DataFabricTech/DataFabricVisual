package com.mobigen.datalake.mycatalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class MyCatalogController {
    private final SessionManager sessionManager;
    private final MyCatalogService myCatalogService;

    /**
     * My 조회 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 조회 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/mySearchDataList")
    public Object PostMySearchDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        return myCatalogService.getSearchDataList(params);
    }

    /**
     * My 추출 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 추출 데이터 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/myDataRequestList")
    public Object PostMyDataRequestList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, String> param = new HashMap<>();
        param.put("user_id", user.get("user_id").toString());
        param.put("searchWord", params.get("searchWord"));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf(Integer.parseInt(params.get("limit")) * (Integer.parseInt(params.get("offset")) - 1)));

        int total = myCatalogService.getDataRequestListCount(param);
        
        List<HashMap<String, Object>> list = myCatalogService.getDataRequestList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * My 동의어 삭제 요청 내역 목록 조회
     * @param param                 조회 정보
     * @return                     My 동의어 삭제 요청 내역 목록 조회
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/getMySynonymDeleteRequestList")
    public Object PostMyRequestDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        return myCatalogService.getMySynonymDeleteRequestList(params);
    }

    /**
     * My 동의어 삭제 요청 내역 목록 조회
     * @param param                 조회 정보
     * @return                      My 동의어 삭제 요청 내역 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/mySynonymDeleteRequestList")
    public Object PostMySynonymDeleteRequestList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        return myCatalogService.getSynonymDeleteRequestList(params);
    }

    /**
     * My 추출 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 추출 데이터 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/myAllExtractedDataList")
    public Object PostMyAllExtractedDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, String> param = new HashMap<>();
        param.put("user_id", user.get("user_id").toString());
        param.put("searchWord", params.get("searchWord"));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf(Integer.parseInt(params.get("limit")) * (Integer.parseInt(params.get("offset")) - 1)));

        int total = myCatalogService.getAllExtractedDataListCount(param);
        
        List<Map<String, String>> list = myCatalogService.getAllExtractedDataList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * My 추출 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 추출 데이터 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/myExtractedDataList")
    public Object PostMyExtractedDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, String> param = new HashMap<>();
        param.put("select_type", params.get("select_type").toString());
        param.put("user_id", user.get("user_id").toString());
        param.put("searchType", params.get("searchType"));
        param.put("searchWord", params.get("searchWord"));
        param.put("searchStatus", params.get("searchStatus"));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf(Integer.parseInt(params.get("limit")) * (Integer.parseInt(params.get("offset")) - 1)));

        int total = myCatalogService.getExtractedDataListCount(param);
        
        List<Map<String, String>> list = myCatalogService.getExtractedDataList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * My 찜 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 찜 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/myFavoriteDataList")
    public Object PostMyFavoriteDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, String> param = new HashMap<>();
        param.put("user_id", user.get("user_id").toString());
        param.put("searchType", params.get("searchType"));
        param.put("searchWord", params.get("searchWord"));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf(Integer.parseInt(params.get("limit")) * (Integer.parseInt(params.get("offset")) - 1)));

        int total = myCatalogService.getFavoriteDataListCount(param);
        
        List<Map<String, String>> list = myCatalogService.getFavoriteDataList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;

    }

    /**
     * My 리뷰 데이터 목록 조회
     * @param param                 조회 정보
     * @return                      My 리뷰 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/myCatalog/myReviewDataList")
    public Object PostMyReviewDataList(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Map<String, String> param = new HashMap<>();
        param.put("user_id", user.get("user_id").toString());
        param.put("searchWord", params.get("searchWord"));
        param.put("searchType", params.get("searchType"));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf((Integer.parseInt(params.get("offset")) - 1)));

        int total = myCatalogService.getMyReviewDataListCount(param);
        
        List<Map<String, String>> list = myCatalogService.getMyReviewDataList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }
}