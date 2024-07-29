package com.mobigen.ovp.mypage.client;

import com.mobigen.framework.configuration.FeignHttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "MyPageClient", url = "${properties.ovp.open-metadata-url}", configuration = FeignHttpClientConfiguration.class)
public interface MyPageClient {
    @GetMapping("users/name/{fqn}")
    Map<String, Object> getUserInfo(@PathVariable String fqn, @RequestParam MultiValueMap<String, String> params);

    @PatchMapping(value = "users/{id}", consumes = "application/json-patch+json")
    Map<String, Object> UpdateUserInfo(@PathVariable String id, @RequestBody List<Map<String, Object>> params);
}
