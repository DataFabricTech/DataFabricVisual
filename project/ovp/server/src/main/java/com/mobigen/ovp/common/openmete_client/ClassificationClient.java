package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ClassificationClient", url = "${properties.ovp.open-metadata-url}/tags")
public interface ClassificationClient {
    // TODO : tag 쪽 코드 merge 되면 아래 코드 이동

    @GetMapping("/{id}")
    Map<String, Object> getTag(@PathVariable String id);

    @DeleteMapping("/{id}")
    Map<String, Object> deleteTag(@PathVariable String id, @RequestParam MultiValueMap<String, String> params);
}