package com.mobigen.datalake.monitoring;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MonitoringService {
    private final MonitoringMapper monitoringMapper;

    // 모니터링 차트 데이터 조회
    public Map<String, Object> getChartData() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> systemTypeCountChartData = new ArrayList<>();
        List<Map<String, Object>> metaTypeCountChartData = new ArrayList<>();
        List<Map<String, Object>> systemResponseData = new ArrayList<>();
        List<Map<String, Object>> systemUsageCpuChartData = new ArrayList<>();
        List<Map<String, Object>> systemUsageMemoryChartData = new ArrayList<>();
        List<Map<String, Object>> systemQuerySuccessFailedChartData = new ArrayList<>();
            
        
        try {            
            int currentYear = LocalDate.now().getYear();

            // 시스템 타입별 메타데이터 수
            systemTypeCountChartData = monitoringMapper.getSystemTypeCountData();
            
            // 메타 타입별 메타데이터 수
            metaTypeCountChartData = monitoringMapper.getMetaTypeCountData();

            // 시스템별 응답시간
            systemResponseData = monitoringMapper.systemResponseData();
            
            // 시스템별 CPU 사용량
            systemUsageCpuChartData = monitoringMapper.systemUsageCpuChartData();
            
            // 시스템별 Memory
            systemUsageMemoryChartData = monitoringMapper.systemUsageMemoryChartData();
            
            // 시스템별 Query (성공/실패) 수
            systemQuerySuccessFailedChartData = monitoringMapper.systemQuerySuccessFailedChartData();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Failed to load monitoring chart data !!");
        }    

        result.put("systemTypeCountChartData", systemTypeCountChartData);
        result.put("metaTypeCountChartData", metaTypeCountChartData);
        result.put("systemResponseData", systemResponseData);
        result.put("systemUsageCpuChartData", systemUsageCpuChartData);
        result.put("systemUsageMemoryChartData", systemUsageMemoryChartData);
        result.put("systemQuerySuccessFailedChartData", systemQuerySuccessFailedChartData);

        return result;
    }

    // 모니터링 CPU 사용량 차트 데이터 조회
    public Map<String, Object> getUsageCpuChartData() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> systemUsageCpuChartData = new ArrayList<>();
            
        
        try {            
            // 시스템별 CPU 사용량
            systemUsageCpuChartData = monitoringMapper.systemUsageCpuChartData();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Failed to load monitoring chart data !!");
        }    
        result.put("systemUsageCpuChartData", systemUsageCpuChartData);

        return result;
    }

    // 모니터링 Memory 사용량 차트 데이터 조회
    public Map<String, Object> getUsageMemoryChartData() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> systemUsageMemoryChartData = new ArrayList<>();
            
        try {            
            // 시스템별 Memory
            systemUsageMemoryChartData = monitoringMapper.systemUsageMemoryChartData();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Failed to load monitoring chart data !!");
        }    
        result.put("systemUsageMemoryChartData", systemUsageMemoryChartData);

        return result;
    }
}
