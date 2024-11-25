package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    // 좌측 카테고리 리스트 조회
    public List<Map<String, Object>> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    // 카테고리별 테이블 수 전체 조회
    public Object getTableCountsByClassification() {
        return categoryMapper.getTableCountsByClassification();
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
    public Object getDetailFilters() {
        Map<String, Object> detailFilters = new HashMap<>();
        Map<String, Object> categoryInfo = new HashMap<>();
        Map<String, Object> systemInfo = new HashMap<>();
        Map<String, Object> metaInfo = new HashMap<>();
        Map<String, Object> tagInfo = new HashMap<>();
        Map<String, Object> orderInfo = new HashMap<>();
        
        // 카테고리 정보
        List<Map<String, Object>> categoryList = categoryMapper.getCategoryListForFilter();
        categoryInfo.put("all", true);
        categoryInfo.put("ord", 1);
        categoryInfo.put("name", "카테고리");
        categoryInfo.put("content", new ArrayList<Map<String, Object>>());

        for (Map<String, Object> category : categoryList) {
            Map<String, Object> content = new HashMap<>();
            if (category.get("name").toString().equals("CATEGORY")) continue;

            content.put("root", category.get("parent"));
            content.put("rootDescription", category.get("parent_description"));
            content.put("id", category.get("id"));
            content.put("name", category.get("name"));
            content.put("description", category.get("description"));
            content.put("count", category.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) categoryInfo.get("content")).add(content);
        }
        
        // 시스템 타입 정보
        List<Map<String, Object>> systemList = categoryMapper.getSystemListForFilter();
        systemInfo.put("all", true);
        systemInfo.put("ord", 2);
        systemInfo.put("name", "시스템 타입");
        systemInfo.put("content", new ArrayList<Map<String, Object>>());

        int i=0;
        for (Map<String, Object> system : systemList) {
            Map<String, Object> content = new HashMap<>();

            content.put("root", "SYSTEMTYPE");
            content.put("rootDescription", "시스템 타입");
            i = i + 1;
            content.put("id", i);
            content.put("name", system.get("systemtype"));
            content.put("description", system.get("systemtype"));
            content.put("count", system.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) systemInfo.get("content")).add(content);
        }
        systemInfo.put("count", 0);

        // 메타 타입 정보
        List<Map<String, Object>> metaList = categoryMapper.getMetaListForFilter();
        metaInfo.put("all", true);
        metaInfo.put("ord", 3);
        metaInfo.put("name", "메타 타입");
        metaInfo.put("content", new ArrayList<Map<String, Object>>());

        i=0;
        for (Map<String, Object> meta : metaList) {
            Map<String, Object> content = new HashMap<>();

            content.put("root", "METATYPE");
            content.put("rootDescription", "메타 타입");
            i = i + 1;
            content.put("id", i);
            content.put("name", meta.get("metatype"));
            content.put("description", meta.get("metatype"));
            content.put("count", meta.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) metaInfo.get("content")).add(content);
        }
        metaInfo.put("count", 0);

        // 태그 정보
        List<Map<String, Object>> tagList = categoryMapper.getTagListForFilter();
        tagInfo.put("all", true);
        tagInfo.put("ord", 4);
        tagInfo.put("name", "태그");
        tagInfo.put("content", new ArrayList<Map<String, Object>>());

        i=0;
        for (Map<String, Object> tag : tagList) {
            Map<String, Object> content = new HashMap<>();
            content.put("root", "TAG");
            content.put("rootDescription", "태그");
            i = i + 1;
            content.put("id", i);
            content.put("name", tag.get("tag_name"));
            content.put("description", tag.get("tag_name"));
            content.put("count", tag.get("count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) tagInfo.get("content")).add(content);
        }
        tagInfo.put("count", 0);

        // 정렬 오름차순, 내림차순
        orderInfo.put("all", true);
        orderInfo.put("ord", 5);
        orderInfo.put("name", "정렬기준");
        orderInfo.put("content", new ArrayList<Map<String, Object>>());

        Map<String, Object> ascContent = new HashMap<>();
        ascContent.put("root", "ORDER");
        ascContent.put("rootDescription", "정렬기준");
        ascContent.put("id", 1);
        ascContent.put("name", "ASC");
        ascContent.put("description", "오름차순");
        ascContent.put("count", 0);
        ascContent.put("checked", false);
        ((List<Map<String, Object>>) orderInfo.get("content")).add(ascContent);
        Map<String, Object> descContent = new HashMap<>();
        descContent.put("root", "ORDER");
        descContent.put("rootDescription", "정렬기준");
        descContent.put("id", 2);
        descContent.put("name", "DESC");
        descContent.put("description", "내림차순");
        descContent.put("count", 0);
        descContent.put("checked", false);
        ((List<Map<String, Object>>) orderInfo.get("content")).add(descContent);

        orderInfo.put("count", 0);

        detailFilters.put("CATEGORY", categoryInfo);
        detailFilters.put("SYSTEMTYPE", systemInfo);
        detailFilters.put("METATYPE", metaInfo);
        detailFilters.put("TAG", tagInfo);
        detailFilters.put("ORDER", orderInfo);

        return detailFilters;
    }
}
