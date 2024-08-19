package com.mobigen.ovp.admin.service.client;

import com.mobigen.ovp.admin.service.client.response.ServiceBaseResponse;
import com.mobigen.ovp.admin.service.client.response.ServiceResponse;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ServiceClient", url="http://192.168.105.26:8585/api/v1")
public interface ServiceClient {

    /**
     * 서비스 리스트
     * @param fields
     * @return
     */
    @GetMapping("/services/databaseServices")
    ServiceBaseResponse<ServiceResponse> getServices(@RequestParam String fields, @RequestParam int limit);

    /**
     * 서비스 수정
     * @param id
     * @param param
     * @return
     */
    @PatchMapping(value = "/services/databaseServices/{id}", consumes = "application/json-patch+json")
    ServiceResponse patchServie(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 서비스 삭제
     *
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    @DeleteMapping("/services/databaseServices/{id}")
    Object deleteService(@PathVariable UUID id, @RequestParam boolean hardDelete, @RequestParam boolean recursive);

}
