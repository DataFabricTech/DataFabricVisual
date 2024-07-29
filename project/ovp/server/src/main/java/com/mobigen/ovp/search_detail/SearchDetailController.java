package com.mobigen.ovp.search_detail;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search/detail")
public class SearchDetailController {

    private final SearchDetailService searchDetailService;

    @ResponseJsonResult
    @GetMapping("/{id}")
    Object getDataModelDetail(@PathVariable String id) {
        log.info("");

        return searchDetailService.getDataModelDetail(id);
    }

    @ResponseJsonResult
    @GetMapping("/schema/{id}")
    Object getDataModelSchema(@PathVariable String id) {
        log.info("");

        return searchDetailService.getDataModelSchema(id);
    }

    @ResponseJsonResult
    @GetMapping("/sample-data/{id}")
    Object getDataModelSampleData(@PathVariable String id) {
        log.info("");
        return searchDetailService.getDataModelSampleData(id);
    }

    @ResponseJsonResult
    @GetMapping("/profile/{id}")
    Object getTableProfile(@PathVariable String id) {
        log.info("");

        return searchDetailService.getTableProfile(id);
    }
}
