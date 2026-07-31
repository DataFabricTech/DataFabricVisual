package com.mobigen.datalake.metaData.fusion;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataFusionService {
    private final ObjectMapper objectMapper;
    private final MetaDataFusionMapper metaDataFusionMapper;

    @Value("${properties.trino.udf-schema}")
    private String trinoUdfSchema;

    @Value("${properties.dataintegration.base_url}")
    private String dataIntegrationBaseUrl;

    @Value("${properties.dataintegration.url.predataintegration}")
    private String dataIntegrationBaseUrlPredaIntegration;

    @Value("${properties.dataintegration.url.confirmdataintegration}")
    private String dataIntegrationBaseUrlConfirmDataIntegration;

    @Value("${properties.dataintegration.url.deletedataintegration}")
    private String dataIntegrationUrlDeleteDataIntegration;

    @Value("${properties.dataintegration.url.udf.register}")
    private String dataIntegrationUrlUdfRegister;

    @Value("${properties.dataintegration.url.udf.update}")
    private String dataIntegrationUrlUdfUpdate;

    @Value("${properties.dataintegration.url.udf.drop}")
    private String dataIntegrationUrlUdfDrop;


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

    // 데이터 융합 관리 > 생성된 융합 뷰 목록 조회
    public Map<String, Object> getFusionViewList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        try {
            params.put("systemId", getFusionCatalog());
            params.put("schema", trinoUdfSchema);
            totCnt = metaDataFusionMapper.getFusionViewListTotCnt(params);
            resultList = metaDataFusionMapper.getFusionViewList(params);
        } catch (Exception e) {
            log.error("Fail to search fusion view list");
            e.printStackTrace();
        }

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    // 쿼리 실행
    public Map<String, Object> executeQuery(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
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
                List<Map<String, Object>> allUdfs = getUdfList();
                String query = qualifyUdfCalls(originalQuery, allUdfs, getFusionCatalog());

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
                } else {
                    String detail = extractQueryExecuteErrorDetail(queryExecuteResult);
                    log.error("Fail to execute query !! detail={}", detail);
                    result.put("errorMessage", buildQueryExecuteErrorMessage(detail));
                    result.put("errorDetail", extractInnerErrorMessage(detail));
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

            // 형식 검증 (DataIntegration 서버에 Timestamp 객체로 전달하면 epoch(ms)로 직렬화되어
            // create_time이 정상 저장되지 않으므로, 프론트에서 받은 문자열 형식 그대로 전달)
            String dateString = metadata.get("create_time") != null ? metadata.get("create_time").toString() : "";
            LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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
            String qualifiedQuery = qualifyUdfCalls(query, allUdfs, getFusionCatalog());
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

    @SuppressWarnings("unchecked")
    public Map<String, Object> deleteFusionMetaData(String metaId, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 융합 전용 시스템 정보 조회
            String fusionSystemId = getFusionCatalog();

            // 2. 삭제 대상 메타데이터 조회
            Map<String, Object> meta = metaDataFusionMapper.getFusionMetaById(metaId);
            if (meta == null) {
                result.put("result", false);
                result.put("reason", "메타데이터를 찾을 수 없습니다.");
                return result;
            }

            // 3. 융합 시스템 소속 여부 검증
            String metaSystemId = meta.get("system_id").toString();
            if (!fusionSystemId.equals(metaSystemId)) {
                result.put("result", false);
                result.put("reason", "융합 데이터가 아닙니다.");
                return result;
            }

            // 4. UDF 의존성 삭제 (포털 전용 테이블)
            metaDataFusionMapper.deleteFusionViewUdfs(metaId);

            // 5. DataIntegration 서버에 위임 (DROP VIEW + <table> + event_log + ESSync)
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("event_type", 0);
            eventInfo.put("event_status", 3);
            eventInfo.put("event_name", "융합 메타 데이터 삭제");
            eventInfo.put("event_user_id", user.get("user_id"));
            eventInfo.put("meta_id", metaId);
            eventInfo.put("meta_name", meta.get("meta_name").toString());
            eventInfo.put("qualified_name", meta.get("qualified_name").toString());

            Map<String, Object> metaInfo = new HashMap<>();
            metaInfo.put("meta_id", metaId);
            metaInfo.put("meta_name", meta.get("meta_name").toString());
            metaInfo.put("system_id", metaSystemId);
            metaInfo.put("qualified_name", meta.get("qualified_name").toString());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("event_info", eventInfo);
            requestBody.put("meta_info", metaInfo);

            Map<String, Object> deleteResult = deleteIntegration(requestBody);
            String statusCode = deleteResult.get("status_code").toString();
            if (!statusCode.equals("200")) {
                result.put("result", false);
                result.put("reason", "DataIntegration 삭제 요청 실패");
                return result;
            }

            result.put("result", true);
        } catch (Exception e) {
            log.error("Fail to delete fusion metadata");
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    private Map<String, Object> deleteIntegration(Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String status_code = "";
        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        String url = dataIntegrationBaseUrl + dataIntegrationUrlDeleteDataIntegration;
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, requestBody, HashMap.class);
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

        if (!system_name.equals("") && !system_type.equals("")) {
          Map<String, Object> collectorMap = metaDataFusionMapper.getFusionSystemInfo(system_name, system_type);
          if (collectorMap != null) {
            collectorMap.put("meta_type", meta_type);
            collectorMap.put("schema", trinoUdfSchema);
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

    // ── Trino UDF 관리 (DataIntegration 서버 위임) ──

    @SuppressWarnings("unchecked")
    private String getFusionCatalog() throws Exception {
        Map<String, Object> fusionResult = getFusionSystemInfo();
        if (Boolean.TRUE.equals(fusionResult.get("result"))) {
            Map<String, Object> info = (Map<String, Object>) fusionResult.get("fusionInfo");
            return info.get("system_id").toString();
        }
        throw new Exception("Fusion system info not found");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildUdfBody(Map<String, Object> udf, String catalog) {
        Map<String, Object> body = new HashMap<>();
        body.put("udf_name", udf.get("udf_name").toString());
        body.put("return_type", udf.get("return_type").toString());
        body.put("body", udf.get("body").toString());
        body.put("catalog", catalog);
        body.put("udf_schema", trinoUdfSchema);

        Object paramsObj = udf.get("params");
        List<Map<String, Object>> paramList = new ArrayList<>();
        if (paramsObj instanceof List) {
            paramList = (List<Map<String, Object>>) paramsObj;
        }
        body.put("params", paramList);
        return body;
    }

    private void callUdfApi(String urlPath, Map<String, Object> requestBody) {
        RestTemplate restTemplate = new RestTemplate();
        String url = dataIntegrationBaseUrl + urlPath;
        restTemplate.postForObject(url, requestBody, HashMap.class);
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

            callUdfApi(dataIntegrationUrlUdfRegister, buildUdfBody(param, getFusionCatalog()));

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
            String udfId = param.get("udf_id").toString();

            int viewCount = metaDataFusionMapper.getUdfViewCount(udfId);
            if (viewCount > 0) {
                result.put("result", false);
                result.put("reason", "UDF를 사용 중인 융합 뷰가 " + viewCount + "개 있습니다. 수정이 불가합니다.");
                return result;
            }

            Map<String, Object> oldUdf = getUdfList().stream()
                .filter(u -> udfId.equals(u.get("udf_id")))
                .findFirst()
                .orElse(null);

            param.put("update_by", user.get("user_id"));
            param.put("udf_name", param.get("udf_name").toString().toLowerCase());
            metaDataFusionMapper.updateUdf(param);

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

            // old/new UDF 정보를 묶어 DataIntegration 서버에 위임
            String catalog = getFusionCatalog();
            Map<String, Object> updateBody = new HashMap<>();
            updateBody.put("old_udf", oldUdf != null ? buildUdfBody(oldUdf, catalog) : buildUdfBody(param, catalog));
            updateBody.put("new_udf", buildUdfBody(param, catalog));
            callUdfApi(dataIntegrationUrlUdfUpdate, updateBody);

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

            Map<String, Object> targetUdf = getUdfList().stream()
                .filter(u -> udfId.equals(u.get("udf_id")))
                .findFirst()
                .orElse(null);

            metaDataFusionMapper.deleteUdf(udfId);

            if (targetUdf != null) {
                callUdfApi(dataIntegrationUrlUdfDrop, buildUdfBody(targetUdf, getFusionCatalog()));
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

    private String qualifyUdfCalls(String query, List<Map<String, Object>> udfs, String catalog) {
        String result = query;
        for (Map<String, Object> udf : udfs) {
            String name = udf.get("udf_name").toString();
            // 이미 정규화된 이름(앞에 점이 있는 경우)은 건너뜀
            result = result.replaceAll(
                "(?i)(?<!\\.)\\b" + Pattern.quote(name) + "\\s*\\(",
                catalog + "." + trinoUdfSchema + "." + name + "("
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

    // 쿼리 실행 POST
    private Map<String, Object> queryExecute(Map<String, Object> executeBody) throws Exception {
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

    // DataIntegration 실패 응답(result.message)에서 원본 예외 메시지 추출
    @SuppressWarnings("unchecked")
    private String extractQueryExecuteErrorDetail(Map<String, Object> queryExecuteResult) {
        try {
            Map<String, Object> res = (Map<String, Object>) queryExecuteResult.get("result");
            if (res != null && res.get("message") instanceof List) {
                List<?> messages = (List<?>) res.get("message");
                if (!messages.isEmpty()) {
                    return messages.get(0).toString();
                }
            }
        } catch (Exception ignore) {
        }
        return "";
    }

    // Trino 예외의 type(USER_ERROR/INTERNAL_ERROR/EXTERNAL 등)에 따라 사용자 안내 메시지 분기
    private String buildQueryExecuteErrorMessage(String detail) {
        Matcher m = Pattern.compile("type=([A-Z_]+)").matcher(detail);
        if (m.find() && "USER_ERROR".equals(m.group(1))) {
            return "쿼리가 잘못되었습니다.";
        }
        return "관리자에게 문의해 주시기 바랍니다.";
    }

    // 중첩된 예외 문자열에서 실제 Trino message="..." 부분만 추출 (없으면 원문 그대로)
    private String extractInnerErrorMessage(String detail) {
        Matcher m = Pattern.compile("message=\"([^\"]*)\"").matcher(detail);
        if (m.find()) {
            return m.group(1);
        }
        return detail;
    }

    // 융합 데이터 생성 POST
    private Map<String, Object> confirmIntegration(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String status_code = "";
        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = dataIntegrationBaseUrl + dataIntegrationBaseUrlConfirmDataIntegration;

        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("event_info", executeBody.get("event_info"));
        requestBody.put("meta_info", executeBody.get("meta_info"));

        log.info("[DEBUG] confirmIntegration request: {}", requestBody);

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);

        log.info("[DEBUG] confirmIntegration response: {}", responseEntity != null ? responseEntity.getBody() : null);

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
