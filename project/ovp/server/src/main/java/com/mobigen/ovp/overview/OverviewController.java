package com.mobigen.ovp.overview;

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

    @GetMapping("/summary/info")
    public Object getSummaryInfo() throws Exception {
        log.info("/api/service/overview/getSummaryInfo");

        return overviewService.getSummaryInfo();
    }

    @GetMapping("/summary/response-time")
    public Object getResponseTime(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("/api/service/overview/getSummaryResponseTime");
        // params: page = 0, size = 5, orderByAsc = true;

        return overviewService.getResponseTime(params);
    }

    @GetMapping("/summary/model-status")
    public Object getModelStatus(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("/api/service/overview/getModelStatus");
        // params: page = 0, size = 5

        return overviewService.getModelStatus(params);
    }
}
