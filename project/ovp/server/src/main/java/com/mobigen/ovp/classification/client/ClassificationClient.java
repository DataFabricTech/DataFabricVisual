package com.mobigen.ovp.classification.client;

import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetail;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetailByName;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ClassificationClient", url = "http://192.168.105.26:8585/api/v1")
public interface ClassificationClient {

    /**
     * 분류 리스트
     * @return
     */
    @GetMapping("/classifications")
    ClassificationData getClassifications();

    /**
     * 분류 리스트 단일 조회
     * @param id
     * @return
     */
    @GetMapping("/classifications/{id}")
    ClassificationDetail getClassificationDetail(@PathVariable("id") String id);

    /**
     * 분류와 매칭되는 태그의 정보 조회 - 위에 2개와 같이 호출
     * @param name
     * @return
     */
    @GetMapping("/classifications/name/{name}")
    ClassificationDetailByName getClassificationDetailByName(@PathVariable("name") String name);

    /**
     * 태그정보 조회
     *
     * @param parent
     * @return
     */
    @GetMapping("/tags")
    ClassificationTag getClassificationTags(
            @RequestParam(defaultValue = "da") String parent);


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
