package com.mobigen.ovp.overview.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SummaryInfoResponse {
    // 서비스 타입 요약
    List<Map<String, Object>> typeSummary;
    // 서비스 상태 요약
    List<Map<String, Object>> statusSummary;
    // 서비스 응답시간
    Map<String, Object> responseTime;
    // 등록된 데이터 모델 현황
    List<List<String>> modelStatus;

    public SummaryInfoResponse(Map<String, Object> typeSummary, Map<String, Object> statusSummary, Map<String, Object> responseTime, List<List<String>> modelStatus) {
        // 서비스 타입 요약
        this.typeSummary = new ArrayList<>();
        if (typeSummary != null) {
            Map<String, Object> aggregations = (Map<String, Object>) typeSummary.get("aggregations");
            if (aggregations != null) {
                Map<String, Object> serviceType = (Map<String, Object>) aggregations.get("sterms#serviceType");
                if (serviceType != null) {
                    List<Map<String, Object>> buckets = (List<Map<String, Object>>) serviceType.get("buckets");
                    if (buckets != null) {
                        for (Map<String, Object> bucket : buckets) {
                            Map<String, Object> summary = new HashMap<>();
                            summary.put("name", bucket.get("key"));
                            summary.put("value", bucket.get("doc_count"));

                            this.typeSummary.add(summary);
                        }
                    }
                }
            }
        }

        // 서비스 상태 요약
        this.statusSummary = new ArrayList<Map<String, Object>>();
        System.out.println(statusSummary);
        if (statusSummary != null && statusSummary.get("data") != null) {
            Map<String, Object> data = (Map<String, Object>) statusSummary.get("data");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                if ("total".equals(key)) {
                    continue;
                }

                Map<String, Object> summary = new HashMap<>();
                summary.put("name", key);
                summary.put("value", entry.getValue());

                this.statusSummary.add(summary);
            }
        }

        // 서비스 응답시간
        this.responseTime = responseTime;

        // 등록된 데이터 모델 현황
        this.modelStatus = modelStatus;
    }
}
