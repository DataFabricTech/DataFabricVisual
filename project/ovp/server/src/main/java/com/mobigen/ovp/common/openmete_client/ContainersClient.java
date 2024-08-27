package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Tables;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ContainersClient", url = "${properties.ovp.open-metadata-url}/containers")
public interface ContainersClient {
    @GetMapping("{id}")
    Tables getStorageById(@PathVariable("id") String id, @RequestParam Map<String, String> params);

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    Map<String, Object> changeStorage(@PathVariable("id") String id, @RequestParam Map<String, String> params, @RequestBody List<Map<String, Object>> body);
}