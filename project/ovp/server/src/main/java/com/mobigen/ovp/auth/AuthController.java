package com.mobigen.ovp.auth;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.utility.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final Token token;
    private final AuthService authService;

    private final static String PASSWORD_KEY = "password";
    /**
     * 로그인 - 토큰 발급
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ResponseJsonResult(errorMessage = "로그인 실패")
    @PostMapping("/login")
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> param) throws Exception {
        String decryptPasswd = token.decryptRSAString(request, (String) param.get(PASSWORD_KEY));
        param.put(PASSWORD_KEY, decryptPasswd);

        return authService.login(request, response, param);
    }

    /**
     * RSA 암호화를 위한 공개키 발급 및 암호화 비밀번호 반환  - Test 코드
     * @param request
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/login/encrypt-passwd")
    public Object getEncryptPasswd(HttpServletRequest request, @RequestBody Map<String, Object> param) {
        return authService.getEncryptPassword(request, param);
    }


    /**
     * 로그인 RSA 공개키 반환
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/login/public-key")
    public Object publicKey(HttpServletRequest request) {
        return authService.getPublicKey(request);
    }
}
