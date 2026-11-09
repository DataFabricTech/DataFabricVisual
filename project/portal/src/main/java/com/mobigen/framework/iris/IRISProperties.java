package com.mobigen.framework.iris;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "properties.iris")
public class IRISProperties {
    private AccessTokenProperties accessToken;
    private RefreshTokenProperties refreshToken;
    private ServerProperties server;
    private UrlProperties url;
    private RouteProperties route;
    private SecurityProperties security;
    private AuthProperties auth;
    private TestProperties test;

    @Data
    @ConfigurationProperties(prefix = "access-token")
    public static class AccessTokenProperties {
        private String name;
        private String secret;
        private int maxAge;
    }

    @Data
    @ConfigurationProperties(prefix = "refresh-token")
    public static class RefreshTokenProperties {
        private String name;
        private String secret;
        private int maxAge;
    }

    @Data
    @ConfigurationProperties(prefix = "test")
    public static class TestProperties {
        private String brickUsername;
        private String brickPassword;
        private Boolean localForceLoginEnabled;
    }

    @Data
    @ConfigurationProperties(prefix = "server")
    public static class ServerProperties {
        private String brick;
        private String angora;
        private String meta;
    }

    @Data
    @ConfigurationProperties(prefix = "url")
    public static class UrlProperties {
        private BrickProperties brick;
        private AngoraProperties angora;

        @Data
        @ConfigurationProperties(prefix = "brick")
        public static class BrickProperties {
            private String auth;
            private String tokenCheck;
        }

        @Data
        @ConfigurationProperties(prefix = "angora")
        public static class AngoraProperties {
            private String auth;
            private String tokenCheck;
        }
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
