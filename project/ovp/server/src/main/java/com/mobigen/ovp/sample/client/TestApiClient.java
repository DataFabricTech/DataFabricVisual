package com.mobigen.ovp.sample.client;

import com.mobigen.framework.utility.restful.AutoRegisterApi;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.util.UriBuilderFactory;

@AutoRegisterApi
public interface TestApiClient {
    @GetExchange()
    Object getDynamicAPI(UriBuilderFactory factory);
}
