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
    private List<Map<String, Object>> getTypeSummary() throws Exception {
        List<Map<String, Object>> typeList = new ArrayList<>();

        MultiValueMap<String, String> dsiParams = new LinkedMultiValueMap<>();
        dsiParams.add("index", "database_search_index");
        dsiParams.add("from", "0");
        dsiParams.add("size", "0");
        dsiParams.add("deleted", "false");

        MultiValueMap<String, String> sssiParams = new LinkedMultiValueMap<>();
        sssiParams.add("index", "storage_service_search_index");
        sssiParams.add("from", "0");
        sssiParams.add("size", "0");
        sssiParams.add("deleted", "false");

        Map<String, Object> tableResult = searchClient.getSearchList(dsiParams);
        Map<String, Object> storageResult = searchClient.getSearchList(sssiParams);

        addBucketsToList(typeList, tableResult);
        addBucketsToList(typeList, storageResult);

        return typeList;
    }

    private void addBucketsToList(List<Map<String, Object>> typeList, Map<String, Object> result) {
        if (result.get("aggregations") != null) {
            Map<String, Object> aggregations = (Map<String, Object>) result.get("aggregations");
            if (aggregations.get("sterms#serviceType") != null) {
                Map<String, Object> serviceType = (Map<String, Object>) aggregations.get("sterms#serviceType");
                if (serviceType.get("buckets") != null) {
                    List<Map<String, Object>> buckets = (List<Map<String, Object>>) serviceType.get("buckets");
                    List<Map<String, Object>> bucketData = buckets.stream().map(service -> {
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("name", service.get("key"));
                        temp.put("value", service.get("doc_count"));
                        return temp;
                    }).collect(Collectors.toList());

                    typeList.addAll(bucketData);
                }
            }
        }
    }

    /**
     * 서비스 상태 요약
     *
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getStatusSummary() throws Exception {
        List<Map<String, Object>> statusList = new ArrayList<>();

        Object monitoringConnectStatus = monitoringClient.connectStatus().get("data");
        List<Map<String, Object>> statusData = (List<Map<String, Object>>) monitoringConnectStatus;

        // 상태별 개수 집계
        Map<String, Long> statusCount = statusData.stream()
                .collect(Collectors.groupingBy(
                        service -> (String) service.get("connectionStatus"),
                        Collectors.counting()
                ));

        // CONNECTED, DISCONNECTED, CONNECT_ERROR 상태를 포함하는 결과 생성
        statusList.add(createStatusMap("connected", statusCount.getOrDefault("CONNECTED", 0L)));
        statusList.add(createStatusMap("disconnected", statusCount.getOrDefault("DISCONNECTED", 0L)));
        statusList.add(createStatusMap("connectError", statusCount.getOrDefault("CONNECT_ERROR", 0L)));

        return statusList;
    }

    private Map<String, Object> createStatusMap(String name, Long value) {
        Map<String, Object> status = new HashMap<>();
        status.put("name", name);
        status.put("value", value);
        return status;
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

        List<Map<String, Object>> typeSummary = getTypeSummary();
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
