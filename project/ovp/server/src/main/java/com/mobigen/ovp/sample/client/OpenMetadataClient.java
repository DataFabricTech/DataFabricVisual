package com.mobigen.ovp.sample.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "OpenMetadataClient", url = "${properties.ovp.open-metadata-url}")
public interface OpenMetadataClient {
    @GetMapping("/users")
    Map<String, Object> getUsers() throws Exception;

    @GetMapping("/search/query")
    Map<String, Object> getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception;

    @GetMapping("/tables/name/{table}")
    Object getTableDetail(@PathVariable String table, @RequestParam String fields, @RequestParam String include) throws Exception;
}
