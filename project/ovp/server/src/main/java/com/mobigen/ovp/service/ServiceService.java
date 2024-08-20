package com.mobigen.ovp.service;

import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {

    private final SearchService searchService;
    private final SearchClient searchClient;

    public Boolean checkDuplicatedNm(MultiValueMap<String, String> params) throws Exception {
        String index = params.getFirst("index");

        String newIndex = index.toLowerCase().equals("minio") ? "container" : "table";
        params.set("index", newIndex);

        Object result = searchClient.getSearchList(params);
        Map<String, Object> response = searchService.convertToMap((Map<String, Object>) result);
        return Integer.parseInt(response.get("totalCount").toString()) > 0;
    }
}
