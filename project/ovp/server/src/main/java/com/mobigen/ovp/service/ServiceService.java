package com.mobigen.ovp.service;

import com.mobigen.ovp.common.openmete_client.AutomationsClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.ServicesClient;
import com.mobigen.ovp.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {

    private final SearchService searchService;
    private final SearchClient searchClient;
    private final ServicesClient servicesClient;
    private final AutomationsClient automationsClient;

    public Boolean checkDuplicatedNm(MultiValueMap<String, String> params) throws Exception {
        String index = params.getFirst("index");

        String newIndex = index.toLowerCase().equals("minio") ? "container" : "table";
        params.set("index", newIndex);

        Object result = searchClient.getSearchList(params);
        Map<String, Object> response = searchService.convertToMap((Map<String, Object>) result);
        return Integer.parseInt(response.get("totalCount").toString()) > 0;
    }

    public Object connectionTest(Map<String, Object> params) {
        System.out.println(params);

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
        System.out.println(definitionId);

        try {
            // 2. workflows 실행
            String serviceId = "mySql";

            Map<String, Object> workflowParams = new HashMap<>();
            workflowParams.put("name", "test-connection-" + serviceId + "-" + getRandomUUID());
            workflowParams.put("workflowType", "TEST_CONNECTION");
            workflowParams.put("request", params.get("request"));

            Map<String, Object> res = automationsClient.workflows(workflowParams);
            System.out.println(res);

            // 3. postWorkflows 실행
            Map<String, Object> res1 = automationsClient.postWorkflows(definitionId);
            System.out.println(res1);

            // 4. getWorkflows 실행
            Map<String, Object> res2 = automationsClient.postWorkflows(definitionId);
            System.out.println(res2);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // 5. deleteWorkflows 실행
            automationsClient.deleteWorkflows(definitionId);
        }

        return null;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
