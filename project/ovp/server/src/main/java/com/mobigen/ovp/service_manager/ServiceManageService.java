package com.mobigen.ovp.service_manager;

import com.mobigen.ovp.common.openmete_client.ServicesClient;
import com.mobigen.ovp.common.openmete_client.dto.Ingestion;
import com.mobigen.ovp.service_manager.client.response.IngestionResponse;
import com.mobigen.ovp.service_manager.client.response.ServiceCollectionLogResponse;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import com.mobigen.ovp.service_manager.client.response.ServiceResponse;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceManageService {

    private final ServicesClient servicesClient;
    private final SearchClient searchClient;

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

    /**
     * 수집 리스트
     * @param service
     * @return
     */
    public List<IngestionResponse> getIngestionList(String service) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "owner,pipelineStatuses");
        params.add("service", service);
        params.add("pipelineType", "metadata,usage,lineage,profiler,dbt");
        params.add("serviceType", "databaseService");

        List<Ingestion> ingestion = servicesClient.getIngestionList(params).getData();
        return ingestion.stream().map(IngestionResponse::new).toList();
    }

    /**
     * 수집 상태
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
     * @param id
     * @return
     */
    public Object triggerIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.triggerIngestion(id);
        if(result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 동기화
     * @param id
     * @return
     */
    public Object deployIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.deployIngestion(id);
        if(result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 삭제
     * @param id
     * @return
     */
    public Object deleteIngestion(UUID id) throws Exception {
        final boolean hardDelete = true;
        ResponseEntity<Object> result = servicesClient.deleteIngestion(id, hardDelete);
        if(result.getStatusCode() == HttpStatus.OK) {
            return result;
        } else {
            throw new Exception();
        }
    }

    /**
     * 수집 KILL
     * @param id
     * @return
     */
    public Object killIngestion(UUID id) throws Exception {
        ResponseEntity<Object> result = servicesClient.killIngestion(id);
        if(result.getStatusCode() == HttpStatus.OK) {
            return result;
        } else {
            throw new Exception();
        }
    }
}
