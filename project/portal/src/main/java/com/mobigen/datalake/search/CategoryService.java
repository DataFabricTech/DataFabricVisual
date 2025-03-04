package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.categorycount}")
    private String esSyncUrlCategorycount;

    // 좌측 카테고리 리스트 조회
    public List<Map<String, Object>> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    // 카테고리별 테이블 수 전체 조회
    public Object getTableCountsByClassification() throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 1. 카테고리 정보 테이블 조회
        List<Map<String, Object>> originList = getCategoryList();
        
        // 카테고리 항목 확인
        // [{ id: 1, name: "MG", count: 0 }]
        Map<String, Object> restResponse = getClassificationCountFromApi();
        if (!restResponse.isEmpty()) {
            Map<String, Object> res = (Map<String, Object>) restResponse.get("result");
            if (res.get("result") != null && res.get("result").toString().equals("success")) {                    
                // API에서 받아온 카테고리 매핑 개수 Map
                Map<String, Object> count = (Map<String, Object>) res.get("count");

                // 2. 갱신된 카테고리 카운트 정보와 연동하여 리스트로 출력
                if (originList != null) {
                    for (int i=0; i<originList.size(); i++) {
                        String c_id = originList.get(i).get("id").toString();
                        if (count.get(c_id) != null) {
                            originList.get(i).put("count", count.get(c_id));
                        } else {
                            originList.get(i).put("count", 0);
                        }
                    }
                }

                resultList = originList;
                
            } else {                  
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        return resultList;
    }

    // DB의 카테고리 목록 데이터 반환
    public Object getDbClassification() {
        return categoryMapper.getDbClassification();
    }

    // 카테고리 1레벨 목록 조회
    public List getCategoryListForChart() {
        return categoryMapper.getCategoryListForChart();
    }

    // 상세필터 정보 조회
    public Object getDetailFilters() throws Exception {
        Map<String, Object> detailFilters = new HashMap<>();
        Map<String, Object> categoryInfo = new HashMap<>();
        Map<String, Object> systemInfo = new HashMap<>();
        Map<String, Object> orderInfo = new HashMap<>();
        
        // 카테고리 카운트 정보
        Map<String, Object> countMap = new HashMap<>();
        Map<String, Object> restResponse = getClassificationCountFromApi();
        if (!restResponse.isEmpty()) {
            Map<String, Object> res = (Map<String, Object>) restResponse.get("result");
            if (res.get("result") != null && res.get("result").toString().equals("success")) {                    
                // API에서 받아온 카테고리 매핑 개수 Map
                countMap = (Map<String, Object>) res.get("count");
            }
        }

        // 카테고리 정보
        List<Map<String, Object>> categoryList = categoryMapper.getCategoryListForFilter();
        categoryInfo.put("allChecked", true);
        categoryInfo.put("ord", 1);
        categoryInfo.put("name", "카테고리");
        categoryInfo.put("content", new ArrayList<Map<String, Object>>());

        for (Map<String, Object> category : categoryList) {
            Map<String, Object> content = new HashMap<>();
            if (category.get("name").toString().equals("CATEGORY")) continue;

            content.put("level", category.get("level"));
            content.put("root", category.get("parent"));
            content.put("rootDescription", category.get("parent_description"));
            content.put("id", category.get("id"));
            content.put("name", category.get("name"));
            content.put("description", category.get("description"));
            int count = 0;
            if (countMap.get(category.get("id")) != null) {
                count = (int) countMap.get(category.get("id"));
            }
            content.put("count", count);
            content.put("checked", false);
            ((List<Map<String, Object>>) categoryInfo.get("content")).add(content);
        }
        categoryInfo.put("checkedCount", 0);
        
        // 시스템 타입 정보
        List<Map<String, Object>> systemList = categoryMapper.getSystemListForFilter();
        systemInfo.put("allChecked", true);
        systemInfo.put("ord", 2);
        systemInfo.put("name", "시스템 타입");
        systemInfo.put("content", new ArrayList<Map<String, Object>>());

        // 코드 테이블에 선언한 시스템 타입 항목이 없는 정보는 제거
        List<String> systemTypes = categoryMapper.getSystemTypes();
        systemList.removeIf(item -> !systemTypes.contains(item.get("system_type")));

        int i=0;
        for (String systemType : systemTypes) {
            i = i + 1;
            boolean added = false;
            for (Map<String, Object> system : systemList) {
                // 코드 테이블에 있는 값만 추가
                if (system.get("system_type").toString().equals(systemType)) {

                    Map<String, Object> content = new HashMap<>();

                    content.put("root", "SYSTEMTYPE");
                    content.put("rootDescription", "시스템 타입");
                    
                    content.put("id", i);
                    content.put("name", system.get("system_type"));
                    content.put("description", system.get("system_type"));
                    content.put("count", system.get("type_count"));
                    content.put("checked", false);
                    ((List<Map<String, Object>>) systemInfo.get("content")).add(content);
                    added = true;
                    break;
                }
            }

            if (!added) {
                Map<String, Object> content = new HashMap<>();

                content.put("root", "SYSTEMTYPE");
                content.put("rootDescription", "시스템 타입");
                
                content.put("id", i);
                content.put("name", systemType);
                content.put("description", systemType);
                content.put("count", 0);
                content.put("checked", false);
                ((List<Map<String, Object>>) systemInfo.get("content")).add(content);
            }
        }        
        systemInfo.put("checkedCount", 0);

        // 정렬 오름차순, 내림차순
        orderInfo.put("allChecked", true);
        orderInfo.put("ord", 5);
        orderInfo.put("name", "수집일");
        orderInfo.put("content", new ArrayList<Map<String, Object>>());

        Map<String, Object> ascContent = new HashMap<>();
        ascContent.put("root", "ORDER");
        ascContent.put("rootDescription", "수집일");
        ascContent.put("id", 1);
        ascContent.put("name", "ASC");
        ascContent.put("description", "오름차순");
        ascContent.put("count", 0);
        ascContent.put("checked", false);
        ((List<Map<String, Object>>) orderInfo.get("content")).add(ascContent);
        Map<String, Object> descContent = new HashMap<>();
        descContent.put("root", "ORDER");
        descContent.put("rootDescription", "수집일");
        descContent.put("id", 2);
        descContent.put("name", "DESC");
        descContent.put("description", "내림차순");
        descContent.put("count", 0);
        descContent.put("checked", false);
        ((List<Map<String, Object>>) orderInfo.get("content")).add(descContent);

        orderInfo.put("checkedCount", 0);

        detailFilters.put("CATEGORY", categoryInfo);
        detailFilters.put("SYSTEMTYPE", systemInfo);
        detailFilters.put("ORDER", orderInfo);

        return detailFilters;
    }

    // 카테고리 카운트 조회 API 호출
    public Map<String, Object> getClassificationCountFromApi() throws Exception {
        Map<String, Object> restResponse = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            String url = esSyncBaseUrl + esSyncUrlCategorycount;
            restResponse = restTemplate.getForObject(url, HashMap.class);   
        } catch (Exception e) {
            log.error("Fail to Execute Category Count API");
        }
        return restResponse;
    }
}
