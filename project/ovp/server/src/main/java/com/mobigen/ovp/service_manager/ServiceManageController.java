package com.mobigen.ovp.service_manager;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/service-manage")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceManageController {

    private final ServiceManageService serviceManageService;

    /**
     * 서비스 리스트
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getServices() {
        return serviceManageService.getServices();
    }

    /**
     * 서비스 검색 리스트
     * @param search
     * @param from
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/list/search")
    public Object searchServices(@RequestParam String search, @RequestParam String from) throws Exception {
        return serviceManageService.searchServices(search, from);
    }

    /**
     * 서비스 수정
     * @param id
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public Object patchService(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) throws Exception {
        return serviceManageService.patchService(id, param);
    }

    /**
     * 서비스 삭제
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    @ResponseJsonResult
    @DeleteMapping("/{id}")
    public Object deleteService(@PathVariable UUID id, @RequestParam boolean hardDelete, @RequestParam boolean recursive) throws Exception {
        return serviceManageService.deleteService(id, hardDelete, recursive);
    }

    /**
     * service : Service - 수집 - 동작 [log] 조회
     * @param id
     * @return
     * **/
    @ResponseJsonResult
    @GetMapping("/collection/log/{id}")
    public Object getServiceCollectionLog(@PathVariable String id) {
        return serviceManageService.getServiceCollectionLog(id);
    }

    /**
     * 수집 탭 리스트
     * @param service
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/ingestion/list")
    public Object getIngestionList(@RequestParam String service) {
        return serviceManageService.getIngestionList(service);
    }

    @ResponseJsonResult
    @GetMapping("/ingestion/status/{name}")
    public Object getIngestionStatus(@PathVariable String name, Long startTs, Long endTs) {
        return serviceManageService.getIngestionStatus(name, startTs, endTs);
    }

    /**
     * 수집 RUN
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/ingestion/trigger/{id}")
    public Object triggerIngestion(@PathVariable UUID id) throws Exception {
        return serviceManageService.triggerIngestion(id);
    }

    /**
     * 수집 동기화
     * @param id
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/ingestion/deploy/{id}")
    public Object deployIngestion(@PathVariable UUID id) throws Exception {
        return serviceManageService.deployIngestion(id);
    }

    /**
     * 수집 삭제
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @DeleteMapping("/ingestion/{id}")
    public Object deleteIngestion(@PathVariable UUID id) throws Exception {
        return serviceManageService.deleteIngestion(id);
    }

    /**
     * 수집 KILL
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/ingestion/kill/{id}")
    public Object killIngestion(@PathVariable UUID id) throws Exception {
        return serviceManageService.killIngestion(id);
    }
}
