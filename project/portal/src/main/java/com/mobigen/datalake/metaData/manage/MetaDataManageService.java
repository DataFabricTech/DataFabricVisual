package com.mobigen.datalake.metaData.manage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mobigen.datalake.atlas.AtlasRestAPI;
import com.mobigen.framework.result.JsonResult;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class MetaDataManageService {

    private final MetaDataManageMapper metaDataManageMapper;
    private final AtlasRestAPI atlasRestAPI;


    // 메타 데이터 통합 관리 대상 목록 조회
    public Object getMetaDataList(Map<String, Object> params) {
        String URL = "/search/basic";
        
        // 아래내용이 왜 있는지 모르겠으나 그냥 조회시 모든 데이터가 다 나와서 일단 주석처리함. keh
        // params.remove("entityFilters");
        // params.put("entityFilters", new HashMap() {
        //     {
        //         put("condition", "OR");
        //         put("criterion", Arrays.asList(
        //             new HashMap() {
        //                 {
        //                     put("attributeName", "__typeName");
        //                     put("operator", "eq");
        //                     put("attributeValue", "hive_table");
        //                 }
        //             }, new HashMap() {
        //                 {
        //                     put("attributeName", "__typeName");
        //                     put("operator", "eq");
        //                     put("attributeValue", "hive_column");
        //                 }
        //             }));
        //     }
        // });

        JsonResult result = atlasRestAPI.post(URL, params);
        Map<String, Object> data = (Map<String, Object>) result.getData();
        List entities = (List) data.get("entities");



        return result;
    }
}
