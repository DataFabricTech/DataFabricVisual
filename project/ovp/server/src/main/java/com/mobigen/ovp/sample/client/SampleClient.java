package com.mobigen.ovp.sample.client;

import com.mobigen.framework.utility.restful.AutoRegisterApi;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@AutoRegisterApi
@HttpExchange("https://jsonplaceholder.typicode.com/")
public interface SampleClient {
    @GetExchange("/users")
    Object getUsers();
    @PostExchange("/asdf-asdfasdfasdfasdfa")
    Object getErrorApi();
}
