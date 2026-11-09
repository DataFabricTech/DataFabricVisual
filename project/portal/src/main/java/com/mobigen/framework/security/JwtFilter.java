package com.mobigen.framework.security;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.iris.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private Token token;
    private SessionManager sessionManager;
    private IRISProperties properties;
    private String[] ignores;
    private String[] permitAlls;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(Token token, SessionManager sessionManager, IRISProperties properties) {
        this.token = token;
        this.sessionManager = sessionManager;
        this.properties = properties;
        this.permitAlls = properties.getSecurity().getPermitAlls();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        for (String item : permitAlls) {
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
                // 이미 인증된 사용자가 있는지 보고 없다면
                // token을 사용해서 재인증을 한다.
                User user = sessionManager.getUser();
                if (user == null) {
                    String xAccessToken = token.getXAccessTokenByHttpServletRequest(request);
                    if (token.isExpiredToken(xAccessToken)) {
                        token.deleteCookieByHttpServletResponse(response, properties.getAccessToken().getName());
                        if(!request.getRequestURI().equals("/") ){
                            response.sendRedirect("/");
                            return;
                        }
                    } else {
                        user = token.getUserByHttpServletRequest(request);
                        sessionManager.auth(user);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}