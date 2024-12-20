package com.mobigen.ovp.main;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class MainController {
    private final MainService mainService;
    private final SearchService searchService;


    /**
     * 사용자가 북마크 한 데이터 정보 출력
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "사용자 북마크 정보 출력 오류")
    @GetMapping("/follows/{id}")
    public Object getUserFollows(@PathVariable("id") String id) throws Exception {
        return mainService.getUserFollows(id);
    }

    @ResponseJsonResult(errorMessage = "데이터 모델 목록 조회 오류")
    @GetMapping("/list")
    public Object getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return searchService.getBothSearchList(params);
    }
}
