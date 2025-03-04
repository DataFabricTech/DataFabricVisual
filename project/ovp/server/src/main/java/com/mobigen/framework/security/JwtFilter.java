package com.mobigen.framework.security;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private Token token;
    private FrameworkProperties properties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String[] ignores = properties.getSecurity().getIgnores();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String item : ignores) {
            if (pathMatcher.match(item, path)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!shouldNotFilter(request)) {
            try {
                // Token 확인
                String accessToken = token.getAccessTokenByRequest(request);

                if (StringUtils.hasText(accessToken) && !token.isExpiredToken(accessToken)) {
                    // Spring Security 인증 처리
                    var authentication = token.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    String url = request.getServletPath();
                    log.debug("Token expired or not found: {}", url);

                    if (!url.startsWith("/api/auth") && !url.startsWith("/portal/login")) {
                        token.deleteTokens(request, response);

                        if (!url.startsWith("/_nuxt/") && !url.contains(".") && !url.contains("/api")) {
                            log.debug("Redirecting to login page");
                            response.sendRedirect(request.getContextPath() + "/portal/login");
                            return;
                        } else if (url.startsWith("/api")) {
                            // API 요청이면 JSON 응답 반환
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"TokenExpired\", \"redirect\": \"/portal/login\"}");
                            response.getWriter().flush();
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Authentication error: ", e);

                // API 요청인지 확인
                boolean isApiRequest = request.getServletPath().startsWith("/api");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");

                if (isApiRequest) {
                    response.getWriter().write("{\"error\": \"InternalServerError\", \"redirect\": \"/portal/login\"}");
                } else {
                    response.sendRedirect(request.getContextPath() + "/portal/login");
                }
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
