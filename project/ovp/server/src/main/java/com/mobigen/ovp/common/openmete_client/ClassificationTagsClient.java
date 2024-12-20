package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Tags;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTag;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
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

@FeignClient(name = "ClassificationTagsClient", url = "${properties.ovp.open-metadata-url}/tags")
public interface ClassificationTagsClient {
    // TODO : tag 쪽 코드 merge 되면 아래 코드 이동

    @GetMapping("")
    Tags gatTags(@RequestParam MultiValueMap<String, String> params);

    @GetMapping("/{id}")
    Map<String, Object> getTag(@PathVariable String id);

    @DeleteMapping("/{id}")
    Map<String, Object> deleteTag(@PathVariable String id, @RequestParam MultiValueMap<String, String> params);

    /**
     * 태그정보 조회
     *
     * @param parent
     * @return
     */
    @GetMapping("")
    ClassificationTag getClassificationTags(@RequestParam(defaultValue = "100000") String parent, @RequestParam Integer limit);

    @PostMapping("")
    Object createTag(@RequestBody Map<String, Object> params);

    /**
     * 태그 추가
     *
     * @param classificationTagAdd
     * @return
     */
    @PostMapping("")
    Object addClassificationTag(@RequestBody ClassificationTagAdd classificationTagAdd);

    /**
     * 분류내 태그 수정
     */
    @PatchMapping(value="/{id}", consumes = "application/json-patch+json")
    Object editClassificationTag(@PathVariable("id") String tagId, @RequestBody List<JsonPatchOperation> param);
}
