package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationAdd;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationEdit;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetail;
import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetailByName;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTag;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
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

@FeignClient(name = "ClassificationClient", url = "${properties.ovp.open-metadata-url}/classifications")
public interface ClassificationClient {
    // TODO : tag 쪽 코드 merge 되면 아래 코드 이동

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
    ClassificationTag getClassificationTags(@RequestParam(defaultValue = "da") String parent);

    @PostMapping("")
    Object createTag(@RequestBody Map<String, Object> params);

    /**
     * 태그 추가
     *
     * @param classificationTagAdd
     * @return
     */
    @PostMapping("")
    ClassificationTagAdd addClassificationTag(@RequestBody ClassificationTagAdd classificationTagAdd);
}
