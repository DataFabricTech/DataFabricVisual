package com.mobigen.ovp.admin.service;

import com.mobigen.ovp.admin.service.client.ServiceClient;
import com.mobigen.ovp.admin.service.client.response.ServiceResponse;
import com.mobigen.ovp.admin.service.client.response.ServiceEntity;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceClient serviceClient;
    private final SearchClient searchClient;

    /**
     * 서비스 리스트
     * @return
     */
    public List<ServiceEntity> getServices() {
        String fields = "owner,tags";
        int limit = 100;
        List<ServiceResponse> result = serviceClient.getServices(fields, limit).getData();
        List<ServiceEntity> serviceResponses = new ArrayList<>();
        for(ServiceResponse service : result) {
            serviceResponses.add(new ServiceEntity(service));
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
        List<ServiceEntity> result = new ArrayList<>();

        for(Map<String, Object> map : source) {
            result.add(new ServiceEntity(map));
        }
        return result;
    }

    /**
     * 서비스 수정
     * @param id
     * @param param
     * @return
     */
    public ServiceEntity patchService(UUID id, List<JsonPatchOperation> param) throws Exception {
        ResponseEntity<ServiceResponse> result = serviceClient.patchServie(id, param);
        if(result.getStatusCode() == HttpStatus.OK) {
            return new ServiceEntity(serviceClient.patchServie(id, param));
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
        ResponseEntity<Object> result = serviceClient.deleteService(id, hardDelete, recursive);
        if(result.getStatusCode() == HttpStatus.OK) {
            return serviceClient.deleteService(id, hardDelete, recursive);
        } else {
            throw new Exception();
        }
    }
}
