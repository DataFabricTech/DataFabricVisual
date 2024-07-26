package com.mobigen.ovp.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "AuthClient", url = "${properties.ovp.open-metadata-url}")
public interface AuthClient {
    @PostMapping("/users/login")
    Map<String, Object> login(@RequestBody Map<String, Object> params) throws Exception;

    @PostMapping("/users/checkEmailInUse")
    boolean checkEmailInUse(@RequestBody Map<String, Object> params) throws Exception;

    @PostMapping("/users")
    Map<String, Object> signUpUser(@RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> params) throws Exception;

    @DeleteMapping("/users/{name}")
    void deleteUser(@RequestHeader HttpHeaders headers,
                    @PathVariable String name,
                    @RequestParam(defaultValue = "true") boolean hardDelete,
                    @RequestParam(defaultValue = "false") boolean recursive) throws Exception;

    @PutMapping("/users/changePassword")
    String changePassword(@RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> params) throws Exception;
}
