package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mobigen.datalake.attrcategory.AttrCategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final AttrCategoryMapper attrCategoryMapper;

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
        return categoryMapper.getCategoryList();
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
        Map<String, Object> metaInfo = new HashMap<>();
        Map<String, Object> fileFormatInfo = new HashMap<>();
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
            if (category.get("name").toString().equals("CATEGORY"))
                continue;

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
        categoryInfo.put("visible", true);

        // 시스템 타입 정보
        List<Map<String, Object>> systemList = categoryMapper.getSystemListForFilter();
        systemInfo.put("allChecked", true);
        systemInfo.put("ord", 2);
        systemInfo.put("name", "시스템 타입");
        systemInfo.put("content", new ArrayList<Map<String, Object>>());

        // 코드 테이블에 선언한 시스템 타입 항목이 없는 정보는 제거
        List<Map<String, Object>> systemTypes = categoryMapper.getSystemTypes();
        Set<String> validSystemTypeNames = systemTypes.stream()
            .map(map -> (String) map.get("name"))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        systemList.removeIf(map -> {
            String systemType = (String) map.get("system_type");
            return systemType == null || !validSystemTypeNames.contains(systemType);
        });

        int i = 0;
        for (Map<String, Object> systemType : systemTypes) {
            i = i + 1;
            boolean added = false;
            for (Map<String, Object> system : systemList) {
                // 코드 테이블에 있는 값만 추가
                if (system.get("system_type").toString().equals(systemType.get("name"))) {

                    Map<String, Object> content = new HashMap<>();

                    content.put("root", "SYSTEMTYPE");
                    content.put("rootDescription", "시스템 타입");

                    content.put("id", i);
                    content.put("name", systemType.get("name"));
                    content.put("description", systemType.get("value"));
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
                content.put("name", systemType.get("name"));
                content.put("description", systemType.get("value"));
                content.put("count", 0);
                content.put("checked", false);
                ((List<Map<String, Object>>) systemInfo.get("content")).add(content);
            }
        }
        systemInfo.put("checkedCount", 0);
        systemInfo.put("visible", true);

        // 메타 타입 정보
        List<Map<String, Object>> metaList = categoryMapper.getMetaListForFilter();
        metaInfo.put("allChecked", true);
        metaInfo.put("ord", 3);
        metaInfo.put("name", "메타 타입");
        metaInfo.put("content", new ArrayList<Map<String, Object>>());

        // 코드 테이블에 선언한 시스템 타입 항목이 없는 정보는 제거
        List<Map<String, String>> metaTypes = categoryMapper.getMetaTypes();

        Set<String> validMetaTypeNames = metaTypes.stream()
            .map(map -> (String) map.get("name"))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        metaList.removeIf(map -> {
            String metaType = (String) map.get("meta_type");
            return metaType == null || !validMetaTypeNames.contains(metaType);
        });

        int j = 0;
        for (Map<String, String> metaType : metaTypes) {
            j = j + 1;
            boolean added = false;
            for (Map<String, Object> meta : metaList) {
                // 코드 테이블에 있는 값만 추가
                if (meta.get("meta_type").toString().equals(metaType.get("name"))) {

                    Map<String, Object> content = new HashMap<>();

                    content.put("root", "METATYPE");
                    content.put("rootDescription", "메타 타입");

                    content.put("id", j);
                    content.put("name", metaType.get("name"));
                    content.put("description", metaType.get("value"));
                    content.put("count", meta.get("type_count"));
                    content.put("checked", false);
                    ((List<Map<String, Object>>) metaInfo.get("content")).add(content);
                    added = true;
                    break;
                }
            }

            if (!added) {
                Map<String, Object> content = new HashMap<>();

                content.put("root", "METATYPE");
                content.put("rootDescription", "메타 타입");

                content.put("id", i);
                content.put("name", metaType.get("name"));
                content.put("description", metaType.get("value"));
                content.put("count", 0);
                content.put("checked", false);
                ((List<Map<String, Object>>) metaInfo.get("content")).add(content);
            }
        }
        metaInfo.put("checkedCount", 0);
        metaInfo.put("visible", true);

        // 파일 확장자 정보
        List<Map<String, Object>> fileFormatList = categoryMapper.getFileFormatListForFilter();
        fileFormatInfo.put("allChecked", true);
        fileFormatInfo.put("ord", 4);
        fileFormatInfo.put("name", "확장자");
        fileFormatInfo.put("content", new ArrayList<Map<String, Object>>());

        // 코드 테이블에 선언한 시스템 타입 항목이 없는 정보는 제거
        List<Map<String, String>> fileFormats = categoryMapper.getFileFormats();

        Set<String> validFileFormatTypes = fileFormats.stream()
            .map(map -> (String) map.get("name"))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        fileFormatList.removeIf(map -> {
            String fileFormat = (String) map.get("file_format");
            return fileFormat == null || !validFileFormatTypes.contains(fileFormat);
        });

        int k = 0;
        for (Map<String, String> fileFormat : fileFormats) {
            k = k + 1;
            boolean added = false;
            for (Map<String, Object> format : fileFormatList) {
                // 코드 테이블에 있는 값만 추가
                if (format.get("file_format").toString().equals(fileFormat.get("name"))) {

                    Map<String, Object> content = new HashMap<>();

                    content.put("root", "FILE_FORMAT");
                    content.put("rootDescription", "확장자");

                    content.put("id", j);
                    content.put("name", fileFormat.get("name"));
                    content.put("description", fileFormat.get("value"));
                    content.put("count", format.get("count"));
                    content.put("checked", false);
                    ((List<Map<String, Object>>) fileFormatInfo.get("content")).add(content);
                    added = true;
                    break;
                }
            }

            if (!added) {
                Map<String, Object> content = new HashMap<>();

                content.put("root", "FILE_FORMAT");
                content.put("rootDescription", "확장자");

                content.put("id", i);
                content.put("name", fileFormat.get("name"));
                content.put("description", fileFormat.get("value"));
                content.put("count", 0);
                content.put("checked", false);
                ((List<Map<String, Object>>) fileFormatInfo.get("content")).add(content);
            }
        }
        fileFormatInfo.put("checkedCount", 0);
        fileFormatInfo.put("visible", false);


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
        orderInfo.put("visible", true);

        // 유사 속성 카테고리 필터
        Map<String, Object> attrCategoryInfo = new HashMap<>();
        attrCategoryInfo.put("allChecked", true);
        attrCategoryInfo.put("ord", 4);
        attrCategoryInfo.put("name", "유사 속성");
        attrCategoryInfo.put("content", new ArrayList<>());
        attrCategoryInfo.put("checkedCount", 0);
        attrCategoryInfo.put("visible", true);

        List<Map<String, Object>> attrCategories = attrCategoryMapper.getCategoryList();
        for (Map<String, Object> cat : attrCategories) {
            Map<String, Object> item = new HashMap<>();
            item.put("root", "ATTR_CATEGORY");
            item.put("rootDescription", "유사 속성 카테고리");
            item.put("id", cat.get("id"));
            item.put("name", cat.get("name"));
            item.put("description", cat.get("name"));
            item.put("count", cat.get("table_count"));
            item.put("checked", false);
            ((List<Map<String, Object>>) attrCategoryInfo.get("content")).add(item);
        }

        detailFilters.put("CATEGORY", categoryInfo);
        detailFilters.put("SYSTEMTYPE", systemInfo);
        detailFilters.put("METATYPE", metaInfo);
        detailFilters.put("ATTR_CATEGORY", attrCategoryInfo);
        detailFilters.put("FILE_FORMAT", fileFormatInfo);
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
