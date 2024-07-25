package com.mobigen.ovp.classification.client;

import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ClassificationClient", url = "http://192.168.105.26:8585/api/v1")
public interface ClassificationClient {

    /**
     * 분류 리스트
     * @return
     */
    @GetMapping("/classifications")
    ClassificationData getClassifications();

//    /**
//     * 분류 수정
//     *
//     * @param param
//     * @return
//     * @Param id
//     */
//    @PatchMapping(value = "/classifications/{id}", consumes = "application/json-patch+json")
//    Object editClassification(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);
//
//    /**
//     * 분류 삭제
//     *
//     * @param id
//     * @param recursive
//     * @param hardDelete
//     * @rerurn
//     */
//    @DeleteMapping("/classifications/{id}")
//    void deleteClassification(@PathVariable UUID id, @RequestParam(defaultValue = "true") Boolean recursive, @RequestParam(defaultValue = "true") Boolean hardDelete);
}
