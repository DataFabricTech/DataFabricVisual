package com.mobigen.datalake.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/monitoring")
@Controller
@AllArgsConstructor
public class MonitoringController {
    
    private final MonitoringService monitoringService;

    @ResponseJsonResult
    @GetMapping("/chartData")
    public Object chartData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return monitoringService.getChartData();
    }
}
