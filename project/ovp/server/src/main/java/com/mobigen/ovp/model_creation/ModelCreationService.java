package com.mobigen.ovp.model_creation;

import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.entity.ModelIndex;
import com.mobigen.ovp.model_creation.client.DolphinClient;
import com.mobigen.ovp.model_creation.client.ModelCreationClient;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.search_detail.SearchDetailService;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import com.mobigen.ovp.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelCreationService {
    private final DolphinClient dolphinClient;
    private final ModelCreationClient modelCreationClient;
    private final SearchDetailService searchDetailService;
    private final UserClient userClient;
    private final SearchService searchService;


    public Object executeQuery(Map<String, Object> param) throws Exception {
        Map<String, Object> stringObjectMap = dolphinClient.executeQuery(param);

        Map<String, Object> transformedMap = new HashMap<>();

        List<Map<String, String>> columnDefs = new ArrayList<>();
        List<Map<String, String>> originalColumns = (List<Map<String, String>>) stringObjectMap.get("columns");

        for (Map<String, String> column : originalColumns) {
            Map<String, String> columnDef = new HashMap<>();
            columnDef.put("field", column.get("name"));
            columnDefs.add(columnDef);
        }

        transformedMap.put("columnDefs", columnDefs);

        Map<String, Object> resultData = (Map<String, Object>) stringObjectMap.get("resultData");
        List<String> columnNames = (List<String>) resultData.get("columns");
        List<List<Object>> rows = (List<List<Object>>) resultData.get("rows");
        List<Map<String, Object>> rowData = new ArrayList<>();

        for (List<Object> row : rows) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                rowMap.put(columnNames.get(i), row.get(i));
            }
            rowData.add(rowMap);
        }

        transformedMap.put("totalRows", stringObjectMap.get("totalRows"));
        transformedMap.put("rowData", rowData);

        String startedTime = (String) stringObjectMap.get("startedTime");
        ZonedDateTime zdt = ZonedDateTime.parse(startedTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = zdt.format(formatter);

        transformedMap.put("runTime", stringObjectMap.get("elapsedTime"));
        transformedMap.put("startTime", startTime);

        return transformedMap;
    }

    public Object addBookMark(String id, String type) throws Exception {

        Map<String, Object> userInfo = userClient.getUserInfo();
        String jsonLoginUserId = "\"" + userInfo.get("id") + "\"";

        if (type.equals("table") || type.equals("model")) {
            modelCreationClient.addTableBookMark(id, jsonLoginUserId);
        } else {
            modelCreationClient.addContainerBookMark(id, jsonLoginUserId);
        }

        return null;
    }

    public Object removeBookMark(String id, String type) throws Exception {

        Map<String, Object> userInfo = userClient.getUserInfo();

        if (type.equals("table") || type.equals("model")) {
            modelCreationClient.deleteTableBookMark(id, (String) userInfo.get("id"));
        } else {
            modelCreationClient.deleteContainerBookMark(id, (String) userInfo.get("id"));
        }
        return null;
    }

    /**
     * 데이터 모델 생성 > MY 리스트
     *
     * @param params
     * @return
     */
    public Object getMyList(MultiValueMap<String, String> params) {
        Map<String, Object> userInfo = userClient.getUserWithFields(params.getFirst("id"), "owns,follows");

        List<Map<String, Object>> followsList = (List<Map<String, Object>>) userInfo.get("follows");
        List<Map<String, Object>> ownerList = (List<Map<String, Object>>) userInfo.get("owns");

        boolean isBookmark = Objects.equals(params.getFirst("type"), "bookmark");
        String bookmarkQuery = isBookmark ? params.getFirst("query") : "";
        String ownsQuery = !isBookmark ? params.getFirst("query") : "";

        int from = Integer.parseInt(params.getFirst("from"));
        int size = Integer.parseInt(params.getFirst("size"));
        List<DataModelDetailResponse> listData = isBookmark
                ? getMyModelList(bookmarkQuery, followsList, from, size)
                : getMyModelList(ownsQuery, ownerList, from, size);

        Map<String, Object> data = new HashMap<>();
        data.put("bookmark", isBookmark ? listData : new ArrayList<>());
        data.put("owner", !isBookmark ? listData : new ArrayList<>());

        // 쿼리 + 전체 검색결과
        // NOTE: User에서 가져오는 정보는 삭제된 데이터도 모두 포함되기 때문에 삭제 처리
        List<String> followListCount = getMyModelListCount(bookmarkQuery, followsList);
        List<String> ownsListCount = getMyModelListCount(ownsQuery, ownerList);

        Map<String, Object> totalCount = new HashMap<>();
        totalCount.put("bookmark", followListCount.size());
        totalCount.put("owner", ownsListCount.size());

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("totalCount", totalCount);

        return result;
    }

    /**
     * 데이터 모델 생성 > 전체 리스트
     *
     * @param params : 탐색 쿼리
     * @return
     * @throws Exception
     */
    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        String type = params.getFirst("index");
        Map<String, Object> result = type.equals("all")
                ? searchService.getAllSearchList(params) :
                (Map<String, Object>) searchService.getSearchList(params);

        // detail 데이터로 변환해야함
        Map<String, Object> dataMap = (Map<String, Object>) result.get("data");
        List<Map<String, Object>> resultMap = switch (type) {
            case "table" -> (List<Map<String, Object>>) dataMap.get("table");
            case "storage" -> (List<Map<String, Object>>) dataMap.get("storage");
            case "model" -> (List<Map<String, Object>>) dataMap.get("model");
            default -> null;
        };
        // follow 데이터 확인 필요
        List<Map<String, Object>> newResult = addFollowData(resultMap);

        // 데이터 재정의
        dataMap.put(type, newResult);
        result.put("data", dataMap);
        return result;
    }

    /**
     * 나의 데이터 > 목록 수
     *
     * @param query : 검색 값
     * @return
     */
    public List<String> getMyModelListCount(String query, List<Map<String, Object>> listData) {
        List<String> allList = new ArrayList<>();

        for (Map<String, Object> follow : listData) {
            // NOTE: 삭제된 데이터 모델 필터링
            if(follow.containsKey("deleted") && (boolean) follow.get("deleted")) {
                continue;
            }

            String dataType = (String) follow.get("type");
            String newDataType = getDataType(dataType);
            if (newDataType == null) {
                continue;
            }

            // 검색 처리
            String modelNm = (String) follow.get("name");
            if (modelNm.toLowerCase().contains(query.toLowerCase())) {
                allList.add((String) follow.get("id"));
            }

        }
        return allList;
    }

    /**
     * 나의 데이터 > 목록 정의
     * @param query
     * @param listData
     * @param from
     * @param size
     * @return
     */
    public List<DataModelDetailResponse> getMyModelList(String query, List<Map<String, Object>> listData, int from, int size) {
        List<DataModelDetailResponse> allList = new ArrayList<>();

        for (Map<String, Object> follow : listData) {
            // NOTE: 삭제된 데이터 모델 필터링
            if(follow.containsKey("deleted") && (boolean) follow.get("deleted")) {
                continue;
            }

            String followId = (String) follow.get("id");
            String dataType = (String) follow.get("type");
            String newDataType = getDataType(dataType);

            if (newDataType == null) {
                continue;
            }

            DataModelDetailResponse dataModelDetail;
            try {
                dataModelDetail = searchDetailService.getDataModelDetail(followId, newDataType);
            } catch (Exception e) {
                continue;
            }

            // 검색 처리
            // TODO: Storage의 경우 상세 데이터(dataModelDetail)를 확인해 Bucket 데이터 제외 필요
            if (dataModelDetail != null && dataModelDetail.getModelNm().toLowerCase().contains(query.toLowerCase())) {
                allList.add(dataModelDetail);
            }
        }
        int last = Math.min(allList.size(), from + size);
        if (from > last) {
            return new ArrayList<>();
        }
        return allList.subList(from, last);
    }

    /**
     * 데이터 상세 > 데이터 상세 조회 시 필요한 데이터 타입으로 변경
     * @param dataType
     * @return
     */
    private String getDataType(String dataType) {
        String newDataType = null;
        for (ModelIndex index : ModelIndex.values()) {
            if (index.name().equals(dataType)) {
                newDataType = index.name().equals(ModelIndex.table.name()) ? ModelType.TABLES.getValue():  ModelType.STORAGE.getValue();
                return newDataType;
            }
        }
        return newDataType;
    }

    /**
     * 데이터 모델 목록 > 상세 조회를 통해 북마크 데이터를 가져와 새로운 목록 리스트 생성
     *
     * @param resultMap
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> addFollowData(List<Map<String, Object>> resultMap) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        List<Map<String, Object>> newResult = new ArrayList<>();
        for (Map<String, Object> row : resultMap) {
            String id = (String) row.get("id");
            String dataType = (String) row.get("type");

            boolean isFollow;
            try {
                isFollow = searchDetailService.getFollowDataModel(userId, id, dataType);
            } catch (Exception e) {
                isFollow = false;
            }

            // follow 데이터 추가
            row.put("isFollow", isFollow);
            newResult.add(row);
        }
        return newResult;
    }

    public boolean checkDuplicateModelName(Map<String, Object> param) {
        String modelName = (String) param.get("modelName");
        String queryFilter = String.format("{\"query\":{\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"fullyQualifiedName\":\"datamodels.internalhive.default.%s\"}}]}}]}}}", modelName);
        String includeSourceFields = "name";

        Map<String, Object> modelInfo = modelCreationClient.selectDataModelName(queryFilter, includeSourceFields);

        Map<String, Object> hits = (Map<String, Object>) modelInfo.get("hits");
        Map<String, Object> total = (Map<String, Object>) hits.get("total");
        int totalValue = (int) total.get("value");

        return totalValue == 1;
    }

    public Object saveModel(Map<String, Object> param) throws Exception {
        return dolphinClient.createModel(param);
    }

}
