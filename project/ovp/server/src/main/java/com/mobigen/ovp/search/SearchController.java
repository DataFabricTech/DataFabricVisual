package com.mobigen.ovp.search;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/api/search")
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * 탐색 - 목록 - filter 단건 조회
     *
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/filter")
    public Object getFilter(@RequestParam MultiValueMap<String, String> params) {
        return searchService.getFilter(params);
    }

    /**
     * 탐색 - 목록 - filter 복수건 조회
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "filter 목록 조회 오류")
    @GetMapping("/filters")
    public Object getFilters() throws Exception {
        return searchService.getFilters();
    }

    /**
     * 탐색 - 목록 - 데이터 모델 조회
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "데이터 모델 목록 조회 오류")
    @GetMapping("/list")
    public Object getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return searchService.getSearchList(params);
    }

    /**
     * 탐색 - 목록 - 미리보기 조회
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "미리보기 조회 오류")
    @GetMapping("/preview/{fqn}")
    public Object getSearchPreview(@PathVariable("fqn") String fqn) {
        return searchService.getSearchPreview(fqn);
    }
}
