package com.mobigen.ovp.service;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;
    /**
     * service : Service - 수집 - 동작 [log] 조회
     * @param id
     * @return
     * **/
    @ResponseJsonResult
    @GetMapping("/collection/log/{id}")
    public Object getServiceCollectionLog(@PathVariable String id) {
        return serviceService.getServiceCollectionLog(id);
    }

}
