package com.mobigen.framework.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobigen.framework.result.JsonResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class Token {
    private static final String PRIVATE_KEY = "private-key";
    private final FrameworkProperties properties;

    /**
     * Request에서 AccessToken, RefreshToken 추출
     * @param request
     * @return
     */
    public Map<String, Object> getTokensByRequest(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        return Arrays.stream(cookies)
                .filter(this::isTokenCookie)
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    /**
     * AccessToken || RefreshToken 쿠키 삭제
     * @param request
     * @param response
     */
    public void deleteTokens(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        Arrays.stream(cookies)
                .filter(this::isTokenCookie)
                .forEach(co -> {
                    co.setMaxAge(0);
                    co.setPath("/");
                    response.addCookie(co);
                });
    }

    /**
     * 쿠키에 토큰값 AccessToken, RefreshToken 추가
     *
     * @param response
     * @param token
     * @return
     */
    public int addTokenToResponse(HttpServletResponse response, Map<String, Object> token) {
        if (token == null || token.isEmpty()) {
            return JsonResult.RESULT_FAIL;
        }

        ResponseCookie accessTokenCookie = ResponseCookie
                .from(properties.getToken().getAccessToken(), (String) token.get(properties.getToken().getAccessToken()))
                .path("/")
                .sameSite(properties.getSecurity().getSameSite())
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from(properties.getToken().getRefreshToken(), (String) token.get(properties.getToken().getRefreshToken()))
                .path("/")
                .sameSite(properties.getSecurity().getSameSite())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        // NOTE: Set_Cookie로 쿠키값이 적용이 안되는 경우가 있어 COOKIE 설정도 같이 해줌
        response.setHeader(HttpHeaders.COOKIE, accessTokenCookie.toString());
        response.setHeader(HttpHeaders.COOKIE, refreshTokenCookie.toString());
        return JsonResult.RESULT_SUCCESS;
    }


    /**
     * Request에서 AccessToken 정보를 추출
     *
     * @param request
     * @return
     * @throws Exception
     */
    public String getAccessTokenByRequest(HttpServletRequest request) {
        Map<String, Object> tokens = getTokensByRequest(request);
        return (String) tokens.getOrDefault(properties.getToken().getAccessToken(), "");
    }

    /**
     * Request에서 AccessToken 정보를 추출 하여 사용자 정보를 생성 한다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    public User getUserByToken(HttpServletRequest request) {
        String accessToken = getAccessTokenByRequest(request);
        User user = null;
        if (accessToken.isBlank()) {
            return user;
        }

        try {
            user = getUserByXAccessToken(accessToken);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return user;
    }


    /**
     * Token Expired 상태 검증
     *
     * @param accessToken
     * @return
     */
    public boolean isExpiredTokenWithSecretKey(String accessToken) {
        DecodedJWT decodedJWT = decodedJWTWithSecretKey(accessToken);
        if (decodedJWT == null) {
            return true;
        }

        Date expiration = decodedJWT.getExpiresAt();
        return Optional.ofNullable(expiration).map(date -> date.before(new Date())).orElse(true);
    }

    /**
     * Token Expired 상태 검증
     * @param accessToken
     * @return
     */
    public boolean isExpiredToken(String accessToken) {
        // JWT 디코딩
        DecodedJWT decodedJWT = JWT.decode(accessToken);

        // 페이로드의 클레임 확인
        Date expiration = decodedJWT.getExpiresAt();

        // 만료 여부 확인
        return Optional.ofNullable(expiration).map(date -> date.before(new Date())).orElse(true);
    }

    /**
     * Cookie에 AccessToken, RefreshToken 값이 있는지 확인
     * @param cookie
     * @return
     */
    private boolean isTokenCookie(Cookie cookie) {
        return cookie.getName().equals(properties.getToken().getAccessToken()) ||
                cookie.getName().equals(properties.getToken().getRefreshToken());
    }


    /**
     * Token 디코딩
     * @param accessToken
     * @return
     */
    public DecodedJWT decodedJWTWithSecretKey(String accessToken) {
        try {
            byte[] secretKey = Base64.getUrlDecoder().decode(properties.getToken().getSecret());
            Algorithm algorithm = Algorithm.HMAC256(Base64.getUrlDecoder().decode(secretKey));
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(accessToken);
        } catch (JWTDecodeException e) {
            log.error("Error decoding JWT: {}", accessToken);
            return null;
        }
    }

    /**
     * X-Access-Token 을 이용하여 사용자 모델 정보를 반환
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    public User getUserByXAccessToken(String accessToken) {
        DecodedJWT jwt = JWT.decode(accessToken);

        return User.builder()
                .userId(jwt.getClaim("email").asString())
                .roles(jwt.getClaim("roles").asList(String.class))
                .email(jwt.getClaim("email").asString())
                .build();
    }

    /**
     * RSA 공개키 발급
     * @param request
     * @return
     */
    public String generateRSAPublicKey(HttpServletRequest request) {
        RSA rsa = new RSA();
        String publicKey = rsa.getBase64PublicKeyFromKeyPair(rsa.getKeyPair());

        HttpSession session = request.getSession();
        session.setAttribute(PRIVATE_KEY, rsa.getKeyPair().getPrivate());

        log.info("GENERATE public-key: {}", publicKey);
        return publicKey;
    }

    /**
     * RSA 복호화
     * @param request
     * @param encrypted
     * @return
     */
    public String decryptRSAString(HttpServletRequest request, String encrypted) {
        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute(PRIVATE_KEY);

        RSA rsa = new RSA();
        return rsa.decryptRSA(encrypted, privateKey);
    }


    /**
     * Security 인증 토큰 생성
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        User user = getUserByXAccessToken(token);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
}
