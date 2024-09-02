package com.mobigen.ovp.service_manager;

import com.mobigen.ovp.common.openmete_client.AutomationsClient;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.ContainersClient;
import com.mobigen.ovp.common.openmete_client.DatabaseClient;
import com.mobigen.ovp.common.openmete_client.DatabaseSchemasClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.ServicesClient;
import com.mobigen.ovp.common.openmete_client.dto.Ingestion;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.glossary.client.GlossaryClient;
import com.mobigen.ovp.glossary.client.dto.TermDto;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailTagDto;
import com.mobigen.ovp.service_manager.client.response.IngestionResponse;
import com.mobigen.ovp.service_manager.client.response.ServiceCollectionLogResponse;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.common.openmete_client.TablesClient;
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
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceManageService {
    private static final String DATA_BASE = "database";
    private static final String STORAGE = "storage";
    private static final String FIELDS = "owner,tags,dataProducts,domain";
    private static final String INCLUDE = "all";

    private final ServicesClient servicesClient;
    private final SearchClient searchClient;
    private final AutomationsClient automationsClient;
    private final SearchService searchService;
    private final ContainersClient containersClient;
    private final DatabaseClient databaseClient;
    private final DatabaseSchemasClient databaseSchemasClient;
    private final ClassificationClient classificationClient;
    private final GlossaryClient glossaryClient;
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
     * 서비스 조회
     *
     * @param type
     * @param name
     * @return
     */
    public Services getService(String type, String name) throws Exception {
        switch (type) {
            case DATA_BASE:
                return servicesClient.getServiceDataBase(name, FIELDS, INCLUDE);
            case STORAGE:
                return servicesClient.getServiceStorage(name, FIELDS, INCLUDE);
            default:
                throw new Exception();
        }
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
        List<ServiceResponse> result = new ArrayList<>();
        List<ServiceResponse> databaseServices = fetchSearchServices(keyword, from, "database_service_search_index", DATA_BASE);
        result.addAll(databaseServices);

        List<ServiceResponse> storageServices = fetchSearchServices(keyword, from, "storage_service_search_index", STORAGE);
        result.addAll(storageServices);

        return result;
    }

    private List<ServiceResponse> fetchSearchServices(String keyword, String from, String index, String type) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", keyword);
        params.add("from", from);
        params.add("index", index);

        List<Map<String, ?>> hits = (List<Map<String, ?>>) ((Map<?, ?>) searchClient.getSearchList(params).get("hits")).get("hits");

        return hits.stream()
                .map(hit -> (Map<String, Object>) hit.get("_source"))
                .map(data -> new ServiceResponse(data, type))
                .collect(Collectors.toList());
    }

    /**
     * 서비스 수정 - DataBase
     *
     * @param id
     * @param param
     * @return
     */
    public ServiceResponse patchServiceDataBase(UUID id, List<JsonPatchOperation> param) throws Exception {
        ResponseEntity<Services> result = servicesClient.patchServieDataBase(id, param);
        if (result.getStatusCode() == HttpStatus.OK) {
            return new ServiceResponse(result, DATA_BASE);
        } else {
            throw new Exception();
        }
    }

    /**
     * 서비스 수정 - Storage
     *
     * @param id
     * @param param
     * @return
     */
    public ServiceResponse patchServiceStorage(UUID id, List<JsonPatchOperation> param) throws Exception {
        ResponseEntity<Services> result = servicesClient.patchServieStorage(id, param);
        if (result.getStatusCode() == HttpStatus.OK) {
            return new ServiceResponse(result, STORAGE);
        } else {
            throw new Exception();
        }
    }

    public Object patchTagService(UUID id, String name, String type, String target, List<Map<String, Object>> body) throws Exception {
        Services service = getService(type, name);

        List<Tag> tags = service.getTags();
        List<JsonPatchOperation> removeBody = makeRemoveBody(tags.size());
        List<JsonPatchOperation> addBody = makeAddBody(tags, body, target);

        ServiceResponse result;
        switch (type) {
            case DATA_BASE:
                patchServiceDataBase(id, removeBody);
                result = patchServiceDataBase(id, addBody);
                break;
            case STORAGE:
                patchServiceStorage(id, removeBody);
                result = patchServiceStorage(id, addBody);
                break;
            default:
                throw new Exception();
        }
        return result;
    }

    private List<JsonPatchOperation> makeRemoveBody(int tagLength) {
        List<JsonPatchOperation> removedBody = new ArrayList<>();
        for (int i = (tagLength - 1); i >= 0; i--) {
            JsonPatchOperation jsonPatchOperation = new JsonPatchOperation();
            jsonPatchOperation.setOp("remove");
            jsonPatchOperation.setPath(new StringBuffer("/tags/").append(i).toString());
            removedBody.add(jsonPatchOperation);
        }

        return removedBody;
    }

    private List<JsonPatchOperation> makeAddBody(List<Tag> tags, List<Map<String, Object>> body, String target) {
        List<JsonPatchOperation> addedBody = new ArrayList<>();
        List<DataModelDetailTagDto> bodyList = new ArrayList<>();
        List<DataModelDetailTagDto> tagList = new ArrayList<>();

        // 데이터 모델의 태크 목록에서 카테고리, 태그, 용어를 분리해서 저장.
        List<DataModelDetailTagDto> glossaryList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains("ovp_category") && "Glossary".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());

        List<DataModelDetailTagDto> classificationList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains("ovp_category") && "Classification".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());


        // { op, path, value }
        // 클라이언트로 받은 변경해야 할 데이터(태그, 카테고라, 용어)를 각각 단일 조회 후에 value 를 셋팅한다.
        if ("Classification".equals(target)) {
            for (Map<String, Object> item : body) {
                String key = "id";

                Map<String, Object> tempTag = classificationClient.getTag(item.get(key).toString());

                DataModelDetailTagDto tag = new DataModelDetailTagDto();
                tag.setName(tempTag.get("name").toString());
                tag.setDisplayName(tempTag.get("displayName").toString());
                tag.setDescription(tempTag.get("description").toString());
                tag.setTagFQN(tempTag.get("fullyQualifiedName").toString());
                tag.setSource(target);
                tag.setLabelType("Manual");
                tag.setStyle((Map<String, Object>) tempTag.get("style"));

                tagList.add(tag);
            }
        } else if ("Glossary".equals(target)) {
            for (Map<String, Object> item : body) {
                TermDto tempTerm = glossaryClient.getGlossaryTermsById(item.get("id").toString(), "all");

                DataModelDetailTagDto tag = new DataModelDetailTagDto();
                tag.setName(tempTerm.getName());
                tag.setDisplayName(tempTerm.getDisplayName());
                tag.setDescription(tempTerm.getDescription());
                tag.setTagFQN(tempTerm.getFullyQualifiedName());
                tag.setSource(target);
                tag.setLabelType("Manual");
                tag.setStyle(tempTerm.getStyle());

                tagList.add(tag);
            }
        }

        // value가 셋팅이 완료 되면 모든 데이터(카테고리, 태그, 용어)를 하나로 합친다.
        if ("Classification".equals(target)) {
            bodyList = Stream.concat(glossaryList.stream(), tagList.stream()).collect(Collectors.toList());
        } else if ("Glossary".equals(target)) {
            bodyList = Stream.concat(tagList.stream(), classificationList.stream()).collect(Collectors.toList());
        }

        // 모두 합쳐진 value를 가지고 patch할 body 데이터를 만든다.
        int bodySize = bodyList.size();
        for (int i = 0; i < bodySize; i++) {
            JsonPatchOperation jsonPatchOperation = new JsonPatchOperation();
            jsonPatchOperation.setOp("add");
            jsonPatchOperation.setPath(new StringBuffer("/tags/").append(i).toString());
            jsonPatchOperation.setValue(bodyList.get(i));
            addedBody.add(jsonPatchOperation);
        }

        return addedBody;
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

    /**
     * 수집 리스트
     *
     * @param service
     * @return
     */
    public List<IngestionResponse> getIngestionList(String service, String serviceType) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "owner,pipelineStatuses");
        params.add("service", service);
        params.add("serviceType", serviceType);
        params.add("limit", "1000");

        if(serviceType.equals("databaseService")) {
            params.add("pipelineType", "metadata,usage,lineage,profiler,dbt");
        } else {
            params.add("pipelineType", "metadata,usage,lineage,profiler,storageProfiler,dbt");
        }

        List<Ingestion> ingestion = servicesClient.getIngestionList(params).getData();
        return ingestion.stream().map(IngestionResponse::new).toList();
    }

    /**
     * 수집 상태
     *
     * @param name
     * @param startTs
     * @param endTs
     * @return
     */
    public Object getIngestionStatus(String name, Long startTs, Long endTs) {
        return servicesClient.getIngestionStatus(name, startTs, endTs).getData();
    }

    /**
     * 수집 RUN
     *
     * @param id
     * @return
     */
    public Object triggerIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.triggerIngestion(id);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 동기화
     *
     * @param id
     * @return
     */
    public Object deployIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.deployIngestion(id);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 삭제
     *
     * @param id
     * @return
     */
    public Object deleteIngestion(UUID id) throws Exception {
        final boolean hardDelete = true;
        ResponseEntity<Object> result = servicesClient.deleteIngestion(id, hardDelete);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result;
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 KILL
     *
     * @param id
     * @return
     */
    public Object killIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.killIngestion(id);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result;
        } else {
            throw new Exception();
        }
    }

    public Boolean checkDuplicatedNm(MultiValueMap<String, String> params) throws Exception {
        String index = params.getFirst("index");

        String newIndex = index.toLowerCase().equals("minio") ? "storage_service_search_index" : "database_service_search_index";
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

    private List<Map<String, Object>> processServiceList(List<Map<String, Object>> serviceList,
                                                         Map<String, Object> serviceParam, boolean isDatabase) {
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
