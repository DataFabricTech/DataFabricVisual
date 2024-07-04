package com.mobigen.framework.utility.restful;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.context.request.RequestContextHolder;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HttpInterfaceInterceptor implements ClientHttpRequestInterceptor {
    private final static String AUTHORIZATION_TYPE = "Bearer";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("HTTP Interface 사용 " + request.getURI());
        HttpHeaders headers = request.getHeaders();

        // header에 Authorization 값이 없다면 강제 주입
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            log.info("Request header Authorization not found.");

            // header에 Authorization 값이 없다면 강제 주입
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest httpServletRequest = attrs.getRequest();
                Map<String, Object> tokenMap = getTokensByRequest(httpServletRequest);
                if (tokenMap != null && !tokenMap.isEmpty()) {
                    HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
                    String accessToken = (String) tokenMap.get("accessToken");

                    String bearerToken = String.format("%s %s", AUTHORIZATION_TYPE, accessToken);
                    requestWrapper.getHeaders().set("Authorization", bearerToken);

                    return execution.execute(requestWrapper, body);
                }
            }
        }

        return execution.execute(request, body);
    }

    private Map<String, Object> getTokensByRequest(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        return Arrays.stream(cookies)
                .filter(this::isTokenCookie)
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    /**
     * Cookie에 AccessToken, RefreshToken 값이 있는지 확인
     * @param cookie
     * @return
     */
    private boolean isTokenCookie(Cookie cookie) {
        return cookie.getName().equals("accessToken") ||
                cookie.getName().equals("refreshToken");
    }
}
