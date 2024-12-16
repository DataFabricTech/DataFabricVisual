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

    @Value("${properties.fastapi.server.host}")
    private String fastApiHost;
    @Value("${properties.fastapi.server.port}")
    private String fastApiPort;

    // 통합 검색
    @ResponseJsonResult
    @PostMapping("/search/getEsSearch")
    public Object PostSearchBasic(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        
        // FastAPI의 URL 생성
        String fastUrl = "http://" + fastApiHost + ":" + fastApiPort + "/api/v1/search/keyword";
        log.info("call fastapi : " + fastUrl);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(param);
        ResponseEntity<HashMap> responseEntity = restTemplate.exchange(fastUrl, HttpMethod.POST, requestEntity, HashMap.class);

        // 검색어 입력 이력 저장
        if(param.get("query") != null){
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            Map<String, Object> textHistoryParam = new HashMap<>();

            textHistoryParam.put("search_string", param.get("query"));
            textHistoryParam.put("userId", user.get("user_id"));
            searchService.insertSearchTextHistory(textHistoryParam);
        }

        return responseEntity.getBody();
    }

    // 통합 검색
    @ResponseJsonResult
    @GetMapping("/search/filterData")
    public Object getFilterData() throws Exception {
        Map<String, Object> filterData = new HashMap<>();

        filterData.put("systemtypes", searchService.getSystemtypes());
        filterData.put("metatypes", searchService.getMetatypes());

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

    // 카테고리 목록 조회
//    @ResponseJsonResult
//    @GetMapping("/search/category/list")
//    public List<Map<String, Object>> getCategoryList(@RequestParam String param) {
//
//        return null;
//    }
//
//    // 카테고리별 개수 조회
//    @ResponseJsonResult
//    @GetMapping("/search/category/counts")
//    public List<Map<String, Object>> getCategoryCounts(@RequestParam String param) {
//
//        return null;
//    }

    // DB 목록 조회
    // @ResponseJsonResult
    // @GetMapping("/search/category/dbList")
    // public List<Map<String, Object>> getMethodName(@RequestParam String param) {
        
    //     return null;
    // }
    

}
