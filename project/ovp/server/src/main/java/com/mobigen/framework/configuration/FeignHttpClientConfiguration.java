package com.mobigen.framework.configuration;

import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHttpClientConfiguration {
    @Bean
    public ApacheHttpClient apacheHttpClient() {
        return new ApacheHttpClient();
    }
}
