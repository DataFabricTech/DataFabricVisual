package com.mobigen.ovp.sample;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RequestMapping("/api/sample")
@RestController
@RequiredArgsConstructor
public class SampleController {
    private final SampleService sampleService;
    /**
     * Sample : HTTP Interface 사용 예시
     * @return
     */
    @ResponseJsonResult
    @GetMapping("")
    public Object getSampleApi() {
        return sampleService.getSampleApi();
    }

    /**
     * Sample : HTTP Interface 사용 예시 - API 오류
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/api-error")
    public Object getErrorApi() {
        log.info("error api");
        return sampleService.getErrorApi();
    }

    /**
     * Sample : HTTP Interface 사용 예시 - 동적 URL 사용 가능
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/test")
    public Object getApi() {
        return sampleService.getDynamicAPI();
    }

    /**
     * Sample : 어노테이션 에러 출력
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "어노테이션 에러 메세지 출력")
    @GetMapping("/errorMessage/anno")
    public Object getAnnotationErrorMessage() throws Exception {
        return sampleService.getAnnotationErrorMessage();
    }

    /**
     * Sample : 에러 처리
     * @return
     * @throws Exception
     */
    @ResponseJsonResult()
    @GetMapping("/errorMessage/basic")
    public Object getBasicErrorMessage() throws Exception {
        return sampleService.getBasicErrorMessage();
    }

    /**
     * Sample : 어노테이션 에러 사용 시 우선 출력
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "어노테이션 우선 출력")
    @GetMapping("/errorMessage/multi")
    public Object getMultiErrorMessage() throws Exception {
        return sampleService.getMultiErrorMessage();
    }

    /**
     * Open Metadata - 사용자 목록 조회
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 사용자 목록 조회 오류")
    @GetMapping("/open-metadata/user/list")
    public Object getUsers(HttpServletRequest request) throws Exception {
        return sampleService.getUsers(request);
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 탐색 목록 조회 오류")
    @GetMapping("/open-metadata/search/query")
    public Object getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("params : {}", params);
        return sampleService.getSearchList(params);
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     * @param table
     * @param fields
     * @param include
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 테이블 상세 조회 오류")
    @GetMapping("/open-metadata/tables/name/{table}")
    public Object getTableDetail(@PathVariable("table") String table, @RequestParam String fields, @RequestParam String include) throws Exception {
        return sampleService.getTableDetail(table, fields, include);
    }
}
