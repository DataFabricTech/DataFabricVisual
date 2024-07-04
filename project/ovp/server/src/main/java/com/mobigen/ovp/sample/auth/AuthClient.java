package com.mobigen.ovp.sample.auth;

import com.mobigen.framework.utility.restful.AutoRegisterApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@AutoRegisterApi
@HttpExchange("http://192.168.105.26:8585/api/v1/users")
public interface AuthClient {
    @PostExchange("/login")
    Map<String, Object> login(@RequestBody Map<String, Object> params) throws Exception;
}
