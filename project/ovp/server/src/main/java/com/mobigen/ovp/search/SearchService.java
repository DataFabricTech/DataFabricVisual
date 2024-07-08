package com.mobigen.ovp.search;

import com.mobigen.ovp.search.client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchClient searchClient;

    private Map<String, Object> convertAggregations(Map<String, Object> response) {
        Map<String, Object> aggregations = (Map<String, Object>) response.get("aggregations");
        Map<String, Object> resultMap = new HashMap<>();

        if (aggregations != null) {
            for (String key : aggregations.keySet()) {
                Map<String, Object> value = (Map<String, Object>) aggregations.get(key);
                Object buckets = value.get("buckets");

                // sterms#... 이런 식으로 key 가 설정되어 있기 때문에 해당 부분 제거해준다. (사용하지 않음)
                String[] parts = key.split("#");
                String newKey = parts.length > 1 ? parts[1] : parts[0];

                if (buckets != null) {
                    resultMap.put(newKey, buckets);
                }
            }
        }
        return resultMap;
    }

    public Object getFilter(MultiValueMap<String, String> params) {
        return convertAggregations(searchClient.getFilter(params));
    }

    public Object getFilters() throws Exception {
        // filter 조회는 params 가 필요 없기 때문에 공백으로 api 호출
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, Object> response = searchClient.getSearchList(params);
        Map<String, Object> result = convertAggregations(response);

        // TODO: category 구현 완료 되면 여기에 추가.
        return result;
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        Map<String, Object> result = searchClient.getSearchList(params);
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> data = (Map<String, Object>) result.get("hits");
        if (data != null) {
            Map<String, Object> totalObj = (Map<String, Object>) data.get("total");
            if (totalObj != null) {
                resultMap.put("totalCount", totalObj.get("value"));
            }
            resultMap.put("data", data.get("hits"));
        }

        return resultMap;
    }
}
