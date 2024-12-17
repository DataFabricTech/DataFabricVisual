package com.mobigen.framework.security;

import com.mobigen.framework.utility.FrameworkProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@AllArgsConstructor
public class CustomInterceptor implements HandlerInterceptor {
    private final FrameworkProperties properties;

    private boolean shouldNotFilter(String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] ignores = properties.getSecurity().getIgnores();
        return Arrays.stream(ignores)
                .anyMatch(ignore -> pathMatcher.match(ignore, path));
    }

    private String isSecure(HttpServletRequest request) {
        return request.isSecure() ? "Secure" : "";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!shouldNotFilter(request.getRequestURI())) {
            Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
            AtomicInteger headerIndex = new AtomicInteger(0);

            headers.forEach(header -> {
                int currentIndex = headerIndex.getAndIncrement(); // getAndIncrement로 변경
                String securePart = isSecure(request);
                String sameSitePart = properties.getSecurity().getSameSite();
                String updatedHeader = String.format("%s; SameSite=%s; %s", header, sameSitePart, securePart);

                if (currentIndex == 0) {
                    response.setHeader(HttpHeaders.SET_COOKIE, updatedHeader);
                } else {
                    response.addHeader(HttpHeaders.SET_COOKIE, updatedHeader);
                }
            });
        }

        return true;
    }
}
