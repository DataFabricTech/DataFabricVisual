package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.classification.client.dto.request.ClassificationAddRequest;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationAdd;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationEdit;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetail;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetailByName;
import org.springframework.cloud.openfeign.FeignClient;
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

@FeignClient(name = "ClassificationClient", url = "${properties.ovp.open-metadata-url}/classifications")
public interface ClassificationClient {
    /**
     * 분류 리스트
     *
     * @return
     */
    @GetMapping("")
    ClassificationData getClassifications(@RequestParam Integer limit);

    /**
     * 분류 리스트 단일 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    ClassificationDetail getClassificationDetail(@PathVariable("id") String id);

    /**
     * 분류와 매칭되는 태그의 정보 조회 - 위에 2개와 같이 호출
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")
    ClassificationDetailByName getClassificationDetailByName(@PathVariable("name") String name);

    @DeleteMapping("/{id}")
    Map<String, Object> deleteTag(@PathVariable String id, @RequestParam MultiValueMap<String, String> params);

    /**
     * 분류 수정
     *
     * @param param
     * @return
     * @Param id
     */
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    ClassificationEdit editClassification(@PathVariable("id") String id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 분류 삭제
     *
     * @param id
     * @param recursive
     * @param hardDelete
     * @rerurn
     */
    @DeleteMapping("/{id}")
    Map<String, Object> deleteClassification(@PathVariable String id, @RequestParam(defaultValue = "true") boolean recursive, @RequestParam(defaultValue = "true") boolean hardDelete);

    /**
     * 분류 추가
     *
     * @return
     */
    @PostMapping("")
    ClassificationAdd addClassification(@RequestBody ClassificationAddRequest classificationAdd);
}
