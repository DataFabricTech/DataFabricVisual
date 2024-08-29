package com.mobigen.ovp.service_manager;

import com.mobigen.ovp.common.openmete_client.AutomationsClient;
import com.mobigen.ovp.common.openmete_client.ContainersClient;
import com.mobigen.ovp.common.openmete_client.DatabaseClient;
import com.mobigen.ovp.common.openmete_client.DatabaseSchemasClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.ServicesClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.service_manager.client.response.RepositoryDescriptionResponse;
import com.mobigen.ovp.service_manager.client.response.ServiceCollectionLogResponse;
import com.mobigen.ovp.service_manager.client.response.ServiceResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceManageService {
    private static final String DATA_BASE = "database";
    private static final String STORAGE = "storage";

    private final ServicesClient servicesClient;
    private final SearchClient searchClient;
    private final AutomationsClient automationsClient;
    private final SearchService searchService;
    private final ContainersClient containersClient;
    private final DatabaseClient databaseClient;
    private final DatabaseSchemasClient databaseSchemasClient;
    private final TablesClient tablesClient;

    /**
     * 서비스 리스트
     *
     * @return
     */
    public List<ServiceResponse> getServices() {
        final int limit = 100;
        List<Services> dataBases = servicesClient.getServices("owner,tags", limit).getData();
        List<Services> storages = servicesClient.getServiceStorage("owner", "non-deleted", limit).getData();
        List<ServiceResponse> serviceResponses = new ArrayList<>();

        for (Services service : dataBases) {
            serviceResponses.add(new ServiceResponse(service, DATA_BASE));
        }
        for (Services service : storages) {
            serviceResponses.add(new ServiceResponse(service, STORAGE));
        }
        return serviceResponses;
    }

    /**
     * 서비스 검색
     *
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
        for (Map<String, ?> data : hits) {
            source.add((Map<String, Object>) data.get("_source"));
        }
        List<ServiceResponse> result = new ArrayList<>();

        for (Map<String, Object> map : source) {
            result.add(new ServiceResponse(map));
        }
        return result;
    }

    /**
     * 서비스 수정
     *
     * @param id
     * @param param
     * @return
     */
    public ServiceResponse patchService(UUID id, List<JsonPatchOperation> param) throws Exception {
        ResponseEntity<Services> result = servicesClient.patchServie(id, param);
        if (result.getStatusCode() == HttpStatus.OK) {
            return new ServiceResponse(result);
        } else {
            throw new Exception();
        }
    }

    /**
     * 서비스 삭제
     *
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    public Object deleteService(UUID id, boolean hardDelete, boolean recursive) throws Exception {
        ResponseEntity<Object> result = servicesClient.deleteService(id, hardDelete, recursive);
        if (result.getStatusCode() == HttpStatus.OK) {
            return servicesClient.deleteService(id, hardDelete, recursive);
        } else {
            throw new Exception();
        }
    }

    /**
     * service : Service - 수집 - 동작 [log] 조회
     *
     * @param id
     * @return
     **/
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
        Map<String, Object> responseMap = new HashMap<>();
        /**
         * connection Test 순서
         * 1. getTestConnectionDefinition  을 통해서 definition ID 를 획득
         * 2. workflows 실행 (workflow 생성 후 workflow id 조회)
         * 3. postWorkflowsTrigger 실행
         * 4. getWorkflows 실행
         * 5. deleteWorkflows 실행
         */

        // 1. getTestConnectionDefinition
        String definitionNm = "Mysql.testConnectionDefinition";
        servicesClient.getTestConnectionDefinition(definitionNm);

        // 2. workflows 실행
        String workflowName = "test-connection-" + params.get("connectionType") + "-" + getRandomUUID();
        Map<String, Object> workflowParams = new HashMap<>();
        workflowParams.put("name", workflowName);
        workflowParams.put("workflowType", "TEST_CONNECTION");
        workflowParams.put("request", params);

        Map<String, Object> workflowInfo = automationsClient.workflows(workflowParams);
        String workflowId = workflowInfo.get("id").toString();
        responseMap.put("workflowId", workflowId);

        try {
            // 3. postWorkflows 실행
            automationsClient.postWorkflowsTrigger(workflowId);

            // 4. workflow 조회
            Map<String, Object> workflowResult = automationsClient.getWorkflows(workflowId);
            Map<String, Object> connectionResponse = (Map<String, Object>) workflowResult.get("response");
            responseMap.put("responseStatus", connectionResponse);
        } catch (FeignException e) {
            throw e;
        } finally {
            // 5. workflow 삭제
            automationsClient.deleteWorkflows(workflowId, true);
        }

        return responseMap;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Object saveDatabase(Map<String, Object> params) {
        return servicesClient.saveDatabase(params);
    }


    public Object saveStorage(Map<String, Object> params) {
        return servicesClient.saveStorage(params);
    }

    /**
     * 저장소관리 > 저장소탭 > Database > '설명'조회
     *
     * @param name
     * @param params
     * @return
     **/
    public Object getRepositoryDescription(String name, MultiValueMap<String, String> params) {
        return servicesClient.getRepositoryDescription(name, params);
    }

    /**
     * 저장소관리 > 저장소탭 > Storage > '설명'조회
     *
     * @param name
     * @param params
     * @return
     **/
    public Object getRepositoryStorageDescription(String name, MultiValueMap<String, String> params) {
        return servicesClient.getRepositoryStorageDescription(name, params);
    }

    /**
     * 저장소관리 > 저장소탭 > Database > '설명'수정
     *
     * @param id
     * @param param
     * @return
     **/
    public Object editRepositoryDescription(String id, List<JsonPatchOperation> param) {
        return servicesClient.editRepositoryDescription(id, param);
    }

    /**
     * 저장소관리 > 저장소탭 > Storage > '설명'수정
     *
     * @param id
     * @param param
     * @return
     **/
    public Object editRepositoryStorageDescription(String id, List<JsonPatchOperation> param) {
        return servicesClient.editRepositoryStorageDescription(id, param);
    }

    public Object saveIngestionPipelines(Map<String, Object> params) {
        /**
         * step0. param 추가 (airflowConfig/startDate, name)
         * - startDate : 금일 자정
         * - name : randomUUID
         * step1. ingestionPipelines
         * step1. deploy
         */

        // step0. startDate
        ZonedDateTime now = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        String formattedDate = now.format(DateTimeFormatter.ISO_INSTANT);

        Map<String, Object> airflowConfig = (Map<String, Object>) params.get("airflowConfig");
        if (airflowConfig == null) {
            airflowConfig = new HashMap<>();
            params.put("airflowConfig", airflowConfig);
        }
        airflowConfig.put("startDate", formattedDate);

        // step0. name
        params.put("name", UUID.randomUUID());

        try {
            // step1.
            Map<String, Object> ipResult = (Map<String, Object>) servicesClient.saveIngestionPipelines(params);
            String id = ipResult.get("id").toString();

            // step2
            servicesClient.ingestionPipelinesDeploy(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Object updateIngestionPipelines(String id, List<JsonPatchOperation> params) {
        /**
         * step1. ingestionPipelines
         * step1. deploy
         */
        try {
            // step1.
            servicesClient.updateIngestionPipelines(id, params);
            // step2
            servicesClient.ingestionPipelinesDeploy(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Object getConnectionInfo(String type, String name, MultiValueMap<String, String> params) {
        Map<String, Object> serviceInfo = DATA_BASE.equals(type) ? servicesClient.getDBConnectionInfo(name, params) : servicesClient.getStorageConnectionInfo(name, params);
        Map<String, Object> connection = (Map<String, Object>) serviceInfo.get("connection");
        return connection.get("config");
    }

    public Object getUpdateConnectionInfo(String type, String id, List<JsonPatchOperation> params) {
        return DATA_BASE.equals(type) ? servicesClient.updateDBConnectionInfo(id, params) : servicesClient.updateStorageConnectionInfo(id, params);
    }

    public Object getDatabaseServiceList(String serviceId) {
        Map<String, Object> params = createParams(serviceId, "owner,tags,usageSummary", "non-deleted");
        Map<String, Object> serviceParam = createServiceParam("owner,usageSummary", "non-deleted", "10000");

        Map<String, Object> clients = databaseClient.getDatabase(params);
        List<Map<String, Object>> serviceList = (List<Map<String, Object>>) clients.get("data");

        return processServiceList(serviceList, serviceParam, true);
    }

    public Object getStorageServiceList(String serviceId) {
        Map<String, Object> params = createParams(serviceId, "owner,tags", "non-deleted");
        params.put("root", true);

        Map<String, Object> serviceParam = createServiceParam("children", "all", null);

        Map<String, Object> clients = containersClient.getContainers(params);
        List<Map<String, Object>> serviceList = (List<Map<String, Object>>) clients.get("data");

        return processServiceList(serviceList, serviceParam, false);
    }

    private Map<String, Object> createParams(String serviceId, String fields, String include) {
        Map<String, Object> params = new HashMap<>();
        params.put("service", serviceId);
        params.put("fields", fields);
        params.put("include", include);
        return params;
    }

    private Map<String, Object> createServiceParam(String fields, String include, String limit) {
        Map<String, Object> serviceParam = new HashMap<>();
        serviceParam.put("fields", fields);
        serviceParam.put("include", include);
        if (limit != null) {
            serviceParam.put("limit", limit);
        }
        return serviceParam;
    }

    private List<Map<String, Object>> processServiceList(List<Map<String, Object>> serviceList, Map<String, Object> serviceParam, boolean isDatabase) {
        return serviceList.stream()
                .map(client -> (String) client.get("fullyQualifiedName"))
                .filter(this::isNotNullOrEmpty)
                .flatMap(fullyQualifiedName -> {
                    if (isDatabase) {
                        // Database Service Logic
                        serviceParam.put("database", fullyQualifiedName);
                        Map<String, Object> result = databaseSchemasClient.getDatabaseSchemas(serviceParam);
                        List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data");

                        return dataList.stream().flatMap(dataItem -> {
                            String nestedFullyQualifiedName = (String) dataItem.get("fullyQualifiedName");
                            Map<String, String> tableParam = new HashMap<>();
                            tableParam.put("databaseSchema", nestedFullyQualifiedName);
                            tableParam.put("include", "non-deleted");
                            Map<String, Object> tableInfo = tablesClient.getTablesInfo(tableParam);

                            List<Map<String, Object>> tableDataList = (List<Map<String, Object>>) tableInfo.get("data");
                            return tableDataList.stream();
                        });
                    } else {
                        // Storage Service Logic
                        Map<String, Object> result = containersClient.getContainersName(fullyQualifiedName, serviceParam);
                        return ((List<Map<String, Object>>) result.get("children")).stream();
                    }
                })
                .map(this::processEntry)
                .collect(Collectors.toList());
    }


    private boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    private Map<String, Object> processEntry(Map<String, Object> entry) {
        Map<String, Object> newEntry = new HashMap<>();
        newEntry.put("fqn", entry.get("fullyQualifiedName"));
        newEntry.put("name", entry.get("name"));
        newEntry.put("id", entry.get("id"));
        newEntry.put("type", determineType(entry.get("serviceType")));
        newEntry.put("desc", entry.get("description"));
        newEntry.put("owner", extractOwner(entry));
        return newEntry;
    }

    private String determineType(Object serviceType) {
        return serviceType != null && serviceType.toString().equalsIgnoreCase("trino") ? "model" : "table";
    }

    private String extractOwner(Map<String, Object> entry) {
        if (entry.containsKey("owner")) {
            Map<String, String> ownerObj = (Map<String, String>) entry.get("owner");
            return ownerObj.get("displayName");
        }
        return null;
    }

    public Map<String, Object> getPipelinesData(String id, Map<String, Object> params) {
        return servicesClient.getPipelinesData(id, params);
    }
}
