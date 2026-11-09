package com.mobigen.datalake.monitoring;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("monitoringMapper")
public interface MonitoringMapper {
    List<Map<String, Object>> getSystemTypeList();
    List<Map<String, Object>> getMetaTypeList();

    List<Map<String, Object>> getSystemTypeCountData();
    List<Map<String, Object>> getMetaTypeCountData();

    List<Map<String, Object>> systemResponseData();
    List<Map<String, Object>> systemUsageCpuChartData();
    List<Map<String, Object>> systemUsageMemoryChartData();
    List<Map<String, Object>> systemQuerySuccessFailedChartData();
}
