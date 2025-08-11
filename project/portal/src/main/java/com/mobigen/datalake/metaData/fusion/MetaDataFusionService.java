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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.system.collector.CollectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataFusionService {
    private final ObjectMapper objectMapper;
    private final CollectorService collectorService;
    private final MetaDataFusionMapper metaDataFusionMapper;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

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
        result.put("sampleDataTotal", totalCount); // 데이터 전체 카운트

        return result;
    }

    public Map<String, Object> saveFusionMetaData(Map<String, Object> metadata, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer saveEventResult = 0; // event log 저장 성공 여부
        Integer saveMetadataResult = 0; // 메타데이터 테이블 저장 성공 여부
        
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
            
            // 2. 메타데이터 테이블에 저장
            saveMetadataResult = metaDataFusionMapper.saveMetadata(metadata);
            if (saveMetadataResult != null && saveMetadataResult > 0) {
                log.info("metadata saved !! !!");
            } else {
                log.error("metadata not saved !!");
            }

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
}
