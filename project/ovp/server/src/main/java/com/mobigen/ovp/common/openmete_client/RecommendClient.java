package com.mobigen.ovp.common.openmete_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "RecommendClient", url = "${properties.ovp.recommend-data-model-url}")
public interface RecommendClient {

    @GetMapping("/clustering")
    Map<String, Object> getClustering(@RequestParam String target_id);

    @GetMapping("/embedding")
    Map<String, Object> getEmbedding(@RequestParam String target_id);
}
