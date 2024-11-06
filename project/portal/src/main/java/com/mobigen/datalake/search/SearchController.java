package com.mobigen.datalake.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobigen.datalake.atlas.ATLASProperties;
import com.mobigen.datalake.atlas.AtlasRestAPI;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.RegexUtils;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ATLASProperties properties;
    private final AtlasRestAPI atlasRestAPI;

    private final SessionManager sessionManager;
    private final SearchService searchService;      

    @Value("${solr.isProd}")
    private boolean isProd;

    @Value("${solr.type-name}")
    private String typeName;

    @Value("${solr.comment}")
    private String comment;

    @Value("${solr.comment2}")
    private String comment2;

    @Value("${solr.description}")
    private String description;

    @Value("${solr.qualified-name}")
    private String qualifiedName;

    @Value("${solr.user-description}")
    private String userDescription;

    @Value("${solr.name}")
    private String name;

    @Value("${solr.display-name}")
    private String displayName;

    @Value("${solr.status}")
    private String status;

    @Value("${solr.specialCharacter}")
    private String specialCharacter;

    @Value("${solr.category}")
    private String category;

    @Value("${solr.label}")
    private String label;

    @Value("${solr.createtime}")
    private String createtime;

    // 통합 검색
    @ResponseJsonResult
    @PostMapping("/search/keyword")
    public Object PostSearchBasic(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        String baseUrl = "/search/basic";

        param.put("excludeDeletedEntities", "true"); // 삭제된 데이터 포함
        param.put("includeSubTypes", "true");
        param.put("includeSubClassifications", "false");
        param.put("includeClassificationAttributes", "true");

        List<String> attributes = new ArrayList<String>();
        attributes.add("comment");
        attributes.add("userDescription");
        attributes.add("table");
        attributes.add("parameters");

        param.put("attributes", attributes);

        // 정렬 방법을 선택한 경우
        if (param.get("sortOrder") != "" && param.get("sortBy") != "") { 
            // 정렬 대상 : 테이블명(name), 등록일(createtime)
            param.put("sortBy", param.get("sortBy"));
            param.put("sortOrder", param.get("sortOrder"));
        } else { // 정렬 방법을 선택하지 않은 경우
            param.remove("sortOrder");
            param.put("sortBy", "__modificationTimestamp");
            param.put("sortOrder", "DESCENDING");
        }

        String queryStr = "";
        // 한글자씩 쪼개기
        if (param.get("query") != null) {
            String[] texts = ((String) param.get("query")).split("");
            // 특수문자 리스트
            String [] sl = specialCharacter.split(" ");
            
            // 한글자씩 확인하여 지정된 특수문자면 앞에 \ 기호 추가
            String textResult = "";
            for (int i=0; i<texts.length; i++) {
                int count = 0; // 특수기호 바뀐 횟수
                for (int j=0; j<sl.length; j++) {
                    // 확인되면 바꾼 값으로 추가하고 break
                    if (texts[i].equals(sl[j])) {
                        count++;
                        textResult = textResult + "\\" + texts[i];
                        break;
                    }
                }

                // 치환된 값이 있으면 추가하지 않음
                if (count == 0) textResult = textResult + texts[i];
            }
            queryStr = textResult;
        
            param.put("query", queryStr);
        }
        
        if(param.get("query") != null){
            String[] searchQueryArr = param.get("query").toString().split(" ");
            StringBuffer searchQuery = new StringBuffer();

            // Solr 동의어 검색 처리
            if (searchQueryArr.length == 1) { // 단일 검색
                // ((테스트) OR ((ddz9_t:(*테스트*) OR 3j7p_t:(*테스트*) OR 3k05_s:(*테스트*) OR 3bb9_t:(*테스트*)))) AND (hdx_t:ACTIVE)
                // 2024-08-08 쿼리 형식 변경됨 
                // 검색어 없음 => f0l_t:hive_table  AND jr9_t:ACTIVE AND ((e2h1_t:(**) OR e2h1_t:(**) OR 42yt_s:(**) OR 3thh_t:(**) OR 426d_t:(**)))
                // 검색어 있음 => f0l_t:hive_table  AND jr9_t:ACTIVE AND ((test) OR (e2h1_t:(*test*) OR e2h1_t:(*test*) OR 42yt_s:(*test*) OR 3thh_t:(*test*) OR 426d_t:(*test*)))
                String word = "*" + searchQueryArr[0] + "*";
                
                searchQuery.append(typeName + ":" + param.get("typeName"));
                searchQuery.append(" AND ");
                searchQuery.append(status + ":ACTIVE");

                // 사내 테스트 시 dw_ dm_ 테이블이 없어 해당 아래 구문 제거 후 테스트
                // 운영서버인 경우
                if (isProd) searchQuery.append(" AND ((" + qualifiedName + ":dw_* OR " + qualifiedName + ":dm_*) AND -" + qualifiedName + ":dev_* )");
                // 개발서버인 경우
                else searchQuery.append(" AND (" + qualifiedName + ":dev_dw_* OR " + qualifiedName + ":dev_dm_*)");
                
                searchQuery.append(" AND ");

                // 상세필터를 사용한 경우 추가
                List<String> categoryList = (ArrayList<String>) param.get("categoryList");
                List<String> systemList = (ArrayList<String>) param.get("systemList");
                List<String> tagList = (ArrayList<String>) param.get("tagList");
                if (param.get("checkUseFilter") != null && ((Boolean) param.get("checkUseFilter")) == true) {
                    searchQuery.append("(");

                    // 카테고리 (category)
                    if (categoryList != null && categoryList.size() > 0) {
                        for (int i=0; i<categoryList.size(); i++) {
                            if (i == 0) { searchQuery.append("("); }
                            searchQuery.append(category + ":" + categoryList.get(i));
                            if (categoryList.size() > 1 && i < categoryList.size() - 1) { // 카테고리 2개 이상 OR 조건
                                searchQuery.append(" OR ");
                            }
                            if (i == categoryList.size() - 1) { // 마지막 값 괄호 닫기
                                searchQuery.append(")");
                            }
                        }
                    }

                    // 시스템 (category)
                    if (systemList !=  null && systemList.size() > 0) {
                        if (categoryList != null && categoryList.size() > 0) { searchQuery.append(" AND "); }
                        
                        for (int i=0; i<systemList.size(); i++) {
                            if (i == 0) { // 시작값 괄호 열기
                                searchQuery.append("(");
                            }
                            searchQuery.append(category + ":" + systemList.get(i));
                            if (systemList.size() > 1 && i < systemList.size() - 1) { // 카테고리 2개 이상 OR 조건
                                searchQuery.append(" OR ");
                            }
                            if (i == systemList.size() - 1) { // 마지막 값 괄호 닫기
                                searchQuery.append(")");
                            }
                        }
                    }

                    // 태그 (label)
                    if (tagList !=  null && tagList.size() > 0) {
                        if ((categoryList != null && categoryList.size() > 0) || systemList != null && systemList.size() > 0) { searchQuery.append(" AND "); }
                        for (int i=0; i<tagList.size(); i++) {
                            if (i == 0) { // 시작값 괄호 열기
                                searchQuery.append("(");
                            }
                            searchQuery.append(label + ":" + tagList.get(i));
                            if (tagList.size() > 1 && i < tagList.size() - 1) { // 카테고리 2개 이상 OR 조건
                                searchQuery.append(" OR ");
                            }
                            if (i == tagList.size() - 1) { // 마지막 값 괄호 닫기
                                searchQuery.append(")");
                            }
                        }
                    }
                } else {
                    searchQuery.append("(");
                }
                 
                // 등록일 (createtime)
                long startDate = 0;
                long endDate = new Date().getTime();
                if (param.get("startDate") != null) startDate = Long.parseLong(param.get("startDate").toString());
                if (param.get("endDate") != null && Long.parseLong(param.get("endDate").toString()) != 0) endDate = Long.parseLong(param.get("endDate").toString());

                // 필터가 있는 경우 날짜표기를 필터내용 뒤에 추가
                if ((categoryList != null && categoryList.size() > 0) || 
                (systemList != null && systemList.size() > 0) || 
                (tagList != null && tagList.size() > 0)) {
                    searchQuery.append(" AND ");
                    searchQuery.append("(");
                    searchQuery.append(createtime + ":[" + startDate + " TO " + endDate + "]");
                    searchQuery.append(")");
                } else { // 날짜필터만 사용하는 경우
                    searchQuery.append("(");
                    searchQuery.append(createtime + ":[" + startDate + " TO " + endDate + "]");
                    searchQuery.append(")");
                }

                searchQuery.append(")");
                searchQuery.append(" AND ");

                if (!searchQueryArr[0].toString().equals("")) {
                    searchQuery.append("((");
                    searchQuery.append(searchQueryArr[0]);
                    searchQuery.append(")");
                    searchQuery.append(" OR ");
                }

                // searchQuery.append("((" + comment + ":(" + word + ") OR " + description + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + "))))");
                if (param.get("typeName").equals("hive_table")) searchQuery.append("((" + comment + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")))");
                else if (param.get("typeName").equals("hive_column")) searchQuery.append("((" + comment2 + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")))");
                else searchQuery.append("((" + comment + ":(" + word + ") OR " + comment2 + ":(" + word + ") OR" + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")))");

                if (!searchQueryArr[0].toString().equals("")) searchQuery.append(")");

            } else if(searchQueryArr.length > 1){ // 다중 검색
                // ((테스트 OR 차량) OR ((ddz9_t:(*테스트*) OR 3j7p_t:(*테스트*) OR 3k05_s:(*테스트*) OR 3bb9_t:(*테스트*) OR ddz9_t:(*차량*) OR 3j7p_t:(*차량*) OR 3k05_s:(*차량*) OR 3bb9_t:(*차량*)))) AND (hdx_t:ACTIVE)
                
                // 앞부분 쿼리 제작
                searchQuery.append(typeName + ":" + param.get("typeName"));
                searchQuery.append(" AND ");
                searchQuery.append(status + ":ACTIVE");

                // 사내 테스트 시 dw_ dm_ 테이블이 없어 해당 아래 구문 제거 후 테스트
                // 운영서버인 경우
                if (isProd) searchQuery.append(" AND ((" + qualifiedName + ":dw_* OR " + qualifiedName + ":dm_*) AND -" + qualifiedName + ":dev_* )");
                // 개발서버인 경우
                else searchQuery.append(" AND (" + qualifiedName + ":dev_dw_* OR " + qualifiedName + ":dev_dm_*)");

                searchQuery.append(" AND ");
                searchQuery.append("((");
                for (int i=0; i<searchQueryArr.length; i++) {
                    searchQuery.append(searchQueryArr[i]);
                    if (i == searchQueryArr.length - 1) break; // 마지막인 경우 OR 붙이지 않음
                    else searchQuery.append(" OR ");
                }
                searchQuery.append(")");

                searchQuery.append(" OR ");

                // 뒷부분 쿼리 제작
                searchQuery.append("((");
                for(int j = 0; j<searchQueryArr.length; j++) {
                    String word = "*" + searchQueryArr[j] + "*";
                    
                    if (param.get("typeName").equals("hive_table")) searchQuery.append(comment + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")");
                    else if (param.get("typeName").equals("hive_column")) searchQuery.append(comment2 + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")");
                    else searchQuery.append(comment + ":(" + word + ") OR " + comment2 + ":(" + word + ") OR " + userDescription + ":(" + word + ") OR " + qualifiedName + ":(" + word + ") OR " + description + ":(" + word + ")");

                    if (j == searchQueryArr.length - 1) break;
                    else searchQuery.append(" OR ");
                }
                searchQuery.append(")))");
            }
            log.info("==== search query ====");
            log.info(searchQuery.toString());
            param.put("query",searchQuery.toString());
        }
        
        if (param.get("offset") == null) {
            param.put("offset", "0");
        }
        if (param.get("limit") == null) {
            param.put("limit", "10");
        }

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        if(queryStr != null){            
            Map<String, Object> textHistoryParam = new HashMap<>();

            textHistoryParam.put("search_string",queryStr);
            textHistoryParam.put("userId", user.get("user_id"));
            searchService.insertSearchTextHistory(textHistoryParam);
        }

        System.out.println("testtest query result");
        System.out.println(param.get("query"));

        // classificatiion 입력값 검증
        if (param.get("classification") != null) {
            String classifi = param.get("classification").toString();
            RegexUtils.sanitizeInput(classifi);
        }

        JsonResult result = atlasRestAPI.post(baseUrl, param);

        // 카테고리, 시스템 가공 후 반환
        Map<String, Object> data = (Map<String, Object>) result.getData();
        if (data != null) {
            List<Map<String, Object>> entities = (List<Map<String, Object>>) data.get("entities");            
            
            // 아직 검증안되서 주석처리함
            entities = searchService.checkDataAccessByEntities(entities, user);
        
            data.put("entities", searchService.processSysCatSort(entities));

            // searchParameters, queryText 제거
            data.remove("searchParameters");
            data.remove("queryText");
            
            result.setData(data);
        }

        return result;
    }

    // 통합 검색
    @ResponseJsonResult
    @PostMapping("/search/keywordRef")
    public Object PostSearchBasicRef(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        String baseUrl = "/search/basic";

        param.put("excludeDeletedEntities", "true"); // 삭제된 데이터 포함
        param.put("includeSubTypes", "true");
        param.put("includeSubClassifications", "false");
        param.put("includeClassificationAttributes", "true");

        List<String> attributes = new ArrayList<String>();
        attributes.add("comment");
        attributes.add("userDescription");
        attributes.add("table");
        attributes.add("parameters");

        param.put("attributes", attributes);

        // 정렬 방법을 선택한 경우
        // if (param.get("sortOrder") != "" && param.get("sortBy") != "") { 
        //     // 정렬 대상 : 테이블명(name), 등록일(createtime)
        //     param.put("sortBy", param.get("sortBy"));
        //     param.put("sortOrder", param.get("sortOrder"));
        // } else { // 정렬 방법을 선택하지 않은 경우
        //     param.remove("sortOrder");
        //     param.put("sortBy", "__modificationTimestamp");
        //     param.put("sortOrder", "DESCENDING");
        // }
        
        List<String> classificationNames = (List<String>) param.get("classificationNames");

        StringBuffer searchQuery = new StringBuffer();

        // Solr 연관 데이터 검색 처리
        searchQuery.append(typeName + ":" + param.get("typeName"));
        searchQuery.append(" AND ");
        searchQuery.append(status + ":ACTIVE");

        // 사내 테스트 시 dw_ dm_ 테이블이 없어 해당 아래 구문 제거 후 테스트
        // 운영서버인 경우
        if (isProd) searchQuery.append(" AND ((" + qualifiedName + ":dw_* OR " + qualifiedName + ":dm_*) AND -" + qualifiedName + ":dev_* )");
        // 개발서버인 경우
        else searchQuery.append(" AND (" + qualifiedName + ":dev_dw_* OR " + qualifiedName + ":dev_dm_*)");
        
        searchQuery.append(" AND ");

        // 연관 데이터 카테고리 항목이 있는 경우 조건 추가
        if (classificationNames != null) {
            searchQuery.append("(");
            for (int i=0; i<classificationNames.size(); i++) {
                searchQuery.append(category + ":" + classificationNames.get(i).toString());
                if (i < classificationNames.size() - 1) searchQuery.append(" AND ");
            }
            searchQuery.append(")");
        }

        param.put("query", searchQuery);
        
        if (param.get("offset") == null) {
            param.put("offset", "0");
        }
        if (param.get("limit") == null) {
            param.put("limit", "10");
        }

        System.out.println("testtest ref query result");
        System.out.println(param.get("query"));

        JsonResult result = atlasRestAPI.post(baseUrl, param);

        // searchParameters, queryText 제거
        Map<String, Object> data = (Map<String, Object>) result.getData();
        if (data != null) {
            data.remove("searchParameters");
            data.remove("queryText");
        }

        return result;
    }

    // 통합 검색 : 외부데이터
    @ResponseJsonResult
    @PostMapping("/search/keyword/externalDataList")
    public Object getExternalDataList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception  {
        return searchService.getExternalDataList(params);
    }

    // 통합 검색 : 외부데이터 카운트 조회
    @ResponseJsonResult
    @PostMapping("/search/keyword/externalDataListCount")
    public Object getExternalDataListCount(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception  {
        return searchService.getExternalDataListCount(params);
    }

    

    // URL, Parameter 검색
    @ResponseJsonResult
    @PostMapping("/search/getApiSearch")
    public Object PostSearchTypeDefs(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonResult result = new JsonResult();
        Map<String, Object> params = (Map<String, Object>) param.get("params");
        result = atlasRestAPI.get((String) param.get("url"), params);

        return result;
    }

     // DB 데이터 조회
    @ResponseJsonResult
    @PostMapping("/search/dbData")
    public Object getDbData(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonResult result = new JsonResult();
        Map<String, Object> params = (Map<String, Object>) param.get("params");
        result = atlasRestAPI.get((String) param.get("url"), params);

        return result;
    }

    // hive_table 전체 개수 조회
    @ResponseJsonResult
    @GetMapping("/search/tableTotal")
    public Object getCategoryTotal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return searchService.getTableTotal();
    }
    
}
