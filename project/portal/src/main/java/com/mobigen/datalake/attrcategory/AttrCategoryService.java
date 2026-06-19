package com.mobigen.datalake.attrcategory;

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
public class AttrCategoryService {

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    private final AttrCategoryMapper attrCategoryMapper;

    // ── 차트 조회 ──────────────────────────────────────────────────────────────

    public List<Map<String, Object>> getCategoryList() {
        return attrCategoryMapper.getCategoryList();
    }

    public Map<String, Object> getTables(String categoryId, String sort, int limit) {
        String sortKey = "name".equalsIgnoreCase(sort) ? "name" : "similarity";
        Map<String, Object> result = new HashMap<>();
        result.put("list", attrCategoryMapper.getTables(categoryId, sortKey, limit));
        result.put("total", attrCategoryMapper.getTablesCount(categoryId));
        return result;
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────

    public Map<String, Object> getList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("list", attrCategoryMapper.getList(params));
        result.put("total", attrCategoryMapper.getListCount(params));
        return result;
    }

    public Map<String, Object> create(Map<String, Object> params, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String id = UUID.randomUUID().toString();
        params.put("id", id);
        params.put("creator", user != null ? user.get("user_id") : null);
        int count = attrCategoryMapper.insert(params);
        result.put("result", count > 0);
        result.put("id", id);
        return result;
    }

    public Map<String, Object> update(Map<String, Object> params, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        params.put("modifier", user != null ? user.get("user_id") : null);
        int count = attrCategoryMapper.update(params);
        if (count > 0) {
            List<String> metaIds = attrCategoryMapper.getMappedMetaIds((String) params.get("id"));
            if (!metaIds.isEmpty()) {
                essyncUpdates(metaIds, 2, "유사 속성 카테고리 수정");
            }
        }
        result.put("result", count > 0);
        return result;
    }

    public Map<String, Object> delete(String id) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<String> metaIds = attrCategoryMapper.getMappedMetaIds(id);
        int count = attrCategoryMapper.delete(id);
        if (count > 0 && !metaIds.isEmpty()) {
            essyncUpdates(metaIds, 2, "유사 속성 카테고리 삭제");
        }
        result.put("result", count > 0);
        return result;
    }

    public Map<String, Object> checkNameDuplicated(String name) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("duplicated", attrCategoryMapper.countByName(name) > 0);
        return result;
    }

    // ── ES 동기화 ──────────────────────────────────────────────────────────────

    private boolean essyncUpdates(List<String> metaIds, Integer eventStatus, String eventName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaids", metaIds);
            requestBody.put("event_status", eventStatus);
            requestBody.put("event_name", eventName);
            String url = esSyncBaseUrl + esSyncUrlUpdates;
            ResponseEntity<HashMap> response = restTemplate.postForEntity(url, requestBody, HashMap.class);
            if (response != null && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.get("result") != null) {
                    Map<String, Object> bodyResult = (Map<String, Object>) body.get("result");
                    return bodyResult != null && "success".equals(bodyResult.get("result"));
                }
            }
        } catch (Exception e) {
            log.error("essyncUpdates API 호출 오류", e);
        }
        return false;
    }
}
