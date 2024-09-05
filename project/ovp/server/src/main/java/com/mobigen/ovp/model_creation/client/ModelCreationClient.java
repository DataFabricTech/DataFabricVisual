package com.mobigen.ovp.model_creation.client;

import com.mobigen.framework.configuration.FeignHttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ModelCreationClient", url = "${properties.ovp.open-metadata-url}", configuration = FeignHttpClientConfiguration.class)
public interface ModelCreationClient {
    @PutMapping(value = "/tables/{id}/followers", consumes = "application/json")
    Map<String, Object> addBookMark(@PathVariable String id, @RequestBody String loginUserId);

    @DeleteMapping(value = "/tables/{id}/followers/{loginUserId}")
    Map<String, Object> deleteBookMark(@PathVariable String id, @PathVariable String loginUserId);
}
