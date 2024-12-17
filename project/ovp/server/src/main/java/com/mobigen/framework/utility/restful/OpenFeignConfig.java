package com.mobigen.framework.utility.restful;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.Token;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenFeignConfig {
    private static final String AUTHORIZATION_TYPE = "Bearer";
    private final Token token;
    private final FrameworkProperties frameworkProperties;

    @Bean
    public RequestInterceptor globalRequestInterceptor() {
        return requestTemplate -> {
            log.debug("OpenFeign Interceptor {}", requestTemplate.url());
            // 헤더에 Authorization 값이 없다면 강제 주입
            if (!requestTemplate.headers().containsKey("Authorization")) {
                log.debug("Request header Authorization not found.");

                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    HttpServletRequest httpServletRequest = attrs.getRequest();
                    Map<String, Object> tokenMap = token.getTokensByRequest(httpServletRequest);
                    if (tokenMap != null && !tokenMap.isEmpty()) {
                        String accessToken = (String) tokenMap.get(frameworkProperties.getToken().getAccessToken());
                        String bearerToken = String.format("%s %s", AUTHORIZATION_TYPE, accessToken);
                        requestTemplate.header("Authorization", bearerToken);
                    }
                }
            }
        };
    }
}
