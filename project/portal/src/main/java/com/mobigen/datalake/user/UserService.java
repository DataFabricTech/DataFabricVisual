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
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final SessionManager sessionManager;
    private final IRISProperties properties;

    @Value("${properties.superadmin.id}")
    String adminId;

    @Value("${properties.superadmin.passwd}")
    String adminPasswd;

    @Value("${properties.tempuser.ids}")
    String tempUserIds;

    @Value("${properties.tempuser.passwds}")
    String tempUserPasswds;

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
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));

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
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));

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
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));

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
        map.put("dept_nm", String.valueOf(user.get("dept_nm")));
        map.put("user_email", String.valueOf(user.get("user_email")));
        map.put("last_login", String.valueOf(user.get("last_login")));

        sessionManager.lastLoginDate.put(String.valueOf(user.get("user_id")), String.valueOf(user.get("last_login")));
        // Date expireDate = getTokenExpiredDate();
        // return JWT.create().withPayload(map).withExpiresAt(expireDate).sign(algorithm);

        Date today = new Date();
        return JWT.create().withPayload(map).withExpiresAt(new Date(today.getTime() + (1000L * properties.getRefreshToken().getMaxAge()))).sign(algorithm);
    }

    // 일반 인증
    public Object authenticate(HttpServletRequest request, HttpServletResponse response, String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        Map user = (Map) userMapper.getUserMaster(user_id);

        // 1. DB에 사용자 정보 없음
        if (user == null) {
            result.put("login_result", "N");
            result.put("login_result_message", "ID와 비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주십시오.");
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
                result.put("login_result_message", "연속 5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                return result;
            } else {
                userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
            }
        }

        // 2-1. 장기 미접속자 체크
        if( (Integer) user.get("login_term") >= loginDeactivationDateThreshold ){
            result.put("login_result", "N");
            result.put("login_result_message", 
                    Integer.toString(loginDeactivationDateThreshold) + "일이상 미접속자는 장기미사용자로 분류 로그인이 차단됩니다. 관리자에게 문의 하시기 바랍니다.");
            return result;                
        }

        // 3. 슈퍼 관리자 여부 확인
        if(user_id.equals(adminId)) { // 슈퍼 관리자
            if(!password.equals(adminPasswd)) {
                result.put("login_result", "N");
                result.put("login_result_message", "ID와 비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주십시오.");
            } else {
                result.put("login_result", "Y");
            }
        } else { // 일반 사용자
            if (!password.equals(user.get("password"))) {
                result.put("login_result", "N");
                result.put("login_result_message", "ID와 비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주십시오.");
            } else {
                result.put("login_result", "Y");
            }
        }

        // 4. 로그인 성공 여부 확인
        if (!"Y".equals(result.get("login_result"))){ // 실패
            userMapper.updateLoginFailCount(user_id);
            return result;
        }

        // HashMap userInfo = new HashMap<String, String>();
        // userInfo.put("user_id", user_id);
        // userInfo.put("username", user.get("user_nm").toString());
        // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
        
        // userMapper.insertLoginHistory(userInfo);    // 로그인 이력 추가
        userMapper.updateLoginUser(user_id);        
        

        if(user.get("role_name") == null) user.put("role_name", "User"); //
        
        User loginUser = User
                .builder()
                .roleName((String) user.get("role_name"))
                .name((String) user.get("user_nm"))
                .userId(String.valueOf(user.get("user_id")))
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

    // 그룹웨어 인증
    public Object authenticateGW(HttpServletRequest request, HttpServletResponse response, String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        // 입력 값 유효성 검증
        if (user_id == null || user_id.trim().isEmpty() ) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // 그룹웨어 인증 처리
        boolean bIsAuthenticated = doAuthInGW(user_id, password);

        // 그룹웨어 인증 성공 시
        if (bIsAuthenticated) {
            result.put("login_result", "Y");
            Map user = (Map) userMapper.getUserMaster(user_id);

            // 1. DB에 사용자 정보 없음
            if (user == null) {
                result.put("login_result", "N");
                result.put("login_result_message", "그룹웨어와 연동이 진행중입니다. 관리자에게 문의해주시기 바랍니다.");
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
                } else {
                    userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                }
            }

            // 2-1. 장기 미접속자 체크
            if( (Integer) user.get("login_term") >= loginDeactivationDateThreshold ){
                result.put("login_result", "N");
                result.put("login_result_message", 
                        Integer.toString(loginDeactivationDateThreshold) + "일이상 미접속자는 장기미사용자로 분류 로그인이 차단됩니다. 관리자에게 문의 하시기 바랍니다.");
                return result;                
            }

            // 3. 로그인 실패 처리
            if (!"Y".equals(result.get("login_result"))){ // 실패
                result.put("login_result", "N");
                result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                userMapper.updateLoginFailCount(user_id);
                return result;
            }

            // HashMap userInfo = new HashMap<String, String>();
            // userInfo.put("user_id", user_id);
            // userInfo.put("username", user.get("user_nm").toString());
            // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
            // userMapper.insertLoginHistory(userInfo);    // 로그인 이력 추가
            userMapper.updateLoginUser(user_id);        // 사용자 정보 업데이트

            
            if(user.get("role_name") == null) user.put("role_name", "User"); // 권한값이 없는 경우 User 권한 설정
            
            User loginUser = User
                    .builder()
                    .roleName((String) user.get("role_name"))
                    .name((String) user.get("user_nm"))
                    .userId(String.valueOf(user.get("user_id")))
                    .deptName((String) user.get("dept_nm"))
                    .email((String) user.get("user_email"))
                    .build();

            sessionManager.auth(loginUser);

            // Token 반환
            result.put("x-access-token", generateAccessToken(user_id));
            result.put("x-refresh-token", generateRefreshToken(user_id));
            result.put("login_result_message", result.get("login_result_message"));
        } else { // 그룹웨어 인증 실패
            // 예외 로그인 성공 여부
            boolean exAuthenticate = exAuth(user_id, password);

            if (exAuthenticate) {
                result.put("login_result", "Y");
                // 예외 사용자 조회
                Map exUser = (Map) userMapper.getUserMaster(user_id);

                // 1. DB에 사용자 정보 없음
                if (exUser == null) {
                    result.put("login_result", "N");
                    result.put("login_result_message", "계정 연동이 진행중입니다. 관리자에게 문의해주시기 바랍니다.");
                    return result;
                }

                // 2. 로그인 fail 개수 체크
                if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    Timestamp loginTime = (Timestamp) exUser.get("last_login");
                    long milliseconds = now.getTime() - loginTime.getTime();
                    int seconds = (int) milliseconds / 1000;
                    int miniutes = seconds / 60;
                    if(miniutes < 60) {
                        result.put("login_result", "N");
                        result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                        return result;
                    } else {
                        userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                    }
                }

                // 2-1. 장기 미접속자 체크
                if( (Integer) exUser.get("login_term") >= loginDeactivationDateThreshold ){
                    result.put("login_result", "N");
                    result.put("login_result_message", 
                            Integer.toString(loginDeactivationDateThreshold) + "일이상 미접속자는 장기미사용자로 분류 로그인이 차단됩니다. 관리자에게 문의해주시기 바랍니다.");
                    return result;                
                }

                // 3. 로그인 실패 처리
                if (!"Y".equals(result.get("login_result"))){ // 실패
                    result.put("login_result", "N");
                    result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                    userMapper.updateLoginFailCount(user_id);
                    return result;
                }

                // HashMap userInfo = new HashMap<String, String>();
                // userInfo.put("user_id", user_id);
                // userInfo.put("username", exUser.get("user_nm").toString());
                // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
                // userMapper.insertLoginHistory(userInfo); // 로그인 이력 추가
                userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트
                
                if(exUser.get("role_name") == null) exUser.put("role_name", "User"); // 권한값이 없는 경우 User 권한 설정
                
                User loginUser = User
                        .builder()
                        .roleName((String) exUser.get("role_name"))
                        .name((String) exUser.get("user_nm"))
                        .userId(String.valueOf(exUser.get("user_id")))
                        .deptName("임시")
                        .email((String) exUser.get("user_email"))
                        .build();

                sessionManager.auth(loginUser);

                // Token 반환
                result.put("x-access-token", generateAccessToken(user_id));
                result.put("x-refresh-token", generateRefreshToken(user_id));
                result.put("login_result_message", result.get("login_result_message"));
                
            } else {
                // 예외 사용자 조회
                Map exUser = (Map) userMapper.getUserMaster(user_id);
                
                // 1. DB에 사용자 정보 없음
                if (exUser == null) {
                    result.put("login_result", "N");
                    result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                    return result;
                }

                // 1. 로그인 fail 개수 체크
                if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    Timestamp loginTime = (Timestamp) exUser.get("last_login");
                    long milliseconds = now.getTime() - loginTime.getTime();
                    int seconds = (int) milliseconds / 1000;
                    int miniutes = seconds / 60;
                    if(miniutes < 60) {
                        result.put("login_result", "N");
                        result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                        return result;
                    } else {
                        userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                    }
                }

                result.put("login_result", "N");
                result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
            }
        }
        String accessToken = result.get("x-access-token");
        
        if(accessToken != null) {
            insertLoginHistory(request, accessToken);    
        }
        
        return result;
    }

    // AD 인증
    public Object authenticateAD(HttpServletRequest request, HttpServletResponse response, String user_id, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        // 입력 값 유효성 검증
        if (user_id == null || user_id.trim().isEmpty() ) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // AD 인증 처리
        boolean bIsAuthenticated = doAuthInAD(user_id, password);

        // AD 인증 성공 시
        if (bIsAuthenticated) {
            result.put("login_result", "Y");
            Map user = (Map) userMapper.getUserMaster(user_id);

            // 1. DB에 사용자 정보 없음
            if (user == null) {
                result.put("login_result", "N");
                result.put("login_result_message", "AD 계정과 연동이 진행중입니다. 관리자에게 문의해주시기 바랍니다.");
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
                } else {
                    userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                }
            }

            // 2-1. 장기 미접속자 체크
            if( (Integer) user.get("login_term") >= loginDeactivationDateThreshold ){
                result.put("login_result", "N");
                result.put("login_result_message", 
                        Integer.toString(loginDeactivationDateThreshold) + "일이상 미접속자는 장기미사용자로 분류 로그인이 차단됩니다. 관리자에게 문의 하시기 바랍니다.");
                return result;                
            }

            // 3. 로그인 실패 처리
            if (!"Y".equals(result.get("login_result"))){ // 실패
                result.put("login_result", "N");
                result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                userMapper.updateLoginFailCount(user_id);
                return result;
            }

            // HashMap userInfo = new HashMap<String, String>();
            // userInfo.put("user_id", user_id);
            // userInfo.put("username", user.get("user_nm").toString());
            // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
            // userMapper.insertLoginHistory(userInfo);    // 로그인 이력 추가
            userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트            

            if(user.get("role_name") == null) user.put("role_name", "User"); // 권한값이 없는 경우 User 권한 설정
            
            User loginUser = User
                    .builder()
                    .roleName((String) user.get("role_name"))
                    .name((String) user.get("user_nm"))
                    .userId(String.valueOf(user.get("user_id")))
                    .deptName((String) user.get("dept_nm"))
                    .email((String) user.get("user_email"))
                    .build();

            sessionManager.auth(loginUser);

            // Token 반환
            result.put("x-access-token", generateAccessToken(user_id));
            result.put("x-refresh-token", generateRefreshToken(user_id));
            result.put("login_result_message", result.get("login_result_message"));
        } else { // AD 인증 실패
            // 예외 로그인 성공 여부
            boolean exAuthenticate = exAuth(user_id, password);

            if (exAuthenticate) {
                result.put("login_result", "Y");
                // 예외 사용자 조회
                Map exUser = (Map) userMapper.getUserMaster(user_id);

                // 1. DB에 사용자 정보 없음
                if (exUser == null) {
                    result.put("login_result", "N");
                    result.put("login_result_message", "계정 연동이 진행중입니다. 관리자에게 문의해주시기 바랍니다.");
                    return result;
                }

                // 2. 로그인 fail 개수 체크
                if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    Timestamp loginTime = (Timestamp) exUser.get("last_login");
                    long milliseconds = now.getTime() - loginTime.getTime();
                    int seconds = (int) milliseconds / 1000;
                    int miniutes = seconds / 60;
                    if(miniutes < 60) {
                        result.put("login_result", "N");
                        result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                        return result;
                    } else {
                        userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                    }
                }

                // 2-1. 장기 미접속자 체크
                if( (Integer) exUser.get("login_term") >= loginDeactivationDateThreshold ){
                    result.put("login_result", "N");
                    result.put("login_result_message", 
                            Integer.toString(loginDeactivationDateThreshold) + "일이상 미접속자는 장기미사용자로 분류되어 로그인이 차단됩니다. 관리자에게 문의 하시기 바랍니다.");
                    return result;                
                }

                // 3. 로그인 실패 처리
                if (!"Y".equals(result.get("login_result"))){ // 실패
                    result.put("login_result", "N");
                    result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                    userMapper.updateLoginFailCount(user_id);
                    return result;
                }

                // HashMap userInfo = new HashMap<String, String>();
                // userInfo.put("user_id", user_id);
                // userInfo.put("username", exUser.get("user_nm").toString());
                // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
                // userMapper.insertLoginHistory(userInfo); // 로그인 이력 추가
                userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트
                
                if(exUser.get("role_name") == null) exUser.put("role_name", "User"); // 권한값이 없는 경우 User 권한 설정
                
                User loginUser = User
                        .builder()
                        .roleName((String) exUser.get("role_name"))
                        .name((String) exUser.get("user_nm"))
                        .userId(String.valueOf(exUser.get("user_id")))
                        .deptName("임시")
                        .email((String) exUser.get("user_email"))
                        .build();

                sessionManager.auth(loginUser);

                // Token 반환
                result.put("x-access-token", generateAccessToken(user_id));
                result.put("x-refresh-token", generateRefreshToken(user_id));
                result.put("login_result_message", result.get("login_result_message"));
                
            } else {
                // 예외 사용자 조회
                Map exUser = (Map) userMapper.getUserMaster(user_id);

                // 1. DB에 사용자 정보 없음
                if (exUser == null) {
                    result.put("login_result", "N");
                    result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
                    return result;
                }

                // 1. 로그인 fail 개수 체크
                if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    Timestamp loginTime = (Timestamp) exUser.get("last_login");
                    long milliseconds = now.getTime() - loginTime.getTime();
                    int seconds = (int) milliseconds / 1000;
                    int miniutes = seconds / 60;
                    if(miniutes < 60) {
                        result.put("login_result", "N");
                        result.put("login_result_message", "5회이상 비밀번호을 잘못입력하였습니다. 1시간이후 재로그인하기 바랍니다.");
                        return result;
                    } else {
                        userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                    }
                }

                result.put("login_result", "N");
                result.put("login_result_message", "로그인에 실패하였습니다. 다시 시도해 주시기 바랍니다.");
            }
        }

        String accessToken = result.get("x-access-token");
        if(accessToken != null ) {
            insertLoginHistory(request, accessToken);    
        }

        return result;
    }

    // 그룹웨어 인증 코드
    public boolean doAuthInGW(String user_id, String password) throws Exception {
        boolean bIsAuthenticated = false;

        String sUserID = user_id;
        String sUserPWD = password;

        try {
            String sExternalAuthUrl = "https://autoway.hyundai.net/webservices/sso/password256.asmx";

            URL url = new URL(sExternalAuthUrl);
            javax.net.ssl.HttpsURLConnection httpConn = (javax.net.ssl.HttpsURLConnection) url.openConnection();

            httpConn.setRequestMethod("POST");
            // httpConn.setRequestProperty("User-Agent", USER_AGENT);
            httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "http://hmc.co.kr/CheckPassword");

            StringBuffer sbSoapParam = new StringBuffer();

            sbSoapParam.append("<?xml version='1.0' encoding='utf-8'?>");
            sbSoapParam.append("<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>");
            sbSoapParam.append("<soap:Body>");
            sbSoapParam.append("<CheckPassword xmlns='http://hmc.co.kr/'>");
            sbSoapParam.append("<strUserID>H110000").append(sUserID).append("</strUserID>");
            sbSoapParam.append("<strPassword>").append(sUserPWD).append("</strPassword>");
            sbSoapParam.append("<strCallerID>SSOADMIN</strCallerID>");
            sbSoapParam.append("</CheckPassword>");
            sbSoapParam.append("</soap:Body>");
            sbSoapParam.append("</soap:Envelope>");

            httpConn.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream())) {
                dos.writeBytes(sbSoapParam.toString());
                dos.flush(); // 버퍼 비우기
            } catch (Exception e) {
                log.error("Error while writing to HTTP connection: ", e);
                throw new RuntimeException("Failed to write request to HTTP connection", e);
            }

            int iResCode = httpConn.getResponseCode();

            StringBuffer sbResp = new StringBuffer(); // 블록 외부에 선언

            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
                String sTmpLine;
                sbResp = new StringBuffer();
                while((sTmpLine = br.readLine()) != null) {
                    sbResp.append(sTmpLine);
                }
            } catch (Exception e) {
                log.error("Error while reading from HTTP connection: ", e);
                throw new RuntimeException("Failed to read response from HTTP connection", e);
            }

//            log.debug(sbResp.toString() + "\n");

            int iPos1 = sbResp.indexOf("<CheckPasswordResult>") + "<CheckPasswordResult>".length();
            int iPos2 = sbResp.indexOf("</CheckPasswordResult>");
            String sRstVal = sbResp.substring(iPos1, iPos2);

            log.debug("GW ID[" + sUserID + "], Result[" + sRstVal + "]");

            if ("Y".equals(sRstVal)) {
                bIsAuthenticated = true;
            }
        } catch (Exception e) {
            bIsAuthenticated = false;
        }

        return bIsAuthenticated;
    }

    // AD 인증코드
    public boolean doAuthInAD(String user_id, String password) throws Exception {
        boolean bIsAuthenticated = false;

        String sUserID = user_id;
        String sUserPWD = password;

        try {
            String sExternalAuthUrl = "http://ebcon.rotem.co.kr/UserService.asmx";
        
            URL url = new URL(sExternalAuthUrl);
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) url.openConnection();

            httpConn.setRequestMethod("POST");
            // httpConn.setRequestProperty("User-Agent", USER_AGENT);
            httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "http://tempuri.org/checkAD");

            StringBuffer sbSoapParam = new StringBuffer();

            sbSoapParam.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            sbSoapParam.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
            sbSoapParam.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
            sbSoapParam.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
            sbSoapParam.append("<soap:Body>");
            sbSoapParam.append("<checkAD xmlns=\"http://tempuri.org/\">");
            sbSoapParam.append("<oid>").append(sUserID).append("</oid>");
            sbSoapParam.append("<pw>").append(sUserPWD).append("</pw>");
            sbSoapParam.append("</checkAD>");
            sbSoapParam.append("</soap:Body>");
            sbSoapParam.append("</soap:Envelope>");

            httpConn.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream())) {
                dos.writeBytes(sbSoapParam.toString());
                dos.flush(); // 버퍼 비우기
            } catch (Exception e) {
                log.error("Error while writing to HTTP connection: ", e);
                throw new RuntimeException("Failed to write request to HTTP connection", e);
            }

            int iResCode = httpConn.getResponseCode();
            // System.out.println(iResCode);

            StringBuffer sbResp = new StringBuffer(); // 블록 외부에 선언

            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
                String sTmpLine;
                sbResp = new StringBuffer();
                while((sTmpLine = br.readLine()) != null) {
                    sbResp.append(sTmpLine);
                }
            } catch (Exception e) {
                log.error("Error while reading from HTTP connection: ", e);
                throw new RuntimeException("Failed to read response from HTTP connection", e);
            }

//            log.debug(sbResp.toString() + "\n");

            int iPos1 = sbResp.indexOf("<checkADResult>") + "<checkADResult>".length();
            int iPos2 = sbResp.indexOf("</checkADResult>");
            String sRstVal = sbResp.substring(iPos1, iPos2);

            log.debug("AD Authentication: ID=" + sUserID + ", Result=" + sRstVal + "");
            bIsAuthenticated = Boolean.parseBoolean(sRstVal);
        } catch (Exception e) {
            bIsAuthenticated = false;
        }

        return bIsAuthenticated;
    }

    // 부서 목록 전체 조회
    public Object getDeptsByCategory(String word) throws Exception {
        return userMapper.getAllDepartments(word);
    }

    public boolean exAuth(String user_id, String password) throws Exception {
        boolean exAuthenticate = false;

        // 관리자 예외처리
        if (user_id.equals(adminId)) {
            // 사용자 패스워드 확인 후 맞으면 통과
            Map adminUser = (Map) userMapper.getUserMaster(user_id);
            if (password.equals(adminPasswd)) { // 1. 패스워드 검증 성공
                if (adminUser != null) {
                    // 로그인 실패 횟수 확인
                    if( (Integer) adminUser.get("login_fail_cnt") >= 5 ){
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        Timestamp loginTime = (Timestamp) adminUser.get("last_login");
                        long milliseconds = now.getTime() - loginTime.getTime();
                        int seconds = (int) milliseconds / 1000;
                        int miniutes = seconds / 60;
                        if(miniutes < 60) {
                            userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                            exAuthenticate = false;
                        } else {
                            userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                            exAuthenticate = true;
                        }
                    } else {
                        exAuthenticate = true;    
                    }
                    
                } else { // 임시 사용자가 등록되어 있지 않은 경우 첫 로그인이기에 업데이트
                    userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트
                    exAuthenticate = true;
                }
            } else { // 2. 패스워드 검증 실패
                // 사용자 로그인 실패 횟수 증가 처리
                // 임시 사용자가 등록되어 있지 않은 경우
                if (adminUser != null) {
                    // 로그인 실패 횟수 확인
                    if( (Integer) adminUser.get("login_fail_cnt") >= 5 ){
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        Timestamp loginTime = (Timestamp) adminUser.get("last_login");
                        long milliseconds = now.getTime() - loginTime.getTime();
                        int seconds = (int) milliseconds / 1000;
                        int miniutes = seconds / 60;
                        if(miniutes < 60) {
                            exAuthenticate = false;
                        } else {
                            userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                            exAuthenticate = false;
                        }
                    }

                    userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                    exAuthenticate = false;
                    
                } else { // 첫 로그인이기에 업데이트
                    userMapper.updateLoginUser(user_id); // 신규 사용자 정보 업데이트
                    userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                    exAuthenticate = false;
                }
            }
        }

        // 임시계정 예외처리
        String[] tempUserIdList = tempUserIds.split(",");
        String[] tempUserPasswdList = tempUserPasswds.split(",");

        for (int i=0; i<tempUserIdList.length; i++) {    
            if (user_id.equals(tempUserIdList[i])) {
                // 사용자 패스워드 확인 후 맞으면 통과
                Map exUser = (Map) userMapper.getUserMaster(user_id);
                if (password.equals(tempUserPasswdList[i])) { // 1. 패스워드 검증 성공
                    if (exUser != null) {
                        // 로그인 실패 횟수 확인
                        if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                            Timestamp now = new Timestamp(System.currentTimeMillis());
                            Timestamp loginTime = (Timestamp) exUser.get("last_login");
                            long milliseconds = now.getTime() - loginTime.getTime();
                            int seconds = (int) milliseconds / 1000;
                            int miniutes = seconds / 60;
                            if(miniutes < 60) {
                                userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                                exAuthenticate = false;
                            } else {
                                userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                                exAuthenticate = true;
                            }
                        } else {
                            exAuthenticate = true;    
                        }
                        
                    } else { // 임시 사용자가 등록되어 있지 않은 경우 첫 로그인이기에 업데이트
                        userMapper.updateLoginUser(user_id); // 사용자 정보 업데이트
                        exAuthenticate = true;
                    }
                } else { // 2. 패스워드 검증 실패
                    // 사용자 로그인 실패 횟수 증가 처리
                    // 임시 사용자가 등록되어 있지 않은 경우
                    if (exUser != null) {
                        // 로그인 실패 횟수 확인
                        if( (Integer) exUser.get("login_fail_cnt") >= 5 ){
                            Timestamp now = new Timestamp(System.currentTimeMillis());
                            Timestamp loginTime = (Timestamp) exUser.get("last_login");
                            long milliseconds = now.getTime() - loginTime.getTime();
                            int seconds = (int) milliseconds / 1000;
                            int miniutes = seconds / 60;
                            if(miniutes < 60) {
                                exAuthenticate = false;
                            } else {
                                userMapper.updateLoginUser(user_id);// 로그인 fail 개수 초기화
                                exAuthenticate = false;
                            }
                        }

                        userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                        exAuthenticate = false;
                        
                    } else { // 첫 로그인이기에 업데이트
                        userMapper.updateLoginUser(user_id); // 신규 사용자 정보 업데이트
                        userMapper.updateLoginFailCount(user_id); // 로그인 fail 개수 증가 처리
                        exAuthenticate = false;
                    }
                    break;
                }
            }
        }

        return exAuthenticate;
    }

    public Integer insertLoginHistory(HttpServletRequest request, String token) throws Exception {
        Map<String, Object> userInfo = sessionManager.getUserByAccessTokenValue(token);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userInfo.get("user_id").toString());
        param.put("username", userInfo.get("username").toString());
        param.put("user_ip_addr", IpAddressUtil.getClientIp(request));
        // param.put("last_login_token", token);

        return userMapper.insertLoginHistory(param);
    }

    /**
     * 특정 사용자의 마지막 로그인 시간을 반환
     */
    public String lastLoginDate(String userId) throws Exception{
        return userMapper.getLastLoginTime(userId);
    }
}
