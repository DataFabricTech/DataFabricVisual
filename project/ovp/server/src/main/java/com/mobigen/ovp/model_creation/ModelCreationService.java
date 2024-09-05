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

    public Object addBookMark(String id) throws Exception {
        Map<String, Object> userInfo = userClient.getUserInfo();
        String jsonLoginUserId = "\"" + userInfo.get("id") + "\"";
        modelCreationClient.addBookMark(id, jsonLoginUserId);
        return null;
    }

    public Object removeBookMark(String id) throws Exception {
        Map<String, Object> userInfo = userClient.getUserInfo();
        modelCreationClient.deleteBookMark(id, (String) userInfo.get("id"));
        return null;
    }

    /**
     * 데이터 모델 생성 > MY 리스트
     *
     * @param id : 사용자 아이디
     * @param query : 검색어
     * @return
     * @throws Exception
     */
    public Object getMyList(String id, String query) {
        Map<String, Object> param = new HashMap<>();
        param.put("bookmark", getMyModelList(id, query, "follows", null));
        param.put("owner", getMyModelList(id, query, "owns",null));

        return param;
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
        List<Map<String, Object>> newResult= addFollowData(resultMap);

        // 데이터 재정의
        dataMap.put(type, newResult);
        result.put("data", dataMap);
        return result;
    }

    /**
     * 나의 데이터 조회
     *
     * @param id : 사용자 아이디
     * @param query : 검색 값
     * @param key : open-metadata key
     * @return
     */
    public Object getMyModelList(String id, String query, String key, Integer limit) {
        Map<String, Object> userInfo = userClient.getUserWithFields(id, key);
        List<Map<String, Object>> followsList = (List<Map<String, Object>>) userInfo.get(key);
        List<DataModelDetailResponse> tempResult = new ArrayList<>();
        List<DataModelDetailResponse> allList = new ArrayList<>();

        for (Map<String, Object> follow : followsList) {
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
            if (dataModelDetail != null && dataModelDetail.getModelNm().contains(query)) {
                tempResult.add(dataModelDetail);
            }

            // 갯수에 따른 목록 파싱
            if (limit != null && limit > 0 && !tempResult.isEmpty()) {
                for (int i = 0; i < Math.min(limit, tempResult.size()); i++) {
                    allList.add(tempResult.get(i));
                }
            } else {
                allList = tempResult;
            }
        }

        return allList;
    }
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
}
