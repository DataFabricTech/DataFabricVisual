package com.mobigen.ovp.search;

import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.entity.ModelIndex;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final ModelConvertUtil modelConvertUtil;

    private Map<String, Object> convertAggregations(Map<String, Object> response) {
        Map<String, Object> aggregations = (Map<String, Object>) response.get("aggregations");
        Map<String, Object> resultMap = new HashMap<>();

        if (aggregations != null) {
            for (String key : aggregations.keySet()) {
                Map<String, Object> value = (Map<String, Object>) aggregations.get(key);
                Object buckets = value.get("buckets");

                // sterms#... 이런 식으로 key 가 설정되어 있기 때문에 해당 부분 제거해준다. (사용하지 않음)
                String[] parts = key.split("#");
                String newKey = parts.length > 1 ? parts[1] : parts[0];

                if (buckets != null) {
                    if (newKey.equals("serviceType") && buckets instanceof List) {
                        // serviceType : trino 는 융합모델 분류값으로 사용하고 있기 때문에 serviceType filter 에는 표시하지 않음.
                        List<Map<String, Object>> filteredBuckets = ((List<Map<String, Object>>) buckets).stream()
                                .filter(bucket -> !"trino".equals(bucket.get("key")))
                                .collect(Collectors.toList());

                        if (!filteredBuckets.isEmpty()) {
                            resultMap.put(newKey, filteredBuckets);
                        }
                    } else {
                        resultMap.put(newKey, buckets);
                    }
                }
            }
        }
        return resultMap;
    }

    public Object getFilter(MultiValueMap<String, String> params) {
        return convertAggregations(searchClient.getFilter(params));
    }

    public Object getFilters() throws Exception {
        // filter 조회는 params 가 필요 없기 때문에 공백으로 api 호출
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, Object> response = searchClient.getSearchList(params);
        Map<String, Object> result = convertAggregations(response);

        // TODO: category 구현 완료 되면 여기에 추가.
        return result;
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> totalCountMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();

        List<Map<String, Object>> responseList = List.of(
                getTableList(new LinkedMultiValueMap<>(params)),
                getStorageList(new LinkedMultiValueMap<>(params)),
                getModelList(new LinkedMultiValueMap<>(params))
        );

        List<String> keys = List.of("table", "storage", "model");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Map<String, Object> res = responseList.get(i);
            totalCountMap.put(key, res.get("totalCount"));
            dataMap.put(key, res.get("data"));
        }

        resultMap.put("totalCount", totalCountMap);
        resultMap.put("data", dataMap);
        return resultMap;
    }

    private Map<String, Object> getList(MultiValueMap<String, String> params) throws Exception {
        Map<String, Object> result = searchClient.getSearchList(params);
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> data = (Map<String, Object>) result.get("hits");
        if (data != null) {
            Map<String, Object> totalObj = (Map<String, Object>) data.get("total");
            if (totalObj != null) {
                resultMap.put("totalCount", totalObj.get("value"));
            }
            resultMap.put("data", modelConvertUtil.convertSearchDataList(data.get("hits")));
        }

        return resultMap;
    }

    private Map<String, Object> getTableList(MultiValueMap<String, String> params) throws Exception {
        // 화면에 표시되는 tab 의 항목의 데이터만 조회되면 되기 때문에 해당 tab 이 아닌 경우, 0건 으로 조회한다.
        if (!params.getFirst("index").equals("table")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.table.name());
        return getList(params);
    }

    private Map<String, Object> getStorageList(MultiValueMap<String, String> params) throws Exception {
        if (!params.getFirst("index").equals("storage")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.container.name());
        return getList(params);
    }

    private Map<String, Object> getModelList(MultiValueMap<String, String> params) throws Exception {
        if (!params.getFirst("index").equals("model")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.all.name());
        params.set("query_filter", params.getFirst("trino_query").toString());
        params.remove("trino_query");


        return getList(params);
    }



    /**
     * Open Metadata - 탐색 목록 미리보기
     *
     * @return
     * @throws Exception
     */
    public Object getSearchPreview(String fqn) {
        Map<String, Object> result = tablesClient.getSearchPreview(fqn);

        String name = (String) result.get("name");
        String description = (String) result.get("description");
        String tableType = (String) result.get("tableType");
        List<Map<String, Object>> tags = (List<Map<String, Object>>) result.get("tags");
        List<Map<String, Object>> columns = (List<Map<String, Object>>) result.get("columns");

        List<Map<String, Object>> tagList = new ArrayList<>();
        List<Map<String, Object>> glossaryList = new ArrayList<>();
        List<Map<String, Object>> columList = new ArrayList<>();

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

        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("desc", description);
        model.put("tableType", tableType);
        model.put("cnt", columList.size());

        Map<String, Object> modelInfo = new HashMap<>();
        modelInfo.put("model", model);
        modelInfo.put("columns", columList);

        Map<String, Object> resultMap = new HashMap<>();
        // TODO: modelType 정보 불러오기 필요 "structured" || "unstructured" (현재 확인 불가)
        resultMap.put("modelType", "structured");
        resultMap.put("modelInfo", modelInfo);
        resultMap.put("glossaries", glossaryList);
        resultMap.put("tags", tagList);

        // TODO: 비정형 API 불러오기 필요 details & url & model.ext (현재 확인 불가)

        return resultMap;
    }
}
