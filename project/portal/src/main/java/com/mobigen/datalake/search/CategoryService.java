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

    // 카테고리별 테이블 수 전체 조회
    public Object getTableCountsByClassification() {
        return categoryMapper.getTableCountsByClassification();
    }

    // 카테고리 목록 조회
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
        Map<String, Object> tagInfo = new HashMap<>();
        Map<String, Object> dateInfo = new HashMap<>();
        
        // 카테고리 정보
        List<Map<String, Object>> categoryList = categoryMapper.getCategoryListForFilter();
        categoryInfo.put("all", true);
        categoryInfo.put("ord", 1);
        categoryInfo.put("name", "카테고리");
        categoryInfo.put("content", new ArrayList<Map<String, Object>>());

        for (Map<String, Object> category : categoryList) {
            Map<String, Object> content = new HashMap<>();
            if (category.get("name").toString().equals("CAT")) continue;

            content.put("root", category.get("parent"));
            content.put("rootDescription", category.get("parent_description"));
            content.put("name", category.get("name"));
            content.put("description", category.get("description"));
            content.put("count", category.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) categoryInfo.get("content")).add(content);
        }
        
        // 시스템 정보
        List<Map<String, Object>> systemList = categoryMapper.getSystemListForFilter();
        systemInfo.put("all", true);
        systemInfo.put("ord", 2);
        systemInfo.put("name", "시스템");
        systemInfo.put("content", new ArrayList<Map<String, Object>>());

        for (Map<String, Object> system : systemList) {
            Map<String, Object> content = new HashMap<>();
            if (system.get("name").toString().equals("SYS")) continue;

            content.put("root", system.get("parent"));
            content.put("rootDescription", system.get("parent_description"));
            content.put("name", system.get("name"));
            content.put("description", system.get("description"));
            content.put("count", system.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) systemInfo.get("content")).add(content);
        }
        systemInfo.put("count", 0);

        // 태그 정보
        List<Map<String, Object>> tagList = categoryMapper.getTagListForFilter();
        tagInfo.put("all", true);
        tagInfo.put("ord", 3);
        tagInfo.put("name", "태그");
        tagInfo.put("content", new ArrayList<Map<String, Object>>());

        for (Map<String, Object> tag : tagList) {
            Map<String, Object> content = new HashMap<>();
            content.put("root", "TAG");
            content.put("rootDescription", "태그");
            content.put("name", tag.get("tag_name"));
            content.put("description", tag.get("tag_name"));
            content.put("count", tag.get("count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) tagInfo.get("content")).add(content);
        }
        tagInfo.put("count", 0);

        // 등록일, 2023, 2024, 2025 설정 요청
        dateInfo.put("all", true);
        dateInfo.put("ord", 4);
        dateInfo.put("name", "등록일");
        dateInfo.put("content", new ArrayList<Map<String, Object>>());

        for (int i=0; i<2; i++) {
            Map<String, Object> content = new HashMap<>();
            content.put("root", "DATE");
            content.put("rootDescription", "등록일");
            content.put("name", 2024 + i);
            content.put("description", (2024 + i) + "년");
            content.put("count", 0);
            content.put("checked", false);
            ((List<Map<String, Object>>) dateInfo.get("content")).add(content);
        }
        dateInfo.put("count", 0);

        detailFilters.put("CAT", categoryInfo);
        detailFilters.put("SYS", systemInfo);
        detailFilters.put("TAG", tagInfo);
        detailFilters.put("DATE", dateInfo);

        return detailFilters;
    }
}
