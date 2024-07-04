package com.mobigen.framework.security;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.Token;
import com.mobigen.ovp.sample.auth.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 로컬 로그인 기능
 */

@Component
@Slf4j
@AllArgsConstructor
public class LocalLoginFilter extends OncePerRequestFilter {
    private AuthService authService;
    private Token token;
    private FrameworkProperties frameworkProperties;

    /**
     * 개발 > 로컬 호스트 확인
     * @param request
     * @return
     */
    private Boolean isLocal(HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();
        return "localhost".equals(remoteHost) || "0:0:0:0:0:0:0:1".equals(remoteHost) || "127.0.0.1".equals(remoteHost);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("localLoginFilter");

        // 로컬 확인 + 로그인/인증 API 확인 + 토큰 여부 확인
        // TODO : 토큰 유효성 검사 확인
        if (!isLocal(request)
                || request.getRequestURI().contains("/api/auth")
                || StringUtils.hasText(token.getAccessTokenByRequest(request))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로컬 로그인 진행
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", frameworkProperties.getTest().getUsername());
        loginBody.put("password", frameworkProperties.getTest().getPassword());

        try {
            Map<String, Object> tokenResult = authService.login(loginBody);
            if (tokenResult == null || tokenResult.isEmpty()) {
                throw new RuntimeException("토큰 결과가 없습니다.");
            }

            HttpServletRequest wrappedRequest = createWrappedRequestWithCookies(request, tokenResult);
            token.addTokenToResponse(response, tokenResult);

            filterChain.doFilter(wrappedRequest, response);
        } catch (Exception e) {
            log.error("로컬 로그인 중 오류 발생", e);
            filterChain.doFilter(request, response);
        }
    }


    /**
     * Reqeust 재설정
     * @param request
     * @param tokenResult
     * @return
     */
    private HttpServletRequest createWrappedRequestWithCookies(HttpServletRequest request, Map<String, Object> tokenResult) {
        String accessTokenKey = frameworkProperties.getToken().getAccessToken();
        String refreshTokenKey = frameworkProperties.getToken().getRefreshToken();

        return new HttpServletRequestWrapper(request) {
            @Override
            public Cookie[] getCookies() {
                Cookie[] existingCookies = super.getCookies();
                if (existingCookies == null) {
                    existingCookies = new Cookie[0];
                }

                Cookie accessTokenCookie = createCookie(accessTokenKey, (String) tokenResult.get(accessTokenKey));
                Cookie refreshTokenCookie = createCookie(refreshTokenKey, (String) tokenResult.get(refreshTokenKey));

                Cookie[] newCookies = new Cookie[existingCookies.length + 2];
                System.arraycopy(existingCookies, 0, newCookies, 0, existingCookies.length);
                newCookies[existingCookies.length] = accessTokenCookie;
                newCookies[existingCookies.length + 1] = refreshTokenCookie;

                return newCookies;
            }
        };
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
