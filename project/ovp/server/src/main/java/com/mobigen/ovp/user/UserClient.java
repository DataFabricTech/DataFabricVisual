package com.mobigen.ovp.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "UserClient", url = "${properties.ovp.open-metadata-url}")
public interface UserClient {
    @GetMapping("/users")
    Map<String, Object> getUsers(@RequestHeader HttpHeaders headers, @RequestParam Map<String, Object> param) throws Exception;
}
