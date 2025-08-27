package com.mobigen.datalake.metaData.fusion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.search.SearchDetailMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataFusionService {
    private final ObjectMapper objectMapper;
    private final SearchDetailMapper searchDetailMapper;
    private final MetaDataFusionMapper metaDataFusionMapper;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.update}")
    private String esSyncUrlUpdate;

    @Value("${properties.catalog-api.base_url}")
    private String catalogApiBaseUrl;
    
    @Value("${properties.catalog-api.url.executeSync}")
    private String catalogApiUrlExecuteSync;

    // 데이터 융합 관리 대상 목록 조회
    public Map<String, Object> getMetaDataFusionList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        try {
            totCnt = metaDataFusionMapper.getMetaDataFusionListTotCnt(params);

            List<String> currentFusionIdList = (List<String>) params.get("currentFusionIdList");
            if (currentFusionIdList != null && currentFusionIdList.size() > 0) {
                params.put("currentFusionIdList", currentFusionIdList.toArray(new String[0]));
            } else {
                params.put("currentFusionIdList", new String[0]);
            }
            resultList = metaDataFusionMapper.getMetaDataFusionList(params);

        } catch (Exception e) {
            log.error("Fail to search system");
            e.printStackTrace();
        }

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    // 쿼리 실행
    public Map<String, Object> executeQuery(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> resultData = new LinkedHashMap<>();
        List<Map> list = new ArrayList<Map>();
        Integer totalCount = 0;

        // 시작 시각 초단위까지
        LocalDateTime startDateTime = LocalDateTime.now();
        String execute_start_time = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        long startTime = System.currentTimeMillis(); // 시작 시간(ms)
        try {
            // 샘플 데이터 조회 쿼리 제작
            String query = params.get("query").toString();
            String countQuery = "SELECT COUNT(*) FROM (" + query + ") T";
            String sampleQuery = query + " LIMIT 100";
            if (!query.equals("")) {
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                try {

                    Properties properties = new Properties();
                    properties.setProperty("SSL", "false");
                    properties.setProperty("user", params.get("user_id").toString());
                    // properties.setProperty("password", "secret");

                    // 샘플데이터 조회를 위한 정보 조회
                    connection = DriverManager.getConnection(trinoJdbcUrl, properties);

                    statement = connection.createStatement();
                    // 샘플데이터 카운트
                    rs = statement.executeQuery(countQuery);

                    String col;
                    while(rs.next()) {
                      ResultSetMetaData md = rs.getMetaData();
                      int soc = md.getColumnCount();
                      for (int indexOfColumn = 0; indexOfColumn < soc; indexOfColumn++) {
                          col = md.getColumnName(indexOfColumn + 1);
                          totalCount = Integer.parseInt(rs.getString(col));
                      }
                    }
                    
                    rs = statement.executeQuery(sampleQuery);

                    ResultSetMetaData metaData = rs.getMetaData();
                    int sizeOfColumn = metaData.getColumnCount(); // 컬럼 수
                    // 메타데이터에서 컬럼명 추출
                    Set<String> columnNames = new HashSet();
                    for (int i = 1; i <= sizeOfColumn; i++) {
                        columnNames.add(metaData.getColumnName(i));
                    }

                    result.put("columnNames" , columnNames);

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
                } catch (Exception e) {
                    // 권한 문제
                    if (e.getMessage().indexOf("Access Denied") != -1) {
                        result.put("errorMessage", "데이터 조회 권한이 없습니다.");
                    }
                    // 쿼리 문제
                    else if (e.getMessage().indexOf("Query failed") != -1) {
                        result.put("errorMessage", "쿼리가 잘못되었습니다.");
                    } else {
                        result.put("errorMessage", "데이터 조회에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
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

            // 융합 데이터 쿼리 조회 API 요청
            
        } catch (Exception e) {
            log.error("Fail to execute query - exception");
            result.put("errorMessage", e.getMessage());
            e.printStackTrace();
        } finally {
          long endTime = System.currentTimeMillis(); // 종료 시간(ms)
          long execute_duration_ms = endTime - startTime;
          double execute_duration_sec = execute_duration_ms / 1000.0;
          result.put("execute_duration", execute_duration_sec); // 실행 시간(초)
          result.put("execute_start_time", execute_start_time); // 실행 시각
        }

        result.put("sampleData", list); // 샘플 데이터
        result.put("columnList", list); // 컬럼 목록
        result.put("sampleDataTotal", totalCount); // 데이터 전체 카운트

        return result;
    }

    public Map<String, Object> saveFusionMetaData(Map<String, Object> metadata, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer saveEventResult = 0; // event log 저장 성공 여부
        boolean createViewResult = false; // View 생성 성공 여부
        Integer saveMetadataResult = 0; // 메타데이터 테이블 저장 성공 여부
        boolean executeResult = false; // 메타데이터 수동수집 실행 여부
        
        try {            
            // 1. event_log 저장
            Map<String, Object> eventParam = new HashMap<>();
            eventParam.put("event_type", 0); // 메타데이터 이벤트: 0 | 시스템 이벤트: 1
            eventParam.put("event_status", 1); // 0 해지 | 1:발생(생성) | 2: 변경 | 3: 삭제
            eventParam.put("event_name", "융합 메타 데이터 등록[발생]");
            eventParam.put("event_user_id", user.get("user_id"));
            eventParam.put("meta_id", metadata.get("meta_id"));
            eventParam.put("meta_name", metadata.get("meta_name"));
            eventParam.put("qualified_name", metadata.get("qualified_name"));
            saveEventResult = metaDataFusionMapper.saveEvent(eventParam);
            if (saveEventResult != null && saveEventResult > 0) {
                log.info("event log saved !!");
            } else {
                log.error("event log not saved !!");
            }

            String dateString = metadata.get("create_time") != null ? metadata.get("create_time").toString() : "";
            // LocalDateTime 변환
            LocalDateTime ldt = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Timestamp 변환
            Timestamp ts = Timestamp.valueOf(ldt);
            metadata.put("create_time", ts);

            // _varchar 타입은 ArrayList가 아닌 String[] 값을 넣어줘야함.
            List<String> classificationList = (List<String>) metadata.get("classifications");
            String[] classifications = null;
            if (classificationList != null) {

                classifications = classificationList.toArray(new String[0]);
            }
            metadata.put("classifications", classifications);

            // _varchar 타입은 ArrayList가 아닌 String[] 값을 넣어줘야함.
            List<String> tagList = (List<String>) metadata.get("tags");
            String[] tags = null;
            if (tagList != null) {
                tags = tagList.toArray(new String[0]);
            }
            metadata.put("tags", tags);
            
            // jsonb 타입은 Map -> JSON형태의 String값으로 변환 필요 넣어줘야함.
            metadata.put("attributes", objectMapper.writeValueAsString(metadata.get("attributes")));
            metadata.put("business_attributes", objectMapper.writeValueAsString(metadata.get("business_attributes")));
            
            // 2. 융합데이터 생성을 위한 CREATE VIEW 수행
            // internalhive.internalhive.default.fusion_test_1
            String[] queryStrSplit = metadata.get("qualified_name").toString().split("\\.");
            String system_id = queryStrSplit[0];
            String catalog = queryStrSplit[1];
            String tableSchema = queryStrSplit[2];
            String tableName = queryStrSplit[3];

            String createViewQuery = "";
            if (metadata.get("query") != null && !"".equals(metadata.get("query").toString())) {
                createViewQuery = "CREATE VIEW \"" + metadata.get("system_id").toString() + "\".\"default\".\"" + metadata.get("meta_name").toString()
                + "\" AS SELECT * FROM (" + metadata.get("query").toString() + ") VT";

                createViewQuery = createViewQuery.replaceAll("\\r?\\n", " ");
            }
             
            log.info("== create view query ==");
            log.info(createViewQuery);
            // Trino CREATE VIEW 수행
            if (!createViewQuery.equals("")) {
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                try {

                    Properties properties = new Properties();
                    properties.setProperty("SSL", "false");
                    // properties.setProperty("user", params.get("user_id").toString());
                    // properties.setProperty("password", "secret");

                    // 융합 데이터 저장을 위한 시스템 정보 조회
                    String connectionUrl = "";
                    Map<String, Object> dbInfo = searchDetailMapper.getDbInfo(system_id);
                    if (dbInfo != null) {
                        properties.setProperty("user", "admin");
                        // properties.setProperty("password", dbInfo.get("password").toString());
                        connectionUrl = trinoJdbcUrl + dbInfo.get("system_id").toString().toLowerCase() + "/"
                                + dbInfo.get("catalog");
                    }

                    if (connectionUrl == null || connectionUrl.equals("")) {
                        return null;
                    }
                    connection = DriverManager.getConnection(connectionUrl, properties);

                    statement = connection.createStatement();
                    statement.executeUpdate(createViewQuery);
                    createViewResult = true;

                    result.put("createViewResult", createViewResult);

                } catch (Exception e) {
                    // 권한 문제
                    if (e.getMessage().indexOf("Access Denied") != -1) {
                        result.put("errorMessage", e.getMessage());
                    }
                    // 쿼리 문제
                    else if (e.getMessage().indexOf("Query failed") != -1) {
                        if (e.getMessage().indexOf(" UCP-0: Unable to start the Universal Connection Pool") != -1) { // ORACLE
                            result.put("errorMessage", e.getMessage());
                        }
                    } else {
                        result.put("errorMessage", "융합 데이터 생성에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
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

                if (createViewResult) {
                    // 3. 메타데이터 테이블에 저장
                    saveMetadataResult = metaDataFusionMapper.saveMetadata(metadata);
                    if (saveMetadataResult != null && saveMetadataResult > 0) {
                        log.info("metadata saved !! !!");
                        // ESSync release 갱신
                        Map<String, Object> essyncParams = new HashMap<>();
                        String meta_id = metadata.get("meta_id").toString();
                        essyncParams.put("metaid", meta_id);
                        essyncParams.put("event_status", 2);
                        essyncParams.put("event_name", "데이터 권한설정 변경");
                        essyncUpdate(essyncParams);

                        Map<String, Object> executeBody = new HashMap<>();
                        executeBody.put("system_id", metadata.get("system_id").toString());
                        executeBody.put("meta_name", metadata.get("meta_name").toString());

                        Map<String, Object> resMap = (Map<String, Object>) executeCollect(executeBody);
                        if (resMap != null && (boolean)(resMap.get("executeResult"))) {
                            log.info("success to start collect !!");    
                            executeResult = true;
                        } else {
                            log.error("fail to start collect !!");
                            executeResult = false;
                        }
                    } else {
                        log.error("metadata not saved !!");
                    }
                } else {
                    log.error("fail to create view !!");
                }

                
            } else {
                log.info("no query for create view !!");
            }

            result.put("executeResult", executeResult);
            result.put("saveEventCount", saveEventResult);
            result.put("saveMetadataCount", saveMetadataResult);
            
        } catch (Exception e) {
            log.error("save fusion metadata Exception");
            e.printStackTrace();
            result.put("saveEventCount", saveEventResult);
            result.put("saveMetadataCount", saveMetadataResult);
        }
        
        return result;
    }

    public Map<String, Object> getFusionSystemInfo() throws Exception {
    Map<String, Object> result = new HashMap<>();

    try {
      String system_type = ""; // 시스템 타입
      String system_name = ""; // 시스템 명
      String meta_type = ""; // 데이터 타입
      // 1. code테이블에서 융합 전용 시스템 타입, 시스템 명 조회
      List<Map<String, Object>> fusionCodeList = metaDataFusionMapper.getFusionCodeInfo();
      if (fusionCodeList != null && fusionCodeList.size() > 0) {
        for (int i=0; i<fusionCodeList.size(); i++) {
          if (fusionCodeList.get(i).get("name") != null && fusionCodeList.get(i).get("name").equals("system_type"))
            system_type = fusionCodeList.get(i).get("value").toString();
            
          if (fusionCodeList.get(i).get("name") != null && fusionCodeList.get(i).get("name").equals("system_name"))
            system_name = fusionCodeList.get(i).get("value").toString();

          if (fusionCodeList.get(i).get("name") != null && fusionCodeList.get(i).get("name").equals("meta_type"))
            meta_type = fusionCodeList.get(i).get("value").toString();
        }

        if (system_name != null && !system_name.equals("") && system_type != null && !system_type.equals("")) {
          Map<String, Object> collectorMap = metaDataFusionMapper.getFusionSystemInfo(system_name, system_type);
          if (collectorMap != null) {
            collectorMap.put("meta_type", meta_type);
            result.put("result", true);
            result.put("fusionInfo", collectorMap);
          } else {
            log.error("Fail to select one for system info.. - no info");

            result.put("result", false);
            result.put("fusionInfo", null);
          }
        } else {
          log.error("Fail to select one for system info.. - no system_name or system_type");

          result.put("result", false);
          result.put("fusionInfo", null);
        }
      } else {
        log.error("Fail to select one for system info.. - no code list");

        result.put("result", false);
        result.put("fusionInfo", null);
      }

    } catch (Exception e) {
      log.error("Fail to select one system info.. - exception");
      e.printStackTrace();

      result.put("result", false);
      result.put("fusionInfo", null);
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

    // 융합 데이터 저장 Trino 수동수집 실행
    public Object executeCollect(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean executeResult = false;

        try {
            String system_id = "";
            String meta_name = "";
            if (executeBody.get("system_id") != null) {
                system_id = executeBody.get("system_id").toString();
            }
            if (executeBody.get("meta_name") != null) {
                meta_name = executeBody.get("meta_name").toString();
            }

            log.info("start to execute ingestion execute");
            
            // 시작 시각 초단위까지
            long startTime = System.currentTimeMillis(); // 시작 시간(ms)
            executeResult = ingestionExecuteSync(system_id, meta_name);
            // 종료 시각 초단위까지
            long endTime = System.currentTimeMillis(); // 종료 시간(ms)
            long execute_duration_ms = endTime - startTime;
            double execute_duration_sec = execute_duration_ms / 1000.0;
            log.info("end to execute ingestion execute - " + execute_duration_sec);

        } catch (Exception e) {
            executeResult = false;
            log.error("fail to execute direct Collect");
            e.printStackTrace();
        }
        
        result.put("executeResult", executeResult);
        return result;
    }

    // 수동 수집 실행 POST - SYNC
    public boolean ingestionExecuteSync(String system_id, String table_name) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = catalogApiBaseUrl + catalogApiUrlExecuteSync;

        Map<String, Object> requestBody = new HashMap<>();
        List<String> include = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        
        // 융합의 경우 table={테이블명}
        if (!table_name.equals("")) {
            // table=my_view1
            include.add("table=" + table_name);
        }

        requestBody.put("system_id", system_id);
        requestBody.put("include", include); 
        requestBody.put("exclude", exclude);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            return true;
        }

        return false;
    }
}
