package com.mobigen.ovp.user;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * 사용자 동기화 > Open metadata의 User와 DB를 동기화 하는 API
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "동기화 실패")
    @PostMapping("/synchronization")
    public Object login() throws Exception {
        return userService.synchronizeUser();
    }
}
