package com.mobigen.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.user.UserService;
import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.utility.RSA;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Decoder;

@Component
@Slf4j
@AllArgsConstructor
public class SessionManager {
    private final IRISProperties properties;
    private final Token token;
    public static HashMap<String, String> lastLoginDate = new HashMap<String, String>();

    private static final String PRIVATE_KEY = "private-key";
    // private final UserService userService;

    /**
     * 계정 정보를 직접 전달하여 세션 생성
     *
     * @param user
     * @return
     * @throws Exception
     */
    public User auth(User user) throws Exception {
        if (user == null) {
            return null;
        }
        // 사용자 Token 생성
        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        switch (user.getRoleName()) {
            case "Super-Admin":
                grantedAuths.add(new SimpleGrantedAuthority("Super-Admin"));
            case "Admin":
                grantedAuths.add(new SimpleGrantedAuthority("Admin"));
            case "Manager":
                grantedAuths.add(new SimpleGrantedAuthority("Manager"));
            case "User":
                grantedAuths.add(new SimpleGrantedAuthority("User"));
                break;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,
                grantedAuths);
        authenticationToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return user;
    }

    public Token getToken() {
        return token;
    }
    // Access Token 반환
    public String getAccessToken(HttpServletRequest request) throws Exception {
        String xAccessToken = token.getXAccessTokenByHttpServletRequest(request);
        return xAccessToken;
    }

    // Refresh Token 반환
    public String getRefreshToken(HttpServletRequest request) throws Exception {
        String xAccessToken = token.getXRefreshTokenByHttpServletRequest(request);
        return xAccessToken;
    }

    /**
     * Access 토큰이 유효한 지 반환
     *
     * @return
     */
    public Boolean isAccessTokenValid(HttpServletRequest request) throws Exception {
        String xAccessToken = token.getXAccessTokenByHttpServletRequest(request);
        return !token.isExpiredToken(xAccessToken);
    }

    public Boolean isAccessTokenValid(String accessToken) throws Exception {
        return !token.isExpiredToken(accessToken);
    }

    /**
     * Refresh 토큰이 유효한 지 반환
     *
     * @return
     */
    public Boolean isRefreshTokenValid(HttpServletRequest request) throws Exception {
        String xRefreshToken = token.getXRefreshTokenByHttpServletRequest(request);
        return !token.isExpiredToken(xRefreshToken);
    }

    public Boolean isRefreshTokenValid(String refreshToken) throws Exception {
        return !token.isExpiredToken(refreshToken);
    }

    /**
     * Access 토큰에서 사용자 계정 정보 추출
     *
     * @return
     */
    public Map<String, Object> getUserByAccessToken(HttpServletRequest request) throws Exception {
        String xAccessToken = token.getXAccessTokenByHttpServletRequest(request);
        return getUserByAccessTokenValue(xAccessToken);
    }

    public Map<String, Object> getUserByAccessTokenValue(String xAccessToken) throws Exception {
        if (xAccessToken == null) {
            return null;
        } else {
            if (xAccessToken.equals("")) {
                return null;
            }
        };
        String[] jwt = xAccessToken.split("\\.");
        Decoder decoder = Base64.getUrlDecoder();

        // Payload
        byte[] decodebyte = decoder.decode(jwt[1]);
        String payload = new String(decodebyte, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> mapPayload = objectMapper.readValue(payload, Map.class);
        return mapPayload;
    }

    public Map<String, Object> getUserByRefreshTokenValue(String xRefreshToken) throws Exception {
        if (xRefreshToken == null) {
            return null;
        } else {
            if (xRefreshToken.equals("")) {
                return null;
            }
        };
        String[] jwt = xRefreshToken.split("\\.");
        Decoder decoder = Base64.getUrlDecoder();

        // Payload
        byte[] decodebyte = decoder.decode(jwt[1]);
        String payload = new String(decodebyte, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> mapPayload = objectMapper.readValue(payload, Map.class);
        return mapPayload;
    }
    /**
     * 현재 세션에 대한 사용자 계정을 반환
     *
     * @return
     */
    public User getUser() {
        User user = null;
        try {
            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details instanceof User) {
                user = (User) details;
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return user;
    }

    /**
     * 현재 세션에 대한 사용자 계정을 반환
     *
     * @return
     */
    public String getUsername() {
        String name = "";
        try {
            User user = this.getUser();
            if (user != null) {
                name = user.getUsername();
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return name;
    }

    // /**
    //  * 개발용으로 로컬 호스트 일때 무조건 세션 생성
    //  *
    //  * @param request
    //  * @param response
    //  * @return
    //  * @throws Exception
    //  */
    // public User localLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     String remoteHost = request.getRemoteHost();
    //     User user = null;
    //     switch (remoteHost) {
    //         case "localhost":
    //         case "0:0:0:0:0:0:0:1":
    //         case "127.0.0.1":
    //             String xAccessToken = token.getXAccessToken(properties.getTest().getBrickUsername(), properties.getTest().getBrickPassword());
    //             this.addTokenToResponse(request, response, xAccessToken);
    //             user = token.getUserByXAccessToken(xAccessToken);
    //             break;
    //         default:
    //             break;
    //     }

    //     return user;
    // }

    public String generateRSAPublicKey(HttpServletRequest request) throws Exception {
        String publicKey = null;
        RSA rsa = new RSA();
        publicKey = rsa.getBase64PublicKeyFromKeyPair(rsa.getKeyPair());

        HttpSession session = request.getSession();
        session.setAttribute(PRIVATE_KEY, rsa.getKeyPair().getPrivate());

        log.info("GENERATE public-key: " + publicKey);
        return publicKey;
    }

    public String decryptRSAString(HttpServletRequest request, String encrypted) throws Exception {
        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute(PRIVATE_KEY);
        
        RSA rsa = new RSA();
        String decrypted = rsa.decryptRSA(encrypted, privateKey);
        return decrypted;
    }
    /**
     * 디비에 있는 마지막 로그인 사용자 정보를 반환
     */ 
    public String getLastLoginUser(String userId) throws Exception {
        return lastLoginDate.get(userId);
    }

}
