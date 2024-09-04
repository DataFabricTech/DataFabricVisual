package com.mobigen.ovp.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "UserClient", url = "${properties.ovp.open-metadata-url}/users")
public interface UserClient {
    @GetMapping("")
    Map<String, Object> getUserAll(@RequestHeader HttpHeaders headers,
                                   @RequestParam Map<String, Object> param) throws Exception;

    @GetMapping("/loggedInUser")
    Map<String, Object> getUserInfo() throws Exception;

    @GetMapping("/{id}")
    Map<String, Object> getUserWithFields(@PathVariable("id") String id, @RequestParam("fields") String fields);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable UUID id,
                                    @RequestParam(defaultValue = "true") Boolean recursive,
                                    @RequestParam(defaultValue = "false") Boolean hardDelete);

    @GetMapping("/generateRandomPwd")
    String getRandomPwd() throws Exception;

    @GetMapping("/name/{name}")
    Map<String, Object> checkDuplicateName(@PathVariable String name) throws Exception;

    @PostMapping("")
    Map<String, Object> addUser(@RequestBody Map<String, Object> param) throws Exception;
}
