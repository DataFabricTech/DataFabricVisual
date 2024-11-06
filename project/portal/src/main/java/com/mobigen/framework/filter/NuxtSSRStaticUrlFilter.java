package com.mobigen.framework.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.stereotype.Component;

import com.mobigen.framework.iris.User;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class NuxtSSRStaticUrlFilter implements Filter {

    public static HashMap blacklistToken = new HashMap();

    @Autowired
    private SessionManager sessionManager;
    
    private void removeToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse hr = (HttpServletResponse)servletResponse;
        sessionManager.getToken().deleteCookieByHttpServletResponse(hr, "x-access-token");
        sessionManager.getToken().deleteCookieByHttpServletResponse(hr, "x-refresh-token");
        String url = ((HttpServletRequest) servletRequest).getRequestURI();
        if(url.indexOf("/api/")> -1) {
            hr.setContentType("application/json");
            hr.setCharacterEncoding("UTF-8");

            // JsonResult jr =new JsonResult();
            // jr.setResult(0);
            // jr.setErrorMessage("expired token");
            // // JSON 데이터 생성 (예: {"status": "success", "message": "Hello, World!"})
            String jsonResponse = "{\"result\": \"0\", \"errorMessage\": \"expired token\"}";

            try {
                OutputStream out = hr.getOutputStream();
                out.write(jsonResponse.getBytes());
                out.flush();
            } catch (ClientAbortException e) {
                // 클라이언트가 응답을 중단한 경우 예외 로그 생략 또는 경고 수준으로 기록
                log.info("클라이언트 연결 중단: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            hr.sendRedirect("/");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // DB login_date
            String lastLoginDate = null;

            // Access Token
            String accessToken = sessionManager.getAccessToken((HttpServletRequest) servletRequest);
            Map<String, Object> userInfoFromAccessToken = null;
            String last_login_from_access_token = null;
            String userIdFromAccessToken = null;

            if(accessToken != null) {
                if (!accessToken.equals("")) {
                    userInfoFromAccessToken = sessionManager.getUserByAccessTokenValue(accessToken);
                    last_login_from_access_token = userInfoFromAccessToken.get("last_login").toString();
                    userIdFromAccessToken = userInfoFromAccessToken.get("user_id").toString();
        
                    lastLoginDate = sessionManager.getLastLoginUser(userIdFromAccessToken);
    
                    if(accessToken != null && lastLoginDate != null && lastLoginDate.compareTo(last_login_from_access_token) > 0 && !"admin".equals(userIdFromAccessToken)) {
                        removeToken(servletRequest, servletResponse);
                        return;
                    }
                }
            } else {
                String url = ((HttpServletRequest) servletRequest).getRequestURI();
                if(!url.equals("/") && url.indexOf("authenticate") < 0 && url.indexOf("/api") > -1) {
                     removeToken(servletRequest, servletResponse);
                 log.info(url);
                }
            }

            // Refresh Token
            String refreshToken = sessionManager.getRefreshToken((HttpServletRequest) servletRequest);
            Map<String, Object> userInfoFromRefreshToken = null;
            String last_login_from_refresh_token = null;
            String userIdFromRefreshToken = null;

            if(refreshToken != null) {
                if (!refreshToken.equals("")) {
                    userInfoFromRefreshToken = sessionManager.getUserByRefreshTokenValue(refreshToken);
                    last_login_from_refresh_token = userInfoFromRefreshToken.get("last_login").toString();
                    userIdFromRefreshToken = userInfoFromRefreshToken.get("user_id").toString();
        
                    lastLoginDate = sessionManager.getLastLoginUser(userIdFromRefreshToken);
    
                    if(refreshToken != null && lastLoginDate != null && lastLoginDate.compareTo(last_login_from_refresh_token) > 0 && !"admin".equals(userIdFromRefreshToken)) {
                        removeToken(servletRequest, servletResponse);
                        return;
                    }
                }
            } else {
                String url = ((HttpServletRequest) servletRequest).getRequestURI();
                if(!url.equals("/") && url.indexOf("authenticate") < 0 && url.indexOf("/api") > -1) {
                     removeToken(servletRequest, servletResponse);
                 log.info(url);
                }
            }
        
            filterChain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                /**
                 * Nuxt를 SSR 모드로 배포 시, NuxtLink 경로에 html 이 없기 때문에
                 * 서버쪽에서 강제로 index.html을 추가하여 매핑한다.
                 * @TODO sub-page 를 만들면 index.html로 해결이 안되기 때문에 좀더 복잡한 패틴 처리가 필요함
                 * @return
                 */
                @Override
                public String getRequestURI() {
                    String url = ((HttpServletRequest) servletRequest).getRequestURI();

                    String searchHtmlFile = "static" + url + "/index.html";
                    if (url.equals("/")) {
                        searchHtmlFile = "static" + url + "index.html";
                    }

                    ClassPathResource resource = new ClassPathResource(searchHtmlFile);
                    if (resource.exists()) {
                        return url + "/index.html";
                    }
                    return url;
                }
            }, servletResponse);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
