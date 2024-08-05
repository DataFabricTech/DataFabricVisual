package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ClassificationClient", url = "http://192.168.105.26:8585/api/v1/tags")
public interface ClassificationClient {
    @GetMapping("/{id}")
    Map<String, Object> getTag(@PathVariable String id);
}
