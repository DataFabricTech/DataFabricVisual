package com.mobigen.ovp.auth;

import com.mobigen.framework.utility.RSA;
import com.mobigen.framework.utility.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Token token;
    private final AuthClient authClient;
    private final static String PASSWORD_KEY = "password";

    /**
     * 로그인 - 토큰 발급 후 쿠키 설정
     * @param request
     * @param response
     * @param param
     * @return
     */
    public Object login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param) throws Exception {
        // base-64 인코딩
        String base64EncodingPasswd = Base64.getEncoder().encodeToString(param.get(PASSWORD_KEY).toString().getBytes());
        param.put(PASSWORD_KEY, base64EncodingPasswd);

        Map<String, Object> result = authClient.login(param);

        return token.addTokenToResponse(response, result);
    }
    /**
     * 로그인 - 토큰 발급
     * @param param
     * @return
     */
    public Map<String, Object> login(Map<String, Object> param) throws Exception {
        // base-64 인코딩
        String base64EncodingPasswd = Base64.getEncoder().encodeToString(param.get(PASSWORD_KEY).toString().getBytes());
        param.put(PASSWORD_KEY, base64EncodingPasswd);

        return authClient.login(param);
    }


    /**
     * RSA 암호화를 위한 공개키 발급 및 암호화 비밀번호 반환  - Test 코드
     *
     * @param request
     * @return
     */
    public Object getEncryptPassword(HttpServletRequest request, Map<String, Object> param) {
        RSA rsa = new RSA();

        HttpSession session = request.getSession();
        session.setAttribute("private-key", rsa.getKeyPair().getPrivate());

        return rsa.encryptRSA((String) param.get(PASSWORD_KEY), rsa.getKeyPair().getPublic());
    }

    public Object getPublicKey(HttpServletRequest request) {
        return token.generateRSAPublicKey(request);
    }
}
