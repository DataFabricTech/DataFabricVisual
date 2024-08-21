package com.mobigen.ovp.service_manager;

import com.mobigen.ovp.common.openmete_client.AutomationsClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.ServicesClient;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.service_manager.client.response.ServiceCollectionLogResponse;
import com.mobigen.ovp.service_manager.client.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceManageService {

    private final ServicesClient servicesClient;
    private final SearchClient searchClient;
    private final AutomationsClient automationsClient;
    private final SearchService searchService;

    /**
     * 서비스 리스트
     * @return
     */
    public List<ServiceResponse> getServices() {
        String fields = "owner,tags";
        int limit = 100;
        List<Services> result = servicesClient.getServices(fields, limit).getData();
        List<ServiceResponse> serviceResponses = new ArrayList<>();
        for(Services service : result) {
            serviceResponses.add(new ServiceResponse(service));
        }
        return serviceResponses;
    }

    /**
     * 서비스 검색
     * @param keyword
     * @param from
     * @return
     * @throws Exception
     */
    public Object searchServices(String keyword, String from) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", keyword);
        params.add("from", from);
        params.add("index", "database_service_search_index");
        List<Map<String, ?>> hits = (List<Map<String, ?>>) ((Map<?, ?>) searchClient.getSearchList(params).get("hits")).get("hits");
        List<Map<String, Object>> source = new ArrayList<>();
        for(Map<String, ?> data : hits) {
            source.add((Map<String, Object>) data.get("_source"));
        }
        List<ServiceResponse> result = new ArrayList<>();

        for(Map<String, Object> map : source) {
            result.add(new ServiceResponse(map));
        }
        return result;
    }

    /**
     * 서비스 수정
     * @param id
     * @param param
     * @return
     */
    public ServiceResponse patchService(UUID id, List<JsonPatchOperation> param) throws Exception {
        ResponseEntity<Services> result = servicesClient.patchServie(id, param);
        if(result.getStatusCode() == HttpStatus.OK) {
            return new ServiceResponse(result);
        } else {
            throw new Exception();
        }
    }

    /**
     * 서비스 삭제
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    public Object deleteService(UUID id, boolean hardDelete, boolean recursive) throws Exception {
        ResponseEntity<Object> result = servicesClient.deleteService(id, hardDelete, recursive);
        if(result.getStatusCode() == HttpStatus.OK) {
            return servicesClient.deleteService(id, hardDelete, recursive);
        } else {
            throw new Exception();
        }
    }

    /**
     * service : Service - 수집 - 동작 [log] 조회
     * @param id
     * @return
     * **/
    public Object getServiceCollectionLog(String id) {
        return new ServiceCollectionLogResponse(servicesClient.getServiceCollectionLog(id));
    }

    public Boolean checkDuplicatedNm(MultiValueMap<String, String> params) throws Exception {
        String index = params.getFirst("index");

        String newIndex = index.toLowerCase().equals("minio") ? "container" : "table";
        params.set("index", newIndex);

        Object result = searchClient.getSearchList(params);
        Map<String, Object> response = searchService.convertToMap((Map<String, Object>) result);
        return Integer.parseInt(response.get("totalCount").toString()) > 0;
    }

    public Object connectionTest(Map<String, Object> params) {
        /**
         * connection Test 순서
         * 1. getTestConnectionDefinition  을 통해서 definition ID 를 획득
         * 2. workflows 실행
         * 3. postWorkflows 실행
         * 4. getWorkflows 실행
         * 5. deleteWorkflows 실행
         */

        // 1. getTestConnectionDefinition
        String definitionNm = "Mysql.testConnectionDefinition";
        Map<String, Object> definition = servicesClient.getTestConnectionDefinition(definitionNm);
        String definitionId = definition.get("id").toString();

        // 2. workflows 실행
        Map<String, Object> workflowParams = new HashMap<>();
        workflowParams.put("name", "test-connection-" + params.get("connectionType") + "-" + getRandomUUID());
        workflowParams.put("workflowType", "TEST_CONNECTION");
        workflowParams.put("request", params);

        Map<String, Object> workflowInfo = automationsClient.workflows(workflowParams);
        String workflowId = workflowInfo.get("id").toString();

        try {
            // 3. postWorkflows 실행
            automationsClient.postWorkflowsTrigger(workflowId);

            // 4. workflow 조회
            automationsClient.getWorkflows(workflowId);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // 5. deleteWorkflows 실행
            automationsClient.deleteWorkflowsTrigger(workflowId);
        }

        return workflowId;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
