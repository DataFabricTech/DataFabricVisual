package com.mobigen.ovp.sample.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

@FeignClient(name = "SampleClient", url = "https://jsonplaceholder.typicode.com/")
public interface SampleClient {
    @GetMapping("/users")
    Object getSampleApi();

    @PostMapping("/test-error")
    Object getErrorApi();

    @GetMapping("")
    Object getDynamicAPI(URI baseUri);
}
