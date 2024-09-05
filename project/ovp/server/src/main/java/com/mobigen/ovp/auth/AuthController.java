package com.mobigen.ovp.auth;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.utility.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private static final String PASSWORD_KEY = "password";

    private final Token token;
    private final AuthService authService;

    /**
     * 로그인 - 토큰 발급
     * @param request
     * @param response
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/login")
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> newParam = decryptRsaPasswordWithParam(request, param);
        return authService.login(response, newParam);
    }


    /**
     * 로그아웃
     * @param request
     * @param response
     * @return
     */
    @ResponseJsonResult(errorMessage = "로그아웃 실패")
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> param = token.getTokensByRequest(request);
        return authService.logout(request, response, param);
    }

    /**
     * 로그인 > RSA 공개키 반환
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/login/public-key")
    public Object publicKey(HttpServletRequest request) {
        return authService.getPublicKey(request);
    }

    /**
     * 회원가입
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/sign-up")
    public Object signUpUser(HttpServletRequest request, @RequestBody Map<String, Object> param) throws Exception {
        return authService.signUpUser(request, param);
    }

    /**
     * 회원가입 > 이메일 중복 검사
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/sign-up/check-email")
    public Object checkDuplicateEmail(@RequestBody Map<String, Object> param) throws Exception {
        return authService.checkDuplicateEmail((String) param.get("email"));
    }

    /**
     * 로그인 > RSA 암호화를 위한 공개키 발급 및 암호화 비밀번호 반환  - Test 코드
     *
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
     * 비밀번호 재설정 > 메일 전송
     *
     * @param request
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/login/password/send-mail")
    public Object sendMail(HttpServletRequest request, @RequestBody Map<String, Object> param) throws Exception {
        return authService.sendMail(request, (String) param.get("email"));
    }


    /**
     * 비밀번호 재설정 > 고유링크 생성 및 DB 저장
     *
     * @param request
     * @param id
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/login/password/change/{id}")
    public Object changePasswordByUniqueLink(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> newParam = decryptRsaPasswordWithParam(request, param);
        return authService.changePasswordByUniqueLink(id, newParam);
    }

    /**
     * 비밀번호 재설정 > 고유링크 유효성 확인
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/login/password/change/check-id/{id}")
    public Object checkIdInChangePassword(@PathVariable String id) {
        return authService.checkIdInChangePassword(id);
    }

    /**
     * 비밀번호 재설정 API
     *
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/login/password/change")
    public Object changePassword(HttpServletRequest request, @RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> newParam = decryptRsaPasswordWithParam(request, param);
        return authService.changePasswordInMypage(newParam);
    }

    /**
     * 비밀번호 복호화
     *
     * @param request
     * @param param
     * @return
     */
    private Map<String, Object> decryptRsaPasswordWithParam(HttpServletRequest request, Map<String, Object> param) {
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            if ("newPassword".equals(key) || "confirmPassword".equals(key) || PASSWORD_KEY.equals(key)) {
                String decryptedValue = token.decryptRSAString(request, (String) entry.getValue());
                param.put(key, decryptedValue);
            }
        }
        return param;
    }
}
