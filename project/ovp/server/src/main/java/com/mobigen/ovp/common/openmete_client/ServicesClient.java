package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Base;
import com.mobigen.ovp.common.openmete_client.dto.Log;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "ServiceClient", url = "${properties.ovp.open-metadata-url}/services")
public interface ServicesClient {

    /**
     * 서비스 리스트
     *
     * @param fields
     * @return
     */
    @GetMapping("/databaseServices")
    Base<Services> getServices(@RequestParam String fields, @RequestParam int limit);

    /**
     * 서비스 수정
     *
     * @param id
     * @param param
     * @return
     */
    @PatchMapping(value = "/databaseServices/{id}", consumes = "application/json-patch+json")
    ResponseEntity<Services> patchServie(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 서비스 삭제
     *
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    @DeleteMapping("/databaseServices/{id}")
    ResponseEntity<Object> deleteService(@PathVariable UUID id, @RequestParam boolean hardDelete, @RequestParam boolean recursive);

    /**
     * service : Service - 수집 - 동작 [log] 조회
     *
     * @param id
     * @return
     **/
    @GetMapping("/ingestionPipelines/logs/{id}/last")
    Log getServiceCollectionLog(@PathVariable("id") String id);

    @GetMapping("/testConnectionDefinitions/name/{definitionNm}")
    Map<String, Object> getTestConnectionDefinition(@PathVariable String definitionNm);

    @PostMapping("/databaseServices")
    Map<String, Object> saveDatabase(@RequestBody Map<String, Object> params);

    @PostMapping("/storageServices")
    Map<String, Object> saveStorage(@RequestBody Map<String, Object> params);

}
