package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "DatabasesClient", url = "${properties.ovp.open-metadata-url}/databases")
public interface DatabaseClient {
    @GetMapping("")
    Map<String, Object> getDatabase(@RequestParam Map<String, Object> params);

}
