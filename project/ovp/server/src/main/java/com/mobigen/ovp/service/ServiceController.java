package com.mobigen.ovp.service;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api/service")
@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * 서비스 명 중복인지 체크
     *
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/isDuplicatedNm")
    public Object isDuplicatedNm(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return serviceService.checkDuplicatedNm(params);
    }

    /**
     * 서비스 명 중복인지 체크
     *
     * @return
     */
    @ResponseJsonResult
    @PostMapping(value = "/connectionTest")
    public Object connectionTest(@RequestBody Map<String, Object> params) throws Exception {
        return serviceService.connectionTest(params);
    }

}
