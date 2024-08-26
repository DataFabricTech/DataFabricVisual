package com.mobigen.ovp.search_detail;

import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.LineageClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import com.mobigen.ovp.common.openmete_client.dto.ProfileColumn;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.Tags;
import com.mobigen.ovp.glossary.client.GlossaryClient;
import com.mobigen.ovp.glossary.client.dto.terms.TermResponse;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailLineageTableResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailSampleDataResponse;
import com.mobigen.ovp.user.UserClient;
import com.mobigen.ovp.user.UserService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SearchDetailService {

    private final UserClient userClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final LineageClient lineageClient;
    private final ClassificationClient classificationClient;
    private final GlossaryClient glossaryClient;

    private final UserService userService;
    private final CategoryRepository categoryRepository;


    public Object getUserFilter() throws Exception {
        return userService.getAllUserList().stream().map(user -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.get("id"));
            data.put("fqn", user.get("fullyQualifiedName"));
            String name = (String) user.get("name");
            data.put("name", name);

            if (user.get("displayName") == null || "".equals(user.get("displayName"))) {
                data.put("displayName", name);
            } else {
                data.put("displayName", user.get("displayName"));
            }

            return data;
        }).collect(Collectors.toList());
    }

    private List<Tag> getTags(List<Tag> tags, String after, boolean isStart) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("limit", "100");

        if (!isStart && (after == null || "".equals(after))) {
           return tags;
        } else if (after != null && !"".equals(after)) {
            params.add("after", after);
        }

        Tags res = classificationClient.gatTags(params);
        List<Tag> resTags = res.getData().stream().filter(tag -> !tag.getFullyQualifiedName().contains("ovp_category")).collect(Collectors.toList());


        List<Tag> mergeTagList = Stream.concat(tags.stream(), resTags.stream()).collect(Collectors.toList());

        return getTags(mergeTagList, res.getPaging().getAfter(), false);
    }

    public Object getTagAll() throws Exception {
        List<Tag> tags = getTags(new ArrayList<>(), "", true);

        return tags.stream().map(tag -> {
            Map<String, Object> data = new HashMap<>();
            String displayName = tag.getDisplayName();
            if(displayName == null || "".equals(displayName)) {
                displayName = tag.getName();
            }

            data.put("name", tag.getName());
            data.put("displayName", displayName);
            data.put("description", tag.getDescription());
            data.put("tagFQN", tag.getFullyQualifiedName());

            return data;
        }).collect(Collectors.toList());
    }

    private Object getGlossaries(List<?> glossaries, String after, boolean isStart) throws Exception {
        if (!isStart && (after == null || "".equals(after))) {
            return glossaries;
        }

        TermResponse res = glossaryClient.getGlossaryTerms("", "", 100, after);

        List mergeTagList = Stream.concat(glossaries.stream(), res.getData().stream()).collect(Collectors.toList());

        return getGlossaries(mergeTagList, res.getPaging().getAfter(), false);
    }

    public Object getGlossaryAll() throws Exception {
//        return getGlossaries(new ArrayList<>(), "", true);

        TermResponse res = glossaryClient.getGlossaryTerms("", "", 100, "");

        return res.getData();
    }

    /**
     *  데이터 모델 상세
     * @param id
     * @param type
     * @return
     */
    public DataModelDetailResponse getDataModelDetail(String id, String type) throws Exception {
        MultiValueMap params = new LinkedMultiValueMap();

        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        if (!ModelType.STORAGE.getValue().equals(type)) {
            // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
            params.add("fields", "owner,followers,tags,votes");
            params.add("include", "all");
            Tables tables = tablesClient.getTablesName(id, params);
            DataModelDetailResponse dataModelDetailResponse = new DataModelDetailResponse(tables, type, userId);

            for(Tag tag : tables.getTags()) {
                String displayName = tag.getDisplayName();

                if (displayName == null || "".equals(displayName)) {
                    displayName = tag.getName();
                    tag.setDisplayName(displayName);
                }

                if (tag.getTagFQN().contains("ovp_category")) {
                    CategoryEntity categoryEntity = categoryRepository.findByIdWithParent(UUID.fromString(tag.getName()));
                    if (categoryEntity != null) {
                        dataModelDetailResponse.getCategory().setName(categoryEntity.getName());
                        dataModelDetailResponse.getCategory().setTagName(tag.getName());
                        dataModelDetailResponse.getCategory().setTagDisplayName(tag.getDisplayName());
                        dataModelDetailResponse.getCategory().setTagDescription(tag.getDescription());
                        dataModelDetailResponse.getCategory().setTagFQN(tag.getTagFQN());
                    }
                } else if ("Glossary".equals(tag.getSource())) {
                    dataModelDetailResponse.getTerms().add(tag);
                } else if ("Classification".equals(tag.getSource())) {
                    dataModelDetailResponse.getTags().add(tag);
                }
            }

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

        return new DataModelDetailSampleDataResponse(tablesClient.getSampleData(id));
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

    public Object followDataModel(String id) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        return tablesClient.follow(id, UUID.fromString(userId));
    }

    public Object unfollowDataModel(String id) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        return tablesClient.unfollow(id, userId);
    }

    public Object changeDataModel(String id, List<Map<String, Object>> body) {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "owner,followers,tags,votes");

        return tablesClient.changeDataModel(id, params, body);
    }
}
