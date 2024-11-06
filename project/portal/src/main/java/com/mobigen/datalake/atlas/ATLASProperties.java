package com.mobigen.datalake.atlas;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import lombok.Data;
@Data
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties("properties.atlas")
public class ATLASProperties {

    private ServerProperties server;

    @Data
    @ConfigurationProperties("server")
    public static class ServerProperties {
        private String scheme;
        private String host;
        private String port;
        private String url;
        private String user;
        private String passwd;
    }
}
