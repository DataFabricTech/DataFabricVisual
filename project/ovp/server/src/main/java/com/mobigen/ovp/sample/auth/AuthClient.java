package com.mobigen.ovp.sample.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "AuthClient", url = "${properties.ovp.open-metadata-url}")
public interface AuthClient {
    @PostMapping("/users/login")
    Map<String, Object> login(@RequestBody Map<String, Object> params) throws Exception;
}
