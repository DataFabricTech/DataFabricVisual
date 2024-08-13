package com.mobigen.ovp.search_detail;

import com.mobigen.ovp.auth.AuthClient;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.LineageClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import com.mobigen.ovp.common.openmete_client.dto.ProfileColumn;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailUpdate;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailLineageTableResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelSampleDataResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SearchDetailService {

    private final AuthClient authClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final LineageClient lineageClient;

    /**
     *  데이터 모델 상세
     * @param id
     * @param type
     * @return
     */
    public DataModelDetailResponse getDataModelDetail(String id, String type) {
        MultiValueMap params = new LinkedMultiValueMap();

        Map<String, Object> user = authClient.getLoggedInUser();
        String userId = user.get("id").toString();

        if (!ModelType.STORAGE.getValue().equals(type)) {
            // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
            params.add("fields", "owner,followers,tags,votes");
            params.add("include", "all");

            DataModelDetailResponse dataModelDetailResponse = new DataModelDetailResponse(tablesClient.getTablesName(id, params), type, userId);

            return dataModelDetailResponse;
        } else {
            return null;
        }
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

        return tablesClient.getTablesName(id, params).getColumns();
    }

    /**
     * 데이터 모델 샘플 데이터
     *
     * @param id
     * @return
     */
    public Object getDataModelSampleData(String id) {

        return new DataModelSampleDataResponse(tablesClient.getSampleData(id));
    }

    /**
     * 프로파일
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getTableProfile(String id) {
        List<ProfileColumn> data = tablesClient.getTableProfile(id).getColumns();
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

    public Object getDataModelQuery(MultiValueMap<String, String> params) throws Exception {
        List<Map<String, Object>> queryList = new ArrayList<>();
        List<Map<String, Object>> hits = new ArrayList<>();
        Map<String, Object> query = searchClient.getSearchList(params);

        if (query.size() > 0) {
            Map<String, Object> queryHits = (LinkedHashMap<String, Object>) query.get("hits");
            hits = (ArrayList<Map<String, Object>>) queryHits.get("hits");
            queryList = hits.stream().map(v -> {
                Map<String, Object> queryRow = new HashMap<>();
                Map<String, Object>  source = (Map<String, Object>) v.get("_source");
                queryRow.put("query", source.get("query"));

                Timestamp timestamp = new Timestamp((Long) source.get("queryDate"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                queryRow.put("queryDate", sdf.format(timestamp));
                return queryRow;
            }).toList();
        }

        return queryList;
    }

    public Object getDataModelLineage(String type, MultiValueMap<String, String> params) {
        if (!ModelType.STORAGE.getValue().equals(type)) {
            return new DataModelDetailLineageTableResponse(lineageClient.getLineage(params));
        } else {
            return null;
        }
    }

    public Object changeVote(String id, DataModelDetailVote dataModelDetailVote) {

        return tablesClient.changeVote(id, dataModelDetailVote);
    }

    public Object followDataModel(String id) {
        Map<String, Object> user = authClient.getLoggedInUser();
        String userId = user.get("id").toString();

        return tablesClient.follow(id, UUID.fromString(userId));
    }

    public Object unfollowDataModel(String id) {
        Map<String, Object> user = authClient.getLoggedInUser();
        String userId = user.get("id").toString();

        return tablesClient.unfollow(id, userId);
    }

    public Object changeDataModel(String id, List<DataModelDetailUpdate> body) {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "owner,followers,tags,votes");

        return tablesClient.changeDataModel(id, params, body);
    }
}
