package com.mobigen.ovp.model_creation.client;

import com.mobigen.framework.configuration.FeignHttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ModelCreationClient", url = "${properties.ovp.open-metadata-url}", configuration = FeignHttpClientConfiguration.class)
public interface ModelCreationClient {
    @PutMapping(value = "/tables/{id}/followers", consumes = "application/json")
    Map<String, Object> addTableBookMark(@PathVariable String id, @RequestBody String loginUserId);

    @PutMapping(value = "/containers/{id}/followers", consumes = "application/json")
    Map<String, Object> addContainerBookMark(@PathVariable String id, @RequestBody String loginUserId);

    @DeleteMapping(value = "/tables/{id}/followers/{loginUserId}")
    Map<String, Object> deleteTableBookMark(@PathVariable String id, @PathVariable String loginUserId);

    @DeleteMapping(value = "/containers/{id}/followers/{loginUserId}")
    Map<String, Object> deleteContainerBookMark(@PathVariable String id, @PathVariable String loginUserId);

    @GetMapping(value = "/search/query")
    Map<String, Object> selectDataModelName(@RequestParam("query_filter") String queryFilter,
                                  @RequestParam("include_source_fields") String includeSourceFields);
}
