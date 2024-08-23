package com.mobigen.ovp.common.openmete_client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "AutomationsClient", url = "${properties.ovp.open-metadata-url}/automations")
public interface AutomationsClient {
    @PostMapping("/workflows")
    Map<String, Object> workflows(@RequestBody Map<String, Object> params) throws FeignException;

    @GetMapping("/workflows/{id}")
    Map<String, Object> getWorkflows(@PathVariable String id) throws FeignException;

    @GetMapping("/workflows/trigger/{id}")
    Map<String, Object> getWorkflowsTrigger(@PathVariable String id) throws FeignException;

    @PostMapping("/workflows/trigger/{id}")
    Map<String, Object> postWorkflowsTrigger(@PathVariable String id) throws FeignException;

    @DeleteMapping("/workflows/{id}")
    Map<String, Object> deleteWorkflows(@PathVariable String id, @RequestParam(defaultValue = "true") Boolean hardDelete) throws FeignException;
}
