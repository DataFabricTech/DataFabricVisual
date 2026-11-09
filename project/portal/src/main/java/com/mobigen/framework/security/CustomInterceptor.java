package com.mobigen.framework.security;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.mobigen.framework.iris.IRISProperties;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomInterceptor implements HandlerInterceptor {
    private final IRISProperties properties;

    private boolean shouldNotFilter(String path) {
        AntPathMatcher p = new AntPathMatcher();
        String[] ignores = properties.getSecurity().getPermitAlls();
        for (int i = 0; i < ignores.length; i++) {
            if (p.match(ignores[i], path)) {
                return true;
            }
        }

        return false;
    }

    private String isSecure(HttpServletRequest request) {
        if (request.isSecure()) {
            return "Secure";
        }
        return "";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!shouldNotFilter(request.getRequestURI())) {
            Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);

            AtomicInteger c = new AtomicInteger(0);
            headers.forEach(header -> {
                int t = c.get();
                String sameSite = String.format("%s; SameSite=%s; %s", header, properties.getSecurity().getSameSite(),
                        isSecure(request));
                if (t == 0) {
                    response.setHeader(HttpHeaders.SET_COOKIE, sameSite);
                } else {
                    response.addHeader(HttpHeaders.SET_COOKIE, sameSite);
                }
                t++;
                c.set(t);
            });
        }

        return true;
    }
}
