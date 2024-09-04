package com.mobigen.ovp.search;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RequestMapping("/api/search")
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final CategoryService categoryService;

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
        Map<String, Object> responseMap = searchService.getFilters();
        responseMap.put("category", categoryService.getCategories());
        return responseMap;
    }

    /**
     * 탐색 - 목록 - 데이터 모델 조회
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "데이터 모델 목록 조회 오류")
    @GetMapping("/list")
    public Object getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception {

        // q 파라메터에 filter 가 걸려있어서 특수문자가 치환됨. tempQuery 를 이용해서 AND (UUID) 등으로 처리하는 코드를 backend 에서 처리해야 검색어 쿼리가 제대로 동작함.
        if (params.getFirst("tempQuery") != null) {
            params.set("q", params.getFirst("q").toString() + params.getFirst("tempQuery").toString());
            params.remove("tempQuery");
        }

        if (params.get("index").get(0).equals("all")) {
            return searchService.getAllSearchList(params);
        }

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

