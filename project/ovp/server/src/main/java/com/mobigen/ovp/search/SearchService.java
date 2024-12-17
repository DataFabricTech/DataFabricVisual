package com.mobigen.ovp.search;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.CategoryConvertUtil;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.entity.ModelIndex;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final ModelConvertUtil modelConvertUtil;
    private final CategoryRepository categoryRepository;
    private final CategoryConvertUtil categoryConvertUtil;

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
        params.set("q", "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"deleted\":false}},{\"terms\":{\"_index\":[\"" + Constants.CONTAINER_INDEX + "\",\"" + Constants.TABLE_INDEX + "\"]}}]}}}");
        params.set("value", ".*.*");
        params.set("index", "all");
        return convertAggregations(searchClient.getFilter(params));
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

    private JSONObject defaultQueryFilterJson() {
        // Step 1: Construct the "terms" part
        JSONObject termsObject = new JSONObject();
        JSONArray indicesArray = new JSONArray();
        indicesArray.put(Constants.CONTAINER_INDEX);
        indicesArray.put(Constants.TABLE_INDEX);
        termsObject.put("_index", indicesArray);

        // Step 2: Construct the "must" part for "containerIndex" and "fileFormats"
        JSONObject termObject = new JSONObject();
        termObject.put("_index", Constants.CONTAINER_INDEX);

        JSONObject existsObject = new JSONObject();
        existsObject.put("field", "fileFormats");

        JSONArray mustArray = new JSONArray();
        mustArray.put(new JSONObject().put("term", termObject));
        mustArray.put(new JSONObject().put("exists", existsObject));

        JSONObject boolMustObject = new JSONObject();
        boolMustObject.put("must", mustArray);

        // Step 3: Construct the "should" part
        JSONArray shouldArray = new JSONArray();
        shouldArray.put(new JSONObject().put("bool", boolMustObject));
        shouldArray.put(new JSONObject().put("term", new JSONObject().put("_index", Constants.TABLE_INDEX)));

        // Step 4: Construct the main "bool" object
        JSONObject mainBoolObject = new JSONObject();
        mainBoolObject.put("must", new JSONArray().put(new JSONObject().put("terms", termsObject)));
        mainBoolObject.put("should", shouldArray);
        mainBoolObject.put("minimum_should_match", 1);

        // Step 5: Construct the final query filter object
        JSONObject queryObject = new JSONObject();
        queryObject.put("bool", mainBoolObject);

        JSONObject finalQueryFilter = new JSONObject();
        finalQueryFilter.put("query", queryObject);

        // Return the JSON as a string
        return finalQueryFilter;
    }

    public JSONObject mergeQueries(String extraQuery) {
        // 기본 쿼리 (index 정보 등)
        JSONObject defaultQuery = defaultQueryFilterJson();

        // 추가 쿼리 -> json 변형
        JSONObject extraQueryObject = new JSONObject(extraQuery);

        // merge 하기 위해 추가 쿼리에서 bool 부분 추출
        JSONObject extraBoolObject = extraQueryObject.getJSONObject("query").optJSONObject("bool");
        if (extraBoolObject == null) {
            return defaultQuery; // 추가 쿼리에 bool이 없으면 기본 쿼리를 반환
        }

        // 기본 쿼리에서 bool 부분 추출
        JSONObject boolObject = defaultQuery.getJSONObject("query").getJSONObject("bool");

        // must 병합
        mergeArrayFields(boolObject, extraBoolObject, "must");

        // must_not 병합
        mergeArrayFields(boolObject, extraBoolObject, "must_not");

        return defaultQuery;
    }

    // 배열 필드 병합 메서드
    private void mergeArrayFields(JSONObject targetBool, JSONObject sourceBool, String fieldName) {
        // targetBool에서 fieldName 배열 가져오기
        JSONArray targetArray = targetBool.optJSONArray(fieldName);
        if (targetArray == null) {
            targetArray = new JSONArray();
            targetBool.put(fieldName, targetArray);
        }

        // sourceBool에서 fieldName 배열 가져오기
        Object sourceArrayObj = sourceBool.opt(fieldName);

        // sourceArrayObj가 배열이면 항목을 추가
        if (sourceArrayObj instanceof JSONArray sourceArray) {
            for (int i = 0; i < sourceArray.length(); i++) {
                targetArray.put(sourceArray.get(i));
            }
        }
        // sourceArrayObj가 객체이면 객체를 추가
        else if (sourceArrayObj instanceof JSONObject sourceObject) {
            targetArray.put(sourceObject);
        }
    }


    private Map<String, Object> getAllList(MultiValueMap<String, String> params) throws Exception {
        params.set("index", ModelIndex.all.name());
        params.set("query_filter", mergeQueries(params.getFirst("query_filter")).toString());

        return getList(params);
    }

    private String getQueryFilter(String index, String queryFilter) {
        // Step 1: queryFilter를 JSON으로 변환
        JSONObject combinedFilter = new JSONObject(queryFilter);

        // Step 2: trino 필터를 별도로 생성
        JSONArray trinoShouldArray = new JSONArray();
        trinoShouldArray.put(new JSONObject().put("term", new JSONObject().put("serviceType", "trino")));

        JSONObject trinoFilter = new JSONObject();
        trinoFilter.put("bool", new JSONObject().put("should", trinoShouldArray));

        // Step 3: 기존 queryFilter에 bool 필드가 있는지 확인
        JSONObject queryBoolObject = combinedFilter.optJSONObject("query");
        if (queryBoolObject == null) {
            queryBoolObject = new JSONObject();
            combinedFilter.put("query", queryBoolObject);
        }

        JSONObject boolObject = queryBoolObject.optJSONObject("bool");
        if (boolObject == null) {
            boolObject = new JSONObject();
            queryBoolObject.put("bool", boolObject);
        }

        // Step 4: must 또는 must_not에 trino 필터 추가
        if (index.equals("table") || index.equals("storage")) {
            // 기존 must_not이 있으면 가져오고, 없으면 새로 생성
            JSONArray mustNotArray = boolObject.optJSONArray("must_not");
            if (mustNotArray == null) {
                mustNotArray = new JSONArray();
                boolObject.put("must_not", mustNotArray);
            }
            // must_not에 trino 필터 추가
            mustNotArray.put(trinoFilter);
        } else if (index.equals("model")) {
            // 기존 must가 있으면 가져오고, 없으면 새로 생성
            JSONArray mustArray = boolObject.optJSONArray("must");
            if (mustArray == null) {
                mustArray = new JSONArray();
                boolObject.put("must", mustArray);
            }
            // must에 trino 필터 추가
            mustArray.put(trinoFilter);
        }

        return combinedFilter.toString();
    }

    private Map<String, Object> getTableList(MultiValueMap<String, String> params) throws Exception {
        // 화면에 표시되는 tab 의 항목의 데이터만 조회되면 되기 때문에 해당 tab 이 아닌 경우, 0건 으로 조회한다.
        if (!params.getFirst("index").equals("table")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.table.name());
        params.set("query_filter", getQueryFilter("table", params.getFirst("query_filter")));

        return getList(params);
    }

    private Map<String, Object> getStorageList(MultiValueMap<String, String> params) throws Exception {
        if (!params.getFirst("index").equals("storage")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.container.name());
        params.set("query_filter", mergeQueries(params.getFirst("query_filter")).toString());

        return getList(params);
    }

    private Map<String, Object> getModelList(MultiValueMap<String, String> params) throws Exception {
        if (!params.getFirst("index").equals("model")) {
            params.set("size", "0");
        }
        params.set("index", ModelIndex.table.name());
        params.set("query_filter", getQueryFilter("model", params.getFirst("query_filter")));

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
        params.set("query_filter", defaultQueryFilterJson().toString());

        Map<String, Object> result = getList(params, true);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("data");
        result.put("data", resultList);
        result.put("totalData", resultList.size());

        return result;
    }

    public Object getGraphCategories(MultiValueMap<String, String> params) throws Exception {
        List<CategoryEntity> categories = categoryRepository.findAll();

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> edgeList = categories.stream()
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", UUID.randomUUID().toString());
                    map.put("sources", List.of(category.getParentId()));
                    map.put("targets", List.of(category.getId()));
                    return map;
                })
                .collect(Collectors.toList());

        result.put("edges", edgeList);


        String index = params.getFirst("index").toString();

        // 데이터 조회
        Map<String, Object> searchData = new HashMap<>();
        if (index.equals("table")) {
            searchData = getTableList(params);
        } else if (index.equals("storage")) {
            searchData = getStorageList(params);
        } else if (index.equals("model")) {
            searchData = getModelList(params);
        }
        List<Map<String, Object>> searchList = (List<Map<String, Object>>) searchData.get("data");


        List<CategoryDTO> dataModelDTOList = searchList.stream()
                .map(dataMap -> {
                    CategoryDTO categoryDTO = new CategoryDTO();

                    categoryDTO.setId(dataMap.get("id").toString());
                    categoryDTO.setParentId(dataMap.get("categoryId").toString());
                    categoryDTO.setName(dataMap.get("modelNm").toString());
                    categoryDTO.setFqn(dataMap.get("fqn").toString());
                    categoryDTO.setIndex(index);

                    return categoryDTO;
                })
                .collect(Collectors.toList());


        CategoryDTO nodeObj = categoryConvertUtil.convertCategoryEntityAndDTO(categories, dataModelDTOList);

        result.put("nodes", nodeObj);

        return result;
    }
}
