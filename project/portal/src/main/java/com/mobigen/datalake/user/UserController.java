package com.mobigen.datalake.user;

import com.mobigen.framework.iris.User;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class UserController {
    private final SessionManager sessionManager;
    private final UserService userService;

    @ResponseJsonResult
    @GetMapping("/authenticate/key")
    public Object publicKey(HttpServletRequest request) throws Exception {
        return sessionManager.generateRSAPublicKey(request);
    }

    // Access Token 재발급
    @ResponseJsonResult
    @PostMapping("/regenerateAccessToken")
    public Object regenerateToken(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user_map = sessionManager.getUserByAccessToken(request);
        String userid = user_map.get("user_id").toString();
        return userService.regenerateAccessToken(userid);
    }

    // Refresh Token 재발급
    @ResponseJsonResult
    @PostMapping("/regenerateRefreshToken")
    public Object regenerateRefreshToken(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user_map = sessionManager.getUserByAccessToken(request);
        String userid = user_map.get("user_id").toString();
        return userService.regenerateRefreshToken(userid);
    }

    @ResponseJsonResult
    @PostMapping("/authenticate")
    public Object authenticate(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String user_id = param.get("user_id");
        // String password = sessionManager.decryptRSAString(request, (String)param.get("password")); // RSA PRIVATE KEY null 상태 탐색중
        String password = param.get("password");
        return userService.authenticate(request, response, user_id, password);
    }

    @ResponseJsonResult
    @GetMapping("/checkDupUserId")
    public Object checkDupUserId(@RequestParam("reg_userid") String reg_userid) throws Exception {
        return userService.checkDupUserId(reg_userid);
    }

    @ResponseJsonResult
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userService.register(param);
    }

    @ResponseJsonResult
    @PostMapping("/delete")
    public Object delete(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userService.delete(param);
    }

    @ResponseJsonResult
    @GetMapping("/user")
    public Object user(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }

    @ResponseJsonResult
    @GetMapping("/heartbeatToken")
    public Object heartBeatToken() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("result", "SUCCESS");
        return result;
    }

    /**
     * 부서 목록 전체 조회
     * @param word             부서 이름
     * @return                 부서 전체 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/depts")
    public Object getDepartments(@RequestParam("word") String word) throws Exception {
        return userService.getDepartments(word);
    }

    // 부서 등록
    @ResponseJsonResult
    @PostMapping("/deptRegister")
    public Object deptRegister(@RequestBody Map<String, String> param) throws Exception {
        return userService.deptRegister(param);
    }

    // 부서 삭제
    @ResponseJsonResult
    @PostMapping("/deptDelete")
    public Object deptDelete(@RequestBody Map<String, String> param) throws Exception {
        return userService.deptDelete(param);
    }

     // role list 조회
     @ResponseJsonResult
     @GetMapping("/roleList")
     public Object getRoleList(@RequestParam("role_type") String role_type) throws Exception {
    return userService.getRoleList(role_type);
     }

     // 부서 정보 조회
     @ResponseJsonResult
     @GetMapping("/getDeptInfo")
     public Object getDeptInfo(@RequestParam("dept_id") String dept_id) throws Exception {
        return userService.getDeptInfo(dept_id);
     }

     // 패스워드 확인
     @ResponseJsonResult
     @PostMapping("/verifyPassword")
     public Object getVerifyPassword(@RequestBody Map<String, String> param) throws Exception {
        String user_id = param.get("user_id");
        String password = param.get("password");

        return userService.getVerifyPassword(user_id, password);
     }

     // 패스워드 변경
     @ResponseJsonResult
     @PostMapping("/changePassword")
     public Object tVerifyPassword(@RequestBody Map<String, String> param) throws Exception {
        String user_id = param.get("user_id");
        String password = param.get("password");

        return userService.changePassword(user_id, password);
     }

}
