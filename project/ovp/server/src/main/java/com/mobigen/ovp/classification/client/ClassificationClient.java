package com.mobigen.ovp.classification.client;

import com.mobigen.ovp.classification.client.dto.classification.ClassificationResponse;
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

@FeignClient(name = "ClassificationClient", url = "http://192.168.105.26:8585/api/v1")
public interface ClassificationClient {

    /**
     * 분류 리스트
     *
     * @param fields
     * @return
     */
    @GetMapping("/classifications")
    ClassificationResponse getClassifications(@RequestParam(required = false, defaultValue = "owner,tags, reviewers,votes,domain") String fields);

    /**
     * 분류 수정
     *
     * @param param
     * @return
     * @Param id
     */
    @PatchMapping(value = "/classifications/{id}", consumes = "application/json-patch+json")
    Object editClassification(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 분류 삭제
     *
     * @param id
     * @param recursive
     * @param hardDelete
     * @rerurn
     */
    @DeleteMapping("/classifications/{id}")
    void deleteClassification(@PathVariable UUID id, @RequestParam(defaultValue = "true") Boolean recursive, @RequestParam(defaultValue = "true") Boolean hardDelete);
}
