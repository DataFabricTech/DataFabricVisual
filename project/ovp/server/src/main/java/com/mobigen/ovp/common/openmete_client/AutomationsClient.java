package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "AutomationsClient", url = "${properties.ovp.open-metadata-url}/automations")
public interface AutomationsClient {
    @PostMapping("/workflows")
    Map<String, Object> workflows(@RequestParam Map<String, Object> params);

    @GetMapping("/workflows/trigger/{id}")
    Map<String, Object> getWorkflows(@PathVariable String id);

    @PostMapping("/workflows/trigger/{id}")
    Map<String, Object> postWorkflows(@PathVariable String id);

    @DeleteMapping("/workflows/trigger/{id}")
    Map<String, Object> deleteWorkflows(@PathVariable String id);
}
