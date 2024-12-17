package com.mobigen.ovp.model_creation.client;

import com.mobigen.framework.configuration.FeignHttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "DolphinClient", url = "${properties.ovp.dolphin-server-url}", configuration = FeignHttpClientConfiguration.class)
public interface DolphinClient {
    @PostMapping("/query/execute")
    Map<String, Object> executeQuery(@RequestBody Map<String, Object> params) throws Exception;

    @PostMapping("/model")
    Map<String, Object> createModel(@RequestBody Map<String, Object> params) throws Exception;
}
