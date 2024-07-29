package com.mobigen.ovp.search_detail;

import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import com.mobigen.ovp.common.openmete_client.dto.ProfileColumn;
import com.mobigen.ovp.search_detail.dto.response.DataModelSampleDataResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SearchDetailService {

    private final TablesClient tabelsClient;

    /**
     * 데이터 모델 상세
     *
     * @param id
     * @return
     */
    public DataModelDetailResponse getDataModelDetail(String id) {
        MultiValueMap params = new LinkedMultiValueMap();

        // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
        params.add("fields", "owner,followers,tags,votes");
        params.add("include", "all");

        return new DataModelDetailResponse(tabelsClient.getTablesName(id, params));
    }

    /**
     * 데이터 모델 스키마
     *
     * @param id
     * @return
     */
    public List<Columns> getDataModelSchema(String id) {
        MultiValueMap params = new LinkedMultiValueMap();

        // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
        params.add("fields", "columns");
        params.add("include", "all");

        return tabelsClient.getTablesName(id, params).getColumns();
    }

    /**
     * 데이터 모델 샘플 데이터
     *
     * @param id
     * @return
     */
    public Object getDataModelSampleData(String id) {

        return new DataModelSampleDataResponse(tabelsClient.getSampleData(id));
    }

    /**
     * 프로파일
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getTableProfile(String id) {
        List<ProfileColumn> data = tabelsClient.getTableProfile(id).getColumns();
        List<Map<String, Object>> columns = new ArrayList<>();

        for (ProfileColumn column : data) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", column.getName());
            row.put("dateTypeDisplay", column.getDataTypeDisplay());
            row.put("nullCount", column.getProfile().getNullCount());
            row.put("uniqueCount", column.getProfile().getUniqueCount());
            row.put("distinctCount", column.getProfile().getDistinctCount());
            row.put("valueCount", column.getProfile().getValueCount());

            columns.add(row);
        }

        return columns;
    }
}
