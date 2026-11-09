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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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

    @Value("${properties.recommender.base_url}")
    private String recommenderBaseUrl;

    @Value("${properties.recommender.url.fusion}")
    private String recommenderFusionUrl;

    @Value("${properties.recommender.url.viewLineage}")
    private String recommenderViewLineageUrl;

    /** 추천 엔진에 요청할 여유분. 융합 뷰가 섞여 와도 topN 을 채우기 위해 더 받는다(계획서 §3.7).
     *  응답 시간은 topN 과 무관해서(실측: topN=30 ≈ topN=10) 과요청 비용이 없다. */
    private static final int RECOMMEND_OVERFETCH = 10;
    private static final int RECOMMEND_CONNECT_TIMEOUT_MS = 3000;
    /** 실측 응답이 4.8~10.6초다(계획서 §3.6). 10s 로 두면 느린 기준 테이블에서 실패한다. */
    private static final int RECOMMEND_READ_TIMEOUT_MS = 15000;
    /** 계보 파싱은 뷰 한 건이라 짧지만, 조회 테이블 적재(_load_lookups)가 함께 돈다. */
    private static final int VIEW_LINEAGE_READ_TIMEOUT_MS = 30000;


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

    // 데이터 융합 관리 > 융합 추천 테이블 조회
    public Map<String, Object> getFusionRecommend(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        result.put("status", "ok");
        result.put("list", list);
        result.put("totalCount", 0);

        String metaId = params.get("meta_id") == null ? null : params.get("meta_id").toString();
        if (metaId == null || metaId.trim().isEmpty()) {
            result.put("status", "error");
            result.put("reason", "기준 데이터가 지정되지 않았습니다.");
            return result;
        }
        int topN = params.get("topN") == null ? 10 : Integer.parseInt(params.get("topN").toString());

        // 1. 추천 엔진 호출
        List<Map<String, Object>> recs = null;
        try {
            recs = callFusionRecommend(metaId, topN + RECOMMEND_OVERFETCH);
        } catch (Exception e) {
            // 추천은 보조 기능이다. 실패해도 융합 관리 본 기능은 계속 쓸 수 있어야 하므로
            // 예외를 올리지 않고 사유만 돌려준다.
            log.error("fail to get fusion recommend - base={} {}", metaId, e.getMessage());
            result.put("status", "error");
            result.put("reason", "추천을 가져오지 못했습니다.");
            return result;
        }

        // 2. 융합 뷰 제외 — 융합 결과물을 다시 융합 후보로 내보내지 않는다(계획서 §3.7).
        //    융합 시스템 id 는 코드 테이블에서 런타임에 결정된다. 환경마다 달라서 하드코딩하지 않는다.
        String fusionSystemId = null;
        try {
            fusionSystemId = getFusionCatalog();
        } catch (Exception e) {
            log.warn("fusion system info not found - 융합 뷰 제외를 건너뜁니다. {}", e.getMessage());
        }

        int fusionViewFiltered = 0;
        List<Map<String, Object>> candidates = new ArrayList<Map<String, Object>>();
        List<String> candidateIds = new ArrayList<String>();
        for (int i = 0; i < recs.size(); i++) {
            Map<String, Object> rec = recs.get(i);
            String systemId = rec.get("system_id") == null ? "" : rec.get("system_id").toString();
            if (fusionSystemId != null && fusionSystemId.equals(systemId)) {
                fusionViewFiltered++;
                continue;
            }
            candidates.add(rec);
            candidateIds.add(rec.get("meta_id") == null ? "" : rec.get("meta_id").toString());
        }

        // 3. 메타 상세 보강 — 융합 데이터 정보 목록 행과 같은 필드 모양을 얻고, 권한 필터를 승계한다.
        //    추천 엔진은 PG 를 직접 조회해 포털 권한을 모른다. 볼 수 없는 데이터는 여기서 떨어진다.
        Map<String, Map<String, Object>> metaById = new HashMap<String, Map<String, Object>>();
        if (candidateIds.size() > 0) {
            Map<String, Object> metaParams = new HashMap<String, Object>();
            metaParams.put("userId", params.get("userId"));
            metaParams.put("metaIds", candidateIds.toArray(new String[0]));
            List<Map<String, Object>> metaList = metaDataFusionMapper.getMetaDataFusionByIds(metaParams);
            for (int i = 0; i < metaList.size(); i++) {
                Map<String, Object> meta = metaList.get(i);
                metaById.put(meta.get("meta_id").toString(), meta);
            }
        }

        // 4. 추천 순위를 보존한 채 1..N 으로 재번호하고 topN 으로 자른다.
        int notVisible = 0;
        for (int i = 0; i < candidates.size(); i++) {
            Map<String, Object> rec = candidates.get(i);
            String candMetaId = rec.get("meta_id") == null ? "" : rec.get("meta_id").toString();
            Map<String, Object> meta = metaById.get(candMetaId);
            if (meta == null) {
                // 권한 없음 / status != ACTIVE / meta_type != table
                notVisible++;
                continue;
            }
            if (list.size() >= topN) {
                break;
            }
            Map<String, Object> row = new HashMap<String, Object>(meta);
            row.put("rank", list.size() + 1);
            row.put("score", rec.get("score"));
            row.put("reason", rec.get("reason"));
            row.put("join_keys", rec.get("join_keys"));
            list.add(row);
        }

        result.put("totalCount", list.size());
        log.info("fusion recommend: base={} topN={} engine={} fusionViewFiltered={} notVisible={} returned={}",
                metaId, topN, recs.size(), fusionViewFiltered, notVisible, list.size());

        return result;
    }

    /**
     * 융합 뷰 한 건의 계보를 즉시 반영한다 (뷰 정의문 파싱 → 계보·조인 키·데이터 흐름조회).
     *
     * 실패해도 저장은 성공으로 둔다. 계보는 부가 정보이고, 전수 파싱 배치가 스케줄로
     * 돌면서 나중에 채운다. 저장을 되돌리면 뷰는 이미 만들어진 상태라 더 나쁘다.
     *
     * ⚠ 이 호출은 <table> 도 채운다. 그 테이블은 추천 점수의 근거(use_known=true)
     * 이면서 추천 점수의 근거이기도 하다. 즉 융합 뷰를 저장하면 그 조인 키가
     * '실사용 이력'으로 등록되어 이후 추천 점수에 되먹임된다. 성능평가는 use_known=false
     * 로 호출하므로 측정값에는 영향이 없다.
     */
    private void buildViewLineage(String metaId) {
        try {
            String url = recommenderBaseUrl + recommenderViewLineageUrl + "?meta_id=" + metaId;

            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(RECOMMEND_CONNECT_TIMEOUT_MS);
            factory.setReadTimeout(VIEW_LINEAGE_READ_TIMEOUT_MS);
            RestTemplate restTemplate = new RestTemplate(factory);

            ResponseEntity<HashMap> response = restTemplate.getForEntity(url, HashMap.class);
            Map<String, Object> body = response == null ? null : response.getBody();
            Object statusCode = body == null ? null : body.get("status_code");
            if (!(statusCode instanceof Integer) || ((Integer) statusCode).intValue() != 200) {
                log.warn("view lineage build failed - meta_id={} status_code={} result={}",
                        metaId, statusCode, body == null ? null : body.get("result"));
                return;
            }
            log.info("view lineage build: meta_id={} result={}", metaId, body.get("result"));
        } catch (Exception e) {
            log.warn("view lineage build error - meta_id={} {}", metaId, e.getMessage());
        }
    }

    /**
     * 추천 엔진(FastAPI) 호출.
     *
     * 이 API 는 오류도 HTTP 200 으로 돌려주고, 그때 result 가 리스트가 아니라 message 를 담은
     * 객체로 온다(없는 meta_id -> status_code 404). 그래서 status_code 와 result 타입을 둘 다 본다.
     * 바로 List 로 캐스팅하면 ClassCastException 이 난다.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> callFusionRecommend(String metaId, int topN) throws Exception {
        String url = recommenderBaseUrl + recommenderFusionUrl + "?meta_id=" + metaId + "&topN=" + topN;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(RECOMMEND_CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(RECOMMEND_READ_TIMEOUT_MS);
        RestTemplate restTemplate = new RestTemplate(factory);

        ResponseEntity<HashMap> response = restTemplate.getForEntity(url, HashMap.class);
        if (response == null || response.getBody() == null) {
            throw new Exception("empty response from recommender");
        }

        Map<String, Object> body = response.getBody();
        Object statusCode = body.get("status_code");
        Object payload = body.get("result");

        if (!(statusCode instanceof Integer) || ((Integer) statusCode).intValue() != 200) {
            String message = String.valueOf(payload);
            if (payload instanceof Map) {
                message = String.valueOf(((Map<String, Object>) payload).get("message"));
            }
            throw new Exception("recommender status_code=" + statusCode + " message=" + message);
        }
        if (!(payload instanceof List)) {
            throw new Exception("unexpected result type: "
                    + (payload == null ? "null" : payload.getClass().getSimpleName()));
        }
        return (List<Map<String, Object>>) payload;
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
                            result.put("params", res.get("params"));
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

            // 중복확인은 meta_id 만 보므로, 뷰 이름(meta_name)이 겹치면 Trino CREATE VIEW 에서야 실패한다.
            // 이벤트 로그를 남기기 전에 먼저 막는다.
            Map<String, Object> dupParam = new HashMap<>();
            dupParam.put("systemId", getFusionCatalog());
            dupParam.put("schema", trinoUdfSchema);
            dupParam.put("metaName", metadata.get("meta_name"));
            if (metaDataFusionMapper.countFusionViewByName(dupParam) > 0) {
                result.put("duplicatedName", true);
                result.put("createViewResult", false);
                return result;
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
                // 5. 계보(리니지) 즉시 반영 — 이 뷰 한 건만 파싱한다.
                //    전수 파싱은 스케줄 배치가 계속 돌므로 생성 시점에 전체를 다시 볼 이유가 없다.
                buildViewLineage(metaId);
            } else {
                log.error("Fail to execute query !!");
                result.put("errorMessage", "쿼리가 잘못되었습니다.");
                // 메타에는 없고 Trino 에만 남은 뷰(생성 후 메타 저장 실패 등)와 이름이 겹친 경우
                if (String.valueOf(confirmIntegrationResult.get("result")).contains("TABLE_ALREADY_EXISTS")) {
                    result.put("duplicatedName", true);
                }
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
