package com.mobigen.datalake.metaData.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mobigen.datalake.metaData.manage.MetaDataManageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataClassificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataClassificationService.class);

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.update}")
    private String esSyncUrlUpdate;

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    @Value("${properties.ESSync.url.convertcategory}")
    private String esSyncUrlConvertcategory;

    private final MetaDataClassificationMapper metaDataClassificationMapper;
    private final MetaDataManageService metaDataManageService;

    // 카테고리 담당부서 조회
    public Object getClassificationDept(String name) throws Exception {
        return metaDataClassificationMapper.getClassificationDept(name);
    }

    // 카테고리 Depth 조회
    public Object getClassificationDepth(String name) throws Exception {
        return metaDataClassificationMapper.getClassificationDepth(name);
    }

    // 카테고리 추가
    public Object insertClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String level = (String) metaDataClassificationMapper
                .getClassificationDepth((String) params.get("superTypeName"));

        // 루트 카테고리의 경우
        if (level == null || level.equals("")) {
            level = "1";
        } else {
            int levelInt = Integer.parseInt(level) + 1;
            level = Integer.toString(levelInt);
        }
        params.put("level", level);

        int resultCount = metaDataClassificationMapper.insertClassification(params);

        result.put("isFailed", resultCount < 0);
        return result;
    }

    // 카테고리 수정
    public Object updateClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean isFailed = true;
        boolean esUpdated = false;

        int resultCount = metaDataClassificationMapper.updateClassification(params);

        if (resultCount > 0) {

            Map<String, Object> restResponse = essyncConvertCategory(params);

            boolean esResult = false;
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                    isFailed = false;
                    esResult = true;
                } else {
                    isFailed = true;
                    esResult = false;
                }
            }

            if (esResult) { // ES 등록 성공
                esUpdated = true;
            } else { // ES 등록 실패
                result.put("errorMessage", "ES 업데이트 실패");
                isFailed = true;
            }
        } else {

        }

        result.put("isFailed", isFailed);
        result.put("esUpdated", esUpdated);
        return result;
    }

    // 카테고리 삭제
    public Object deleteClassification(String id) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String errorMessage = "";

        // 매핑된 메타 데이터가 있는지 확인
        List<Map<String, Object>> mappedDataList = metaDataManageService
                .getMappedMetaDataManageListByClassificationId(new HashMap<String, Object>() {
                    {
                        put("id", id);
                    }
                });
        if (!mappedDataList.isEmpty()) {
            errorMessage = "해당 카테고리를 참조하고 있는 개체가 있습니다.";
            result.put("errorMessage", errorMessage);
            result.put("isFailed", true);
            return result;
        }

        // 하위 카테고리가 있는지 확인
        List<Map<String, Object>> childrenList = metaDataClassificationMapper.getClassificationChildrenById(id);
        if (!childrenList.isEmpty()) {
            errorMessage = "해당 카테고리를 참조하고 있는 개체가 있습니다.";
            result.put("errorMessage", errorMessage);
            result.put("isFailed", true);
            return result;
        }

        // 그 외의 경우 카테고리 삭제
        int resultCount = metaDataClassificationMapper.deleteClassification(id);

        result.put("isFailed", resultCount <= 0);

        return result;
    }

    // 카테고리명(영문명) 중복 여부 조회
    public Object checkDuplicateClassificationName(String name) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = metaDataClassificationMapper.getClassificationListByName(name);

        result.put("isDuplicate", resultList.size() > 0);
        return result;
    }

    // 카테고리 맵핑
    public Object mapClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;

        // 매핑 카테고리 아이디
        String classificationId = params.get("classificationId").toString();
        // 매핑 대상 entity 아이디 목록
        List<String> entityIds = (List<String>) params.get("entityIds");
        int entityIdsCount = entityIds.size();
        // 매핑 카테고리의 상위 타입 목록
        List<String> superTypes = (List<String>) params.get("superTypes");

        // 테이블 유형일 경우 하위 컬럼 ID 목록 추출하여 함께 카테고리 맵핑
        // List<String> tableTypes = (List<String>) params.get("tableTypes");
        // if (tableTypes.size() > 0) {
        // List<String> columnIds = new ArrayList<>();

        // for (String tableTypeId : tableTypes) {
        // columnIds.addAll(this.getColumns(tableTypeId));
        // }

        // entityIds.addAll(columnIds);
        // }

        // 중복된 카테고리 매핑이 있는 엔티티 제거
        entityIds = removeDuplicateMappingEntity(classificationId, entityIds);
        entityIdsCount = entityIds.size();

        if (entityIds.size() <= 0) {
            return result;
        }

        // 메타 데이터 카테고리 매핑
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("classificationId", classificationId);
        updateParam.put("entityIds", entityIds);
        int resultCount = metaDataManageService.updateMetaDataClassification(updateParam);

        // 매핑 성공 시
        if (resultCount > 0) {
            // update classification count
            updateParam.put("entityIdsCount", entityIdsCount);

            // 맵핑 성공 시 상위 카테고리 맵핑 대상 확인 후 처리
            if (superTypes.size() > 0) {
                this.mapSuperTypeClassification(entityIds, superTypes);
            }
        }

        // es sync api 호출
        Integer failCount = 0;
        for (String id : entityIds) {
            Map<String, Object> esParam = new HashMap<>();
            esParam.put("meta_id", id);
            esParam.put("event_status", 2);
            esParam.put("event_name", "카테고리 매핑");
            Map<String, Object> restResponse = essyncUpdate(esParam);

            boolean esResult = false;
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                    esResult = true;
                } else {
                    failCount++;
                    esResult = false;
                }
            }
        }

        result.put("isFailed", resultCount < 0 || failCount > 0);
        return result;
    }

    // 상위 카테고리 맵핑
    public void mapSuperTypeClassification(List<String> ids, List<String> superTypes) throws Exception {
        for (int j = 0; j < superTypes.size(); j++) {
            String classificationId = superTypes.get(j);
            List<String> entityIds = removeDuplicateMappingEntity(classificationId, ids);
            if (entityIds.size() <= 0) {
                continue;
            }

            Map<String, Object> updateParam = new HashMap<>();
            updateParam.put("classificationId", classificationId);
            updateParam.put("entityIds", entityIds);
            int resultCount = metaDataManageService.updateMetaDataClassification(updateParam);
        }
    }

    // 카테고리 맵핑 제거
    public Object deleteMapClassification(String id, Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        List<String> targetIds = new ArrayList<>(); // 카테고리 맵핑 제거 대상 id 목록
        String guid = (String) params.get("id"); // 사용자가 선택한 엔티티 guid
        targetIds.add(guid);

        // Boolean isTableType = (Boolean) params.get("isTableType");
        // if (isTableType) {
        // // 테이블 유형일 경우 하위 목록 조회 및 추가
        // targetIds.addAll(this.getColumns(guid));
        // }
        // else {
        // // 컬럼 유형일 경우 현재 엔티티가 속한 테이블 & 해당 테이블의 하위 컬럼(현재 엔티티 제외) 목록 조회 및 추가
        // targetIds.addAll(this.getTableWithOtherColumns(guid));
        // }

        for (String targetId : targetIds) {
            // 선택된 classification 정보
            Map<String, Object> removeMapping = (Map<String, Object>) params.get("deleteMapping");
            // 기존 classification mapping 정보(id 목록)
            List<String> orgMappingList = (List<String>) params.get("classifications"); // ["1", "5"]

            // 선택된 classification id의 하위 목록 탐색 후 삭제 대상에 추가
            String removeId = removeMapping.get("id").toString(); // ["1"]
            List<String> subTypes = (List<String>) removeMapping.get("subTypes"); // ["2"]
            List<String> removeIds = new ArrayList<>();
            removeIds.add(removeId);
            removeIds.addAll(subTypes);

            // 기존 mapping list에서 지워지는 id 목록 제거 후 해당 데이터로 업데이트
            List<String> updateIds = new ArrayList<>();
            for (String mapId : orgMappingList) {
                // 삭제 id 목록에 포함되지 않은 id일 경우 update id 목록에 추가
                if (!removeIds.contains(mapId)) {
                    updateIds.add(mapId);
                }
            }

            Map<String, Object> delParams = new HashMap<>();
            delParams.put("id", targetId);
            delParams.put("classificationIds", updateIds);

            int resultCount = metaDataManageService.deleteMetaDataClassification(delParams);
        }

        // es sync api 호출
        Integer failCount = 0;
        for (String target : targetIds) {
            Map<String, Object> esParam = new HashMap<>();
            esParam.put("meta_id", target);
            esParam.put("event_status", 2);
            esParam.put("event_name", "카테고리 매핑 제거");
            Map<String, Object> restResponse = essyncUpdate(esParam);

            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                } else {
                    failCount++;
                }
            }
        }

        if (failCount > 0) {
            result.put("essyncIsFailed", true);
        }

        return result;
    }

    public Object deleteMappingClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean isFailed = false;
        Boolean esUpdated = false; // 사용자 데이터 설명 ES 변경됬는지 여부

        try {
            String guid = params.get("guid").toString();
            String deleteCategoryId = params.get("deleteCategoryId").toString();

            // 1. 삭제할 카테고리 id에 대한 하위 카테고리 모두 조회 (본인 id 포함된 결과)
            List<String> list = metaDataClassificationMapper.getClassificationChildByParentId(deleteCategoryId);

            // 2. 현재 메타에 있는 카테고리 항목 조회
            List<String> originClassifications = metaDataClassificationMapper.getClassifications(guid);

            // 3. 존재하는 카테고리에 대한 목록 생성
            List<String> realDeleteIdsList = new ArrayList<>();
            for (int i = 0; i < originClassifications.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    String originIds = originClassifications.get(i).toString();
                    if (originIds.equals(list.get(j).toString())) {
                        realDeleteIdsList.add(originIds);
                    }
                }
            }

            String[] deleteIds = realDeleteIdsList.toArray(new String[0]);

            // 4. <table> 테이블에서 해당 카테고리 id값들 모두 제거
            Map<String, Object> map = new HashMap<>();
            map.put("guid", guid);
            map.put("deleteIds", deleteIds);
            int deleteResult = metaDataClassificationMapper.deleteMappingClassification(map);

            int downCount = 0;

            // 5. ES Sync 처리
            // ES 업데이트 API 수행 결과 - API 호출 필요
            // es sync api 호출
            Integer failCount = 0;

            Map<String, Object> esParam = new HashMap<>();
            esParam.put("meta_id", guid);
            esParam.put("event_status", 2);
            esParam.put("event_name", "카테고리 매핑 제거");
            Map<String, Object> restResponse = essyncUpdate(esParam);

            boolean esResult = false;
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                    isFailed = false;
                    esResult = true;
                } else {
                    isFailed = true;
                    esResult = false;
                }
            }

            if (esResult) { // ES 등록 성공
                esUpdated = true;
            } else { // ES 등록 실패
                result.put("errorMessage", "ES 업데이트 실패");
                isFailed = true;
            }

            result.put("deleteResult", deleteResult);
            result.put("downCount", downCount);
            result.put("isFailed", isFailed);
            result.put("esUpdated", esUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isFailed", true);
            result.put("esUpdated", esUpdated);
        }

        // 3. ES Sync 처리
        return result;
    }

    // 하위 컬럼 id 목록 추출
    // private List<String> getColumns(String id) throws Exception {
    // List<String> ids = new ArrayList<>();

    // List<Map<String, Object>> childrenList =
    // metaDataManageService.getMetaDataListByParentId(id);
    // for (Map<String, Object> item : childrenList) {
    // String columnId = (String) item.get("id");
    // ids.add(columnId);
    // }

    // return ids;
    // }

    // 테이블 guid 및 선택 컬럼을 제외한 나머지 하위 컬럼 guid 목록 추출
    private List<String> getTableWithOtherColumns(String guid) {
        List<String> guids = new ArrayList<>();

        // JsonResult detail = this.atlasRestAPI.get("/entity/guid/" + guid);
        // if (detail.getResult() > 0) {
        // Map data = (Map) detail.getData();
        // Object tableGuid = this.findNestedMapValue(data,
        // "entity/attributes/table/guid");
        //
        // if (tableGuid != null && tableGuid instanceof String) {
        // guids.add((String) tableGuid);
        //
        // List<String> columns = this.getColumns((String) tableGuid);
        // for (String column : columns) {
        // if (!guid.equals(column)) {
        // guids.add(column);
        // }
        // }
        // }
        // }

        return guids;
    }

    // 다중 중첩된 Map에서 필요한 값 추출
    private Object findNestedMapValue(Map map, String props) {
        Object value = map;
        String[] keys = props.split("/");

        for (int i = 0; i < keys.length; i++) {
            if (value == null) {
                break;
            }

            String key = keys[i];
            Object curVal = ((Map) value).get(key);
            if (curVal != null) {
                value = curVal;
            } else {
                value = null;
            }
        }

        return value;
    }

    // 순서 변경을 위한 카테고리 목록 조회
    public Object getClassificationList(String level) throws Exception {
        List<Map<String, Object>> list = metaDataClassificationMapper.getClassificationList(level);
        return list;
    }

    // 순서 변경 처리
    public Object updateClassificationOrdNo(List<Map<String, Object>> params) throws Exception {
        int updateResult = 0;

        for (Map<String, Object> param : params) {
            updateResult += metaDataClassificationMapper.updateClassificationOrdNo(param);
        }

        return updateResult;
    }

    // 중복된 매핑이 있는지 검사 후 삭제
    private List<String> removeDuplicateMappingEntity(String classificationId, List<String> entityIds)
            throws Exception {

        // 중복된 카테고리 매핑이 있는 엔티티 검색
        Map<String, Object> findParam = new HashMap<>();
        findParam.put("id", classificationId);
        findParam.put("entityIds", entityIds);
        List<Map<String, Object>> findDuplicateCategory = metaDataManageService
                .getMappedMetaDataManageListByClassificationId(findParam);

        // 중복된 카테고리 매핑이 있을 경우 해당 entity 삭제
        List<String> removeEntityIds = new ArrayList<>();
        if (!findDuplicateCategory.isEmpty()) {
            for (Map<String, Object> item : findDuplicateCategory) {
                String id = item.get("meta_id").toString();
                removeEntityIds.add(id);
            }
            LOGGER.info("invalid parameters: entity: {}, already associated with classification: {}", removeEntityIds,
                    classificationId);
        }

        List<String> newEntityIds = new ArrayList<>();
        for (String id : entityIds) {
            if (!removeEntityIds.contains(id)) {
                newEntityIds.add(id);
            }
        }

        return newEntityIds;
    }

    public Map<String, Object> essyncUpdate(Map<String, Object> params) throws Exception {
        Map<String, Object> restResponse = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaid", params.get("meta_id"));
            requestBody.put("event_status", params.get("event_status")); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", params.get("event_name"));

            String url = esSyncBaseUrl + esSyncUrlUpdate;
            restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        } catch (Exception e) {
            log.error("Failed essync Update");
        }

        return restResponse;
    }

    public Map<String, Object> essyncUpdates(Map<String, Object> params) throws Exception {
        Map<String, Object> restResponse = new HashMap<>();
        List<String> meta_ids = (List<String>) params.get("meta_ids");
        Integer eventStatus = Integer.parseInt(params.get("event_status").toString());
        String eventName = params.get("event_name").toString();
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaids", meta_ids);
            requestBody.put("event_status", eventStatus); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", eventName);

            String url = esSyncBaseUrl + esSyncUrlUpdates;
            restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        } catch (Exception e) {
            log.error("Failed essync Updates");
        }

        return restResponse;
    }

    public Map<String, Object> essyncConvertCategory(Map<String, Object> params) throws Exception {
        Map<String, Object> restResponse = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("c_id", params.get("id"));
            requestBody.put("c_description", params.get("description"));

            String url = esSyncBaseUrl + esSyncUrlConvertcategory;
            restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        } catch (Exception e) {
            log.error("Failed essync convert category");
        }
        return restResponse;
    }
}
