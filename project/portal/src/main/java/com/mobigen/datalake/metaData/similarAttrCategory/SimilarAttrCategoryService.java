package com.mobigen.datalake.metaData.similarAttrCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimilarAttrCategoryService {
    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    private final SimilarAttrCategoryMapper similarAttrCategoryMapper;

    // 유사 속성 카테고리 목록 조회 (검색/사용여부 필터/페이징 + 매핑 건수)
    public Map<String, Object> getList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> list = similarAttrCategoryMapper.getList(params);
        int total = similarAttrCategoryMapper.getListCount(params);

        result.put("list", list);
        result.put("total", total);
        return result;
    }

    // 유사 속성 카테고리 상세 조회
    public Map<String, Object> getInfo(String id) throws Exception {
        return similarAttrCategoryMapper.getInfo(id);
    }

    // 유사 속성 카테고리 등록
    public Map<String, Object> create(Map<String, Object> params, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String id = UUID.randomUUID().toString();
        params.put("id", id);
        params.put("creator", user != null ? user.get("user_id") : null);

        int count = similarAttrCategoryMapper.insert(params);

        result.put("result", count > 0);
        result.put("id", id);
        return result;
    }

    // 유사 속성 카테고리 수정
    public Map<String, Object> update(Map<String, Object> params, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();

        params.put("modifier", user != null ? user.get("user_id") : null);

        int count = similarAttrCategoryMapper.update(params);

        if (count > 0) {
            // 카테고리 정의가 변경됐으므로 매핑된 메타데이터의 ES 문서를 갱신
            String id = (String) params.get("id");
            List<String> metaIds = similarAttrCategoryMapper.getMappedMetaIds(id);
            if (!metaIds.isEmpty()) {
                essyncUpdates(metaIds, 2, "유사 속성 카테고리 수정");
            }
        }

        result.put("result", count > 0);
        return result;
    }

    // 유사 속성 카테고리 삭제 (<table>은 ON DELETE CASCADE로 함께 삭제)
    public Map<String, Object> delete(String id) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // CASCADE로 삭제되기 전에 매핑된 메타데이터 목록 확보
        List<String> metaIds = similarAttrCategoryMapper.getMappedMetaIds(id);

        int count = similarAttrCategoryMapper.delete(id);

        if (count > 0 && !metaIds.isEmpty()) {
            essyncUpdates(metaIds, 2, "유사 속성 카테고리 삭제");
        }

        result.put("result", count > 0);
        return result;
    }

    // 카테고리명 중복 체크
    public Map<String, Object> checkNameDuplicated(String name) throws Exception {
        Map<String, Object> result = new HashMap<>();

        int count = similarAttrCategoryMapper.countByName(name);

        result.put("duplicated", count > 0);
        return result;
    }

    // ES 동기화 API 호출
    private boolean essyncUpdates(List<String> meta_ids, Integer eventStatus, String eventName) throws Exception {
        boolean result = false;

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaids", meta_ids);
            requestBody.put("event_status", eventStatus); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", eventName);

            String url = esSyncBaseUrl + esSyncUrlUpdates;
            ResponseEntity<HashMap> restResponse = restTemplate.postForEntity(url, requestBody, HashMap.class);
            if (restResponse != null && restResponse.getBody() != null) {
                Map<String, Object> body = restResponse.getBody();
                if (body.get("result") != null) {
                    Map<String, Object> bodyResultMap = (Map<String, Object>) body.get("result");
                    if (bodyResultMap != null && "success".equals(bodyResultMap.get("result"))) {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("essyncUpdates API 호출 오류", e);
            result = false;
        }

        return result;
    }
}
