package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "AutomationsClient", url = "${properties.ovp.open-metadata-url}/automations")
public interface AutomationsClient {
    @PostMapping("/workflows")
    Map<String, Object> workflows(@RequestBody Map<String, Object> params);

    @GetMapping("/workflows/{id}")
    Map<String, Object> getWorkflows(@PathVariable String id);

    @GetMapping("/workflows/trigger/{id}")
    Map<String, Object> getWorkflowsTrigger(@PathVariable String id);

    @PostMapping("/workflows/trigger/{id}")
    Map<String, Object> postWorkflowsTrigger(@PathVariable String id);

    @DeleteMapping("/workflows/trigger/{id}")
    Map<String, Object> deleteWorkflowsTrigger(@PathVariable String id);
}
