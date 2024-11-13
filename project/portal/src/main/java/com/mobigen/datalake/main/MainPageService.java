package com.mobigen.datalake.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.atlas.AtlasRestAPI;
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
    private final AtlasRestAPI atlasRestAPI;

    @Value("${solr.isProd}")
    private boolean isProd;

    @Value("${solr.type-name}")
    private String typeName;

    @Value("${solr.qualified-name}")
    private String qualifiedName;

    @Value("${solr.status}")
    private String status;

    @Value("${properties.isSumExternal}")
    private boolean isSumExternal;

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
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyRequestDataList(user_id);
        return shareDataList;
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

        // 1. 조회수 높은 데이터 조회
        List<Map<String, Object>> popularList = mainPageMapper.getPopularDataList();
        
        // 2. 해당 조회수 높은 데이터들을 이용하여 guid를 이용하여 Atlas API로 상세정보 검색
        String URL = "/search/basic";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("excludeDeletedEntities", true);
        params.put("includeClassificationAttributes", true);
        params.put("includeSubClassifications", true);
        params.put("includeSubTypes", true);

        Map<String, Object> entityFilters = new HashMap<>();

        List<Map<String, Object>> criterion = new ArrayList<>();
        for (int i=0; i<popularList.size(); i++) {
            Map<String, Object> criterionMap = new HashMap<>();
            Map<String, Object> popularData = (Map<String, Object>) popularList.get(i);
            criterionMap.put("attributeName", "__guid");
            criterionMap.put("operator", "eq");
            criterionMap.put("attributeValue", popularData.get("guid"));
            criterion.add(criterionMap);
        }
        entityFilters.put("condition", "OR");
        entityFilters.put("criterion", criterion);


        params.put("entityFilters", entityFilters);
        
        params.put("attributes", Arrays.asList("__guid", "comment"));
        // params.put("sortBy", "__timestamp");
        // params.put("sortOrder", "DESCENDING");
        params.put("limit", 50);
        params.put("offset", 0);
        params.put("typeName", "hive_table");
        params.put("classification", null);
        params.put("tagFilters", null);
        params.put("termName", null);

        JsonResult result = atlasRestAPI.post(URL, params);
        Map<String, Object> resultMap = (Map<String, Object>) result.getData();
        if (resultMap != null) {
            List<Map<String, Object>> entities = (List<Map<String, Object>>) resultMap.get("entities");
            // 3. 조회수가 높은 순서로 정렬
            // 조회 순서대로 받아온 guid 항목을 하나씩 탐색
            int matchedCount = 0;
            List<Map<String, Object>> sortedList = new ArrayList<>();
            for (int i=0; i<popularList.size(); i++) {
                if (entities != null) {
                    for (int j=0; j<entities.size(); j++) {
                        if (popularList.get(i).get("guid").toString().equals(entities.get(j).get("guid").toString())) {
                            matchedCount++;
                            sortedList.add(entities.get(j));
                            if (matchedCount == 6) break;
                        }
                    }
                }
                if (matchedCount == 6) break;
            }

            // 4. 카테고리, 시스템 가공 후 반환
            return processSysCatSort(sortedList);
        }

        return new ArrayList<>();
    }

    // Atlas에서 최신 등록 데이터 (6건) 조회
    public Object getLatestData() throws Exception {
        String URL = "/search/basic";

        StringBuffer searchQuery = new StringBuffer();
        searchQuery.append(typeName + ":" + "hive_table");
        searchQuery.append(" AND ");
        searchQuery.append(status + ":ACTIVE");

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("excludeDeletedEntities", true);
            put("includeSubClassifications", true);
            put("excludeDeletedEntities", true);
            put("includeSubClassifications", true);
            put("includeSubTypes", true);
            put("includeClassificationAttributes", true);
            put(
                "entityFilters",
                new HashMap() {{
                    put("condition", "AND");
                    put(
                        "criterion",
                        Arrays.asList(
                            new HashMap() {{
                                put("attributeName", "__timestamp");
                                put("operator", "timerange");
                                put("attributeValue", "LAST_30_DAYS");
                            }}
                        )
                    );
                }}
            );
            put("attributes", Arrays.asList("comment", "name", "description"));
            put("sortBy", "__timestamp");
            put("sortOrder", "DESCENDING");
            put("limit", 6);
            put("offset", 0);
            put("typeName", "hive_table");
            put("classification", null);
            put("termName", null);
            put("query", searchQuery);
            
        }};

        JsonResult result = atlasRestAPI.post(URL, params);
        Map<String, Object> resultMap = (Map<String, Object>) result.getData();


        Object sortedList = new ArrayList<>();
        if (resultMap.get("entities") != null) {
            sortedList = resultMap.get("entities");
        }

        return processSysCatSort(sortedList);
    }

    // Atlas에서 추천수가 많은 데이터 (6건) 조회
    public Object getRecommendData() throws Exception {

        // 1. 추천수 높은 데이터 조회
        List<Map<String, Object>> recommendList = mainPageMapper.getRecommendDataList();
        
        // 2. 해당 조회수 높은 데이터들을 이용하여 guid를 이용하여 Atlas API로 상세정보 검색
        String URL = "/search/basic";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("excludeDeletedEntities", true);
        params.put("includeClassificationAttributes", true);
        params.put("includeSubClassifications", true);
        params.put("includeSubTypes", true);

        Map<String, Object> entityFilters = new HashMap<>();

        List<Map<String, Object>> criterion = new ArrayList<>();
        for (int i=0; i<recommendList.size(); i++) {
            Map<String, Object> criterionMap = new HashMap<>();
            Map<String, Object> recommendData = (Map<String, Object>) recommendList.get(i);
            criterionMap.put("attributeName", "__guid");
            criterionMap.put("operator", "eq");
            criterionMap.put("attributeValue", recommendData.get("guid"));
            criterion.add(criterionMap);
        }
        entityFilters.put("condition", "OR");
        entityFilters.put("criterion", criterion);


        params.put("entityFilters", entityFilters);
        
        params.put("attributes", Arrays.asList("__guid", "comment"));
        // params.put("sortBy", "__timestamp");
        // params.put("sortOrder", "DESCENDING");
        params.put("limit", 50);
        params.put("offset", 0);
        params.put("typeName", "hive_table");
        params.put("classification", null);
        params.put("tagFilters", null);
        params.put("termName", null);

        JsonResult result = atlasRestAPI.post(URL, params);
        Map<String, Object> resultMap = (Map<String, Object>) result.getData();
        if (resultMap != null) {
            List<Map<String, Object>> entities = (List<Map<String, Object>>) resultMap.get("entities");
            // 3. 조회수가 높은 순서로 정렬
            // 조회 순서대로 받아온 guid 항목을 하나씩 탐색
            int matchedCount = 0;
            List<Map<String, Object>> sortedList = new ArrayList<>();
            for (int i=0; i<recommendList.size(); i++) {
                if (entities != null) {
                    for (int j=0; j<entities.size(); j++) {
                        if (recommendList.get(i).get("guid").toString().equals(entities.get(j).get("guid").toString())) {
                            matchedCount++;
                            sortedList.add(entities.get(j));
                            if (matchedCount == 6) break;
                        }
                    }
                }
                if (matchedCount == 6) break;
            }

            // 4. 카테고리, 시스템 가공 후 반환
            return processSysCatSort(sortedList);
        }

        return new ArrayList<>();
    }

    public Object getChartCategoryList() throws Exception {
        return mainPageMapper.getChartCategoryList();
    }

    public Object processSysCatSort(Object sortedList) throws Exception {

        // 1. 카테고리, 시스템이 들어있는 항목 확인
        for (int a=0; a <((List<Map<String, Object>>) sortedList).size(); a++) {
            List<String> classificationNames = (List<String>) ((List<Map<String, Object>>) sortedList).get(a).get("classificationNames");
            List<String> sysList = new ArrayList<>();
            List<String> catList = new ArrayList<>();
            List<String> newClassificationNames = new ArrayList<>();
            for (int i=0; i<classificationNames.size(); i++) {
                if (classificationNames.get(i) != null) {
                    String cn = classificationNames.get(i);
                    // 2. 시스템 항목 1개 추출
                    if (cn.startsWith("SYS")) {
                        sysList.add(cn);
                        continue;
                    }

                    // 3. 카테고리 항목 1개 추출
                    if (cn.startsWith("CAT")) {
                        catList.add(cn);
                        continue;
                    }
                }
            }

            // 4. 시스템 항목, 카테고리 항목 1개씩 추가
            if (sysList.size() > 0) {
                newClassificationNames.add(sysList.get(0));
            }
            if (catList.size() > 0) {
                newClassificationNames.add(catList.get(0));
            }

            // 3. classificationNames에 덮어쓰기
            ((List<Map<String, Object>>) sortedList).get(a).put("classificationNames", newClassificationNames);
        }
        return sortedList;
    }
}
