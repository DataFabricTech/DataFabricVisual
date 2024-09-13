package com.mobigen.ovp.overview;

import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.overview.client.MonitortingClient;
import com.mobigen.ovp.overview.dto.response.ModelStatusResponse;
import com.mobigen.ovp.overview.dto.response.SummaryInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OverviewService {

    private final SearchClient searchClient;
    private final MonitortingClient monitortingClient;

    public Object getSummaryInfo() throws Exception {
        // 서비스 타입 요약
        MultiValueMap<String, String> typeSummaryParams = new LinkedMultiValueMap<>();
        // index=table_search_index&from=0&size=0&deleted=false
        typeSummaryParams.add("index", "table_search_index");
        typeSummaryParams.add("from", "0");
        typeSummaryParams.add("size", "0");
        typeSummaryParams.add("deleted", "false");

        Map<String, Object> typeSummary = searchClient.getSearchList(typeSummaryParams);

        // 서비스 상태 요약
        Map<String, Object> statusSummary = monitortingClient.getConnectStatus();

        // 서비스 응답시간
        MultiValueMap<String, String> responseTimeParams = new LinkedMultiValueMap<>();
        responseTimeParams.add("page", "0");
        responseTimeParams.add("size", "5");
        responseTimeParams.add("orderByAsc", "true");

        Map<String, Object> responseTime = monitortingClient.getResponseTime(responseTimeParams);

        // 등록된 데이터 모델 현황
        MultiValueMap<String, String> modelStatusParams = new LinkedMultiValueMap<>();
        modelStatusParams.add("page", "0");
        modelStatusParams.add("size", "5");

        List<List<String>> modelStatus = new ModelStatusResponse(monitortingClient.getModels(modelStatusParams)).getSource();

        return new SummaryInfoResponse(typeSummary, statusSummary, responseTime, modelStatus);
    }

    public Object getResponseTime(MultiValueMap<String, String> params) throws Exception {
        return monitortingClient.getResponseTime(params);
    }

    public Object getModelStatus(MultiValueMap<String, String> params) throws Exception {
        return new ModelStatusResponse(monitortingClient.getModels(params)).getSource();
    }
}
