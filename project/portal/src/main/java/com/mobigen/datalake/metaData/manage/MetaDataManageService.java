package com.mobigen.datalake.metaData.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataManageService {
    private final MetaDataManageMapper metaDataManageMapper;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    // 메타 데이터 통합 관리 대상 목록 조회
    public Map<String, Object> getMetaDataManageList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        totCnt = metaDataManageMapper.getMetaDataManageListTotCnt(params);
        resultList = metaDataManageMapper.getMetaDataManageList(params);

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    public List<Map<String, Object>> getMappedMetaDataManageListByClassificationId(Map<String, Object> params)
            throws Exception {
        return metaDataManageMapper.getMappedMetaDataManageListByClassificationId(params);
    };

    public List<Map<String, Object>> getMetaDataListByParentId(String parentId) throws Exception {
        return metaDataManageMapper.getMetaDataListByParentId(parentId);
    }

    public int updateMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.updateMetaDataClassification(params);
    }

    public int deleteMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.deleteMetaDataClassification(params);
    }

    public List<String> getFilterOptions() throws Exception {
        return metaDataManageMapper.getFilterOptions();
    }

    // 공개설정 권한 처리
    public Object setDataAuth(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        boolean addResult = false;
        boolean esResult = false;
        try {
            // meta_ids가 있는 경우만 처리
            List<String> meta_ids = (List<String>) params.get("meta_ids");
            if (meta_ids != null && meta_ids.size() > 0) {
                int addResultCount = metaDataManageMapper.updateMetaDataRelease(params);
                if (addResultCount > 0) {
                    addResult = true;
                    essyncUpdates(meta_ids, 2, "데이터 권한 공개설정");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("result", addResult);
        result.put("esResult", esResult);

        return result;
    }

    // ES 동기화 API 호출
    public boolean essyncUpdates(List<String> meta_ids, Integer eventStatus, String eventName) throws Exception {
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
                if (body != null && body.get("result") != null) {
                    Map<String, Object> bodyResultMap = (Map<String, Object>) body.get("result");
                    if (bodyResultMap != null) {
                        String rs = bodyResultMap.get("result").toString();
                        if (rs.equals("success")) {
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("essyncUpdates API 호출 오류");
            result = false;
        }

        return result;
    }
}
