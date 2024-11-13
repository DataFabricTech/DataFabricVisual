package com.mobigen.datalake.metaData.manage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.datalake.atlas.AtlasRestAPI;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/metadata/manage")
@RestController
@RequiredArgsConstructor
public class MetaDataManageController {

    private final AtlasRestAPI atlasRestAPI;
    private final MetaDataManageService metaDataManageService;

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

    /**
     * 메타 데이터 통합 관리 > 메타 데이터 통합 관리 대상 목록 조회
     *
     * @params String       카테고리명
     * @return Object       메타 데이터 통합 관리 대상 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getMetaDataList(@RequestBody Map<String, Object> param) {
        
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
        
            param.put("word", queryStr);
        }
        
        if(param.get("word") != null){
            String[] searchQueryArr = param.get("word").toString().split(" ");
            StringBuffer searchQuery = new StringBuffer();

            // Solr 동의어 검색 처리
            if (searchQueryArr.length == 1) { // 단일 검색
                // 2024-08-08 쿼리 형식 변경됨 
                // 검색어 없음 => f0l_t:hive_table  AND jr9_t:ACTIVE AND ((e2h1_t:(**) OR e2h1_t:(**) OR 42yt_s:(**) OR 3thh_t:(**) OR 426d_t:(**)))
                // 검색어 있음 => f0l_t:hive_table  AND jr9_t:ACTIVE AND ((test) OR (e2h1_t:(*test*) OR e2h1_t:(*test*) OR 42yt_s:(*test*) OR 3thh_t:(*test*) OR 426d_t:(*test*)))
                String word = "*" + searchQueryArr[0] + "*";
                
                searchQuery.append(typeName + ":" + param.get("typeName"));
                searchQuery.append(" AND ");
                searchQuery.append(status + ":ACTIVE");

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
            param.put("limit", (int) param.get("limit"));
            param.put("offset", (int) param.get("offset"));
        }
        
        if (param.get("offset") == null) {
            param.put("offset", "0");
        }
        if (param.get("limit") == null) {
            param.put("limit", "10");
        }

        param.remove("searchType2");

        JsonResult result = atlasRestAPI.post(baseUrl, param);

        Map<String, Object> data = (Map<String, Object>) result.getData();

        if (data != null) {
            // searchParameters, queryText 제거
            data.remove("searchParameters");
            data.remove("queryText");
        }

        return result;
    }

}
