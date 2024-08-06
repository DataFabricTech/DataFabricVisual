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
                    log.info("Spring Security token 처리 {}", authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    token.deleteTokens(request, response);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
