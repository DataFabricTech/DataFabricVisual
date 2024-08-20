package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ContainerClient", url = "${properties.ovp.open-metadata-url}/containers")
public interface ContainerClient {
    @GetMapping("/{id}")
    Map<String, Object> getContainersObject(
            @PathVariable("id") String id,
            @RequestParam(value = "fields", required = false) String fields
    );
}
