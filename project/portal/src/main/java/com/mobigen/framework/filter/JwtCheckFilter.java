package com.mobigen.framework.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Component
@Slf4j
public class JwtCheckFilter extends OncePerRequestFilter {

    public static HashMap blacklistToken = new HashMap();

    // 
    private static final String ROOT_URL = "/";
    private static final String[] PUBLIC_API_PATTERNS = {
        "authenticate",
        "/api/depts",
        "/api/checkDupUserId",
        "register"
    };
    private static final String API_PREFIX = "/api";

    @Autowired
    private SessionManager sessionManager;
    
    private void removeToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        sessionManager.getToken().deleteCookieByHttpServletResponse(response, "x-access-token");
        sessionManager.getToken().deleteCookieByHttpServletResponse(response, "x-refresh-token");
        String url = request.getRequestURI();
        if(url.indexOf("/api/")> -1) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JsonResult jr =new JsonResult();
            // jr.setResult(0);
            // jr.setErrorMessage("expired token");
            // // JSON 데이터 생성 (예: {"status": "success", "message": "Hello, World!"})
            String jsonResponse = "{\"result\": \"0\", \"errorMessage\": \"expired token\"}";

            try {
                OutputStream out = response.getOutputStream();
                out.write(jsonResponse.getBytes());
                out.flush();
            } catch (ClientAbortException e) {
                // 클라이언트가 응답을 중단한 경우 예외 로그 생략 또는 경고 수준으로 기록
                log.info("클라이언트 연결 중단: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/");
        }
    }

    private boolean isPublicUrl(String url) {
        if (url.equals(ROOT_URL)) {
            return true;
        }
        
        for (String pattern : PUBLIC_API_PATTERNS) {
            if (url.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }

    private boolean needsTokenValidation(String url) {
        return url.contains(API_PREFIX) && !isPublicUrl(url);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // DB login_date
            String lastLoginDate = null;

            // Access Token
            String accessToken = sessionManager.getAccessToken(request);
            Map<String, Object> userInfoFromAccessToken = null;
            String last_login_from_access_token = null;
            String userIdFromAccessToken = null;

            if(accessToken != null) {
                if (!accessToken.equals("")) {
                    userInfoFromAccessToken = sessionManager.getUserByAccessTokenValue(accessToken);
                    if(userInfoFromAccessToken.get("last_login") != null) {
                        last_login_from_access_token = userInfoFromAccessToken.get("last_login").toString();
                    }

                    if (userInfoFromAccessToken.get("user_id") != null) {
                        userIdFromAccessToken = userInfoFromAccessToken.get("user_id").toString();
                    }
                    
                    lastLoginDate = sessionManager.getLastLoginUser(userIdFromAccessToken);

                    if(accessToken != null && lastLoginDate != null && lastLoginDate.compareTo(last_login_from_access_token) > 0 && !"admin".equals(userIdFromAccessToken)) {
                        removeToken(request, response);
                        return;
                    }
                }
            } else {
                String url = (request).getRequestURI();
                if (needsTokenValidation(url)) {
                    removeToken(request, response);
                    log.info(url);
                }
            }

            // Refresh Token
            String refreshToken = sessionManager.getRefreshToken(request);
            Map<String, Object> userInfoFromRefreshToken = null;
            String last_login_from_refresh_token = null;
            String userIdFromRefreshToken = null;

            if(refreshToken != null) {
                if (!refreshToken.equals("")) {
                    userInfoFromRefreshToken = sessionManager.getUserByRefreshTokenValue(refreshToken);
                    if (userInfoFromRefreshToken.get("last_login") != null) {
                        last_login_from_refresh_token = userInfoFromRefreshToken.get("last_login").toString();
                    }

                    if (userInfoFromRefreshToken.get("user_id") != null) {
                        userIdFromRefreshToken = userInfoFromRefreshToken.get("user_id").toString();
                    }
        
                    lastLoginDate = sessionManager.getLastLoginUser(userIdFromRefreshToken);

                    if(refreshToken != null && lastLoginDate != null && lastLoginDate.compareTo(last_login_from_refresh_token) > 0 && !"admin".equals(userIdFromRefreshToken)) {
                        removeToken(request, response);
                        // return;
                    }
                }
            } else {
                String url = ((HttpServletRequest) request).getRequestURI();
                if (needsTokenValidation(url)) {
                    removeToken(request, response);
                    log.info(url);
                }
            }
        } catch (Exception e) {
            log.error("jwtCheckFilter Error !!");
        }
        
        filterChain.doFilter(request, response);
    }
    
}
