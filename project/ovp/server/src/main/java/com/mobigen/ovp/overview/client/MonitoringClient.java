package com.mobigen.ovp.overview.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "MonitoringClient", url = "${properties.ovp.monitoring-service-url}/monitoring")
public interface MonitoringClient {
    /**
     * 서비스 상태 요약
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/connectStatus")
    Map<String, Object> connectStatus() throws Exception;

    /**
     * 서비스 응답 시간
     *
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/recResponseTime")
    Map<String, Object> getResponseTime(@RequestParam MultiValueMap<String, String> params) throws Exception;

    /**
     * 등록된 모델 등록 현황
     *
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/models")
    Map<String, Object> getMonitoringModels(@RequestParam MultiValueMap<String, String> params) throws Exception;

    /**
     * 서비스 상태
     *
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/connectionHistory")
    Map<String, Object> getConnectionHistory(@RequestParam MultiValueMap<String, String> params) throws Exception;

    /**
     * 수집 히스토리
     *
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/ingestionHistory")
    Map<String, Object> getIngestionHistory(@RequestParam MultiValueMap<String, String> params) throws Exception;
}
