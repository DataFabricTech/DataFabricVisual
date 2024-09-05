package com.mobigen.ovp.search;

import com.mobigen.ovp.common.constants.Constants;
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
import java.util.Arrays;
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
                    } else if (newKey.equals("tags.tagFQN") && buckets instanceof List) {
                        List<Map<String, Object>> filteredBuckets = ((List<Map<String, Object>>) buckets).stream()
                                .filter(bucket -> !((String) bucket.get("key")).contains(Constants.OVP_CATEGORY + "."))
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

    public Map<String, Object> getFilter(MultiValueMap<String, String> params) {
        params.set("q", "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"deleted\":false}}]}}}");
        params.set("value", ".*.*");

        Map<String, Object> combinedAggregations = new HashMap<>();

        List<String> indices = Arrays.asList("table_search_index", "container_search_index");

        for (String index : indices) {
            params.set("index", index);
            Map<String, Object> resultSet = convertAggregations(searchClient.getFilter(params));

            resultSet.forEach((key, value) -> {
                combinedAggregations.merge(key, value, (oldValue, newValue) -> {
                    List<Object> mergedList = new ArrayList<>((List<Object>) oldValue);
                    mergedList.addAll((List<Object>) newValue);
                    return mergedList;
                });
            });
        }

        return combinedAggregations;
    }

    public Map<String, Object> getFilters() throws Exception {
        List<String> tagArrays = Arrays.asList(
                "owner.displayName.keyword",
                "tags.tagFQN",
                "service.displayName.keyword",
                "serviceType",
                "database.displayName.keyword",
                "databaseSchema.displayName.keyword",
                "columns.name.keyword"
        );

        Map<String, Object> responseMap = new HashMap<>();

        for (String tag : tagArrays) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.set("field", tag);
            Map<String, Object> filterResult = getFilter(params);
            responseMap.putAll(filterResult);
        }

        return responseMap;
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        List<Map<String, Object>> responseList = List.of(
                getTableList(new LinkedMultiValueMap<>(params)),
                getStorageList(new LinkedMultiValueMap<>(params)),
                getModelList(new LinkedMultiValueMap<>(params))
        );
        List<String> keys = List.of("table", "storage", "model");
        return getSearchList(keys, responseList);
    }

    public Map<String, Object> getAllSearchList(MultiValueMap<String, String> params) throws Exception {
        List<Map<String, Object>> responseList = List.of(
                getAllList(new LinkedMultiValueMap<>(params))
        );
        List<String> keys = List.of("all");
        return getSearchList(keys, responseList);
    }

    private Map<String, Object> getSearchList(List<String> keys, List<Map<String, Object>> responseList) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> totalCountMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
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

    // function wrapper
    private Map<String, Object> getList(MultiValueMap<String, String> params) throws Exception {
        return getList(params, false);
    }

    private Map<String, Object> getList(MultiValueMap<String, String> params, Boolean useFilter) throws Exception {
        Map<String, Object> result = searchClient.getSearchList(params);
        return convertToMap(result, useFilter);
    }

    // function wrapper
    public Map<String, Object> convertToMap(Map<String, Object> result) {
        return convertToMap(result, false);
    }

    public Map<String, Object> convertToMap(Map<String, Object> result, boolean useFilter) {
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> data = (Map<String, Object>) result.get("hits");
        if (data != null) {
            Map<String, Object> totalObj = (Map<String, Object>) data.get("total");
            if (totalObj != null) {
                resultMap.put("totalCount", totalObj.get("value"));
            }
            resultMap.put("data", modelConvertUtil.convertSearchDataList(data.get("hits"), useFilter));
        }

        return resultMap;

    }

    private Map<String, Object> getAllList(MultiValueMap<String, String> params) throws Exception {
        params.set("index", ModelIndex.all.name());
        return getList(params);
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
        params.set("index", ModelIndex.table.name());
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
        Map<String, Object> resultMap = modelConvertUtil.convertPreviewData(tablesClient.getSearchPreview(fqn), "structured", fqn);

        return resultMap;
    }

    public Object getBothSearchList(MultiValueMap<String, String> params) throws Exception {
        int initSize = Integer.parseInt(params.getFirst("size").toString());
        int curSize = initSize;

        // 데이터가 너무 적을경우 initSize 를 채우기 위해서 무한으로 while 이 실행될꺼기 때문에 5번의 한도를 둔다.
        int maxAttempts = 5;
        int limitCnt = 0;
        Map<String, Object> result = getList(params, true);
        List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");

        while (data.size() < initSize && limitCnt < maxAttempts) {
            limitCnt++;
            curSize += initSize;
            params.set("size", String.valueOf(curSize));
            result = getList(params, true);
            data = (List<Map<String, Object>>) result.get("data");

            if (data.size() >= initSize) {
                break;
            }
        }

        List<Map<String, Object>> trimmedData = data.stream()
                .limit(initSize)
                .collect(Collectors.toList());

        result.put("data", trimmedData);
        result.put("totalData", trimmedData.size());

        return result;
    }
}
