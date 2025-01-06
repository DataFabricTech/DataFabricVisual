package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;

    @Value("${properties.fastapi.server.host}")
    private String fastApiHost;
    @Value("${properties.fastapi.server.port}")
    private String fastApiPort;

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
    public Object getRelatedData(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 해당 데이터의 카테고리 조회
        List<Map<String, Object>> classifications = searchMapper.getMetaDataClassifications(params);
        Map<String, Object> map = classifications.get(0);

        if (map != null) {
            // 2. 카테고리를 가지고 있는 메타데이터 id 조회 (system_type, meta_type 동일한 데이터로 createtime 내림차순)
            params.put("classifications", map.get("classifications"));
            List<String> guids = searchMapper.getMetaDataInfoFromClassifications(params);
            if (guids != null && guids.size() > 0) {
                RestTemplate restTemplate = new RestTemplate();
            
                // FastAPI의 URL 생성
                String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/keywordRef";
                log.info("call fastapi : " + fastUrl);

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
                        resultMap.put("results", new ArrayList<>());
                        resultMap.put("total_count", 0);
                    }
                }
            } else {
                resultMap.put("results", new ArrayList<>());
                resultMap.put("total_count", 0);
            }

            
        } else {
            resultMap.put("results", new ArrayList<>());
            resultMap.put("total_count", 0);
        }

        

        return resultMap;
    }

}
