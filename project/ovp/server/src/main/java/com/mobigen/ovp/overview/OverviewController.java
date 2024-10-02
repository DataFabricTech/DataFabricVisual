package com.mobigen.ovp.overview;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service/overview")
public class OverviewController {
    private final OverviewService overviewService;

    /**
     * 요약 데이터
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "요약 데이터 조회 에러")
    @GetMapping("/summary-info")
    public Object getSummaryInfo() throws Exception {
        log.info("get overview summary info");

        return overviewService.getSummaryInfo();
    }

    /**
     * 서비스 응답 시간
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "서비스 응답시간 조회 에러")
    @GetMapping("/response-time")
    public Object getResponseTime(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("get overview response time");

        return overviewService.getResponseTime(params);
    }

    /**
     * 등록된 모델 등록 현황
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "등록된 모델 등록 현황 조회 에러")
    @GetMapping("/service-models")
    public Object getServiceModelList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("get overview service models");

        return overviewService.getServiceModelList(params);
    }

    /**
     * 서비스 상태
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "서비스 상태 조회 에러")
    @GetMapping("/connection-history")
    public Object getConnectionHistory(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("get overview connection history");

        return overviewService.getConnectionHistory(params);
    }

    /**
     * 수집 히스토리
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "수집 히스토리 에러")
    @GetMapping("ingestion-history")
    public Object getIngestionHistory(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("get overview ingestion history");

        return overviewService.getIngestionHistory(params);
    }
}
