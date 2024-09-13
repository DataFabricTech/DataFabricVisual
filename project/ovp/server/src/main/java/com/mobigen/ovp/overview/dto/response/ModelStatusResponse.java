package com.mobigen.ovp.overview.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ModelStatusResponse {
    List<List<String>> source;

    public ModelStatusResponse(Map<String, Object> modelStatus) {
        this.source = new ArrayList<>();

        if (modelStatus != null && modelStatus.get("data") != null) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) modelStatus.get("data");

            for (Map<String, Object> m : data) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(m.get("name")));
                row.add(String.valueOf(m.get("omModelCount")));
                row.add(String.valueOf(m.get("modelCount")));
                source.add(row);
            }
        }
    }
}
