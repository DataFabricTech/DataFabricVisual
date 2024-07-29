package com.mobigen.ovp.mypage;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/mypage")
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 - 사용자 정보 조회
     * @param fqn
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 조회 오류")
    @GetMapping("/users/{fqn}")
    public Object getUserInfo(@PathVariable("fqn") String fqn) throws Exception {
        return myPageService.getUserInfo(fqn);
    }

    /**
     * 마이페이지 - 사용자 정보(표시이름, 설명, 역할) 수정
     * @param id
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 정보 수정 오류")
    @PatchMapping("/users/{id}")
    public Object UpdateUserInfo(@PathVariable("id") String id, @RequestBody Map<String, Object> params) throws Exception {
        return myPageService.updateUserInfo(id, params);
    }

}
