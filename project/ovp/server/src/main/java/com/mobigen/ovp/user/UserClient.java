package com.mobigen.ovp.user;

import com.mobigen.ovp.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "UserClient", url = "${properties.ovp.open-metadata-url}")
public interface UserClient {
    @GetMapping("/users")
    Map<String, Object> getUsers(@RequestHeader HttpHeaders headers, @RequestParam Map<String, Object> param) throws Exception;

    @GetMapping("/users/loggedInUser")
    UserInfoDTO getUserInfo() throws Exception;

    @GetMapping("/users/{id}")
    Map<String, Object> getUserFollows(@PathVariable("id") String id, @RequestParam("fields") String fields);
}
