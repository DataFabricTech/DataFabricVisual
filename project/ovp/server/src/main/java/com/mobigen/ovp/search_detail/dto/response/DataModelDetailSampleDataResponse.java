package com.mobigen.ovp.search_detail.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.SampleData;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class DataModelDetailSampleDataResponse {
    private List<Columns> columns;
    private List<Map<String, Object>> sampleList;

    public DataModelDetailSampleDataResponse(SampleData sampleData) {
        this.columns = sampleData.getColumns();
        List<Map<String, Object>> sampleList = new ArrayList<>();
        List<String> columns = sampleData.getSampleData().getColumns();

        for (List<?> items : sampleData.getSampleData().getRows()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 0; i < items.size(); i++) {
                row.put(columns.get(i), items.get(i));
            }
            sampleList.add(row);
        }

        this.sampleList = sampleList;
    }

}
