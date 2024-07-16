package com.mobigen.ovp.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "SearchClient", url = "${properties.ovp.open-metadata-url}")
public interface SearchClient {
    @GetMapping("/search/aggregate")
    Map<String, Object> getFilter(MultiValueMap<String, String> params);

    @GetMapping("/search/query")
    Map<String, Object> getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception;
}
