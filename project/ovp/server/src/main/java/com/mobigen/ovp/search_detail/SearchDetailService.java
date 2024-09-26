package com.mobigen.ovp.search_detail;

import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.ClassificationTagsClient;
import com.mobigen.ovp.common.openmete_client.ContainersClient;
import com.mobigen.ovp.common.openmete_client.LineageClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Base;
import com.mobigen.ovp.common.openmete_client.dto.Columns;
import com.mobigen.ovp.common.openmete_client.dto.Followers;
import com.mobigen.ovp.common.openmete_client.dto.Profile;
import com.mobigen.ovp.common.openmete_client.dto.ProfileColumn;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.Tags;
import com.mobigen.ovp.common.openmete_client.GlossaryClient;
import com.mobigen.ovp.common.openmete_client.dto.Term;
import com.mobigen.ovp.glossary.client.dto.TermDto;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailTagDto;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailLineageTableResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailSampleDataResponse;
import com.mobigen.ovp.user.UserClient;
import com.mobigen.ovp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(SearchDetailService.class);
    private final UserClient userClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final ContainersClient containersClient;
    private final LineageClient lineageClient;
    private final ClassificationTagsClient classificationTagsClient;
    private final GlossaryClient glossaryClient;

    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final ModelConvertUtil modelConvertUtil;

    /**
     * 사용자 목록 (전체)
     *
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
     *
     * @param tags
     * @param after
     * @param isStart
     * @return
     */
    public List<Tag> getTags(List<Tag> tags, String after, boolean isStart) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("limit", "100");

        if (!isStart && (after == null || "".equals(after))) {
            return tags;
        } else if (after != null && !"".equals(after)) {
            params.add("after", after);
        }

        Tags res = classificationTagsClient.gatTags(params);
        List<Tag> resTags = res.getData().stream().filter(tag -> {
            String classificationName = tag.getClassification().getName();
            return !(classificationName.contains("ovp_category") || classificationName.contains("PersonalData") || classificationName.contains("PII") || classificationName.contains("Tier"));
        }).collect(Collectors.toList());


        List<Tag> mergeTagList = Stream.concat(tags.stream(), resTags.stream()).collect(Collectors.toList());

        return getTags(mergeTagList, res.getPaging().getAfter(), false);
    }

    /**
     * 태그 목록 (전체)
     *
     * @return
     * @throws Exception
     */
    public Object getTagAll() throws Exception {
        List<Tag> tags = getTags(new ArrayList<>(), "", true);

        // TODO: 페이징 처리 필요
        return tags.stream().map(tag -> {
            Map<String, Object> data = new HashMap<>();
            String displayName = tag.getDisplayName();
            if (displayName == null || "".equals(displayName)) {
                displayName = tag.getName();
            }

            data.put("id", tag.getId());
            data.put("name", tag.getName());
            data.put("displayName", displayName);
            data.put("description", tag.getDescription());
            data.put("tagFQN", tag.getFullyQualifiedName());

            return data;
        }).collect(Collectors.toList());
    }

    /**
     * 용어 전체 순회
     *
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("limit", "100");
        if (after != null && !after.equals("undefined") && !after.isEmpty()) {
            params.add("after", after);
        }
        Base<Term> res = glossaryClient.getGlossaryTerms(params);

        List mergeTagList = Stream.concat(glossaries.stream(), res.getData().stream()).collect(Collectors.toList());

        return getGlossaries(mergeTagList, res.getPaging().getAfter(), false);
    }

    /**
     * 용어 목록 (전체)
     *
     * @return
     * @throws Exception
     */
    public Object getGlossaryAll() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("limit", "300");
        Base<Term> glossaryTerms = glossaryClient.getGlossaryTerms(params);

        // TODO: 페이징 처리 필요
        return glossaryTerms.getData().stream().map(term -> {
            Map<String, Object> data = new HashMap<>();
            String displayName = term.getDisplayName();
            if (displayName == null || "".equals(displayName)) {
                displayName = term.getName();
            }

            data.put("id", term.getId());
            data.put("name", term.getName());
            data.put("displayName", displayName);
            data.put("description", term.getDescription());
            data.put("tagFQN", term.getFullyQualifiedName());

            return data;
        }).collect(Collectors.toList());
    }

    /**
     * 데이터 모델 상세 (테이블, 스토리지)
     *
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

        for (Tag tag : tables.getTags()) {
            String displayName = tag.getDisplayName();

            if (displayName == null || "".equals(displayName)) {
                displayName = tag.getName();
                tag.setDisplayName(displayName);
            }

            if (tag.getTagFQN().contains(Constants.OVP_CATEGORY)) {
                CategoryEntity categoryEntity = modelConvertUtil.getCategoryEntity(tag.getName());
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
     *
     * @param userId
     * @param id
     * @param type
     * @return
     */
    public boolean getFollowDataModel(String userId, String id, String type) {
        boolean isFollow = false;

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "followers");
        params.add("include", "all");

        Tables resultData = ModelType.STORAGE.getValue().equals(type)
                ? containersClient.getStorageById(id, params)
                : tablesClient.getTablesName(id, params);

        for (Followers followers : resultData.getFollowers()) {
            if (followers.getId().equals(userId)) {
                isFollow = true;
                break;
            }
        }
        return isFollow;
    }


    /**
     * 데이터 모델 스키마 (테이블, 스토리지)
     *
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
     *
     * @param id
     * @return
     */
    public Object getDataModelSampleData(String id, String type) throws Exception {
        if (!ModelType.STORAGE.getValue().equals(type)) {
            return new DataModelDetailSampleDataResponse(tablesClient.getSampleData(id), type);
        } else {
            return new DataModelDetailSampleDataResponse(containersClient.getSampleData(id), type);
        }
    }

    /**
     * 프로파일 - table
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

            if(column.getProfile() != null) {
                row.put("nullCount", column.getProfile().getNullCount());
                row.put("uniqueCount", column.getProfile().getUniqueCount());
                row.put("distinctCount", column.getProfile().getDistinctCount());
                row.put("valueCount", column.getProfile().getValueCount());
            } else {
                row.put("nullCount", "");
                row.put("uniqueCount", "");
                row.put("distinctCount", "");
                row.put("valueCount", "");
            }
            columns.add(row);
        }

        return columns;
    }

    /**
     * 프로파일 - container
     *
     * @param fqn
     * @return
     */
    public List<Map<String, Object>> getContainersTableProfile(String fqn) {

        log.info("fqn체크: " + fqn);
        List<ProfileColumn> data = containersClient.getContainersTableProfile(fqn).getDataModel().getColumns();
        List<Map<String, Object>> columns = new ArrayList<>();

        for (ProfileColumn column : data) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", column.getName());
            row.put("dateTypeDisplay", column.getDataTypeDisplay());

            if(column.getProfile() != null) {
                row.put("nullCount", column.getProfile().getNullCount());
                row.put("uniqueCount", column.getProfile().getUniqueCount());
                row.put("distinctCount", column.getProfile().getDistinctCount());
                row.put("valueCount", column.getProfile().getValueCount());
            } else {
                row.put("nullCount", "");
                row.put("uniqueCount", "");
                row.put("distinctCount", "");
                row.put("valueCount", "");
            }
            columns.add(row);
        }

        return columns;
    }

    /**
     * 쿼리
     *
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
                Map<String, Object> source = (Map<String, Object>) v.get("_source");
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
     *
     * @param params
     * @return
     */
    public Object getDataModelLineage(MultiValueMap<String, String> params) {

        return new DataModelDetailLineageTableResponse(lineageClient.getLineage(params));
    }

    /**
     * 추천 (테이블, 스토리지)
     *
     * @param id
     * @param dataModelDetailVote
     * @return
     */
    public Object changeVote(String id, String type, DataModelDetailVote dataModelDetailVote) {

        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.changeVote(id, dataModelDetailVote);

        }

        return containersClient.changeVote(id, dataModelDetailVote);
    }

    /***
     * 비추천 (테이블, 스토리지)
     * @param id
     * @return
     * @throws Exception
     */
    public Object followDataModel(String id, String type) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.follow(id, UUID.fromString(userId));
        }

        return containersClient.follow(id, UUID.fromString(userId));
    }

    /**
     * 북먀크 (테이블, 스토리지)
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Object unfollowDataModel(String id, String type) throws Exception {
        Map<String, Object> user = userClient.getUserInfo();
        String userId = user.get("id").toString();

        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.unfollow(id, userId);
        }

        return containersClient.unfollow(id, userId);
    }

    /**
     * 데이터 모델 변경 (테이블, 스토리지)
     *
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

    private List<Map<String, Object>> makeRemoveBody(int tagLength) {
        List<Map<String, Object>> removedBody = new ArrayList<>();

        for (int i = (tagLength - 1); i >= 0; i--) {
            Map<String, Object> row = new HashMap<>();
            row.put("op", "remove");
            row.put("path", new StringBuffer("/tags/").append(i).toString());
            removedBody.add(row);
        }

        return removedBody;
    }

    private List<Map<String, Object>> makeAddBody(List<Tag> tags, List<Map<String, Object>> body, String target, boolean isCategory) {
        List<Map<String, Object>> addedBody = new ArrayList<>();
        List<DataModelDetailTagDto> bodyList = new ArrayList<>();
        List<DataModelDetailTagDto> tagList = new ArrayList<>();

        // 데이터 모델의 태크 목록에서 카테고리, 태그, 용어를 분리해서 저장.
        List<DataModelDetailTagDto> glossaryList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains(Constants.OVP_CATEGORY) && "Glossary".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());
        ;
        List<DataModelDetailTagDto> classificationList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains(Constants.OVP_CATEGORY) && "Classification".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());
        List<DataModelDetailTagDto> categoryList = tags.stream()
                .filter(tag -> tag.getTagFQN().contains(Constants.OVP_CATEGORY) && "Classification".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());

        // { op, path, value }
        // 클라이언트로 받은 변경해야 할 데이터(태그, 카테고라, 용어)를 각각 단일 조회 후에 value 를 셋팅한다.
        if ("Classification".equals(target)) {
            for (Map<String, Object> item : body) {
                String key = "id";
                if (isCategory) {
                    key = "tagId";
                }

                Map<String, Object> tempTag = classificationTagsClient.getTag(item.get(key).toString());

                DataModelDetailTagDto tag = new DataModelDetailTagDto();
                tag.setName(tempTag.get("name").toString());
                tag.setDisplayName(tempTag.get("displayName").toString());
                tag.setDescription(tempTag.get("description").toString());
                tag.setTagFQN(tempTag.get("fullyQualifiedName").toString());
                tag.setSource(target);
                tag.setLabelType("Manual");
                tag.setStyle((Map<String, Object>) tempTag.get("style"));

                tagList.add(tag);
            }
        } else if ("Glossary".equals(target)) {
            for (Map<String, Object> item : body) {
                TermDto tempTerm = glossaryClient.getGlossaryTermsById(item.get("id").toString(), "all");

                DataModelDetailTagDto tag = new DataModelDetailTagDto();
                tag.setName(tempTerm.getName());
                tag.setDisplayName(tempTerm.getDisplayName());
                tag.setDescription(tempTerm.getDescription());
                tag.setTagFQN(tempTerm.getFullyQualifiedName());
                tag.setSource(target);
                tag.setLabelType("Manual");
                tag.setStyle(tempTerm.getStyle());

                tagList.add(tag);
            }
        }

        // value가 셋팅이 완료 되면 모든 데이터(카테고리, 태그, 용어)를 하나로 합친다.
        if (isCategory) {
            bodyList = Stream.concat(glossaryList.stream(), classificationList.stream()).collect(Collectors.toList());
            bodyList = Stream.concat(bodyList.stream(), tagList.stream()).collect(Collectors.toList());
        } else if ("Classification".equals(target)) {
            bodyList = Stream.concat(glossaryList.stream(), tagList.stream()).collect(Collectors.toList());
            bodyList = Stream.concat(bodyList.stream(), categoryList.stream()).collect(Collectors.toList());
        } else if ("Glossary".equals(target)) {
            bodyList = Stream.concat(tagList.stream(), classificationList.stream()).collect(Collectors.toList());
            bodyList = Stream.concat(bodyList.stream(), categoryList.stream()).collect(Collectors.toList());
        }

        // 모두 합쳐진 value를 가지고 patch할 body 데이터를 만든다.
        int bodySize = bodyList.size();
        for (int i = 0; i < bodySize; i++) {
            Map<String, Object> operationMap = new HashMap<>();
            operationMap.put("op", "add");
            operationMap.put("path", new StringBuffer("/tags/").append(i).toString());
            operationMap.put("value", bodyList.get(i));
            addedBody.add(operationMap);
        }

        return addedBody;
    }

    public Object ChangeDataModelTag(String id, String type, String target, boolean isCategory, List<Map<String, Object>> body) {
        Tables tables = null;
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "tags");
        params.add("include", "all");

        if (!ModelType.STORAGE.getValue().equals(type)) {
            tables = tablesClient.getTablesName(id, params);
        } else {
            tables = containersClient.getStorageById(id, params);
        }

        Map<String, Object> result = new HashMap<>();

        List tags = tables.getTags();
        List removeBody = makeRemoveBody(tags.size());
        List addBody = makeAddBody(tags, body, target, isCategory);

        changeDataModel(id, type, removeBody);
        changeDataModel(id, type, addBody);

        return true;
    }

    public Object deleteDataModel(String id, String type) {
        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.delete(id, true, true);
        } else {
            return containersClient.delete(id, false, true);
        }
    }
}
