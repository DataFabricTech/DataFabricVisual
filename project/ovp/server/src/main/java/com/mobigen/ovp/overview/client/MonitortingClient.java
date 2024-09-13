package com.mobigen.ovp.overview.client;

import com.mobigen.framework.configuration.FeignHttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "MonitoringClient", url = "${properties.ovp.monitoring-server-url}", configuration = FeignHttpClientConfiguration.class)
public interface MonitortingClient {

    @GetMapping("/connectStatus")
    Map<String, Object> getConnectStatus() throws Exception;

    @GetMapping("/responseTime")
    Map<String, Object> getResponseTime(@RequestParam MultiValueMap<String, String> params) throws Exception;

    @GetMapping("/models")
    Map<String, Object> getModels(@RequestParam MultiValueMap<String, String> params) throws Exception;

    @GetMapping("/eventHistory")
    Object getEventHistory() throws Exception;
}
