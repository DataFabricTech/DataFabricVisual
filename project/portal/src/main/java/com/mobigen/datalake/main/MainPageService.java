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
import com.mobigen.framework.result.JsonResult;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MainPageMapper mainPageMapper;
    @Value("${properties.isSumExternal}")
    private boolean isSumExternal;
    
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

        // 전체 데이터수 => (데이터레이크) or (데이터레이크 + 외부데이터) 적용 여부
        if (isSumExternal) {
            // 외부데이터 전체 개수 합산
            int lakeCount = mainPageMapper.getTotalDataCount();
            int exCount = mainPageMapper.getExternalTotalCount();
            dataCountMap.put("totalDataCount", lakeCount + exCount);
        } else {
            dataCountMap.put("totalDataCount", mainPageMapper.getTotalDataCount());
        }
        
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

    // 인기 데이터 > 조회수가 가장 높은 순 조회 (Atlas API)
    public Object getPopularDataList() throws Exception {
        return getPopularData();
    }

    // 인기 데이터 > 최신 등록 데이터 순 조회 (Atlas API)
    public Object getLatestDataList() throws Exception {
        return getLatestData();
    }

    // 인기 데이터 > 추천수가 가장 높은 순 조회 (Atlas API)
    public Object getRecommendDataList() throws Exception {
        return getRecommendData();
    }

    // My 데이터 요청내역 조회
    public Object getMyRequestDataList(String user_id) throws Exception {
        List<Map<String, Object>> dataRequestList = mainPageMapper.getMyRequestDataList(user_id);
        return dataRequestList;
    }

    private final SearchDetailService searchDetailService;

    // My 찜데이터 조회
    public Object getMyWishDataList(String user_id) throws Exception {
        // 결과를 저장할 리스트
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyWishDataList(user_id);
    
        // JSON 데이터를 처리하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();
    
        // 각 guid에 대해 detail 데이터를 가져와서 shareDataList에 추가
        for (int i = 0; i < shareDataList.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("guid", shareDataList.get(i).get("guid"));
    
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

    // Atlas에서 조회수가 많은 데이터 (6건) 조회
    public Object getPopularData() throws Exception {
        // // 1. 조회수 높은 데이터 조회
        List<Map<String, Object>> popularList = mainPageMapper.getPopularDataList();

        List<String> guids = new ArrayList<>();
        for (int i=0; i<popularList.size(); i++) {
            Map<String, Object> popularData = (Map<String, Object>) popularList.get(i);
            guids.add(popularData.get("guid").toString());
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
                            if (popularList.get(i).get("guid").toString().equals(entities.get(j).get("id").toString())) {
                                matchedCount++;
                                sortedList.add(entities.get(j));
                                if (matchedCount == 6) break;
                            }
                        }
                    }
                    if (matchedCount == 6) break;
                }
                result.put("results", sortedList);
            }
        }
        return result;
    }

    // Atlas에서 최신 등록 데이터 (6건) 조회
    public Object getLatestData() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        
        // FastAPI의 URL 생성
        String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/latestData";
        log.info("call fastapi : " + fastUrl);

        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(fastUrl, HashMap.class);
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
                            if (i > 5) break;
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

    // Atlas에서 추천수가 많은 데이터 (6건) 조회
    public Object getRecommendData() throws Exception {
        // 1. 추천수 높은 데이터 조회
        List<Map<String, Object>> recommendList = mainPageMapper.getRecommendDataList();
        List<String> guids = new ArrayList<>();
        for (int i=0; i<recommendList.size(); i++) {
            Map<String, Object> popularData = (Map<String, Object>) recommendList.get(i);
            guids.add(popularData.get("guid").toString());
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
                            if (recommendList.get(i).get("guid").toString().equals(entities.get(j).get("id").toString())) {
                                matchedCount++;
                                sortedList.add(entities.get(j));
                                if (matchedCount == 6) break;
                            }
                        }
                    }
                    if (matchedCount == 6) break;
                }
                result.put("results", sortedList);
            }
        }
        return result;
    }

    public Object getChartCategoryList() throws Exception {
        return mainPageMapper.getChartCategoryList();
    }
}
