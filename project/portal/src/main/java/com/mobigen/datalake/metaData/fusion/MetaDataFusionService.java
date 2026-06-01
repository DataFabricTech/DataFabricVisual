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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.search.SearchDetailMapper;
import org.postgresql.util.PGobject;

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

    @Value("${properties.trino.udf-schema}")
    private String trinoUdfSchema;

    @Value("${properties.trino.udf-catalog}")
    private String trinoUdfCatalog;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.update}")
    private String esSyncUrlUpdate;

    @Value("${properties.catalog-api.base_url}")
    private String catalogApiBaseUrl;
    
    @Value("${properties.catalog-api.url.executeSync}")
    private String catalogApiUrlExecuteSync;

    @Value("${properties.catalog-api.url.profile.default}")
    private String catalogApiUrlProfile;

    @Value("${properties.dataintegration.base_url}")
    private String dataIntegrationBaseUrl;

    @Value("${properties.dataintegration.url.predataintegration}")
    private String dataIntegrationBaseUrlPredaIntegration;

    @Value("${properties.dataintegration.url.confirmdataintegration}")
    private String dataIntegrationBaseUrlConfirmDataIntegration;


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
            String originalQuery = params.get("query").toString();
            if (!originalQuery.equals("")) {
                // UDF 호출을 internalhive.default.name( 형태로 치환
                List<Map<String, Object>> allUdfs = getUdfList();
                String query = qualifyUdfCalls(originalQuery, allUdfs);

                // 쿼리 실행 API 호출
                Map<String, Object> executeBody = new HashMap<>();
                executeBody.put("query", query);
                executeBody.put("system_infos", params.get("system_infos"));
                executeBody.put("user_id", params.get("user_id").toString());
                Map<String, Object> queryExecuteResult = queryExecute(executeBody);

                String status_code = queryExecuteResult.get("status_code").toString();
                if (status_code.equals("200")) {
                    Map<String, Object> res = (Map<String, Object>) queryExecuteResult.get("result");
                    if (res != null && res.size() > 0) {
                        if (res.get("count") != null) {
                            totalCount = Integer.parseInt(res.get("count").toString());
                        }
                        // DataIntegration이 반환한 쿼리 사용 (실제 테이블명으로 치환된 버전)
                        if (res.get("query") != null) {
                            result.put("query", res.get("query").toString());
                        }
                        if (res.get("params") != null) {
                            result.put("params", res.get("params").toString());
                        }
                        if (res.get("query_result_sample") != null) {
                            list = (List<Map>) res.get("query_result_sample");
                        }
                    }
                    resultData = (Map<String, Object>) queryExecuteResult.get("result");
                    if (resultData != null && resultData.size() > 0) {
                        if (resultData.get("data") != null) {
                            list = (List<Map>) resultData.get("data");
                        }
                        if (resultData.get("totalCount") != null) {
                            totalCount = Integer.parseInt(resultData.get("totalCount").toString());
                        }
                    }
                } else {
                    log.error("Fail to execute query !!");
                    result.put("errorMessage", "쿼리가 잘못되었습니다.");
                }
            }
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
        boolean createViewResult = false;

        try {
            // Trino 뷰 이름 정규화: 공백 → _
            if (metadata.get("meta_name") != null) {
                String sanitizedName = metadata.get("meta_name").toString().replaceAll("\\s+", "_");
                metadata.put("meta_name", sanitizedName);
                // qualified_name은 프론트에서 meta_name 포함해 조합되므로 함께 재구성
                if (metadata.get("qualified_name") != null) {
                    metadata.put("qualified_name", metadata.get("qualified_name").toString().replaceAll("\\s+", "_"));
                }
            }

            // 1. event_log 저장
            Map<String, Object> eventParam = new HashMap<>();
            eventParam.put("event_type", 0); // 메타데이터 이벤트: 0 | 시스템 이벤트: 1
            eventParam.put("event_status", 1); // 0 해지 | 1:발생(생성) | 2: 변경 | 3: 삭제
            eventParam.put("event_name", "융합 메타 데이터 등록[발생]");
            eventParam.put("event_user_id", user.get("user_id"));
            eventParam.put("meta_id", metadata.get("meta_id"));
            eventParam.put("meta_name", metadata.get("meta_name"));
            eventParam.put("qualified_name", metadata.get("qualified_name"));

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
            
            // 2. UDF 호출을 정규화된 이름으로 치환 후 View 생성
            String query = metadata.get("query") != null ? metadata.get("query").toString() : "";
            List<Map<String, Object>> allUdfs = getUdfList();
            String qualifiedQuery = qualifyUdfCalls(query, allUdfs);
            metadata.put("query", qualifiedQuery);

            // 3. 융합데이터 생성 API 호출
            Map<String, Object> confirmIntegrationBody = new HashMap<>();
            confirmIntegrationBody.put("event_info", eventParam);
            confirmIntegrationBody.put("meta_info", metadata);

            Map<String, Object> confirmIntegrationResult = confirmIntegration(confirmIntegrationBody);
            String status_code = confirmIntegrationResult.get("status_code").toString();
            if (status_code.equals("200")) {
                createViewResult = true;
                // 4. UDF 의존성 기록 (원본 쿼리 기준)
                String metaId = metadata.get("meta_id").toString();
                saveViewUdfDependencies(metaId, query);
            } else {
                log.error("Fail to execute query !!");
                result.put("errorMessage", "쿼리가 잘못되었습니다.");
            }

            result.put("createViewResult", createViewResult);

        } catch (Exception e) {
            log.error("save fusion metadata Exception");
            e.printStackTrace();
            result.put("createViewResult", false);
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

    // trino 프로파일링 실행
    public Object executeProfile(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean executeResult = false;

        try {
            log.info("start to execute profile execute");
            executeResult = profileExecute(executeBody);
            log.info("end to execute profile execute");

        } catch (Exception e) {
            executeResult = false;
            log.error("fail to execute direct Collect");
            e.printStackTrace();
        }
        
        result.put("executeResult", executeResult);
        return result;
    }

    // 수동 프로파일링 실행 POST
    public boolean profileExecute(Map<String, Object> executeBody) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = catalogApiBaseUrl + catalogApiUrlProfile;

        Map<String, Object> requestBody = new HashMap<>();
        List<String> include = new ArrayList<>();
        List<String> exclude = new ArrayList<>();

        // table=my_view
        include.add("table=" + executeBody.get("meta_name").toString());
        
        requestBody.put("system_id", executeBody.get("system_id").toString());
        requestBody.put("profile_type", executeBody.get("profile_type").toString());
        requestBody.put("include", include); 
        requestBody.put("exclude", exclude);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            return true;
        }

        return false;
    }

    // ── Trino UDF 관리 ──

/** Hive 카탈로그에 UDF를 CREATE OR REPLACE FUNCTION으로 등록 */
    private void registerUdfsToHive(List<Map<String, Object>> udfList, String catalog) {
        if (udfList == null || udfList.isEmpty()) return;
        for (Map<String, Object> udf : udfList) {
            try {
                executeTrinoStatement(buildCreateFunctionSql(udf, catalog, trinoUdfSchema));
            } catch (Exception e) {
                log.error("Fail to register UDF to Hive: {}", udf.get("udf_name"), e);
            }
        }
    }

    /** Hive 카탈로그에서 UDF를 DROP — Trino는 파라미터 타입까지 포함해야 함 */
    @SuppressWarnings("unchecked")
    private void dropUdfFromHive(Map<String, Object> udf, String catalog) {
        String udfName = udf.get("udf_name").toString();
        try {
            String paramTypes = "";
            Object paramsObj = udf.get("params");
            if (paramsObj instanceof List) {
                paramTypes = ((List<Map<String, Object>>) paramsObj).stream()
                    .map(p -> p.get("param_type").toString())
                    .collect(Collectors.joining(", "));
            }
            executeTrinoStatement(
                String.format("DROP FUNCTION IF EXISTS %s.%s.%s(%s)", catalog, trinoUdfSchema, udfName, paramTypes)
            );
        } catch (Exception e) {
            log.error("Fail to drop UDF from Hive: {}", udfName, e);
        }
    }

    /** CREATE OR REPLACE FUNCTION SQL 생성 */
    @SuppressWarnings("unchecked")
    private String buildCreateFunctionSql(Map<String, Object> udf, String catalog, String schema) {
        String name       = udf.get("udf_name").toString();
        String returnType = udf.get("return_type").toString();
        String body       = udf.get("body").toString();

        String paramStr = "";
        Object paramsObj = udf.get("params");
        if (paramsObj instanceof List) {
            List<Map<String, Object>> paramList = (List<Map<String, Object>>) paramsObj;
            paramStr = paramList.stream()
                .map(p -> p.get("param_name") + " " + p.get("param_type"))
                .collect(Collectors.joining(", "));
        }

        String bodyPart = body.toUpperCase().startsWith("BEGIN") ? body : "RETURN " + body;

        return String.format(
            "CREATE OR REPLACE FUNCTION %s.%s.%s(%s) RETURNS %s %s",
            catalog, schema, name, paramStr, returnType, bodyPart
        );
    }

    /** Trino JDBC로 DDL 실행 */
    private void executeTrinoStatement(String sql) throws Exception {
        Properties props = new Properties();
        props.setProperty("user", "trino");
        try (Connection conn = DriverManager.getConnection(trinoJdbcUrl, props);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public List<Map<String, Object>> getUdfList() throws Exception {
        List<Map<String, Object>> list = metaDataFusionMapper.getUdfList();
        for (Map<String, Object> udf : list) {
            Object paramsObj = udf.get("params");
            if (paramsObj instanceof PGobject) {
                String json = ((PGobject) paramsObj).getValue();
                List<Map<String, Object>> paramList = objectMapper.readValue(json,
                    new TypeReference<List<Map<String, Object>>>() {});
                udf.put("params", paramList);
            }
        }
        return list;
    }

    public Map<String, Object> registerUdf(Map<String, Object> param, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            String udfId = UUID.randomUUID().toString();
            param.put("udf_id", udfId);
            param.put("create_by", user.get("user_id"));
            // DB에 저장되는 udf_name은 lower() 강제 (mapper SQL)
            param.put("udf_name", param.get("udf_name").toString().toLowerCase());
            metaDataFusionMapper.insertUdf(param);

            List<Map<String, Object>> params = (List<Map<String, Object>>) param.get("params");
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Map<String, Object> p = params.get(i);
                    p.put("udf_id", udfId);
                    p.put("param_order", i);
                    metaDataFusionMapper.insertUdfParam(p);
                }
            }

            // Hive 카탈로그에 등록
            registerUdfsToHive(List.of(param), trinoUdfCatalog);

            result.put("result", true);
        } catch (Exception e) {
            log.error("Fail to register UDF");
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    public Map<String, Object> updateUdf(Map<String, Object> param, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            param.put("update_by", user.get("user_id"));
            param.put("udf_name", param.get("udf_name").toString().toLowerCase());
            metaDataFusionMapper.updateUdf(param);

            String udfId = param.get("udf_id").toString();
            metaDataFusionMapper.deleteUdfParams(udfId);

            List<Map<String, Object>> params = (List<Map<String, Object>>) param.get("params");
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Map<String, Object> p = params.get(i);
                    p.put("udf_id", udfId);
                    p.put("param_order", i);
                    metaDataFusionMapper.insertUdfParam(p);
                }
            }

            // View 사용 여부와 관계없이 항상 Hive 동기화
            registerUdfsToHive(getUdfListByNames(List.of(param.get("udf_name").toString())), trinoUdfCatalog);

            result.put("result", true);
        } catch (Exception e) {
            log.error("Fail to update UDF");
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    public Map<String, Object> deleteUdf(String udfId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            int viewCount = metaDataFusionMapper.getUdfViewCount(udfId);
            if (viewCount > 0) {
                result.put("result", false);
                result.put("reason", "UDF를 사용 중인 융합 뷰가 " + viewCount + "개 있습니다.");
                return result;
            }

            // DROP 전 UDF 정보 조회 (params 타입 포함 필요)
            Map<String, Object> targetUdf = getUdfList().stream()
                .filter(u -> udfId.equals(u.get("udf_id")))
                .findFirst()
                .orElse(null);

            metaDataFusionMapper.deleteUdf(udfId);

            if (targetUdf != null) {
                dropUdfFromHive(targetUdf, trinoUdfCatalog);
            }

            result.put("result", true);
        } catch (Exception e) {
            log.error("Fail to delete UDF");
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }


    private void saveViewUdfDependencies(String metaId, String query) {
        try {
            metaDataFusionMapper.deleteFusionViewUdfs(metaId);
            List<String> calledNames = extractCalledFunctionNames(query);
            if (calledNames.isEmpty()) return;
            List<Map<String, Object>> usedUdfs = metaDataFusionMapper.getUdfListByNames(calledNames);
            for (Map<String, Object> udf : usedUdfs) {
                Map<String, Object> param = new HashMap<>();
                param.put("meta_id", metaId);
                param.put("udf_id", udf.get("udf_id").toString());
                metaDataFusionMapper.insertFusionViewUdf(param);
            }
        } catch (Exception e) {
            log.error("Fail to save view UDF dependencies");
            e.printStackTrace();
        }
    }

    /** 쿼리 내 UDF 호출을 internalhive.default.name( 형태로 치환 */
    private String qualifyUdfCalls(String query, List<Map<String, Object>> udfs) {
        String result = query;
        for (Map<String, Object> udf : udfs) {
            String name = udf.get("udf_name").toString();
            // 이미 정규화된 이름(앞에 점이 있는 경우)은 건너뜀
            result = result.replaceAll(
                "(?i)(?<!\\.)\\b" + Pattern.quote(name) + "\\s*\\(",
                trinoUdfCatalog + "." + trinoUdfSchema + "." + name + "("
            );
        }
        return result;
    }

    private List<String> extractCalledFunctionNames(String query) {
        Set<String> names = new HashSet<>();
        Matcher m = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(").matcher(query);
        while (m.find()) {
            names.add(m.group(1));
        }
        return new ArrayList<>(names);
    }

    private List<Map<String, Object>> getUdfListByNames(List<String> names) throws Exception {
        List<Map<String, Object>> list = metaDataFusionMapper.getUdfListByNames(names);
        for (Map<String, Object> udf : list) {
            Object paramsObj = udf.get("params");
            if (paramsObj instanceof PGobject) {
                String json = ((PGobject) paramsObj).getValue();
                List<Map<String, Object>> paramList = objectMapper.readValue(json,
                    new TypeReference<List<Map<String, Object>>>() {});
                udf.put("params", paramList);
            }
        }
        return list;
    }


    // 쿼리 실행 POST
    public Map<String, Object> queryExecute(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String status_code = "";
        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = dataIntegrationBaseUrl + dataIntegrationBaseUrlPredaIntegration;

        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("query", executeBody.get("query").toString());
        requestBody.put("system_infos", executeBody.get("system_infos"));
        requestBody.put("user_id", executeBody.get("user_id")); 

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            if (responseEntity.getBody() != null) {
                status_code = responseEntity.getBody().get("status_code").toString();
                result = (Map<String, Object>) responseEntity.getBody().get("result");
            }
        }

        resultMap.put("status_code", status_code);
        resultMap.put("result", result);
        return resultMap;
    }

    // 융합 데이터 생성 POST
    public Map<String, Object> confirmIntegration(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String status_code = "";
        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = dataIntegrationBaseUrl + dataIntegrationBaseUrlConfirmDataIntegration;

        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("event_info", executeBody.get("event_info"));
        requestBody.put("meta_info", executeBody.get("meta_info"));

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            if (responseEntity.getBody() != null) {
                status_code = responseEntity.getBody().get("status_code").toString();
                result = (Map<String, Object>) responseEntity.getBody().get("result");
            }
        }

        resultMap.put("status_code", status_code);
        resultMap.put("result", result);
        return resultMap;
    }
}
