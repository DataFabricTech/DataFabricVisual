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

    @Value("${properties.tempuser.ids}")
    private final String[] tempUsers;
    

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
        String username = user_map.get("user_id").toString();
        return userService.regenerateAccessToken(username);
    }

    // Refresh Token 재발급
    @ResponseJsonResult
    @PostMapping("/regenerateRefreshToken")
    public Object regenerateRefreshToken(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user_map = sessionManager.getUserByAccessToken(request);
        String username = user_map.get("user_id").toString();
        return userService.regenerateRefreshToken(username);
    }

    @ResponseJsonResult
    @PostMapping("/authenticate")
    public Object authenticate(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = param.get("username");
        // String password = sessionManager.decryptRSAString(request, (String)param.get("password")); // RSA PRIVATE KEY null 상태 탐색중
        String password = param.get("password");
        return userService.authenticate(request, response, username, password);
    }

    @ResponseJsonResult
    @PostMapping("/authenticateGW")
    public Object authenticateGW(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = param.get("username");
        // String password = sessionManager.decryptRSAString(request, (String)param.get("password")); // RSA PRIVATE KEY null 상태 탐색중
        String password = param.get("password");
        return userService.authenticateGW(request, response, username, password);
    }

    @ResponseJsonResult
    @PostMapping("/authenticateAD")
    public Object authenticateAD(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = param.get("username");
        // String password = sessionManager.decryptRSAString(request, (String)param.get("password")); // RSA PRIVATE KEY null 상태 탐색중
        String password = param.get("password");
        return userService.authenticateAD(request, response, username, password);
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
    public Object getDeptsByCategory(@RequestParam("word") String word) throws Exception {
        return userService.getDeptsByCategory(word);
    }

}
