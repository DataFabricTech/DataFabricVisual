package com.mobigen.ovp.main;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class MainController {
    private final MainService mainService;


    /**
     * 사용자가 북마크 한 데이터 정보 출력
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 북마트 정보 출력 오류")
    @GetMapping("/follows/{id}")
    public Object getUserFollows(@PathVariable("id") String id) throws Exception {
        return mainService.getUserFollows(id);
    }
}
