package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;

    @Value("${properties.ESSearch.server.host}")
    private String esSearchApiHost;
    @Value("${properties.ESSearch.server.port}")
    private String esSearchApiPort;
    
    @Value("${properties.recommender.base_url}")
    private String recommenderBaseUrl;

    @Value("${properties.recommender.url.clustering}")
    private String recommenderClustringUrl;

    // 검색 이력 저장
    public void insertSearchTextHistory(Map<String, Object> textHistoryParam) {
        searchMapper.insertSearchTextHistory(textHistoryParam);
    }

    public List<String> getSystemTypes() {
        return searchMapper.getSystemTypes();
    }

    public List<String> getMetaTypes() {
        return searchMapper.getMetaTypes();
    }

    // hive_table 전체 개수 조회
    public int getTableTotal() {
        return searchMapper.getTableTotal();
    }

    // 연관 데이터 조회
    public Map<String, Object> getRelatedDataFromEs(Map<String, Object> params) throws Exception {
        log.info("get related data from ES !!");
        Map<String, Object> resultMap = new HashMap<>();

        try {
            // 1. 해당 데이터의 카테고리 조회
            List<Map<String, Object>> classifications = searchMapper.getMetaDataClassifications(params);
            Map<String, Object> map = classifications.get(0);

            if (map != null) {
                // 2. 카테고리를 가지고 있는 메타데이터 id 조회 (system_type, meta_type 동일한 데이터로 createtime 내림차순)
                if (map.get("classifications") != null) params.put("classifications", map.get("classifications"));
                
                List<String> guids = searchMapper.getMetaDataInfoFromClassifications(params);
                if (guids != null && guids.size() > 0) {
                    RestTemplate restTemplate = new RestTemplate();

                    // esSearch API의 URL 생성
                    String fastUrl = "http://" + esSearchApiHost + ":" + esSearchApiPort + "/api/v1/search/keywordRef";
                    log.info("call essearch api : " + fastUrl);

                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("guids", guids);
                    Map<String, Object> restResponse = restTemplate.postForObject(fastUrl, requestBody, HashMap.class);

                    if (!restResponse.isEmpty()) {
                        Map<String, String> res = (Map<String, String>) restResponse.get("result");
                        if (res.get("result") != null && res.get("result").equals("success")) {
                            resultMap.put("results", res.get("results"));
                            resultMap.put("total_count", res.get("total_count"));
                            return resultMap;
                        } else {
                            log.error("fail to get related data from ES !!");
                            resultMap.put("results", new ArrayList<>());
                            resultMap.put("total_count", 0);
                        }
                    }
                } else {
                    log.error("fail to get related data from ES - guids is null or size < 0 !!");
                    resultMap.put("results", new ArrayList<>());
                    resultMap.put("total_count", 0);
                }

            } else {
                log.error("fail to get related data from ES - classifications is null !!");
                resultMap.put("results", new ArrayList<>());
                resultMap.put("total_count", 0);
            }
        } catch (Exception e) {
            log.error("fail to get related data from ES !! - {}", e.getMessage());
            resultMap.put("results", new ArrayList<>());
            resultMap.put("total_count", 0);
        }
        
        return resultMap;
    }

    // 연관 데이터 조회
    public Object getRelatedData(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        String meta_id = params.get("guid").toString();
        String topN = params.get("topN").toString();

        // 1. Recommender 서버로부터 조회
        try {
            log.info("get related data from clustering !!");
            RestTemplate restTemplate = new RestTemplate();


            String url = recommenderBaseUrl + recommenderClustringUrl + "?meta_id=" + meta_id + "&topN=" + topN;
            ResponseEntity<HashMap> restResponse = restTemplate.getForEntity(url, HashMap.class);

            if (restResponse != null && restResponse.getBody() != null) {
                Map<String, Object> body = restResponse.getBody();
                log.info("body: {}", body);
                if (body.get("result") != null && body.get("status_code") != null && (Integer) body.get("status_code") == 200) {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("result");
                    if (results != null) {
                        if (results.size() > 0) {
                            // 메타정보 상세 조회
                            for (int i=0; i<results.size(); i++) {
                                String id = results.get(i).get("meta_id").toString();
                                Map<String, Object> meta = searchMapper.getMetadataInfo(id);
                                // 카테고리, description, meta_name
                                results.get(i).put("meta_name", meta.get("meta_name"));
                                results.get(i).put("description", meta.get("description"));
                                PGobject pgObj = (PGobject) meta.get("classifications");
                                String json = pgObj.getValue();
                                ObjectMapper objectMapper = new ObjectMapper();
                                List<Map<String, Object>> classifications = objectMapper.readValue(json,new TypeReference<List<Map<String, Object>>>() {});
                                results.get(i).put("classifications", classifications);
                            }

                            resultMap.put("results", results);
                            resultMap.put("total_count", results.size());
                        } else {
                            log.info("No related data from clustering !!");
                            resultMap = getRelatedDataFromEs(params);
                        }
                    } else {
                        log.error("Failed to get related data from clustering !!");
                        resultMap = getRelatedDataFromEs(params);
                    }
                } else {
                    log.error("Failed to get related data from clustering !!");
                    resultMap = getRelatedDataFromEs(params);
                }
            } else {
                log.info("No related data from clustering !!");
                resultMap = getRelatedDataFromEs(params);
            }
        } catch (Exception e) {
            log.error("Failed to get related from clustering !! {}", e.getMessage());
            resultMap = getRelatedDataFromEs(params);
        }

        return resultMap;
    }

    // 특정 사용자 role_id 조회
    public List<String> getRoleIds(Map<String, Object> user) throws Exception {
        List<String> result = new ArrayList<>();
        try {
            // 1. 사용자가 가지고 있는 role_id 모두 조회 (user_id, dept_id)
            List<String> roleIdsFromUser = searchMapper.getRoleIdsFromUser(user.get("user_id").toString(),
                    user.get("dept_id").toString());

            if (roleIdsFromUser != null && roleIdsFromUser.size() > 0) {
                result = roleIdsFromUser;
            }
        } catch (Exception e) {
            result = new ArrayList<>();
        }
        return result;
    }

    // 메타 타입 기본값 조회
    public List<Map<String, Object>> getDefaultMetaType() throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            // 1. 메타 타입 기본값 조회
            result = searchMapper.getDefaultMetaType();
            if (result != null && result.size() > 0) {
            } else {
                log.warn("메타 타입 기본값이 없습니다.");
            }
        } catch (Exception e) {
            log.error("메타 타입 기본값 조회 중 오류 발생", e);
            e.printStackTrace();
        }
        return result;
    }
}
