package com.mobigen.ovp.sample.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "SampleClient", url = "https://jsonplaceholder.typicode.com/")
public interface SampleClient {
    @GetMapping("/users")
    Object getSampleApi();

    @PostMapping("/test-error")
    Object getErrorApi();

    @GetMapping("")
    Object getDynamicAPI(URI baseUri);

    // NOTE: consumes = "application/json-patch+json" 필수
    @PatchMapping(value = "posts/1", consumes = "application/json-patch+json")
    Object usePatchApi(@RequestBody Map<String, Object> params);
}
