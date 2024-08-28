package com.mobigen.ovp.search_detail;

import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.ContainersClient;
import com.mobigen.ovp.common.openmete_client.LineageClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import com.mobigen.ovp.common.openmete_client.dto.Followers;
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
    private final ContainersClient containersClient;
    private final LineageClient lineageClient;
    private final ClassificationClient classificationClient;
    private final GlossaryClient glossaryClient;

    private final UserService userService;
    private final CategoryRepository categoryRepository;

    /**
     * 사용자 목록 (전체)
     * @return
     * @throws Exception
     */
    public Object getUserAll() throws Exception {
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

    /**
     * 태그 전체 순회
     * @param tags
     * @param after
     * @param isStart
     * @return
     */
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

    /**
     * 태그 목록 (전체)
     * @return
     * @throws Exception
     */
    public Object getTagAll() throws Exception {
        List<Tag> tags = getTags(new ArrayList<>(), "", true);

        // TODO: 페이징 처리 필요
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

    /**
     * 용어 전체 순회
     * @param glossaries
     * @param after
     * @param isStart
     * @return
     * @throws Exception
     */
    private Object getGlossaries(List<?> glossaries, String after, boolean isStart) throws Exception {
        if (!isStart && (after == null || "".equals(after))) {
            return glossaries;
        }

        TermResponse res = glossaryClient.getGlossaryTerms("", "", 100, after);

        List mergeTagList = Stream.concat(glossaries.stream(), res.getData().stream()).collect(Collectors.toList());

        return getGlossaries(mergeTagList, res.getPaging().getAfter(), false);
    }

    /**
     * 용어 목록 (전체)
     * @return
     * @throws Exception
     */
    public Object getGlossaryAll() throws Exception {
//        return getGlossaries(new ArrayList<>(), "", true);

        // TODO: 페이징 처리 필요
        TermResponse res = glossaryClient.getGlossaryTerms("", "", 100, "");

        return res.getData();
    }

    /**
     *  데이터 모델 상세 (테이블, 스토리지)
     * @param id
     * @param type
     * @return
     */
    public DataModelDetailResponse getDataModelDetail(String id, String type) throws Exception {
        MultiValueMap params = new LinkedMultiValueMap();
        // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
        params.add("fields", "owner,followers,tags,votes");
        params.add("include", "all");

        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        DataModelDetailResponse dataModelDetailResponse = null;
        Tables tables = null;

        if (!ModelType.STORAGE.getValue().equals(type)) {
            tables = tablesClient.getTablesName(id, params);
            dataModelDetailResponse = new DataModelDetailResponse(tables, type, userId);
        } else {
            tables = containersClient.getStorageById(id, params);
            dataModelDetailResponse = new DataModelDetailResponse(tables, type, userId);
        }

        for(Tag tag : tables.getTags()) {
            String displayName = tag.getDisplayName();

            if (displayName == null || "".equals(displayName)) {
                displayName = tag.getName();
                tag.setDisplayName(displayName);
            }

            if (tag.getTagFQN().contains("ovp_category")) {
                CategoryEntity categoryEntity = categoryRepository.findByIdWithParent(UUID.fromString(tag.getName()));
                if (categoryEntity != null) {
                    dataModelDetailResponse.getCategory().setId(categoryEntity.getId());
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
    }

    /**
     * 데이터 모델 상세 > 팔로우(북마크) 데이터 추출
     * @param userId
     * @param id
     * @param type
     * @return
     */
    public boolean getFollowDataModel(String userId, String id, String type) {
        boolean isFollow = false;
        if (ModelType.STORAGE.getValue().equals(type)) {
            Map<String, Object> container = containersClient.getContainersObject(id, "followers");
            List<Map<String, Object>> followers = (List<Map<String, Object>>) container.get("followers");
            for(Map<String, Object> follower: followers) {
                if (follower.get("id").equals(userId)) {
                    isFollow = true;
                    break;
                }
            }
            return isFollow;
        } else {
            MultiValueMap params = new LinkedMultiValueMap();
            params.add("fields", "followers");
            params.add("include", "all");

            Tables tables = tablesClient.getTablesName(id, params);

            for(Followers followers: tables.getFollowers()) {
                if (followers.getId().equals(userId)) {
                    isFollow = true;
                    break;
                }
            }
            return isFollow;
        }
    }


    /**
     * 데이터 모델 스키마 (테이블, 스토리지)
     * @param id
     * @return
     */
    public List<Columns> getDataModelSchema(String id, String type) {
        MultiValueMap params = new LinkedMultiValueMap();

        // fields=tableConstraints,tablePartition,usageSummary,owner,customMetrics,columns,tags,followers,joins,schemaDefinition,dataModel,extension,testSuite,domain,dataProducts,lifeCycle,sourceHash
        params.add("include", "all");

        if (!ModelType.STORAGE.getValue().equals(type)) {
            params.add("fields", "columns");
            return tablesClient.getTablesName(id, params).getColumns();
        } else {
            params.add("fields", "dataModel");
            return containersClient.getStorageById(id, params).getDataModel().getColumns();
        }
    }

    /**
     * 데이터 모델 샘플 데이터
     * @param id
     * @return
     */
    public Object getDataModelSampleData(String id) {

        return new DataModelDetailSampleDataResponse(tablesClient.getSampleData(id));
    }

    /**
     * 프로파일
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

    /**
     * 쿼리
     * @param params
     * @return
     * @throws Exception
     */
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

    /**
     * 리니지 그래프 (테이블, 스토리지)
     * @param params
     * @return
     */
    public Object getDataModelLineage(MultiValueMap<String, String> params) {

        return new DataModelDetailLineageTableResponse(lineageClient.getLineage(params));
    }

    /**
     * 추천 (테이블, 스토리지)
     * @param id
     * @param dataModelDetailVote
     * @return
     */
    public Object changeVote(String id, DataModelDetailVote dataModelDetailVote) {

        return tablesClient.changeVote(id, dataModelDetailVote);
    }

    /***
     * 비추천 (테이블, 스토리지)
     * @param id
     * @return
     * @throws Exception
     */
    public Object followDataModel(String id) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        return tablesClient.follow(id, UUID.fromString(userId));
    }

    /**
     * 북먀크 (테이블, 스토리지)
     * @param id
     * @return
     * @throws Exception
     */
    public Object unfollowDataModel(String id) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        return tablesClient.unfollow(id, userId);
    }

    /**
     * 데이터 모델 변경 (테이블, 스토리지)
     * @param id
     * @param type
     * @param body
     * @return
     */
    public Object changeDataModel(String id, String type, List<Map<String, Object>> body) {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "owner,followers,tags,votes");

        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.changeDataModel(id, params, body);
        } else {
            return containersClient.changeStorage(id, params, body);
        }
    }
}
