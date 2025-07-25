package com.mobigen.datalake.monitoring;

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
        List<Map<String, Object>> systemStatusChartData = new ArrayList<>();
            
        
        try {
            // 시스템 타입별 메타데이터 수
            systemTypeCountChartData = monitoringMapper.getSystemTypeCountData();
            
            // 메타 타입별 메타데이터 수
            metaTypeCountChartData = monitoringMapper.getMetaTypeCountData();

            // 시스템별 응답시간, 시스템별 CPU 사용량, 시스템별 Memory, 시스템별 Query (성공/실패) 수
            systemStatusChartData = monitoringMapper.getSystemStatusData();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Failed to load monitoring chart data !!");
        }    

        result.put("systemTypeCountChartData", systemTypeCountChartData);
        result.put("metaTypeCountChartData", metaTypeCountChartData);
        result.put("systemStatusChartData", systemStatusChartData);

        return result;
    }
}
