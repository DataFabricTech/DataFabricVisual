package com.mobigen.ovp.service_manager;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
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
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/service-manage")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceManageController {

    private final ServiceManageService service;

    /**
     * 서비스 리스트
     *
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getServices() {
        return service.getServices();
    }

    /**
     * 서비스 검색 리스트
     *
     * @param search
     * @param from
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/list/search")
    public Object searchServices(@RequestParam String search, @RequestParam String from) throws Exception {
        return service.searchServices(search, from);
    }

    /**
     * 서비스 수정
     *
     * @param id
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public Object patchService(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) throws Exception {
        return service.patchService(id, param);
    }

    /**
     * 서비스 삭제
     *
     * @param id
     * @param hardDelete
     * @param recursive
     * @return
     */
    @ResponseJsonResult
    @DeleteMapping("/{id}")
    public Object deleteService(@PathVariable UUID id, @RequestParam boolean hardDelete,
                                @RequestParam boolean recursive) throws Exception {
        return service.deleteService(id, hardDelete, recursive);
    }

    /**
     * service : Service - 수집 - 동작 [log] 조회
     *
     * @param id
     * @return
     **/
    @ResponseJsonResult
    @GetMapping("/collection/log/{id}")
    public Object getServiceCollectionLog(@PathVariable String id) {
        return service.getServiceCollectionLog(id);
    }


    /**
     * 서비스 명 중복인지 체크
     *
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/isDuplicatedNm")
    public Object isDuplicatedNm(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return service.checkDuplicatedNm(params);
    }

    /**
     * connection test
     *
     * @return
     */
    @ResponseJsonResult
    @PostMapping(value = "/connectionTest")
    public Object connectionTest(@RequestBody Map<String, Object> params) throws Exception {
        return service.connectionTest(params);
    }

    @ResponseJsonResult
    @PostMapping(value = "/save/{type}")
    public Object saveService(@PathVariable String type, @RequestBody Map<String, Object> params) {
        return type.toLowerCase().equals("database") ? service.saveDatabase(params) : service.saveStorage(params);
    }

    @ResponseJsonResult
    @PostMapping(value = "/pipelines")
    public Object saveIngestionPipelines(@RequestBody Map<String, Object> params) {
        return service.saveIngestionPipelines(params);
    }

    @ResponseJsonResult
    @PatchMapping(value = "/pipelines/{id}")
    public Object updateIngestionPipelines(@PathVariable String id, @RequestBody List<JsonPatchOperation> params) {
        return service.updateIngestionPipelines(id, params);
    }

    @ResponseJsonResult
    @GetMapping("/pipelines/{id}")
    public Object getPipelinesData(@PathVariable String id, @RequestParam Map<String, Object> params) {
        return service.getPipelinesData(id, params);
    }

    @ResponseJsonResult
    @GetMapping("/database-services/{id}")
    public Object getDatabaseServiceList(@PathVariable String id) {
        return service.getDatabaseServiceList(id);
    }

    @ResponseJsonResult
    @GetMapping("/storage-services/{id}")
    public Object getStorageServiceList(@PathVariable String id) {
        return service.getStorageServiceList(id);
    }


    /**
     * 연결정보 조회
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping(value = "/{type}/{name}")
    public Object getConnectionInfo(@PathVariable String type, @PathVariable String name,
                                    @RequestParam MultiValueMap<String, String> params) throws Exception {
        return service.getConnectionInfo(type, name, params);
    }

    /**
     * 연결정보 수정
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PatchMapping(value = "/update/{type}/{id}", consumes = "application/json-patch+json")
    public Object updateConnectionInfo(@PathVariable String type, @PathVariable String id,
                                       @RequestBody List<JsonPatchOperation> params) {
        return service.getUpdateConnectionInfo(type, id, params);
    }

    /**
     * 저장소관리 > 저장소탭 > Database > '설명'조회
     *
     * @param name
     * @return
     **/
    @ResponseJsonResult
    @GetMapping("/repository/description/{name}")
    public Object getRepositoryDescription(@PathVariable String name, @RequestParam MultiValueMap<String, String> params) {
        return service.getRepositoryDescription(name, params);
    }

    /**
     * 저장소관리 > 저장소탭 > Storage > '설명'조회
     *
     * @param name
     * @return
     **/
    @ResponseJsonResult
    @GetMapping("/repository/storage/description/{name}")
    public Object getRepositoryStorageDescription(@PathVariable String name, @RequestParam MultiValueMap<String, String> params) {
        return service.getRepositoryStorageDescription(name, params);
    }


    /**
     * 저장소관리 > 저장소탭 > Database > '설명'수정
     *
     * @param id
     * @param param
     * @return
     **/
    @ResponseJsonResult
    @PatchMapping("/repository/description/{id}")
    public Object editRepositoryDescription(@PathVariable String id, @RequestBody List<JsonPatchOperation> param) {
        return service.editRepositoryDescription(id, param);
    }

    /**
     * 저장소관리 > 저장소탭 > Storage > '설명'수정
     *
     * @param id
     * @param param
     * @return
     **/
    @ResponseJsonResult
    @PatchMapping( "/repository/storage/description/{id}")
    public Object editRepositoryStorageDescription(@PathVariable String id, @RequestBody List<JsonPatchOperation> param) {
        return service.editRepositoryStorageDescription(id, param);
    }


//    @ResponseJsonResult
//    @PostMapping(value = "/pipelines")
//    public Object saveIngestionPipelines(@RequestBody Map<String, Object> params) {
//        return service.saveIngestionPipelines(params);
//    }
//
//    @ResponseJsonResult
//    @PatchMapping(value = "/pipelines/{id}")
//    public Object updateIngestionPipelines(@PathVariable String id, List<JsonPatchOperation> params) {
//        return service.updateIngestionPipelines(id, params);
//    }
}
