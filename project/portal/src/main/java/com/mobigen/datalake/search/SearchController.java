package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobigen.framework.iris.User;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RequestMapping("/api")
@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SessionManager sessionManager;
    private final SearchService searchService;

    @Value("${properties.ESSearch.server.host}")
    private String esSearchApiHost;
    @Value("${properties.ESSearch.server.port}")
    private String esSearchApiPort;

    // 통합 검색
    @ResponseJsonResult
    @PostMapping("/search/getEsSearch")
    public Object PostSearchBasic(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);

            // 사용자 데이터 권한(role_id) 확인
            List<String> role_ids = searchService.getRoleIds(user);
            param.put("roleids", role_ids);

            RestTemplate restTemplate = new RestTemplate();
        
            // esSearch API의 URL 생성
            String fastUrl = "http://" + esSearchApiHost + ":" + esSearchApiPort + "/api/v1/search/keyword";
            log.info("call essearch api : " + fastUrl);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(param);
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(fastUrl, HttpMethod.POST, requestEntity, HashMap.class);

            // 검색어 입력 이력 저장
            if(param.get("query") != null){
                Map<String, Object> textHistoryParam = new HashMap<>();
                textHistoryParam.put("search_string", param.get("query"));
                textHistoryParam.put("userId", user.get("user_id"));
                searchService.insertSearchTextHistory(textHistoryParam);
            }
            
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = responseEntity.getBody();

                if (body != null) {
                    Map<String, Object> rs = (Map<String, Object>) body.get("result");
                    if (rs != null) {
                        List<Map<String, Object>> esResults = (List<Map<String, Object>>) rs.get("results");
                        if (esResults != null && esResults.size() > 0) {
                            result.put("results", esResults);
                            result.put("total_count", rs.get("total_count"));
                        }
                    }
                }

                // 권한 관련 키,값 제거
                if (body != null && body.containsKey("release")) body.remove("release");
                if (body != null && body.containsKey("role_id")) body.remove("role_id");
                
            } else {
                result.put("results", new ArrayList<>());
                result.put("total_count", 0);
                throw new Exception("통합 검색에 실패하였습니다.");
            }
            
        } catch (Exception e) {
            result.put("results", new ArrayList<>());
            result.put("total_count", 0);
            throw new Exception("통합검색에 실패하였습니다.");
        }

        return result;
    }

    // 통합 검색
    @ResponseJsonResult
    @GetMapping("/search/filterData")
    public Object getFilterData() throws Exception {
        Map<String, Object> filterData = new HashMap<>();

        filterData.put("system_types", searchService.getSystemTypes());
        filterData.put("meta_types", searchService.getMetaTypes());

        return filterData;
    }
    
    // 테이블 개수 조회
    @ResponseJsonResult
    @GetMapping("/search/tableTotal")
    public Object getTableTotal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return searchService.getTableTotal();
    }

    // 연관 데이터 검색
    @ResponseJsonResult
    @PostMapping("/search/keywordRef")
    public Object getRelatedData(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception  {
        return searchService.getRelatedData(param);
    }
}
