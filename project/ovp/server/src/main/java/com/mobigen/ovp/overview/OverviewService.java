package com.mobigen.ovp.overview;

import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.overview.client.MonitoringClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OverviewService {

    private final SearchClient searchClient;
    private final MonitoringClient monitoringClient;

    /**
     * 서비스 타입 요약
     *
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> geTypeSummary() throws Exception {
        List<Map<String, Object>> typeList = new ArrayList<>();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("index", "table_search_index");
        params.add("from", "0");
        params.add("size", "0");
        params.add("deleted", "false");

        Map<String, Object> result = searchClient.getSearchList(params);

        if (result.get("aggregations") != null) {
            Map<String, Object> aggregations = (Map<String, Object>) result.get("aggregations");
            if (aggregations.get("sterms#serviceType") != null) {
                Map<String, Object> serviceType = (Map<String, Object>) aggregations.get("sterms#serviceType");
                if (serviceType.get("buckets") != null) {
                    List<Map<String, Object>> buckets = (List<Map<String, Object>>) serviceType.get("buckets");
                    typeList = buckets.stream().map(service -> {
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("name", service.get("key"));
                        temp.put("value", service.get("doc_count"));

                        return temp;
                    }).collect(Collectors.toList());
                }
            }
        }

        return typeList;
    }

    /**
     * 서비스 상태 요약
     *
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getStatusSummary() throws Exception {
        List<Map<String, Object>> statusList = new ArrayList<>();

        Map<String, Object> monitoringConnectStatus = (Map<String, Object>) monitoringClient.connectStatus().get("data");

        monitoringConnectStatus.forEach((k, v) -> {
            Map<String, Object> status = new HashMap<>();
            if ("total".equals(k)) {
                return;
            }

            status.put("name", k);
            status.put("value", v);
            statusList.add(status);
        });

        return statusList;
    }

    /**
     * 요약 데이터
     *
     * @return
     * @throws Exception
     */
    public Object getSummaryInfo() throws Exception {
        Map<String, Object> result = new HashMap<>();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("index", "table_search_index");
        params.add("from", "0");
        params.add("size", "0");
        params.add("deleted", "false");

        List<Map<String, Object>> typeSummary = geTypeSummary();
        List<Map<String, Object>> statusList = getStatusSummary();

        result.put("typeSummary", typeSummary);
        result.put("statusList", statusList);

        return result;
    }

    /**
     * 서비스 응답 시간
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getResponseTime(MultiValueMap<String, String> params) throws Exception {
        return monitoringClient.getResponseTime(params);
    }

    /**
     * 등록된 모델 등록 현황
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getServiceModelList(MultiValueMap<String, String> params) throws Exception {
        return monitoringClient.getMonitoringModels(params);
    }

    /**
     * 서비스 상태
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getConnectionHistory(MultiValueMap<String, String> params) throws Exception {
        return monitoringClient.getConnectionHistory(params);
    }

    /**
     * 수집 히스토리
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getIngestionHistory(MultiValueMap<String, String> params) throws Exception {
        return monitoringClient.getIngestionHistory(params);
    }
}
