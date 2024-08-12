package com.mobigen.ovp.auth;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.RSA;
import com.mobigen.framework.utility.Token;
import com.mobigen.ovp.email.EmailUtil;
import com.mobigen.ovp.common.OvpProperties;
import com.mobigen.ovp.user.UserRoleService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String PASSWORD_KEY = "password";
    private static final String USER_ID_KEY = "email";

    private final Token token;
    private final AuthClient authClient;
    private final FrameworkProperties frameworkProperties;
    private final OvpProperties ovpProperties;
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;
    private final PwResetRepository pwResetRepository;
    private final EmailUtil emailUtil;

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

        Map<String, Object> result = new HashMap<>();
        try{
            result = authClient.login(param);
        } catch (Exception e) {
            throw new Exception("아이디 및 비밀번호가 유효하지 않습니다.");
        }

        return token.addTokenToResponse(response, result);
    }

    /**
     * 로그아웃
     *
     * @param response
     * @param param
     * @return
     */
    public Object logout(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param) throws Exception {
        Map<String, Object> newParam = new HashMap<>();
        newParam.put("token", param.get(frameworkProperties.getToken().getAccessToken()));

        // 1. 로그아웃
        String result = authClient.logout(newParam);

        // 2. Session(Security 보안 세션) 로그아웃
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        // 3. Token 쿠키값 삭제
        token.deleteTokens(request, response);

        return result;
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

    /**
     * 비밀번호 재설정 > 메일 전송
     *
     * @param request
     * @param email
     * @return
     * @throws Exception
     */
    public Object sendMail(HttpServletRequest request, String email) throws Exception {
        // 1-1. 이메일 유효성 검증
        boolean checkedEmailValidation = EmailUtil.isValidEmail(email);
        if (!checkedEmailValidation) {
            throw new Exception("이메일이 유효하지 않습니다.");
        }

        // 1-2. 이메일 사용 여부 검증
        boolean checkedEmailInUse = this.checkDuplicateEmail(email);
        if (!checkedEmailInUse) {
            throw new Exception("등록된 이메일이 아닙니다.");
        }

        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String id = UUID.randomUUID().toString();
        Context context = prepareEmailContext(host, id);

        boolean sendEmail = emailUtil.sendHTMLMail(email, ovpProperties.getMail().getTitle(), context,"ovp_pwReset");

        // Email 전송 성공 시 DB에 데이터 저장
        if (sendEmail) {
            savePwResetEntity(email, id);
        }
        return null;
    }

    /**
     * 비밀번호 재설정 > 고유링크 생성 및 DB 저장
     *
     * @param id
     * @param param
     * @return
     * @throws Exception
     */
    public boolean changePasswordByUniqueLink(String id, Map<String, Object> param) throws Exception {
        // 1. 링크 유효성 확인
        PwResetEntity pwResetData = pwResetRepository.findById(id).orElseThrow(() -> new Exception("링크가 유효하지 않습니다."));

        // 2. 시간 유효성 확인
        if (EmailUtil.isLinkExpiredWithValidTime(pwResetData.getValidTime(), pwResetData.getCreateDate())) {
            throw new Exception("링크가 유효하지 않습니다.");
        }

        // 1. 사용자 입력값 검증
        String confirmPassword = (String) param.get("confirmPassword");
        String newPassword = (String) param.get("newPassword");
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("비밀번호가 일치 하지 않습니다.");
        }

        param.put("username", pwResetData.getUserName());

        boolean isChange = changePassword(param);
        if (isChange) {
            pwResetRepository.deleteById(id);
        } else {
            throw new Exception("비밀번호 설정이 잘못되었습니다.");
        }
        return isChange;
    }
    /**
     * 비밀번호 재설정 > 고유링크 유효성 확인
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean checkIdInChangePassword(String id) {
        Optional<PwResetEntity> pwResetDataOpt = pwResetRepository.findById(id);
        boolean checkedData = pwResetDataOpt.isPresent();
        if (!checkedData) {
            return false;
        }

        return !EmailUtil.isLinkExpiredWithValidTime(pwResetDataOpt.get().getValidTime(), pwResetDataOpt.get().getCreateDate());
    }

    /**
     * 비밀번호 > 비밀번호 재설정 API
     *
     * @param param
     * @return
     * @throws Exception
     */
    public boolean changePasswordInMypage(Map<String, Object> param) throws Exception {
        boolean isChange = changePassword(param);
        if (!isChange) {
            throw new Exception("비밀번호 설정이 잘못되었습니다.");
        }
        return isChange;
    }

    public boolean changePassword(Map<String, Object> param) throws Exception {
        param.put("requestType", "USER");

        // 1. 사용자 입력값 검증
        String confirmPassword = (String) param.get("confirmPassword");
        String newPassword = (String) param.get("newPassword");
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("비밀번호가 일치 하지 않습니다.");
        }

        // 2. 비밀번호 변경
        try {
            HttpHeaders adminAuthorizationHeader = adminLoginHeader();
            authClient.changePassword(adminAuthorizationHeader, param);
        } catch (Exception e) {
            return false;
        }
        return true;
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
            throw new Exception("회원가입에 실패 했습니다. 관리자에게 문의해주세요.");
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
            throw new Exception("회원가입에 실패 했습니다. 관리자에게 문의해주세요.");
        }
    }


    /**
     * 비밀번호 재설정 > HTML Context 설정
     * @param host
     * @param id
     * @return
     */
    private Context prepareEmailContext(String host, String id) {
        Context context = new Context();
        context.setVariable("url", host + ovpProperties.getMail().getHref());
        context.setVariable("token", id);
        context.setVariable("validationTime", ovpProperties.getMail().getValidTime());
        return context;
    }

    /**
     * 비밀번호 재설정 > 링크 DB 저장
     * @param email
     * @param id
     * @throws Exception
     */
    private void savePwResetEntity(String email, String id) throws Exception {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("등록된 이메일이 아닙니다."));

        PwResetEntity pwResetEntity = new PwResetEntity();
        pwResetEntity.setId(id);
        pwResetEntity.setEmail(email);
        pwResetEntity.setUserName(user.getName());
        pwResetEntity.setValidTime(ovpProperties.getMail().getValidTime());
        pwResetRepository.save(pwResetEntity);
    }
}
