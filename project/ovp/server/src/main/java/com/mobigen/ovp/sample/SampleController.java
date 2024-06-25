package com.mobigen.ovp.sample;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequestMapping("/api/sample")
@Controller
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
        return sampleService.getUsers();
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
}
