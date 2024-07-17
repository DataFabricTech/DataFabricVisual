package com.mobigen.framework.security;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private Token token;
    private String[] ignores;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(Token token, FrameworkProperties properties) {
        this.token = token;
        this.ignores = properties.getSecurity().getIgnores();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
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
        log.debug("JwtFilter doFilterInternal");
        if (!shouldNotFilter(request)) {
            try {
                // Token 확인
                String xAccessToken = token.getAccessTokenByRequest(request);
                if (token.isExpiredTokenWithSecretKey(xAccessToken)) {
                    token.deleteTokens(request, response);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
