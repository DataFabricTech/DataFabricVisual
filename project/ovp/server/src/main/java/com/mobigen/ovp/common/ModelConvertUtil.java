package com.mobigen.ovp.common;

import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.constants.ModelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelConvertUtil {

    private final CategoryRepository categoryRepository;

    /**
     * Open Meta Api 에서 모델 데이터 조회 한 후에, 해당 데이터를 화면에 사용 할 수 있게 데이터 정제하는 부분을 공통 코드로 구현.
     * Open Meta Api 에서 response 를
     * 1. 복수건 으로 받았을 경우, hits 를 통째로 받아서 처리하거나
     * 2. 단건 으로 받았을 경우, _source 를 받아서 처리한다.
     *
     * @param hits
     * @return
     */
    public Object convertSearchDataList(Object hits, boolean useFilter) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) hits;
        List<Map<String, Object>> modifiedList = list.stream()
                .filter(hit -> {
                    if (useFilter) {
                        String index = hit.get("_index").toString();
                        return index.equals("table_search_index") || index.equals("container_search_index");
                    }
                    return true;
                })
                .map(hit -> {
                    String index = hit.get("_index").toString().equals("table_search_index") ? "table" : "storage";
                    if (hit.containsKey("_source")) {
                        Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                        Map<String, Object> convertedData = convertSourceDataOne(index, source);
                        convertedData.put("_index", hit.get("_index"));
                        return convertedData;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return modifiedList;
    }

    /**
     * 단건(_source) 받아서 처리
     *
     * @param source
     * @return
     */
    public Map<String, Object> convertSourceDataOne(String index, Map<String, Object> source) {
        Map<String, Object> modifiedSource = new HashMap<>();

        String serviceType = source.get("serviceType") != null ? source.get("serviceType").toString().toLowerCase() : "";
        modifiedSource.put("type", "trino".equals(serviceType) ? "model" : index);

        modifiedSource.put("id", source.get("id"));
        modifiedSource.put("serviceIcon", new StringBuffer("type-img type-img-").append(serviceType).toString());

        String[] splitArray = source.get("fullyQualifiedName").toString().split("\\.");
        List<String> resultList = new ArrayList<>(Arrays.asList(splitArray));
        // TODO: index 와 실제 데이터가 매칭이 되지 않고 있음.
        if (!ModelType.STORAGE.getValue().equals(index)) {
            resultList.remove(resultList.size() - 1);
            modifiedSource.put("depth", resultList);
        } else {
            // TODO : [개발] 마이 페이지 -> 나의 데이터 에 'admin' 계정인 경우, all 로 모든 데이터가 표시되기 때문에 아래 코드에서 에러남. try/catch 로 에러 대응함. 추후 코드 수정 필요함.
            try {
                modifiedSource.put("depth", List.of(new String[]{resultList.get(0), resultList.get(1)}));
            } catch (Exception e) {
                modifiedSource.put("depth", resultList);
            }
        }

        modifiedSource.put("firModelNm", source.get("displayName"));
        modifiedSource.put("modelNm", source.get("name"));
        modifiedSource.put("displayName", source.get("displayName"));
        modifiedSource.put("modelDesc", source.get("description"));
        modifiedSource.put("fqn", source.get("fullyQualifiedName"));
        modifiedSource.put("owner", source.get("owner"));

        String owner = "";
        if (source.get("owner") != null) {
            Map<String, Object> ownerObj = (Map<String, Object>) source.get("owner");
            owner = ownerObj.get("displayName").toString();
            modifiedSource.put("ownerDisplayName", owner);
        }

        modifiedSource.put("category", new HashMap<>());

        if (source.get("tags") != null) {
            String categoryId = "";

            List<Map<String, Object>> tags = (List<Map<String, Object>>) source.get("tags");
            for (Map<String, Object> tag : tags) {
                String displayName = tag.get("displayName").toString();

                if (displayName == null || "".equals(displayName)) {
                    displayName = tag.get("name").toString();
                    tag.put("displayName", displayName);
                }

                if (tag.get("tagFQN").toString().contains(Constants.OVP_CATEGORY)) {
                    CategoryEntity categoryEntity = getCategoryEntity(tag.get("name").toString());
                    if (categoryEntity != null) {
                        categoryId = categoryEntity.getId().toString();
                        Map<String, Object> category = (Map<String, Object>) modifiedSource.get("category");
                        category.put("id", categoryEntity.getId());
                        category.put("name", categoryEntity.getName());
                        category.put("tagName", tag.get("name"));
                        category.put("tagDisplayName", tag.get("displayName"));
                        category.put("tagDescription", tag.get("description"));
                        category.put("tagFQN", tag.get("tagFQN"));

                        modifiedSource.put("category", category);
                        break;
                    }
                }
            }

            // 루프가 끝난 후 categoryId가 있으면 추가
            if (!categoryId.isEmpty()) {
                modifiedSource.put("category", categoryId);
                System.out.println("HERE");
            }
        }

        return modifiedSource;
    }

    // TODO : [개발] 추후에 카테고리 DB 정리하면서 아래 코드 수정 필요함.
    public CategoryEntity getCategoryEntity(String tagName) {
        if (tagName.equals(Constants.UNDEFINED_TAG_NAME)) {
            return categoryRepository.findByName(tagName);
        } else {
            return categoryRepository.findByTagId(UUID.fromString(tagName));
        }
    }


    public Map<String, Object> convertPreviewData(Map<String, Object> data, String type, String fqn) {
        fqn = (fqn == null) ? "" : fqn;

        String name = (String) data.get("name");
        String displayName = (String) data.get("displayName");
        String description = (String) data.get("description");
        List<Map<String, Object>> tags = (List<Map<String, Object>>) data.getOrDefault("tags", new ArrayList<>());
        List<Map<String, Object>> columns = new ArrayList<>();
        Number size = null;
        String fileFormats = null;
        String tableType = null;
        String fullyQualifiedName = null;
        String serviceType = null;

        if (type.equals("unstructured")) {
            size = (Number) data.get("size");
            fullyQualifiedName = (String) data.get("fullyQualifiedName");
            List<String> fileFormatsList = (List<String>) data.getOrDefault("fileFormats", new ArrayList<>());
            fileFormats = fileFormatsList.isEmpty() ? "" : fileFormatsList.get(0);
            Map<String, Object> dataModel = (Map<String, Object>) data.getOrDefault("dataModel", new HashMap<>());
            columns = (List<Map<String, Object>>) dataModel.getOrDefault("columns", new ArrayList<>());
        } else if (type.equals("structured")) {
            tableType = (String) data.get("tableType");
            columns = (List<Map<String, Object>>) data.get("columns");
            serviceType = (String) data.get("serviceType");
        }

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
        model.put("displayName", displayName);
        model.put("desc", description);
        if (type.equals("unstructured")) {
            model.put("size", size);
            model.put("ext", fileFormats);
        } else if (type.equals("structured")) {
            model.put("tableType", tableType);
            model.put("cnt", columList.size());
        }

        Map<String, Object> modelInfo = new HashMap<>();
        modelInfo.put("model", model);
        modelInfo.put("columns", columList);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("modelInfo", modelInfo);
        resultMap.put("glossaries", glossaryList);
        resultMap.put("tags", tagList);
        resultMap.put("id", data.get("id"));

        if (type.equals("unstructured")) {
            resultMap.put("modelType", "unstructured");
            resultMap.put("index", "storage");
            resultMap.put("fqn", fullyQualifiedName);
        } else if (type.equals("structured")) {
            if (serviceType.equals("Trino")) {
                resultMap.put("index", "model");
            } else {
                resultMap.put("index", "table");
            }
            resultMap.put("modelType", "structured");
            resultMap.put("fqn", fqn);
        }

        return resultMap;
    }
}

