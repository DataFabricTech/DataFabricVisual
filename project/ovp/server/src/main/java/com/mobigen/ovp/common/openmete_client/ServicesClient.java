package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Ingestion;
import com.mobigen.ovp.common.openmete_client.dto.Log;
import com.mobigen.ovp.common.openmete_client.dto.Base;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
     * @param fields
     * @return
     */
    @GetMapping("/databaseServices")
    Base<Services> getServices(@RequestParam String fields, @RequestParam int limit);

    /**
     * 서비스 조회 - 데이터 베이스
     * @param name
     * @param fields
     * @param include
     * @return
     */
    @GetMapping("/databaseServices/name/{name}")
    Services getServiceDataBase(@PathVariable String name, @RequestParam String fields, @RequestParam String include);

    /**
     * 서비스 조회 - 스토리지
     * @param name
     * @param fields
     * @param include
     * @return
     */
    @GetMapping("/storageServices/name/{name}")
    Services getServiceStorage(@PathVariable String name, @RequestParam String fields, @RequestParam String include);

    /**
     * 서비스 리스트 - 스토리지
     *
     * @return
     */
    @GetMapping("/storageServices")
    Base<Services> getServiceStorage(@RequestParam String fields, @RequestParam String include,
                                     @RequestParam int limit);

    /**
     * 서비스 수정 - 데이터 베이스
     * @param id
     * @param param
     **/
    @PatchMapping(value = "/databaseServices/{id}", consumes = "application/json-patch+json")
    ResponseEntity<Services> patchServieDataBase(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 서비스 수정 - 스토리지
     * @param id
     * @param param
     */
    @PatchMapping(value = "/storageServices/{id}", consumes = "application/json-patch+json")
    ResponseEntity<Services> patchServieStorage(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);


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
     * @param id
     * @return
     * **/
    @GetMapping("/ingestionPipelines/logs/{id}/last")
    Log getServiceCollectionLog(@PathVariable("id") String id);

    /**
     * 수집 리스트
     * @return
     */
    @GetMapping("/ingestionPipelines")
    Base<Ingestion> getIngestionList(@RequestParam MultiValueMap<String, String> params);

    /**
     * 수집 상태
     * @param name
     * @param startTs
     * @param endTs
     * @return
     */
    @GetMapping("/ingestionPipelines/{name}/pipelineStatus")
    Base<Object> getIngestionStatus(@PathVariable String name, @RequestParam Long startTs, @RequestParam Long endTs);

    /**
     * 수집 RUN
     * @param id
     * @return
     */
    @PostMapping("/ingestionPipelines/trigger/{id}")
    ResponseEntity<Object> triggerIngestion(@PathVariable UUID id);

    /**
     * 수집 동기화
     * @param id
     * @return
     */
    @PostMapping("/ingestionPipelines/deploy/{id}")
    ResponseEntity<Object> deployIngestion(@PathVariable UUID id);

    /**
     * 수집 삭제
     * @param id
     * @param hardDelete
     * @return
     */
    @DeleteMapping("/ingestionPipelines/{id}")
    ResponseEntity<Object> deleteIngestion(@PathVariable UUID id, @RequestParam boolean hardDelete);

    /**
     * 수집 KILL
     * @param id
     * @return
     */
    @PostMapping("/ingestionPipelines/kill/{id}")
    ResponseEntity<Object> killIngestion(@PathVariable UUID id);

    @GetMapping("/testConnectionDefinitions/name/{definitionNm}")
    Map<String, Object> getTestConnectionDefinition(@PathVariable String definitionNm);

    @PostMapping("/databaseServices")
    Map<String, Object> saveDatabase(@RequestBody Map<String, Object> params);

    @PostMapping("/storageServices")
    Map<String, Object> saveStorage(@RequestBody Map<String, Object> params);

    @PostMapping("/ingestionPipelines")
    Object saveIngestionPipelines(@RequestBody Map<String, Object> params);

    @PatchMapping(value="/ingestionPipelines/{id}", consumes = "application/json-patch+json")
    Object updateIngestionPipelines(@PathVariable String id, @RequestBody List<JsonPatchOperation> params);

    @PostMapping("/ingestionPipelines/deploy/{id}")
    Object ingestionPipelinesDeploy(@PathVariable String id);

    @GetMapping("/ingestionPipelines/name/{id}")
    Map<String, Object> getPipelinesData(@PathVariable String id, @RequestParam Map<String, Object> params);


    @GetMapping("/databaseServices/name/{name}")
    Map<String, Object> getDBConnectionInfo(@PathVariable String name,
                                            @RequestParam MultiValueMap<String, String> params);

    @GetMapping("/storageServices/name/{name}")
    Map<String, Object> getStorageConnectionInfo(@PathVariable String name,
                                                 @RequestParam MultiValueMap<String, String> params);

    @PatchMapping(value = "/databaseServices/{id}", consumes = "application/json-patch+json")
    Map<String, Object> updateDBConnectionInfo(@PathVariable String id, @RequestBody List<JsonPatchOperation> params);

    @PatchMapping(value = "/storageServices/{id}", consumes = "application/json-patch+json")
    Map<String, Object> updateStorageConnectionInfo(@PathVariable String id,
                                                    @RequestBody List<JsonPatchOperation> params);

}
