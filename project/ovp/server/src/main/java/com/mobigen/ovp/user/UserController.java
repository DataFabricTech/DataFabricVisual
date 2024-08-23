package com.mobigen.ovp.user;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 모든 사용자 호출
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult()
    @GetMapping("/all")
    public Object getAllUserList() throws Exception {
        return userService.getAllUserList();
    }

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

    /**
     * 관리자 > 사용자 정보 목록 조회
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 목록 조회 오류")
    @GetMapping("/list")
    public Object getUserList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return userService.getUserList(params);
    }

    /**
     * 관리자 > 사용자 정보 삭제
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 정보 삭제 오류")
    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable UUID id) throws Exception {
        return userService.deleteUser(id);
    }

    /**
     * 관리자 > 랜덤 비밀번호 발급
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "랜덤 비밀번호 자동 생성 오류")
    @GetMapping("/random-pwd")
    public Object getRandomPwd() throws Exception {
        return userService.getRandomPwd();
    }

    /**
     * 관리자 > 사용자 이름 중복 체크
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 이름 중복 체크 오류")
    @GetMapping("/check-name/{name}")
    public Object checkDuplicateName(@PathVariable String name) throws Exception {
        return userService.checkDuplicateName(name);
    }

    /**
     * 관리자 > 사용자 정보 추가
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 추가 오류")
    @PostMapping("/add")
    public Object addUser(@RequestBody Map<String, Object> params) throws Exception {
        return userService.addUser(params);
    }
}
