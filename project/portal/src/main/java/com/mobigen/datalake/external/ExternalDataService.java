package com.mobigen.datalake.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExternalDataService {
    private final ExternalDataMapper externalDataMapper;

    public Map<String, Object> getExternalDataList(Map<String, String> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // 1. 외부데이터 항목 조회
        List<Map<String, Object>> externalDataList = externalDataMapper.getExternalDataList(params);

        result.put("list", externalDataList);
        result.put("total", externalDataMapper.getExternalDataListTotal(params));
        return result;
    }

    public Map<String, Object> getExternalData(String id) throws Exception {
        Map<String, Object> result = externalDataMapper.getExternalData(id);
        result.put("system", externalDataMapper.getSystem(id));
        return externalDataMapper.getExternalData(id);
    }

    public Object deleteData(String id) throws Exception {
        return externalDataMapper.deleteData(id);
    }

    public Object getExternalInfo() throws Exception {
        Map<String, Object> registerInfo = new HashMap<>();
        Map<String, Object> categories = new HashMap<>();
        Map<String, Object> systems = new HashMap<>();
        
        // 카테고리
        List<Map<String, Object>> categoryList = externalDataMapper.getCategoryList();
        categories.put("all", true);
        categories.put("ord", 1);
        categories.put("name", "카테고리");
        categories.put("content", new ArrayList<Map<String, Object>>());
        for (Map<String, Object> category : categoryList) {
            Map<String, Object> content = new HashMap<>();
            if (category.get("name").toString().equals("CAT")) continue;

            content.put("root", category.get("parent"));
            content.put("rootDescription", category.get("parent_description"));
            content.put("name", category.get("name"));
            content.put("description", category.get("description"));
            content.put("count", category.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) categories.get("content")).add(content);
        }

        // 시스템
        List<Map<String, Object>> systemList = externalDataMapper.getSystemList();
        systems.put("all", true);
        systems.put("ord", 2);
        systems.put("name", "시스템");
        systems.put("content", new ArrayList<Map<String, Object>>());
        for (Map<String, Object> system : systemList) {
            Map<String, Object> content = new HashMap<>();
            if (system.get("name").toString().equals("SYS")) continue;

            content.put("root", system.get("parent"));
            content.put("rootDescription", system.get("parent_description"));
            content.put("name", system.get("name"));
            content.put("description", system.get("description"));
            content.put("count", system.get("type_count"));
            content.put("checked", false);
            ((List<Map<String, Object>>) systems.get("content")).add(content);
        }

        // 태그
        List<Map<String, Object>> tags = externalDataMapper.getTagList();

        registerInfo.put("categories", categories);
        registerInfo.put("systems", systems);
        registerInfo.put("tags", tags);

        return registerInfo;
    }

    public Object registerExternalData(Map<String, Object> params) throws Exception {
        // _varchar(varchar[] or character varying[]) 컬럼에 List<String> 데이터를 넣기 위해 변환
        List<String> categories = (List<String>) params.get("categories");
        params.put("categories", "{" + categories.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        List<String> systems = (List<String>) params.get("systems");
        params.put("systems", "{" + systems.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        List<String> tags = (List<String>) params.get("tags");
        params.put("tags", "{" + tags.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        return externalDataMapper.registerExternalData(params);
    }

    public Object updateExternalData(Map<String, Object> params) throws Exception {
        // _varchar(varchar[] or character varying[]) 컬럼에 List<String> 데이터를 넣기 위해 변환
        List<String> categories = (List<String>) params.get("categories");
        params.put("categories", "{" + categories.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        List<String> systems = (List<String>) params.get("systems");
        params.put("systems", "{" + systems.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        List<String> tags = (List<String>) params.get("tags");
        params.put("tags", "{" + tags.stream().map(item -> item).collect(Collectors.joining(", ")) + "}");

        return externalDataMapper.updateExternalData(params);
    }
}
