package com.mobigen.ovp.service;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
