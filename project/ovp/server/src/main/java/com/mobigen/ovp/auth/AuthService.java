package com.mobigen.ovp.auth;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.RSA;
import com.mobigen.framework.utility.Token;
import com.mobigen.ovp.user.UserRoleService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.entity.UserRoleEntity;
import com.mobigen.ovp.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String PASSWORD_KEY = "password";
    private static final String USER_ID_KEY = "email";

    private final Token token;
    private final AuthClient authClient;
    private final FrameworkProperties frameworkProperties;
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;

    /**
     * 로그인 - 토큰 발급 후 쿠키 설정
     *
     * @param response
     * @param param
     * @return
     */
    public Object login(HttpServletResponse response, Map<String, Object> param) throws Exception {
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
     * 로그인 > 공개키 반환
     * @param request
     * @return
     */
    public Object getPublicKey(HttpServletRequest request) {
        return token.generateRSAPublicKey(request);
    }

    /**
     * 로그인 > "관리자"로 강제 로그인
     * - Properties에 저장된 관리자 게정정보를 가지고 관리자로 로그인 해 관리자용 API를 사용할 수 있도록 처리
     *
     * @return
     * @throws Exception
     */
    public HttpHeaders adminLoginHeader() throws Exception {
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put(USER_ID_KEY, frameworkProperties.getTest().getUsername());
        loginBody.put(PASSWORD_KEY, frameworkProperties.getTest().getPassword());
        Map<String, Object> tokenResult = login(loginBody);
        log.info("admin token {}", tokenResult);

        HttpHeaders headers = new HttpHeaders();
        String authorizationToken = String.format("%s %s", "Bearer", tokenResult.get(frameworkProperties.getToken().getAccessToken()));
        headers.add(HttpHeaders.AUTHORIZATION, authorizationToken);
        return headers;
    }

    /**
     * 로그인 > RSA 암호화를 위한 공개키 발급 및 암호화 비밀번호 반환  - Test 코드
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

    /**
     * 회원가입
     *
     * @param param
     * @return
     * @throws Exception
     */
    public Object signUpUser(HttpServletRequest request, Map<String, Object> param) throws Exception {
        // 0. 비밀번호 복호화
        String decryptPasswd = token.decryptRSAString(request, (String) param.get(PASSWORD_KEY));
        param.put(PASSWORD_KEY, decryptPasswd);

        // 1. 사용자 기본 역할 조회
        List<String> userRoles = userRoleService.getUserRolesIdList();

        // 2. Admin 계정으로 로그인
        HttpHeaders adminAuthorizationHeader = adminLoginHeader();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(USER_ID_KEY, param.get(USER_ID_KEY));
        paramMap.put(PASSWORD_KEY, param.get(PASSWORD_KEY));
        paramMap.put("confirmPassword", param.get(PASSWORD_KEY));
        paramMap.put("createPasswordType", "ADMIN_CREATE");
        paramMap.put("name", param.get("name"));
        paramMap.put("roles", userRoles);
        paramMap.put("isAdmin", false);
        paramMap.put("isBot", false);


        // 3. 회원가입 API 호출 + DB 저장
        Map<String, Object> signUpResult = singUpAPI(adminAuthorizationHeader, paramMap);
        saveUserToDatabase(signUpResult, adminAuthorizationHeader);

        return null;
    }

    /**
     * 회원가입 > OMD 서버 회원가입 API 통신
     *
     * @param adminAuthorizationHeader : 관리자 토큰 헤더
     * @param paramMap : 회원가입 바디 정보
     * @return
     * @throws Exception
     */
    private Map<String, Object> singUpAPI(HttpHeaders adminAuthorizationHeader, Map<String, Object> paramMap) throws Exception {
        try {
            return authClient.signUpUser(adminAuthorizationHeader, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("회원가입 실패. 관리자에게 문의하세요");
        }
    }

    /**
     * 회원가입 > User DB 저장
     *
     * @param signUpResult : OMD 서버에 저장된 사용자 정보
     * @param adminAuthorizationHeader : 관리자 토큰 헤더
     * @throws Exception : 저장 시 Exception이 발생되면 OMD 서버에 저장된 User 정보도 삭제
     */
    private void saveUserToDatabase(Map<String, Object> signUpResult, HttpHeaders adminAuthorizationHeader) throws Exception {
        try {
            userRepository.save(new UserEntity(
                    (String) signUpResult.get("id"),
                    (String) signUpResult.get("name"),
                    (String) signUpResult.get(USER_ID_KEY),
                    UserRole.USER
            ));
        } catch (Exception e) {
            authClient.deleteUser(adminAuthorizationHeader, (String) signUpResult.get("id"), true, false);
            log.error(e.getMessage(), e);
            throw new Exception("회원가입 실패. 관리자에게 문의하세요");
        }
    }

    /**
     * 회원가입 > 이메일 중복 검사
     *
     * @param email : 이메일 정보
     * @throws Exception
     */
    public boolean checkDuplicateEmail(String email) throws Exception {
        Map<String, Object> checkEmailParam = new HashMap<>();
        checkEmailParam.put(USER_ID_KEY, email);

        return authClient.checkEmailInUse(checkEmailParam);
    }
}
