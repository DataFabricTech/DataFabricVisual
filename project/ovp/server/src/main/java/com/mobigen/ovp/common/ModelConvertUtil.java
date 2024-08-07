package com.mobigen.ovp.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelConvertUtil {

    /**
     * Open Meta Api 에서 모델 데이터 조회 한 후에, 해당 데이터를 화면에 사용 할 수 있게 데이터 정제하는 부분을 공통 코드로 구현.
     * Open Meta Api 에서 response 를
     * 1. 복수건 으로 받았을 경우, hits 를 통째로 받아서 처리하거나
     * 2. 단건 으로 받았을 경우, _source 를 받아서 처리한다.
     *
     * @param hits
     * @return
     */
    public Object convertSearchDataList(Object hits) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) hits;
        List<Map<String, Object>> modifiedList = list.stream().map(hit -> {
            String index = hit.get("_index").toString().equals("table_search_index") ? "table" : "storage";
            if (hit.containsKey("_source")) {
                Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                return convertSourceDataOne(index, source);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return modifiedList;
    }

    /**
     * 단건(_source) 받아서 처리
     *
     * @param source
     * @return
     */
    public Map<String, Object> convertSourceDataOne(String index, Map<String, Object> source) {
        Map<String, Object> modifiedSource = new HashMap<>();

        String serviceType = source.get("serviceType") != null ? source.get("serviceType").toString().toLowerCase() : "";
        modifiedSource.put("type", "trino".equals(serviceType) ? "model" : index);

        modifiedSource.put("id", source.get("id"));
        // TODO : ICON 처리 완료되면 아래 코드 수정 필요
        modifiedSource.put("serviceIcon", "");

        modifiedSource.put("depth", source.get("fullyQualifiedName") != null ? source.get("fullyQualifiedName").toString().split("\\.") : new String[]{});
        modifiedSource.put("firModelNm", source.get("displayName"));
        modifiedSource.put("modelNm", source.get("name"));
        modifiedSource.put("modelDesc", source.get("description"));
        modifiedSource.put("fqn", source.get("fullyQualifiedName"));

        String owner = "";
        if (source.get("owner") != null) {
            Map<String, Object> ownerObj = (Map<String, Object>) source.get("owner");
            owner = ownerObj.get("displayName").toString();
        }
        modifiedSource.put("owner", owner);

        String domain = "";
        if (source.get("domain") != null) {
            Map<String, Object> domainObj = (Map<String, Object>) source.get("domain");
            domain = domainObj.get("displayName").toString();
        }
        modifiedSource.put("category", domain);

        return modifiedSource;
    }
}

