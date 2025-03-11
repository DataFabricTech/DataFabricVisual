package com.mobigen.datalake.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.search.SearchDetailService;
import com.mobigen.datalake.search.SearchService;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MainPageMapper mainPageMapper;
    private final SessionManager sessionManager;
    private final SearchService searchService;

    @Value("${properties.ESSearch.server.host}")
    private String esSearchApiHost;
    @Value("${properties.ESSearch.server.port}")
    private String esSearchApiPort;

    // 추천 검색어 조회
    public Object getRecommendedWordList() throws Exception {
        List<Map<String, Object>> RecommendedWordList = mainPageMapper.getRecommendedWordList();
        return RecommendedWordList;
    }

    // 전체 데이터 수, 데이터 활용 횟수, 신규 데이터 조회
    public Object getDataCountData() throws Exception {
        Map<String, Object> dataCountMap = new HashMap<>();
        dataCountMap.put("totalDataCount", mainPageMapper.getTotalDataCount());
        dataCountMap.put("useDataCount", mainPageMapper.getUseDataCount());
        return dataCountMap;
    }

    // 공지사항, FAQ 목록 조회
    public Object getPostListData() throws Exception {
        Map<String, Object> postDataMap = new HashMap<>();
        postDataMap.put("noticeList", mainPageMapper.getNoticeList());
        postDataMap.put("faqList", mainPageMapper.getFaqList());
        return postDataMap;
    }

    // 활용사례 조회
    public Object getShareDataList() throws Exception {
        List<Map<String, Object>> shareDataList = mainPageMapper.getShareDataList();
        return shareDataList;
    }

    // 최신 등록 데이터 순 조회 (ES API)
    public Object getLatestDataList(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);

            // ES 검색쿼리 설정
            params.put("classification", "");
            params.put("entityFilters", new HashMap<>());
            params.put("limit", 4);
            params.put("meta_type", "table");
            params.put("offset", 0);
            params.put("query", "");
            params.put("sortBy", "create_time");
            params.put("sortOrder", "DESC");
            params.put("system_type", "");
            params.put("tag", "");

            // 사용자 데이터 권한(role_id) 확인
            List<String> role_ids = searchService.getRoleIds(user);
            params.put("roleids", role_ids);

            RestTemplate restTemplate = new RestTemplate();

            // esSearch API의 URL 생성
            String fastUrl = "http://" + esSearchApiHost + ":" + esSearchApiPort + "/api/v1/search/keyword";
            log.info("call essearch api : " + fastUrl);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(fastUrl, HttpMethod.POST, requestEntity,
                    HashMap.class);

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
                if (body != null && body.containsKey("release"))
                    body.remove("release");
                if (body != null && body.containsKey("role_id"))
                    body.remove("role_id");

            } else {
                result.put("results", new ArrayList<>());
                result.put("total_count", 0);
                throw new Exception("최신 등록 데이터 검색에 실패하였습니다.");
            }

        } catch (Exception e) {
            result.put("results", new ArrayList<>());
            result.put("total_count", 0);
            throw new Exception("최신 등록 데이터 검색에 실패하였습니다.");
        }

        return result;
    }

    // My 데이터 요청내역 조회
    public Object getMyRequestDataList(String user_id) throws Exception {
        List<Map<String, Object>> dataRequestList = mainPageMapper.getMyRequestDataList(user_id);
        return dataRequestList;
    }

    private final SearchDetailService searchDetailService;

    // My 찜데이터 조회
    public Object getMyFavoriteDataList(String user_id) throws Exception {
        // 결과를 저장할 리스트
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyFavoriteDataList(user_id);

        // JSON 데이터를 처리하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 각 guid에 대해 detail 데이터를 가져와서 shareDataList에 추가
        for (int i = 0; i < shareDataList.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("meta_id", shareDataList.get(i).get("meta_id"));

            // getDetailData 호출
            Object detailData = searchDetailService.getDetailDataForMain(params);

            // detailData가 JSON 형식일 경우 처리
            if (detailData != null) {
                // JSON 결과를 Map 형태로 변환
                Map<String, Object> detailDataMap = objectMapper.convertValue(detailData, Map.class);

                // "data" 필드가 있는지 확인
                if (detailDataMap.containsKey("data")) {
                    Object data = detailDataMap.get("data");

                    // "data"가 LinkedHashMap인 경우 처리
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

                    // "entity" 키가 있는지 확인하여 shareDataList에 추가
                    if (dataMap.containsKey("entity")) {
                        shareDataList.get(i).put("entity", dataMap.get("entity"));
                    }
                }
            }
        }

        // shareDataList를 리턴
        return shareDataList;
    }

    // My 리뷰 데이터 조회
    public Object getMyReviewDataList(String user_id) throws Exception {
        // 결과를 저장할 리스트
        List<Map<String, Object>> shareDataList = mainPageMapper.getMyReviewDataList(user_id);

        // JSON 데이터를 처리하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 각 guid에 대해 detail 데이터를 가져와서 shareDataList에 추가
        for (int i = 0; i < shareDataList.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("meta_id", shareDataList.get(i).get("meta_id"));

            // getDetailData 호출
            Object detailData = searchDetailService.getDetailDataForMain(params);

            // detailData가 JSON 형식일 경우 처리
            if (detailData != null) {
                // JSON 결과를 Map 형태로 변환
                Map<String, Object> detailDataMap = objectMapper.convertValue(detailData, Map.class);

                // "data" 필드가 있는지 확인
                if (detailDataMap.containsKey("data")) {
                    Object data = detailDataMap.get("data");

                    // "data"가 LinkedHashMap인 경우 처리
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

                    // "entity" 키가 있는지 확인하여 shareDataList에 추가
                    if (dataMap.containsKey("entity")) {
                        shareDataList.get(i).put("entity", dataMap.get("entity"));
                    }
                }
            }
        }

        // shareDataList를 리턴
        return shareDataList;
    }

    // My 데이터 요청내역 조회
    public Object getMyExtractedDataList(String user_id) throws Exception {
        List<Map<String, Object>> dataRequestList = mainPageMapper.getMyExtractedDataList(user_id);
        return dataRequestList;
    }

    public Object getChartCategoryList() throws Exception {
        return mainPageMapper.getChartCategoryList();
    }

    public Map<String, Object> getMainBannerSubject() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getMainBannerSubject();
            if (list != null && list.size() > 0) {
                result.put("subjectList", list);
            } else {
                result.put("subjectList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("subjectList", new ArrayList<>());
        }

        return result;
    }

    public Object updateCodeTable(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("list");

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    // 기존 데이터 조회
                    Map<String, Object> originInfo = mainPageMapper.getCodeInfo(list.get(i));
                    if (originInfo != null) {
                        mainPageMapper.updateCodeTableFromName(list.get(i));
                    } else {
                        // 기존 데이터가 없는 경우 신규 데이터 추가
                        mainPageMapper.insertCodeTable(list.get(i));
                    }
                }
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail to save layout Info");
            result.put("result", false);
        }

        return result;
    }

    public Map<String, Object> getLayoutList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getLayoutList();
            if (list != null && list.size() > 0) {
                result.put("layoutList", list);
            } else {
                result.put("layoutList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("layoutList", new ArrayList<>());
        }

        return result;
    }

    public Map<String, Object> getFooterList() throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> list = mainPageMapper.getFooterList();
            if (list != null && list.size() > 0) {
                result.put("footerList", list);
            } else {
                result.put("footerList", new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Fail to load Main Banner Subject");
            result.put("footerList", new ArrayList<>());
        }

        return result;
    }
}
