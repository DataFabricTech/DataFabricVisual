package com.mobigen.datalake.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.IpAddressUtil;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final SessionManager sessionManager;
    private final IRISProperties properties;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${properties.superadmin.id}")
    String adminId;

    @Value("${properties.superadmin.passwd}")
    String adminPasswd;

    @Value("${properties.loginDeactivationDateThreshold}")
    int loginDeactivationDateThreshold;

    // 권한 설정
    private String setUserEmptyRoleName(Object roleName) {
        return StringUtils.hasText((String) roleName) ? (String) roleName : "User";
    }

    // 자정 만료시간 조회
    public Date getTokenExpiredDate() {
        // 토큰의 만료 시간을 자정에서 현재시간을 뺀 시간으로 설정
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        
        int midnight = 24 * 3600;
        int expTime = midnight - ((hour * 3600) + (minute * 60) + second); // 토큰 만료 시간

        Date today = new Date();
        return new Date(today.getTime() + (1000L * expTime));
    }

    // 현재 인증된 사용자 이름 가져오기 (Spring Security 사용)
    private String getAuthenticatedUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Object regenerateAccessToken(String username) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(properties.getAccessToken().getSecret());
        // 사용자 이름 유효성 검증
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        
        Map user = (Map) userMapper.getUserMaster(username);
        Map<String, String> map = new HashMap<>();
        map.put("authority", setUserEmptyRoleName(user.get("role_name")));
        map.put("username", (String) user.get("user_nm"));
        map.put("user_id", String.valueOf(user.get("user_id")));
        map.put("dept_id", String.valueOf(user.get("dept_cd")));
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));
        map.put("role_id", String.valueOf(user.get("role_id")));
        map.put("user_role_name", String.valueOf(user.get("user_role_name")));

        sessionManager.lastLoginDate.put(String.valueOf(user.get("user_id")), String.valueOf(user.get("last_login")));
        // Date expireDate = getTokenExpiredDate();
        // return JWT.create().withPayload(map).withExpiresAt(expireDate).sign(algorithm)
        Date today = new Date();
        return JWT.create().withPayload(map).withExpiresAt(new Date(today.getTime() + (1000L * properties.getAccessToken().getMaxAge()))).sign(algorithm);
    }

    // Access Token 발급
    public String generateAccessToken(String username) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(properties.getAccessToken().getSecret());
        // 사용자 이름 유효성 검증
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        Map user = (Map) userMapper.getUserMaster(username);
        Map<String, String> map = new HashMap<>();
        map.put("authority", setUserEmptyRoleName(user.get("role_name")));
        map.put("username", (String) user.get("user_nm"));
        map.put("user_id", String.valueOf(user.get("user_id")));
        map.put("dept_id", String.valueOf(user.get("dept_cd")));
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));
        map.put("role_id", String.valueOf(user.get("role_id")));
        map.put("user_role_name", String.valueOf(user.get("user_role_name")));

        sessionManager.lastLoginDate.put(String.valueOf(user.get("user_id")), String.valueOf(user.get("last_login")));
        // Date expireDate = getTokenExpiredDate();
        // return JWT.create().withPayload(map).withExpiresAt(expireDate).sign(algorithm);
        Date today = new Date();
        return JWT.create().withPayload(map).withExpiresAt(new Date(today.getTime() + (1000L * properties.getAccessToken().getMaxAge()))).sign(algorithm);
    }

    // Refresh Token 재발급
    String regenerateRefreshToken(String username) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(properties.getRefreshToken().getSecret());        
        // 사용자 이름 유효성 검증
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        // // 현재 인증된 사용자 이름 가져오기 (Spring Security 사용)
        // String authenticatedUserName = getAuthenticatedUserName();
        // if (!authenticatedUserName.equals(username)) {
        //     throw new SecurityException("Access denied: unauthorized access attempt");
        // }
        // 사용자 정보 조회 및 검증
        Map<String, Object> user = (Map<String, Object>) userMapper.getUserMaster(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("authority", setUserEmptyRoleName(user.get("role_name")));
        map.put("username", (String) user.get("user_nm"));
        map.put("user_id", String.valueOf(user.get("user_id")));
        map.put("dept_id", String.valueOf(user.get("dept_cd")));
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));
        map.put("role_id", String.valueOf(user.get("role_id")));
        map.put("user_role_name", String.valueOf(user.get("user_role_name")));

        sessionManager.lastLoginDate.put(String.valueOf(user.get("user_id")), String.valueOf(user.get("last_login")));
        // Date expireDate = getTokenExpiredDate();
        // return JWT.create().withPayload(map).withExpiresAt(expireDate).sign(algorithm);

        Date today = new Date();
        return JWT.create().withPayload(map).withExpiresAt(new Date(today.getTime() + (1000L * properties.getRefreshToken().getMaxAge()))).sign(algorithm);
    }

    // Refresh Token 발급
    public String generateRefreshToken(String username) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(properties.getRefreshToken().getSecret());

        // 사용자 이름 유효성 검증
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        // // 현재 인증된 사용자 이름 가져오기 (Spring Security 사용)
        // String authenticatedUserName = getAuthenticatedUserName();
        // if (!authenticatedUserName.equals(username)) {
        //     throw new SecurityException("Access denied: unauthorized access attempt");
        // }
        // 사용자 정보 조회 및 검증
        Map<String, Object> user = (Map<String, Object>) userMapper.getUserMaster(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("authority", setUserEmptyRoleName(user.get("role_name")));
        map.put("username", (String) user.get("user_nm"));
        map.put("user_id", String.valueOf(user.get("user_id")));
        map.put("dept_id", String.valueOf(user.get("dept_cd")));
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));
        map.put("role_id", String.valueOf(user.get("role_id")));
        map.put("user_role_name", String.valueOf(user.get("user_role_name")));

        sessionManager.lastLoginDate.put(String.valueOf(user.get("user_id")), String.valueOf(user.get("last_login")));
        // Date expireDate = getTokenExpiredDate();
        // return JWT.create().withPayload(map).withExpiresAt(expireDate).sign(algorithm);

        Date today = new Date();
        return JWT.create().withPayload(map).withExpiresAt(new Date(today.getTime() + (1000L * properties.getRefreshToken().getMaxAge()))).sign(algorithm);
    }

    public Object authenticate(HttpServletRequest request, HttpServletResponse response, String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        // 1. 입력 값 유효성 검증
        if (user_id == null || user_id.trim().isEmpty() ) { // 계정
            result.put("login_result", "N");
            result.put("login_result_message", "사용자 계정을 올바르게 입력해 주세요.");
            return result;
        }
        if (password == null || password.trim().isEmpty()) { // 비밀번호
            result.put("login_result", "N");
            result.put("login_result_message", "비밀번호를 입력해 주세요.");
            return result;
        }

        // 미승인 유저 조회
        Map user = (Map) userMapper.getPendingApprovalUsers(user_id);

        if (user != null) {
            result.put("login_result", "N");
            result.put("login_result_message", "승인되지 않는 계정입니다.");
            return result;
        }

        // 사용자 조회
        user = (Map) userMapper.getApprovalUserMaster(user_id);

        // 1. DB에 사용자 정보 없음
        if (user == null) {
            result.put("login_result", "N");
            result.put("login_result_message", "계정이 존재하지 않습니다.");
            return result;
        }

        // 2. 로그인 fail 개수 체크
        if( (Integer) user.get("login_fail_cnt") >= 5 ){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp loginTime = (Timestamp) user.get("last_login");
            long milliseconds = now.getTime() - loginTime.getTime();
            int seconds = (int) milliseconds / 1000;
            int miniutes = seconds / 60;
            if(miniutes < 60) {
                result.put("login_result", "N");
                result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                return result;
            }
        }

        // 3. 비밀번호 일치여부 확인
        String inputPassword = password;
        String userPw = user.get("password").toString();
        if (!passwordEncoder.matches(inputPassword, userPw)) { // 패스워드 불일치
            result.put("login_result", "N");
            result.put("login_result_message", "비밀번호가 일치하지 않습니다.");
            userMapper.updateLoginFailCount(user_id);
            return result;
        }

        // 4. 장기 미접속자 체크
        if( (Integer) user.get("login_term") >= loginDeactivationDateThreshold ){
            result.put("login_result", "N");
            result.put("login_result_message", 
                    Integer.toString(loginDeactivationDateThreshold) + "일 이상 접속하지 않아 로그인이 차단되었습니다. 관리자에게 문의해주시기 바랍니다.");
            return result;                
        }

        result.put("login_result", "Y");
        userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트
        
        if(user.get("role_name") == null) user.put("role_name", "User"); // 권한값이 없는 경우 User 권한 설정
        
        User loginUser = User
                .builder()
                .roleName((String) user.get("role_name"))
                .name((String) user.get("user_nm"))
                .userId(String.valueOf(user.get("user_id")))
                .deptId((String) user.get("dept_cd"))
                .deptName((String) user.get("dept_nm"))
                .email((String) user.get("user_email"))
                .build();

        sessionManager.auth(loginUser);

        // Token 반환
        result.put("x-access-token", generateAccessToken(user_id));
        result.put("x-refresh-token", generateRefreshToken(user_id));
        result.put("login_result_message", result.get("login_result_message"));

        String accessToken = result.get("x-access-token");
        
        if(accessToken != null) {
            insertLoginHistory(request, accessToken);    
        }
        
        return result;
    }

    // 부서 목록 전체 조회
    public Object getDepartments(String word) throws Exception {
        return userMapper.getDepartments(word);
    }

    // 부서 등록
    public Object deptRegister(Map<String, String> params) throws Exception {
        return userMapper.deptRegister(params);
    }

    // 부서 삭제
    public Object deptDelete(Map<String, String> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            int deptCount = userMapper.getCheckDeptExists(params);

            if(deptCount > 0){
                result.put("register_result", "N");
                result.put("errorMessage", "하위 조직이 존재합니다.");
                return result;
            }
            
            int userCount = userMapper.getCheckUserExists(params);

            if(userCount > 0){
                result.put("register_result", "N");
                result.put("errorMessage", "해당 조직에 속한 사용자가 존재합니다.");
                return result;
            }

            int deleteDeptCount = userMapper.deptDelete(params);

            if (deleteDeptCount > 0){
                result.put("register_result", "Y");
            } else {
                result.put("register_result", "N");
                result.put("errorMessage", "부서 삭제중에 실패하였습니다. 관리자에게 문의해주시기 바랍니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("register_result", "N");
            result.put("register_result_message", "부서 삭제중에 실패하였습니다. 관리자에게 문의해주시기 바랍니다.");
        }

        return result;
    }

    // 로그인 이력 저장
    public Integer insertLoginHistory(HttpServletRequest request, String token) throws Exception {
        Map<String, Object> userInfo = sessionManager.getUserByAccessTokenValue(token);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userInfo.get("user_id").toString());
        param.put("username", userInfo.get("username").toString());
        param.put("user_ip_addr", IpAddressUtil.getClientIp(request));
        param.put("dept_id", userInfo.get("dept_id").toString());
        param.put("dept_name", userInfo.get("dept_nm").toString());
        param.put("login_type", "0");

        return userMapper.insertLoginHistory(param);
    }

    public Object register(Map<String, String> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            // 해당 ID 중복확인
            String user_id = params.get("reg_userid");

            int dupCnt = userMapper.checkDupUserId(user_id);
            if (dupCnt > 0) {
                result.put("register_result", "N");
                result.put("register_result_message", "중복되는 계정입니다.");
                return result;
            }
            
            // 회원 등록
            // 1. 비밀번호 암호화
            String encPassword = passwordEncoder.encode(params.get("reg_password"));
            params.put("reg_password", encPassword);

            int insertResult = userMapper.registerUser(params);
            if (insertResult > 0) {
                result.put("register_result", "Y");
            } else {
                result.put("register_result", "N");
                result.put("register_result_message", "회원가입에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("register_result", "N");
            result.put("register_result_message", "회원가입에 실패하였습니다. 관리자에게 문의해주시기 바랍니다.");
        }

        return result;
    }

    public Object delete(Map<String, String> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            userMapper.delete(params);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("register_result", "N");
            result.put("register_result_message", "회원 삭제중에 실패하였습니다. 관리자에게 문의해주시기 바랍니다.");
        }

        return result;
    }

    public Object checkDupUserId(String user_id) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            // 해당 ID 중복확인
            int dupCnt = userMapper.checkDupUserId(user_id);
            if (dupCnt > 0) {
                result.put("register_result", "N");
                result.put("register_result_message", "중복되는 계정입니다.");
            } else {
                result.put("register_result", "Y");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("register_result", "N");
            result.put("register_result_message", "아이디 중복확인에 실패하였습니다. 관리자에게 문의해주시기 바랍니다.");
        }

        return result;
    }

    // role list 조회
    public Object getRoleList(String role_type) throws Exception {
        return userMapper.getRoleList(role_type);
    }

    // 부서 정보 조회
    public Object getDeptInfo(String role_type) throws Exception {
        return userMapper.getDeptInfo(role_type);
    }
    
    // 패스워드 인증
    public Object getVerifyPassword(String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        // 1. 입력 값 유효성 검증
        if (password == null || password.trim().isEmpty()) { // 비밀번호
            result.put("login_result", "N");
            result.put("login_result_message", "비밀번호를 입력해 주세요.");
            return result;
        }

        // 미승인 유저 조회
        Map user = (Map) userMapper.getPendingApprovalUsers(user_id);

        if (user != null) {
            result.put("login_result", "N");
            result.put("login_result_message", "승인되지 않는 계정입니다.");
            return result;
        }

        // 사용자 조회
        user = (Map) userMapper.getApprovalUserMaster(user_id);

        // 1. DB에 사용자 정보 없음
        if (user == null) {
            result.put("login_result", "N");
            result.put("login_result_message", "계정이 존재하지 않습니다.");
            return result;
        }

        // 2. 로그인 fail 개수 체크
        if( (Integer) user.get("login_fail_cnt") >= 5 ){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp loginTime = (Timestamp) user.get("last_login");
            long milliseconds = now.getTime() - loginTime.getTime();
            int seconds = (int) milliseconds / 1000;
            int miniutes = seconds / 60;
            if(miniutes < 60) {
                result.put("login_result", "N");
                result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                return result;
            }
        }

        // 3. 비밀번호 일치여부 확인
        String inputPassword = password;
        String userPw = user.get("password").toString();
        if (!passwordEncoder.matches(inputPassword, userPw)) { // 패스워드 불일치
            result.put("login_result", "N");
            result.put("login_result_message", "비밀번호가 일치하지 않습니다.");
            return result;
        }

        // 4. 장기 미접속자 체크
        if( (Integer) user.get("login_term") >= loginDeactivationDateThreshold ){
            result.put("login_result", "N");
            result.put("login_result_message", 
                    Integer.toString(loginDeactivationDateThreshold) + "일 이상 접속하지 않아 로그인이 차단되었습니다. 관리자에게 문의해주시기 바랍니다.");
            return result;                
        }

        result.put("login_result", "Y");
        
        return result;
    }

    // 패스워드 변경
    public Object changePassword(String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        
        // 비밀번호 암호화
        String encPassword = passwordEncoder.encode(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("password", encPassword);

        int insertResult = userMapper.updatePassword(params);
        if (insertResult > 0) {
            result.put("register_result", "Y");
        } else {
            result.put("register_result", "N");
            result.put("register_result_message", "변경 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }
        
        return result;
    }
}
