package com.mobigen.framework.utility;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "properties.framework")
public class FrameworkProperties {
    private TokenProperties token;
    private RouteProperties route;
    private SecurityProperties security;
    private AuthProperties auth;
    private TestProperties test;

    @Data
    @ConfigurationProperties(prefix = "token")
    public static class TokenProperties {
        private String accessToken;
        private String refreshToken;
        private String secret;
    }

    @Data
    @ConfigurationProperties(prefix = "test")
    public static class TestProperties {
        private String username;
        private String password;
        private Boolean localForceLoginEnabled;
    }

    @Data
    @ConfigurationProperties(prefix = "route")
    public static class RouteProperties {
        private String[] locales;
    }

    @Data
    @ConfigurationProperties(prefix = "security")
    public static class SecurityProperties {
        private String[] ignores;
        private String[] permitAlls;
        private String redirectUrl;
        private String iframeOption;
        private String sameSite;
        private CorsProperties cors;
        private Boolean csrf;
    }

    @Data
    @ConfigurationProperties(prefix = "auth")
    public static class AuthProperties {
        private Boolean rsaEnabled;
    }

    @Data
    @ConfigurationProperties(prefix = "cors")
    public static class CorsProperties {
        private String pattern;
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
        private Boolean allowCredentials;
    }
}
