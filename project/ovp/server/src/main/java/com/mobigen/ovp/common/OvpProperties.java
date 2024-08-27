package com.mobigen.ovp.common;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "properties.ovp")
public class OvpProperties {
    private MailProperties mail;

    @Data
    @ConfigurationProperties(prefix = "mail")
    public static class MailProperties {
        private String validTime;
        private String redirectUrl;
        private String redirectErrorUrl;
        private String href;
        private String title;
        private String contentsFilePath;
    }
}
