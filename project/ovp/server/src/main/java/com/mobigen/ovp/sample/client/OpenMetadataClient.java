package com.mobigen.ovp.sample.client;

import com.mobigen.framework.utility.restful.AutoRegisterApi;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@AutoRegisterApi
@HttpExchange("http://192.168.105.26:8585/api/v1")
public interface OpenMetadataClient {
    @GetExchange("/users")
    Map<String, Object> getUsers() throws Exception;

    @GetExchange("/search/query")
    Map<String, Object> getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception;

    @GetExchange("/tables/name/{table}")
    Object getTableDetail(@PathVariable String table, @RequestParam String fields, @RequestParam String include) throws Exception;
}
