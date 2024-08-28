package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "ContainersClient", url = "${properties.ovp.open-metadata-url}/containers")
public interface ContainersClient {
    @GetMapping("{id}")
    Tables getStorageById(@PathVariable("id") String id, @RequestParam Map<String, String> params);

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    Map<String, Object> changeStorage(@PathVariable("id") String id, @RequestParam Map<String, String> params, @RequestBody List<Map<String, Object>> body);

    @GetMapping("")
    Map<String, Object> getContainers(@RequestParam Map<String, Object> params);

    @GetMapping("/name/{fqn}")
    Map<String, Object> getContainersName(@PathVariable String fqn, @RequestParam Map<String, Object> params);

    @PutMapping("/{id}/vote")
    Tables changeVote(@PathVariable("id") String id, @RequestBody DataModelDetailVote dataModelDetailVote);

    @PutMapping("/{modelId}/followers")
    Object follow(@PathVariable("modelId") String modelId, @RequestBody UUID id);

    @DeleteMapping("/{id}/followers/{userId}")
    Object unfollow(@PathVariable("id") String id, @PathVariable("userId") String userId);
}
