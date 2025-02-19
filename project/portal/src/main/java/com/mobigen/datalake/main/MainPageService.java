package com.mobigen.datalake.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.search.SearchDetailService;
import com.mobigen.datalake.search.SearchService;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MainPageMapper mainPageMapper;
    private final SessionManager sessionManager;
    private final SearchService searchService;

    @Value("${properties.fastapi.server.host}")
    private String fastApiHost;
    @Value("${properties.fastapi.server.port}")
    private String fastApiPort;

    // 추천 검색어 조회
    public Object getRecommendedWordList() throws Exception {
        List<Map<String, Object>> RecommendedWordList = mainPageMapper.getRecommendedWordList();
        return RecommendedWordList;
    }

    // 전체 데이터 수, 데이터 활용 횟수, 신규 데이터 조회
    public Object getDataCountData() throws Exception {
        Map<String, Object> dataCountMap = new HashMap<>();
        dataCountMap.put("totalDataCount", mainPageMapper.getTotalDataCount());
        dataCountMap.put("useDataCount", mainPageMapper.getUseDataCount());
        dataCountMap.put("newDataCount", mainPageMapper.getNewDataCount());
        return dataCountMap;
    }

    // 공지사항, FAQ 목록 조회
    public Object getPostListData() throws Exception {
        Map<String, Object> postDataMap = new HashMap<>();
        postDataMap.put("noticeList", mainPageMapper.getNoticeList());
        postDataMap.put("faqList", mainPageMapper.getFaqList());
        return postDataMap;
    }

    // 활용사례 조회
    public Object getShareDataList() throws Exception {
        List<Map<String, Object>> shareDataList = mainPageMapper.getShareDataList();
        return shareDataList;
    }

    // 인기 데이터 > 조회수가 가장 높은 순 조회 (ES API)
    public Object getPopularDataList(HttpServletRequest request) throws Exception {
        return getPopularData(request);
    }

    // 인기 데이터 > 최신 등록 데이터 순 조회 (ES API)
    public Object getLatestDataList(HttpServletRequest request) throws Exception {
        return getLatestData(request);
    }

    // 인기 데이터 > 추천수가 가장 높은 순 조회 (ES API)
    public Object getRecommendDataList(HttpServletRequest request) throws Exception {
        return getRecommendData(request);
    }

    // My 데이터 요청내역 조회
    public Object getMyRequestDataList(String user_id) throws Exception {
        List<Map<String, Object>> dataRequestList = mainPageMapper.getMyRequestDataList(user_id);
        return dataRequestList;
    }

    private final SearchDetailService searchDetailService;

    // My 찜데이터 조회
    public Object getMyFavoriteDataList(String user_id) throws Exception {
        // 결과를 저장할 리스트
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyFavoriteDataList(user_id);
    
        // JSON 데이터를 처리하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();
    
        // 각 guid에 대해 detail 데이터를 가져와서 shareDataList에 추가
        for (int i = 0; i < shareDataList.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("meta_id", shareDataList.get(i).get("meta_id"));
    
            // getDetailData 호출
            Object detailData = searchDetailService.getDetailData(params);
    
            // detailData가 JSON 형식일 경우 처리
            if (detailData != null) {
                // JSON 결과를 Map 형태로 변환
                Map<String, Object> detailDataMap = objectMapper.convertValue(detailData, Map.class);
    
                // "data" 필드가 있는지 확인
                if (detailDataMap.containsKey("data")) {
                    Object data = detailDataMap.get("data");
    
                    // "data"가 LinkedHashMap인 경우 처리
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

                    // "entity" 키가 있는지 확인하여 shareDataList에 추가
                    if (dataMap.containsKey("entity")) {
                        shareDataList.get(i).put("entity", dataMap.get("entity"));
                    }
                }
            }
        }
    
        // shareDataList를 리턴
        return shareDataList;
    }

    // My 리뷰 데이터 조회
    public Object getMyReviewDataList(String user_id) throws Exception {
        // 결과를 저장할 리스트
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyReviewDataList(user_id);
    
        // JSON 데이터를 처리하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();
    
        // 각 guid에 대해 detail 데이터를 가져와서 shareDataList에 추가
        for (int i = 0; i < shareDataList.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("meta_id", shareDataList.get(i).get("meta_id"));
    
            // getDetailData 호출
            Object detailData = searchDetailService.getDetailData(params);
    
            // detailData가 JSON 형식일 경우 처리
            if (detailData != null) {
                // JSON 결과를 Map 형태로 변환
                Map<String, Object> detailDataMap = objectMapper.convertValue(detailData, Map.class);
    
                // "data" 필드가 있는지 확인
                if (detailDataMap.containsKey("data")) {
                    Object data = detailDataMap.get("data");
    
                    // "data"가 LinkedHashMap인 경우 처리
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

                    // "entity" 키가 있는지 확인하여 shareDataList에 추가
                    if (dataMap.containsKey("entity")) {
                        shareDataList.get(i).put("entity", dataMap.get("entity"));
                    }
                }
            }
        }
    
        // shareDataList를 리턴
        return shareDataList;
    }

    // My 데이터 요청내역 조회
    public Object getMyExtractedDataList(String user_id) throws Exception {
        List<Map<String, Object>> dataRequestList = mainPageMapper.getMyExtractedDataList(user_id);
        return dataRequestList;
    }

    // ES에서 조회수가 많은 데이터 (4건) 조회
    public Object getPopularData(HttpServletRequest request) throws Exception {
        // // 1. 조회수 높은 데이터 조회
        List<Map<String, Object>> popularList = mainPageMapper.getPopularDataList();

        List<String> guids = new ArrayList<>();
        for (int i=0; i<popularList.size(); i++) {
            Map<String, Object> popularData = (Map<String, Object>) popularList.get(i);
            guids.add(popularData.get("meta_id").toString());
        }

        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        
        // FastAPI의 URL 생성
        String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/popularData";
        log.info("call fastapi : " + fastUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("guids", guids);
        Map<String, Object> restResponse = restTemplate.postForObject(fastUrl, requestBody, HashMap.class);
        
        if (!restResponse.isEmpty()) {
            Map<String, Object> body = (Map<String, Object>)restResponse.get("result");
            if (body != null) {
                result.put("result", body.get("result"));
                result.put("results", body.get("results"));
                result.put("total_count", body.get("total_count"));
            }
            
            if (body.get("result") != null) {
                List<Map<String, Object>> entities = (List<Map<String, Object>>)body.get("results");
                // 3. 조회수가 높은 순서로 정렬
                // 조회 순서대로 받아온 guid 항목을 하나씩 탐색
                int matchedCount = 0;
                List<Map<String, Object>> sortedList = new ArrayList<>();
                for (int i=0; i<popularList.size(); i++) {
                    if (entities != null) {
                        for (int j=0; j<entities.size(); j++) {
                            if (popularList.get(i).get("meta_id").toString().equals(entities.get(j).get("meta_id").toString())) {
                                matchedCount++;
                                sortedList.add(entities.get(j));
                                if (matchedCount == 4) break;
                            }
                        }
                    }
                    if (matchedCount == 4) break;
                }
                result.put("results", sortedList);
            }
        }
        return result;
    }

    // ES 에서 최신 등록 데이터 (4건) 조회
    public Object getLatestData(HttpServletRequest request) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        
        // FastAPI의 URL 생성
        String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/latestData";
        log.info("call fastapi : " + fastUrl);

        Map<String, Object> requestBody = new HashMap<>();

        // 사용자 데이터 권한(role_id) 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        List<String> role_ids = searchService.getRoleIds(user);
        
        requestBody.put("roleids", role_ids);
        
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(fastUrl, requestBody, HashMap.class);
        
        if (responseEntity != null) {
            Map<String, Object> body = responseEntity.getBody();
            if (body != null) {
                LinkedHashMap res = (LinkedHashMap) body.get("result");
                if (res != null) {
                    result.put("result", res.get("result"));

                    List<Map<String, Object>> results = (List<Map<String, Object>>) res.get("results");
                    List<Map<String, Object>> newResults = new ArrayList<>();
                    if (results != null) {
                        for (int i=0; i<results.size(); i++) {
                            if (i > 4) break;
                            newResults.add(results.get(i));
                        }
                    }
                    result.put("results", newResults); 
                    result.put("total_count", res.get("total_count"));
                }
            }    
        }

        return result;
    }

    // ES 에서 추천수가 많은 데이터 (4건) 조회
    public Object getRecommendData(HttpServletRequest request) throws Exception {
        // 1. 추천수 높은 데이터 조회
        List<Map<String, Object>> recommendList = mainPageMapper.getRecommendDataList();
        List<String> guids = new ArrayList<>();
        for (int i=0; i<recommendList.size(); i++) {
            Map<String, Object> popularData = (Map<String, Object>) recommendList.get(i);
            guids.add(popularData.get("meta_id").toString());
        }

        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        
        // FastAPI의 URL 생성
        String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/popularData";
        log.info("call fastapi : " + fastUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("guids", guids);
        Map<String, Object> restResponse = restTemplate.postForObject(fastUrl, requestBody, HashMap.class);
        
        if (!restResponse.isEmpty()) {
            Map<String, Object> body = (Map<String, Object>)restResponse.get("result");
            if (body != null) {
                result.put("result", body.get("result"));
                result.put("results", body.get("results"));
                result.put("total_count", body.get("total_count"));
            }
            
            if (body.get("result") != null) {
                List<Map<String, Object>> entities = (List<Map<String, Object>>)body.get("results");
                // 3. 추천수가 높은 순서로 정렬
                // 추천 순서대로 받아온 guid 항목을 하나씩 탐색
                int matchedCount = 0;
                List<Map<String, Object>> sortedList = new ArrayList<>();
                for (int i=0; i<recommendList.size(); i++) {
                    if (entities != null) {
                        for (int j=0; j<entities.size(); j++) {
                            if (recommendList.get(i).get("meta_id").toString().equals(entities.get(j).get("meta_id").toString())) {
                                matchedCount++;
                                sortedList.add(entities.get(j));
                                if (matchedCount == 4) break;
                            }
                        }
                    }
                    if (matchedCount == 4) break;
                }
                result.put("results", sortedList);
            }
        }
        return result;
    }

    public Object getChartCategoryList() throws Exception {
        return mainPageMapper.getChartCategoryList();
    }

    public Map<String, Object> getMainBannerSubject() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getMainBannerSubject();
            if (list != null && list.size() > 0) {
                result.put("subjectList", list);
            } else {
                result.put("subjectList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("subjectList", new ArrayList<>());
        }

        return result;
    }

    public Object updateCodeTable(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("list");

            if (list != null && list.size() > 0) {
                int updateCnt = 0;
                int saveCnt = 0;
                for (int i=0; i<list.size(); i++) {
                    // 기존 데이터 조회
                    Map<String, Object> originInfo = mainPageMapper.getCodeInfo(list.get(i));
                    if (originInfo != null) {
                        updateCnt = mainPageMapper.updateCodeTableFromName(list.get(i));
                    } else {
                        // 기존 데이터가 없는 경우 신규 데이터 추가
                        saveCnt += mainPageMapper.insertCodeTable(list.get(i));
                    }
                }
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail to save layout Info");
            result.put("result", false);
        }

        return result;
    }

    public Map<String, Object> getLayoutList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getLayoutList();
            if (list != null && list.size() > 0) {
                result.put("layoutList", list);
            } else {
                result.put("layoutList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("layoutList", new ArrayList<>());
        }

        return result;
    }

    public Map<String, Object> getFooterList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getFooterList();
            if (list != null && list.size() > 0) {
                result.put("footerList", list);
            } else {
                result.put("footerList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("footerList", new ArrayList<>());
        }

        return result;
    }
}
