package com.mobigen.ovp.user;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 사용자 > 토큰을 통한 사용자 세부 정보 출력
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 출력 오류")
    @GetMapping("/info")
    public Object getUserInfo() throws Exception {
        return userService.getUserInfo();
    }
}
