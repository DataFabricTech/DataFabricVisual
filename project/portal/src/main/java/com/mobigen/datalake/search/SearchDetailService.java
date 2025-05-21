package com.mobigen.datalake.search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.framework.result.JsonResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchDetailService {
    private final SearchDetailMapper searchDetailMapper;
    private final ObjectMapper objectMapper;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.update}")
    private String esSyncUrlUpdate;

    // 데이터 상세 조회
    public Object getDetailData(Map<String, Object> params) throws Exception {
        Map<String, Object> result = searchDetailMapper.getDetailData(params.get("meta_id").toString());
        if (result != null) {
            // Postgresql jsonb 타입(JSON 문자열) Map 형태로 변환
            result.put("attributes", convertJsonbToMap(result.get("attributes")));
            result.put("business_attributes", convertJsonbToMap(result.get("business_attributes")));

            // _varchar 객체 배열에 있는 값으로 카테고리 정보 조회하여 Map 형태로 변환
            String[] ids = (String[]) result.get("classifications");

            // 카테고리 정보 목록 조회
            // 구조 [{ c_id, c_name, c_description }]
            if (ids != null && ids.length > 0) {
                List<Map<String, Object>> classificationList = searchDetailMapper.getClassificationList(ids);
                result.put("classifications", classificationList);
            }

            // 상세 페이지 조회 이력 저장
            Map<String, Object> saveData = new LinkedHashMap<>();
            saveData.put("user_id", params.get("user_id"));
            saveData.put("system_id", result.get("system_id"));
            saveData.put("system_name", result.get("system_name"));
            saveData.put("system_type", result.get("system_type"));
            saveData.put("meta_id", params.get("meta_id"));
            saveData.put("meta_name", result.get("meta_name"));
            saveData.put("meta_type", result.get("meta_type"));
            saveData.put("qualified_name", result.get("qualified_name"));
            saveData.put("description", result.get("description"));
            saveData.put("user_name", params.get("user_name"));
            saveData.put("dept_id", params.get("dept_id"));
            saveData.put("dept_name", params.get("dept_name"));
            searchDetailMapper.insertSearchMetaHistory(saveData);
        }

        return result;
    }

    // 데이터 상세 조회
    public Object getDetailDataForMain(Map<String, Object> params) throws Exception {
        Map<String, Object> result = searchDetailMapper.getDetailData(params.get("meta_id").toString());
        if (result != null) {
            // Postgresql jsonb 타입(JSON 문자열) Map 형태로 변환
            result.put("attributes", convertJsonbToMap(result.get("attributes")));
            result.put("business_attributes", convertJsonbToMap(result.get("business_attributes")));

            // _varchar 객체 배열에 있는 값으로 카테고리 정보 조회하여 Map 형태로 변환
            String[] ids = (String[]) result.get("classifications");

            // 카테고리 정보 목록 조회
            // 구조 [{ c_id, c_name, c_description }]
            if (ids != null && ids.length > 0) {
                List<Map<String, Object>> classificationList = searchDetailMapper.getClassificationList(ids);
                result.put("classifications", classificationList);
            }
        }

        return result;
    }

    // Jsonb => Map 타입 변환 함수
    public Map<String, Object> convertJsonbToMap(Object jsonb) throws Exception {
        if (jsonb != null) {
            return objectMapper.readValue((String) jsonb, new TypeReference<Map<String, Object>>() {
            });
        }
        return null;
    }

    // 데이터 상세 조회 - 메타데이터 가공
    public Object getDetailMetadatainfo(Map<String, Object> params) {
        JsonResult result = null;
        return result;
    }

    // front-end 에서 했던 로직을 server로 이전
    // 컬럼 목록 정리하는 로직
    public List makeReferredEntitiesAttributes(Map data) {
        List referredEntitiesAttributes = null;

        Map entity = (Map) data.get("entity");

        Map relationshipAttributes = (Map) entity.get("relationshipAttributes");
        if (relationshipAttributes == null) {
            return null;
        }

        List<Map> relationColumnsDataList = (List<Map>) relationshipAttributes.get("columns");
        List<Map> relationPartitionKeysDataList = (List<Map>) relationshipAttributes.get("partitionKeys");

        Map referredEntitiesDataList = (Map) data.get("referredEntities");

        if (relationColumnsDataList != null && relationColumnsDataList.size() > 0) {
            referredEntitiesAttributes = new ArrayList();
            int dataCnt = 0;

            Set<String> referredEntitiesDataKeys = referredEntitiesDataList.keySet();

            for (Map relationColumnsData : relationColumnsDataList) {
                for (String item : referredEntitiesDataKeys) {
                    if (relationColumnsData.get("guid").equals(item)) {
                        dataCnt++;
                        Map itemMap = (Map) referredEntitiesDataList.get(item);
                        referredEntitiesAttributes.add(itemMap.get("attributes"));
                    }
                }
            }

            if (relationPartitionKeysDataList != null && relationPartitionKeysDataList.size() > 0) {
                for (Map relationPartitionKeysData : relationPartitionKeysDataList) {
                    for (String item : referredEntitiesDataKeys) {
                        if (relationPartitionKeysData.get("guid").equals(item)) {
                            dataCnt++;
                            Map itemMap = (Map) referredEntitiesDataList.get(item);
                            Map itemAttributes = (Map) itemMap.get("attributes");
                            itemAttributes.put("position", dataCnt);
                            referredEntitiesAttributes.add(itemAttributes);
                        }
                    }
                }
            }

            // position 값을 기준으로 오름차순 정렬
            Collections.sort(referredEntitiesAttributes, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                    // position 값을 int로 변환하여 비교
                    Integer pos1 = (Integer) map1.get("position");
                    Integer pos2 = (Integer) map2.get("position");

                    return pos1.compareTo(pos2); // 오름차순 정렬
                }
            });
        }

        return referredEntitiesAttributes;
    }

    public Map makeMetaDataInfoResult(Map data, List<Map> referredEntitiesAttributes) {
        Map result = new HashMap<>();
        Map metaDataInfoResult = new HashMap<>();
        List refComment = new ArrayList();

        Map entity = (Map) data.get("entity");
        Map<String, Object> obj = (Map) entity.get("attributes");
        Map<String, Object> relationshipAttributes = (Map) entity.get("relationshipAttributes");

        for (String key : obj.keySet()) {
            Object value = obj.get(key);

            // 객체 타입인지 아닌지 판단
            if (!(value instanceof Map || value instanceof List)) {
                metaDataInfoResult.put(key, value);
            } else {
                if (value instanceof List && value != null) {
                    List<Object> guidByDisplayText = new ArrayList<>();
                    List<Map<String, Object>> keyList = (List<Map<String, Object>>) value;

                    if ("columns".equals(key)
                            && (referredEntitiesAttributes != null && referredEntitiesAttributes.size() > 0)) {
                        for (Map<String, Object> refItem : referredEntitiesAttributes) {
                            for (Map<String, Object> valItem : keyList) {
                                if (valItem.containsKey("guid")) {
                                    Object attr = relationshipAttributes.get(key);
                                    if (attr instanceof Map) {
                                        Map<String, Object> attrMap = (Map<String, Object>) attr;
                                        if (attrMap.containsKey("guid") && attrMap.get("guid") != null) {
                                            if (valItem.get("guid").equals(attrMap.get("guid"))
                                                    && refItem.get("name").equals(attrMap.get("displayText"))) {
                                                refComment.add(refItem.get("comment"));
                                                valItem.put("displayText", attrMap.get("displayText"));
                                                guidByDisplayText.add(attrMap.get("displayText"));
                                            }
                                        }
                                    } else if (attr instanceof List) {
                                        List<Map<String, Object>> attrList = (List<Map<String, Object>>) attr;
                                        for (Map attrRow : attrList) {
                                            if (attrRow.containsKey("guid") && attrRow.get("guid") != null) {
                                                if (valItem.get("guid").equals(attrRow.get("guid"))
                                                        && refItem.get("name").equals(attrRow.get("displayText"))) {
                                                    refComment.add(refItem.get("comment"));
                                                    valItem.put("displayText", attrRow.get("displayText"));
                                                    guidByDisplayText.add(attrRow.get("displayText"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        for (Object valItem : keyList) {
                            if (valItem instanceof Map) {
                                Map<String, Object> valMap = (Map<String, Object>) valItem;

                                if (valMap.containsKey("guid")) {
                                    Object attr = relationshipAttributes.get(key);
                                    if (attr instanceof Map) {
                                        Map<String, Object> attrMap = (Map<String, Object>) attr;
                                        if (attrMap.containsKey("guid") && attrMap.get("guid") != null) {
                                            if (valMap.get("guid").equals(attrMap.get("guid"))) {
                                                valMap.put("displayText", attrMap.get("displayText"));
                                                guidByDisplayText.add(attrMap.get("displayText"));
                                            }
                                        }
                                    } else if (attr instanceof List) {
                                        List<Map<String, Object>> attrList = (List<Map<String, Object>>) attr;
                                        for (Map attrRow : attrList) {
                                            if (attrRow.containsKey("guid") && attrRow.get("guid") != null) {
                                                if (valMap.get("guid").equals(attrRow.get("guid"))) {
                                                    valMap.put("displayText", attrRow.get("displayText"));
                                                    guidByDisplayText.add(attrRow.get("displayText"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    metaDataInfoResult.put(key, guidByDisplayText);
                } else {
                    if (value != null) {
                        if (((Map<String, Object>) value).containsKey("guid")
                                && ((Map<String, Object>) value).get("guid") != null) {
                            Object attr = relationshipAttributes.get(key);
                            if (attr instanceof Map && ((Map<String, Object>) attr).containsKey("displayText")) {
                                metaDataInfoResult.put(key, ((Map<String, Object>) attr).get("displayText"));
                            } else {
                                metaDataInfoResult.put(key, value);
                            }
                        } else {
                            metaDataInfoResult.put(key, value);
                        }
                    }
                }
            }

        }

        result.put("metaDataInfoResult", metaDataInfoResult);
        result.put("refComment", refComment);

        return result;
    }

    // 프로파일 데이터 조회
    public Object getDetailDataProfile(Map<String, Object> params) {
        JsonResult result = new JsonResult();
        Map<String, Object> resultData = new LinkedHashMap<>();

        // 테이블 프로파일 데이터
        Map<String, Object> tableData = searchDetailMapper.getTableData((String) params.get("guid"));
        resultData.put("tableData", tableData);

        // 컬럼 프로파일 데이터
        if (params.get("columnData") != null) {
            if (!((List) params.get("columnData")).isEmpty()) {
                List<Map> columnData = searchDetailMapper.getColumnData((List<String>) params.get("columnData"));
                resultData.put("columnData", columnData);
            }
        }

        result.setData(resultData);

        return result;
    }

    // 데이터 전체 건수 조회
    public Object getRowCount(String guid) throws Exception {
        int rowCount = 0;

        // 테이블 프로파일에서 조회
        Map<String, Object> tblData = searchDetailMapper.getTableData(guid);
        if (tblData != null) {
            if (tblData.get("num_rows") != null)
                rowCount = Integer.parseInt(tblData.get("num_rows").toString());
        }

        return rowCount;
    }

    // 샘플 데이터 10건 조회
    public Object getSampleData(Map<String, Object> params) {
        JsonResult result = new JsonResult();
        Map<String, Object> resultData = new LinkedHashMap<>();
        List<Map> list = new ArrayList<Map>();

        // 샘플 데이터 조회 쿼리 제작
        String query = "";

        // 테이블 Type인 경우 => DB 스키마 :
        // 109ae637-9e13-44f9-9686-a79cf1e12499.data_catalog.public /
        if (params.get("typeName").toString().endsWith("table")) {
            String[] queryStrSplit = params.get("qualified_name").toString().split("\\.");
            String system_id = queryStrSplit[0];
            String dbName = queryStrSplit[1];
            String tableSchema = queryStrSplit[2];
            String tableNameStr = queryStrSplit[3];
            String[] tableNameArr = tableNameStr.split("\\.");
            StringBuilder tableName = new StringBuilder();
            if (tableNameArr.length > 1) {
                for (int i = 0; i < tableNameArr.length; i++) {
                    if (!tableName.toString().equals("")) {
                        tableName.append(".").append("\"" + tableNameArr[i] + "\"");
                    } else {
                        tableName.append("\"" + tableNameArr[i] + "\"");
                    }
                }
            } else {
                tableName.append("\"" + tableNameStr + "\"");
            }

            if (params.get("columns") != null && params.get("columns") != "") {
                // query = "select " + params.get("columns") + " from \"" + dbName + "\".\"" +
                // catalog + "\".\"" + tableSchema + "." + tableName + " limit 10";
                query = "select " + params.get("columns") + " from \"" + system_id + "\".\"" + tableSchema + "."
                        + tableName + " limit 10";
            } else {
                // query = "select * from \"" + dbName + "\".\"" + catalog + "\".\"" +
                // tableSchema + "\"." + tableName + " limit 10";
                query = "select * from \"" + system_id + "\".\"" + tableSchema + "\"." + tableName + " limit 10";
            }
        }

        // Trino 조회
        if (!query.equals("")) {
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            try {

                Properties properties = new Properties();
                properties.setProperty("SSL", "false");
                // properties.setProperty("user", params.get("user_id").toString());
                // properties.setProperty("password", "secret");

                // 샘플데이터 조회를 위한 정보 조회
                String connectionUrl = "";
                String[] queryStrSplit = params.get("qualified_name").toString().split("\\.");
                String system_id = queryStrSplit[0];
                Map<String, Object> dbInfo = searchDetailMapper.getDbInfo(system_id);
                if (dbInfo != null) {
                    properties.setProperty("user", "admin");
                    // properties.setProperty("password", dbInfo.get("password").toString());
                    connectionUrl = trinoJdbcUrl + dbInfo.get("system_id").toString().toLowerCase() + "/"
                            + dbInfo.get("database");
                }

                if (connectionUrl == null || connectionUrl.equals("")) {
                    return null;
                }
                connection = DriverManager.getConnection(connectionUrl, properties);

                statement = connection.createStatement();
                rs = statement.executeQuery(query);

                ResultSetMetaData metaData = rs.getMetaData();
                int sizeOfColumn = metaData.getColumnCount(); // 컬럼 수

                String column;
                while (rs.next()) {
                    Map<String, Object> columnMap = new LinkedHashMap<>();
                    for (int indexOfColumn = 0; indexOfColumn < sizeOfColumn; indexOfColumn++) {
                        column = metaData.getColumnName(indexOfColumn + 1);

                        columnMap.put(column, rs.getString(column));
                    }
                    list.add(columnMap);
                }

                rs.close();
                statement.close();
                connection.close();
                resultData.put("sampleData", list);

            } catch (Exception e) {
                // 권한 문제
                if (e.getMessage().indexOf("Access Denied") != -1) {
                    resultData.put("errorMessage", e.getMessage());
                }
                // 쿼리 문제
                else if (e.getMessage().indexOf("Query failed") != -1) {
                    if (e.getMessage().indexOf(" UCP-0: Unable to start the Universal Connection Pool") != -1) { // ORACLE
                        resultData.put("errorMessage", e.getMessage());
                    }
                } else {
                    resultData.put("errorMessage", "샘플데이터 조회에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
                }
                e.printStackTrace();
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (statement != null)
                    try {
                        statement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (connection != null)
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

        result.setData(resultData);

        return result;
    }

    // 사용자 데이터 설명 목록 조회
    public Object getUserDescriptionList(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 목록 조회
            List<Map> userDescriptionList = searchDetailMapper.getUserDescriptionList(params);
            result.put("userDescriptionList", userDescriptionList);

            // 메뉴 권한 조회
            int authCount = searchDetailMapper.getUserDescriptionAdminAuthCnt(params);
            if (authCount > 0) {
                result.put("isUserDescriptionMenuAuth", true);
            }

        } catch (Exception e) {
            log.error("사용자 데이터 설명 목록 조회 실패");
            result.put("userDescriptionList", new ArrayList<>());
            result.put("isUserDescriptionMenuAuth", false);
        }

        return result;
    }

    // 사용자 데이터 설명 등록
    public Object insertUserDescription(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>(); // ES 업데이트 API 호출 결과
        int insertResult = 0; // user_description postgres 등록 여부
        int updateMetaResult = 0; // <table> 테이블에 등록 여부
        Boolean isFailed = false; // ES 업데이트 API 호출 성공 여부
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        // 1. postgres 등록
        insertResult = searchDetailMapper.insertUserDescription(params);

        if (insertResult > 0) {
            // ES에 description 등록 결정 로직
            Map favoriteData = searchDetailMapper.getUserDescription((String) params.get("guid"));
            if (!ObjectUtils.isEmpty(favoriteData)) {
                // 조회된 데이터 설명 추천수가 0이면
                if ((int) favoriteData.get("favorite") == 0) {
                    // 2. MetaData 테이블에 user_description 업데이트
                    updateMetaResult = searchDetailMapper.updateMetaUserDescription(params);

                    // 3. ES 신규 user_description 등록
                    // ES 업데이트 API 수행 결과 - API 호출 필요
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("metaid", params.get("guid"));
                    requestBody.put("event_status", 1); // 1=생성, 2=수정, 3=삭제
                    requestBody.put("event_name", "create user_description");
                    Map<String, Object> restResponse = essyncUpdate(requestBody);

                    boolean esResult = false;
                    if (!restResponse.isEmpty()) {
                        Map<String, String> res = (Map<String, String>) restResponse.get("result");
                        if (res.get("result").equals("success")) {
                            isFailed = false;
                            esResult = true;
                        } else {
                            isFailed = true;
                            esResult = false;
                        }
                    }

                    if (esResult) { // ES 등록 성공
                        esUpdated = true;
                    } else { // ES 등록 실패
                        result.put("errorMessage", "ES 업데이트 실패");
                        isFailed = true;
                    }
                }
            }
        }

        result.put("insertResult", insertResult);
        result.put("updateMetaResult", updateMetaResult);
        result.put("isFail", isFailed);
        result.put("esUpdated", esUpdated);

        return result;
    }

    // 사용자 데이터 설명 수정
    public Object updateUserDescription(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>(); // ES 업데이트 API 호출 결과
        int updateResult = 0; // user_description postgres 수정 여부
        int updateMetaResult = 0; // <table> 테이블에 등록 여부
        Boolean isFailed = false; // ES 업데이트 API 호출 성공 여부
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        // 1. postgres 수정
        updateResult = searchDetailMapper.updateUserDescription(params);

        if (updateResult > 0) {
            // ES에 description 등록 결정 로직
            // 사용자 데이터 설명 추천수 높은 1건 조회
            Map favoriteData = searchDetailMapper.getUserDescription((String) params.get("guid"));
            if (!ObjectUtils.isEmpty(favoriteData)) {
                // 조회된 id와 수정하려는 id가 같은 경우
                if (favoriteData.get("id").toString().equals(params.get("id").toString())) {
                    // 2. MetaData 테이블에 user_description 업데이트
                    updateMetaResult = searchDetailMapper.updateMetaUserDescription(params);

                    // 3. ES 신규 user_description 등록
                    // ES 업데이트 API 수행 결과 - API 호출 필요
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("metaid", params.get("guid"));
                    requestBody.put("event_status", 2); // 1=생성, 2=수정, 3=삭제
                    requestBody.put("event_name", "update user_description");
                    Map<String, Object> restResponse = essyncUpdate(requestBody);

                    boolean esResult = false;
                    if (!restResponse.isEmpty()) {
                        Map<String, String> res = (Map<String, String>) restResponse.get("result");
                        if (res.get("result").equals("success")) {
                            isFailed = false;
                            esResult = true;
                        } else {
                            isFailed = true;
                            esResult = false;
                        }
                    }

                    if (esResult) { // ES 수정 성공
                        esUpdated = true;
                    } else { // ES 등록 실패
                        result.put("errorMessage", "ES 업데이트 실패");
                        isFailed = true;
                    }
                }
            }
        }

        result.put("updateResult", updateResult);
        result.put("updateMetaResult", updateMetaResult);
        result.put("isFail", isFailed);
        result.put("esUpdated", esUpdated);

        return result;
    }

    // 사용자 데이터 설명 삭제
    public Object deleteUserDescription(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>(); // ES 업데이트 API 호출 결과
        int deleteResult = 0; // user_description postgres 삭제 여부
        int updateMetaResult = 0; // <table> 테이블에 업데이트 여부
        int deleteMetaResult = 0; // <table> 테이블에서 null 처리 여부
        Boolean isFailed = false; // ES 업데이트 API 호출 성공 여부
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        // 1. postgres 등록
        deleteResult = searchDetailMapper.deleteUserDescription(params);

        if (deleteResult > 0) {
            // ES에 description 등록 결정 로직
            // 사용자 데이터 설명 추천수 높은 1건 조회
            Map favoriteData = searchDetailMapper.getUserDescription((String) params.get("guid"));

            // 3. ES 신규 user_description 등록
            // ES 업데이트 API 수행 결과 - API 호출 필요
            Map<String, Object> requestBody = new HashMap<>();

            if (!ObjectUtils.isEmpty(favoriteData)) { // 조회된 데이터가 있으면 해당 값으로 설정
                requestBody.put("metaid", favoriteData.get("meta_id"));
                requestBody.put("event_status", 2); // 1=생성, 2=수정, 3=삭제
                requestBody.put("event_name", "delete user_description");

                // MetaData 테이블에 해당 데이터로 user_description 업데이트
                Map<String, Object> newParams = new HashMap<>();
                newParams.put("meta_id", params.get("guid"));
                newParams.put("user_description", favoriteData.get("user_description"));
                updateMetaResult = searchDetailMapper.updateMetaUserDescription(newParams);
            } else {
                requestBody.put("metaid", params.get("guid"));
                requestBody.put("event_status", 3); // 1=생성, 2=수정, 3=삭제
                requestBody.put("event_name", "delete user_description");

                // MetaData 테이블에 해당 데이터로 user_description Null 처리
                deleteMetaResult = searchDetailMapper.deleteMetaUserDescription((String) params.get("guid"));
            }

            Map<String, Object> restResponse = essyncUpdate(requestBody);

            boolean esResult = false;
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                    isFailed = false;
                    esResult = true;
                } else {
                    isFailed = true;
                    esResult = false;
                }
            }

            if (esResult) { // ES 삭제 성공
                esUpdated = true;
            } else { // ES 등록 실패
                result.put("errorMessage", "ES 업데이트 실패");
                isFailed = true;
            }
        }

        result.put("deleteResult", deleteResult);
        result.put("updateMetaResult", updateMetaResult);
        result.put("deleteMetaResult", deleteMetaResult);
        result.put("isFail", isFailed);
        result.put("esUpdated", esUpdated);

        return result;
    }

    // 사용자 데이터 설명 추천 상태 조회
    public Object getUserDescriptionFavoriteStatus(Map<String, Object> params) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        Integer favoriteCounts = 0; // 해당 설명에 대한 전체 추천수
        Boolean isFavorite = false; // 해당 설명에 대한 로그인 유저의 추천 정보

        favoriteCounts = searchDetailMapper.getUserDescriptionFavoriteCounts(params);
        Map favoriteData = searchDetailMapper.getUserDescriptionFavoriteStatus(params);

        // 추천 정보가 존재하면
        if (!ObjectUtils.isEmpty(favoriteData)) {
            isFavorite = true;
        }

        resultMap.put("favoriteCounts", favoriteCounts);
        resultMap.put("isFavorite", isFavorite);

        return resultMap;
    }

    // 사용자 데이터 설명 추천
    public Object insertUserDescriptionFavorite(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>(); // ES 업데이트 API 호출 결과
        int insertResult = 0; // postgres 등록 여부
        int addResult = 0; // postgres count + 1 여부
        Boolean isFavorite = false; // 좋아요 클릭 여부
        int favoriteCounts = 0; // 해당 설명에 대한 전체 추천수
        int updateMetaResult = 0; // <table> 테이블에 업데이트 여부
        Boolean isFailed = false; // ES 업데이트 API 호출 성공 여부
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        insertResult = searchDetailMapper.insertUserDescriptionFavorite(params);
        addResult = searchDetailMapper.addUserDescriptionFavoriteCount((Integer) params.get("id"));

        // 추천 등록 성공
        if (insertResult == 1 && addResult == 1) {
            isFavorite = true;
            favoriteCounts = searchDetailMapper.getUserDescriptionFavoriteCounts(params); // 해당 설명에 대한 전체 추천수

            // 추천수 높은 1건 조회
            Map<String, Object> favoriteData = searchDetailMapper.getUserDescription((String) params.get("guid"));

            if (!ObjectUtils.isEmpty(favoriteData)) {
                // 내가 추천한 글이 가장 높은 추천수인 경우
                if (favoriteData.get("id").toString().equals(params.get("id").toString())) {
                    // 2. MetaData 테이블에 user_description 업데이트
                    Map<String, Object> newParams = new HashMap<>();
                    newParams.put("guid", favoriteData.get("meta_id"));
                    newParams.put("user_description", favoriteData.get("user_description"));
                    updateMetaResult = searchDetailMapper.updateMetaUserDescription(newParams);

                    // 3. ES 신규 user_description 등록
                    // ES 업데이트 API 수행 결과 - API 호출 필요
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("metaid", params.get("guid"));
                    requestBody.put("event_status", 2); // 1=생성, 2=수정, 3=삭제
                    requestBody.put("event_name", "update user_description");
                    Map<String, Object> restResponse = essyncUpdate(requestBody);

                    boolean esResult = false;
                    if (!restResponse.isEmpty()) {
                        Map<String, String> res = (Map<String, String>) restResponse.get("result");
                        if (res.get("result").equals("success")) {
                            isFailed = false;
                            esResult = true;
                        } else {
                            isFailed = true;
                            esResult = false;
                        }
                    }

                    if (esResult) { // ES 수정 성공
                        esUpdated = true;
                    } else { // ES 등록 실패
                        result.put("errorMessage", "ES 업데이트 실패");
                        isFailed = true;
                    }
                }
            }
        }

        result.put("insertResult", insertResult);
        result.put("addResult", addResult);
        result.put("isFailed", isFailed);
        result.put("isFavorite", isFavorite);
        result.put("favoriteCounts", favoriteCounts);
        result.put("esUpdated", esUpdated);
        result.put("updateMetaResult", updateMetaResult);

        return result;
    }

    // 사용자 데이터 설명 추천 취소
    public Object deleteUserDescriptionFavorite(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>(); // ES 업데이트 API 호출 결과
        int deleteResult = 0; // postgres 등록 여부
        int subResult = 0; // postgres count -1 여부
        Boolean isFavorite = false; // 좋아요 클릭 여부
        int favoriteCounts = 0; // 해당 설명에 대한 전체 추천수
        int updateMetaResult = 0; // <table> 테이블에 업데이트 여부
        Boolean isFailed = false; // ES 업데이트 API 호출 성공 여부
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        deleteResult = searchDetailMapper.deleteUserDescriptionFavorite(params);
        subResult = searchDetailMapper.subUserDescriptionFavoriteCount((Integer) params.get("id"));

        // 추천 등록 성공
        if (deleteResult == 1 && subResult == 1) {
            isFavorite = false;
            favoriteCounts = searchDetailMapper.getUserDescriptionFavoriteCounts(params); // 해당 설명에 대한 전체 추천수

            // 추천수 높은 1건 조회
            Map<String, Object> favoriteData = searchDetailMapper.getUserDescription((String) params.get("guid"));
            if (!ObjectUtils.isEmpty(favoriteData)) {
                // 추천취소한 설명과 가장 높은 추천수 데이터가 다른 경우 수정
                if (!favoriteData.get("id").toString().equals(params.get("id").toString())) {
                    // MetaData 테이블에 해당 데이터로 user_description 업데이트
                    Map<String, Object> newParams = new HashMap<>();
                    newParams.put("guid", favoriteData.get("meta_id"));
                    newParams.put("user_description", favoriteData.get("user_description"));
                    updateMetaResult = searchDetailMapper.updateMetaUserDescription(newParams);

                    // ES 신규 user_description 등록
                    // ES 업데이트 API 수행 결과 - API 호출 필요
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("metaid", params.get("guid"));
                    requestBody.put("event_status", 2); // 1=생성, 2=수정, 3=삭제
                    requestBody.put("event_name", "update user_description");
                    Map<String, Object> restResponse = essyncUpdate(requestBody);

                    boolean esResult = false;
                    if (!restResponse.isEmpty()) {
                        Map<String, String> res = (Map<String, String>) restResponse.get("result");
                        if (res.get("result").equals("success")) {
                            isFailed = false;
                            esResult = true;
                        } else {
                            isFailed = true;
                            esResult = false;
                        }
                    }

                    if (esResult) { // ES 수정 성공
                        esUpdated = true;
                    } else { // ES 등록 실패
                        result.put("errorMessage", "ES 업데이트 실패");
                        isFailed = true;
                    }
                }
            }
        }

        result.put("deleteResult", deleteResult);
        result.put("subResult", subResult);
        result.put("isFailed", isFailed);
        result.put("isFavorite", isFavorite);
        result.put("favoriteCounts", favoriteCounts);
        result.put("esUpdated", esUpdated);
        result.put("updateMetaResult", updateMetaResult);

        return result;
    }

    // 사용자 리뷰 목록 조회
    public Object getReviewData(String guid) {
        JsonResult result = new JsonResult();
        Map<String, Object> resultData = new LinkedHashMap<>();

        List<Map> reviewDatalist = searchDetailMapper.getReviewData(guid);
        resultData.put("reviewDatalist", reviewDatalist);
        result.setData(resultData);

        return result;
    }

    // 사용자 리뷰 등록
    public Object insertUserReview(Map<String, Object> params) {
        return searchDetailMapper.insertUserReview(params);
    }

    // 사용자 리뷰 수정
    public Object updateUserReview(Map<String, Object> params) {
        return searchDetailMapper.updateUserReview(params);
    }

    // 사용자 리뷰 삭제
    public Object deleteUserReview(int params) {
        return searchDetailMapper.deleteUserReview(params);
    }

    // 데이터 추천 & 찜하기 여부 조회
    public Object getRecommendStatus(Map<String, Object> params) {
        Map<String, Object> result = new LinkedHashMap<>();
        Boolean isRecommended = false; // 데이터 추천 여부
        Boolean isFavored = false; // 데이터 찜하기 여부

        Integer recommendCounts = searchDetailMapper.getDataRecommendedCounts(params);
        Map recommendData = searchDetailMapper.getDataRecommendedByUser(params); // 추천 유저 정보
        Map favoredData = searchDetailMapper.getDataFavoredByUser(params); // 찜하기 유저 정보

        if (!ObjectUtils.isEmpty(recommendData)) {
            isRecommended = true;
        }

        if (!ObjectUtils.isEmpty(favoredData)) {
            isFavored = true;
        }

        result.put("counts", recommendCounts);
        result.put("isRecommended", isRecommended);
        result.put("isFavored", isFavored);

        return result;
    }

    // 데이터 추천 저장
    public Object saveDataRecommendation(Map<String, Object> params) {
        Boolean isDeleting = (Boolean) params.get("isDeleting"); // 추천 취소 상태 여부
        if (isDeleting) {
            return searchDetailMapper.deleteDataRecommendation(params);
        } else {
            return searchDetailMapper.insertDataRecommendation(params);
        }
    }

    // 데이터 찜하기 등록
    public Object saveDataFavorite(Map<String, Object> params) {
        Boolean isDeleting = (Boolean) params.get("isDeleting"); // 찜하기 취소 상태 여부
        if (isDeleting) {
            return searchDetailMapper.deleteDataFavorite(params);
        } else {
            return searchDetailMapper.insertDataFavorite(params);
        }
    }

    // 비지니스 속성 등록
    public Object saveEntityAttributes(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean isFailed = false;
        boolean esUpdated = false;

        // 비즈니스 속성 업데이트를 위한 값 가공
        Map<String, Object> entity = (Map<String, Object>) params.get("entity");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("guid", entity.get("guid"));
        paramMap.put("business_attributes", objectMapper.writeValueAsString(entity.get("business_attributes")));

        int insertResult = searchDetailMapper.updateMetaBusinessAttributes(paramMap);
        // 3. ES 신규 business_attributes 등록
        // ES 업데이트 API 수행 결과 - API 호출 필요
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("metaid", entity.get("guid"));
        requestBody.put("event_status", 2); // 1=생성, 2=수정, 3=삭제
        requestBody.put("event_name", "update business_attributes");
        Map<String, Object> restResponse = essyncUpdate(requestBody);

        boolean esResult = false;
        if (!restResponse.isEmpty()) {
            Map<String, String> res = (Map<String, String>) restResponse.get("result");
            if (res.get("result").equals("success")) {
                isFailed = false;
                esResult = true;
            } else {
                isFailed = true;
                esResult = false;
            }
        }

        if (esResult) { // ES 등록 성공
            esUpdated = true;
        } else { // ES 등록 실패
            result.put("errorMessage", "ES 업데이트 실패");
            isFailed = true;
        }
        return insertResult;
    }

    // 데이터 조회 사용자 권한 확인
    // public Object getAuthCheckList(Map<String, Object> params) {
    // Map<String, Object> result = new LinkedHashMap<>();
    // List<Map> authCheckList = new ArrayList<>();
    // Boolean isAuthorized = false; // 권한 여부

    // // 최상위 관리자인 경우
    // if (params.get("userId").toString().equals("bigadm")) {
    // isAuthorized = true;
    // } else {
    // authCheckList = searchDetailMapper.getAuthCheckList(params);
    // if (authCheckList.size() <= 0) isAuthorized = true;
    // }

    // result.put("authCheckList", authCheckList);
    // result.put("isAuthorized", isAuthorized);

    // return result;
    // }

    // 데이터 담당 부서 조회
    public Object getDataManagerInfoList(Map<String, Object> params) {
        JsonResult result = new JsonResult();
        List<Map<String, String>> dataManagerInfoList = new ArrayList<>();
        try {
            List<Object> classificationsList = (List<Object>) params.get("classificationsList");
            List<String> paramList = new ArrayList<>();
            for (Object classificationsData : classificationsList) {
                Map<String, String> data = (Map<String, String>) classificationsData;
                paramList.add(data.get("typeName"));
            }

            List<Map<String, String>> infoList = searchDetailMapper.getDataManagerInfoList();

            for (Map<String, String> info : infoList) {
                Map<String, String> dataManagerInfo = new LinkedHashMap<>();
                dataManagerInfo.put("user_nm", info.get("user_nm"));
                dataManagerInfo.put("dept_nm", info.get("dept_nm"));
                dataManagerInfo.put("dept_nm2", info.get("dept_nm2"));
                // if(info.get("user_email_ext") != null){ // 현진 담장자의 이메일을 사용하지 않아 주석처리
                // dataManagerInfo.put("user_email",
                // Crypto.AES_Decode(info.get("user_email_ext")));
                // }else{
                // if(info.get("user_email") != null){
                // dataManagerInfo.put("user_email", Crypto.AES_Decode(info.get("user_email")));
                // }
                // }
                dataManagerInfoList.add(dataManagerInfo);
            }
            result.setData(dataManagerInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 데이터 존재유무 확인
    public Map<String, Object> checkExistMetaData(String meta_id) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer cnt = searchDetailMapper.checkExistMetaData(meta_id);
            if (cnt != null && cnt > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }

        } catch(Exception e) {
            result.put("result", false);
        }
        return result;
    }

    // 사용자의 데이터 접근 권한 조회
    public Map<String, Object> getUserDataAuth(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 데이터 관리자 확인
            // 메타 데이터 관리 > 메타 데이터 통합 관리의 상세페이지인 경우, 메뉴권한이 있는 경우 모두 허용처리
            // 현재 메뉴, role_id 확인하여 권한있으면 모두 허용
            Integer authCnt = 0;
            boolean isAdmin = params.get("is_admin") != null ? (Boolean) params.get("is_admin") : false;

            // 관리자 페이지에서 상세조회
            if (isAdmin) {
                authCnt = searchDetailMapper.getMetaDataAdminAuthCnt(params);
                if (authCnt != null && authCnt > 0) {
                    result.put("role_auth", 2);
                }
                // 사용자 페이지에서 상세조회
            } else {
                // 2. release 확인 (0: secret, 1: 모두허용, 2: 권한체크)
                String meta_id = params.get("meta_id").toString();
                Integer release = searchDetailMapper.getMetaDataRelease(meta_id);

                if (release != null) {
                    if (release == 2) {
                        Integer authValue = searchDetailMapper.getUserDataAuth(params);
                        int role_auth = (authValue != null) ? authValue : -1;
                        result.put("role_auth", role_auth);
                    } else if (release == 1) {
                        result.put("role_auth", 2);
                    } else { // 나머지는 -1 처리
                        result.put("role_auth", -1);
                    }
                } else {
                    result.put("role_auth", -1);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("권한 조회 실패");
            result.put("role_auth", -1);
        }

        return result;
    }

    public Map<String, Object> essyncUpdate(Map<String, Object> params) throws Exception {
        Map<String, Object> restResponse = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaid", params.get("metaid"));
            requestBody.put("event_status", params.get("event_status")); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", params.get("event_name"));

            String url = esSyncBaseUrl + esSyncUrlUpdate;
            restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        } catch (Exception e) {
            log.error("essyncUpdate API 호출 오류");
        }

        return restResponse;
    }
}
