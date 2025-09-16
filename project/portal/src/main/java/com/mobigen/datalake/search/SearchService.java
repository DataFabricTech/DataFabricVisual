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
    public Object getRelatedData(String meta_id, Integer topN) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. Recommender 서버로부터 조회
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = recommenderBaseUrl + recommenderClustringUrl + "?meta_id=" + meta_id + "&topN=" + topN;
            ResponseEntity<HashMap> restResponse = restTemplate.getForEntity(url, HashMap.class);

            if (restResponse != null && restResponse.getBody() != null) {
                Map<String, Object> body = restResponse.getBody();
                if (body.get("result") != null) {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("result");
                    if (results != null && results.size() > 0) {
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
                    }
                } else {
                    resultMap.put("results", new ArrayList<>());
                    resultMap.put("total_count", 0);
                }
            }
        } catch (Exception e) {
            log.error("Failed to get related data meta_id list !! {}", e);
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
