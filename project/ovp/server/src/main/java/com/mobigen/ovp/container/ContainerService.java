package com.mobigen.ovp.container;

import com.mobigen.ovp.common.openmete_client.ContainerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerClient containerClient;

    public Object getContainersObject(String id) {
        Map<String, Object> result = containerClient.getContainersObject(id, "dataModel,tags, owner");

        String name = (String) result.get("name");
        String description = (String) result.get("description");
        Number size = (Number) result.get("size");
        List<String> fileFormatsList = (List<String>) result.getOrDefault("fileFormats", new ArrayList<>());
        String fileFormats = fileFormatsList.isEmpty() ? "" : fileFormatsList.get(0);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) result.getOrDefault("tags", new ArrayList<>());
        Map<String, Object> dataModel = (Map<String, Object>) result.getOrDefault("dataModel", new HashMap<>());
        List<Map<String, Object>> columns = (List<Map<String, Object>>) dataModel.getOrDefault("columns", new ArrayList<>());

        List<Map<String, Object>> tagList = new ArrayList<>();
        List<Map<String, Object>> glossaryList = new ArrayList<>();
        List<Map<String, Object>> columList = new ArrayList<>();

        if (!tags.isEmpty()) {
            for (Map<String, Object> tag : tags) {
                String tagName = (String) tag.get("name");
                String tagFQN = (String) tag.get("tagFQN");
                String tagSource = (String) tag.get("source");

                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("name", tagName);
                tagMap.put("category", tagFQN);

                if ("Glossary".equals(tagSource)) {
                    glossaryList.add(tagMap);
                } else {
                    tagList.add(tagMap);
                }
            }
        }

        if (!columns.isEmpty()) {
            for (Map<String, Object> column : columns) {
                String columnName = (String) column.get("name");
                String columnDataType = (String) column.get("dataType");
                String columnDesc = (String) column.get("description");
                String columnConstraint = (String) column.get("constraint");

                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("name", columnName);
                columnMap.put("dataType", columnDataType);
                columnMap.put("desc", columnDesc);
                columnMap.put("constraint", columnConstraint);

                columList.add(columnMap);
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("desc", description);
        model.put("size", size);
        model.put("ext", fileFormats);

        Map<String, Object> modelInfo = new HashMap<>();
        modelInfo.put("model", model);
        modelInfo.put("columns", columList);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("modelType", "unstructured");
        resultMap.put("modelInfo", modelInfo);
        resultMap.put("glossaries", glossaryList);
        resultMap.put("tags", tagList);

        return resultMap;
    }
}
