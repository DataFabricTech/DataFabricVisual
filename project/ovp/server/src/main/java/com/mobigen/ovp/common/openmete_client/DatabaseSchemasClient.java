package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "DatabaseSchemasClient", url = "${properties.ovp.open-metadata-url}/databaseSchemas")
public interface DatabaseSchemasClient {
    @GetMapping("")
    Map<String, Object> getDatabaseSchemas(@RequestParam Map<String, Object> params);

}
