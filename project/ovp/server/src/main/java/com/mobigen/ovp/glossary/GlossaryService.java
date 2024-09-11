package com.mobigen.ovp.glossary;

import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.GlossaryClient;
import com.mobigen.ovp.common.openmete_client.dto.Base;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.TermDto;
import com.mobigen.ovp.glossary.client.dto.GlossaryDto;
import com.mobigen.ovp.common.openmete_client.dto.GlossaryActivity;
import com.mobigen.ovp.common.openmete_client.dto.Glossary;
import com.mobigen.ovp.common.openmete_client.dto.Term;
import com.mobigen.ovp.glossary.client.response.Glossaries;
import com.mobigen.ovp.glossary.client.response.GlossaryActivities;
import com.mobigen.ovp.glossary.client.response.Terms;
import com.mobigen.ovp.search_detail.SearchDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final GlossaryClient glossaryClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;
    private final SearchDetailService searchDetailService;

    /**
     * 용어 사전 생성
     * @param dto
     * @return
     * @throws Exception
     */
    public Object createGlossary(GlossaryDto dto) throws Exception {
        ResponseEntity<Object> response = glossaryClient.createGlossary(dto);
        if(response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            return response;
        } else {
            throw new Exception();
        }
    }

    /**
     * 용어 사전 리스트
     * @return
     */
    public Map<String, Object> getGlossaries(String after) {
        final String FIELDS = "owner,tags,reviewers,votes,domain";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", FIELDS);
        params.add("limit", "1000");
        if (after != null && !after.equals("undefined") && !after.isEmpty()) {
            params.add("after", after);
        }
        List<Glossaries> glossaries = new ArrayList<>();
        Base<Glossary> response = glossaryClient.getGlossaries(params);

        if(response != null && response.getData() != null) {
            glossaries = response.getData().stream().map(Glossaries::new).toList();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", glossaries);
        result.put("paging", response.getPaging());
        return result;
    }

    /**
     * 용어 사전 수정
     * @param id
     * @param param
     * @return
     */
    public Glossaries editGlossary(UUID id, List<JsonPatchOperation> param) {
        return new Glossaries(glossaryClient.editGlossary(id, param));
    }

    /**
     * 용어 사전 삭제
     * @param id
     * @return
     */
    public Object deleteGlossary(UUID id) throws Exception {
        ResponseEntity<Object> response = glossaryClient.deleteGlossary(id, true, true);
        if(response.getStatusCode() == HttpStatus.OK ) {
            return response.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 용어 추가
     * @param dto
     * @return
     */
    public Object createTerm(TermDto dto) throws Exception {
        ResponseEntity<Object> response = glossaryClient.createTerms(dto);
        if(response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            return response;
        } else {
            throw new Exception();
        }
    }

    /**
     * 용어 리스트
     * @param directChildrenOf
     * @return
     */
    public Map<String, Object> glossaryTerms(String directChildrenOf, String after) {
        final String FIELDS = "tags,relatedTerms";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("directChildrenOf", directChildrenOf);
        params.add("fields", FIELDS);
        params.add("limit", "20");
        if (after != null && !after.equals("undefined") && !after.isEmpty()) {
            params.add("after", after);
        }
        Base<Term> response = glossaryClient.getGlossaryTerms(params);

        Map<String, Object> result = new HashMap<>();
        result.put("terms", response.getData().stream().map(Terms::new).toList());
        result.put("paging", response.getPaging() != null ? response.getPaging() : null);

        return result;
    }

    /**
     * 용어 수정
     * @param id
     * @param param
     * @return
     */
    public Object editGlossaryTerm(UUID id, List<JsonPatchOperation> param) {
        return glossaryClient.editGlossaryTerm(id, param);
    }

    /**
     * 용어 변경 > 데이터 모델 삭제
     * @param id
     * @param param
     * @return
     */
    public Object updateGlossaryTerm(UUID id, List<Object> param) {
        Map<String, Object> body = new HashMap<>();
        body.put("assets", param);
        body.put("dryRun", false);
        body.put("glossaryTags", new ArrayList<>());

        return glossaryClient.updateGlossaryTerm(id, body);
    }

    /**
     * 용어 삭제
     * @param id
     * @return
     */
    public Object deleteGlossaryTerm(UUID id) throws Exception {
        ResponseEntity<Object> response = glossaryClient.deleteGlossaryTerms(id, true, true);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new Exception();
        }
    }

    /**
     * 용어 사전 활동 사항
     * @param entityLink
     * @param after
     * @return
     */
    public Map<String, Object> getGlossaryActivities(String entityLink, String after) {
        final String TYPE = "Conversation";
        Map<String, Object> result = new HashMap<>();

        Base<GlossaryActivity> response = glossaryClient.getGlossaryActivities(entityLink, after, TYPE);

        List<GlossaryActivities> activities = new ArrayList<>();
        if (response != null && response.getData() != null) {
            activities = response.getData().stream()
                    .map(GlossaryActivities::new)
                    .toList();
        }
        result.put("activities", activities);
        result.put("paging", response != null ? response.getPaging() : null);

        return result;
    }


    /**
     * 용어 사전 활동 사항 개수
     * @param entityLink
     * @return
     */
    public int getGlossaryActivitiesCount(String entityLink) {
        Map<String, Object> response = glossaryClient.getGlossaryActivitiesCount(entityLink);

        List<Map<String, Object>> data  = (List<Map<String, Object>>) response.get("data");
        int result = 0;

        if (data != null) {
            for (Map<String, Object> map : data) {
                Object countObj = map.get("conversationCount");
                if (countObj instanceof Number) {
                    result += ((Number) countObj).intValue();
                }
            }
        }
        return result;
    }

    /**
     * 태그 리스트 호출
     * @return
     * @throws Exception
     */
    public Object getAllTags() {
        List<Tag> tags = searchDetailService.getTags(new ArrayList<>(), "", true);

        return tags.stream().map(tag -> {
            Map<String, Object> data = new HashMap<>();
            String displayName = tag.getDisplayName();
            if (displayName == null || "".equals(displayName)) {
                displayName = tag.getName();
            }
            data.put("name", tag.getName());
            data.put("displayName", displayName);
            data.put("description", tag.getDescription());
            data.put("tagFQN", tag.getFullyQualifiedName());
            data.put("source", "Classification");
            data.put("labelType", "Manual");
            data.put("style", new HashMap<String, Object>());
            data.put("state", "Confirmed");

            return data;
        }).toList();
    }

    /**
     * 데이터 모델 리스트
     * @param search
     * @return
     * @throws Exception
     */
    public Object getDataModels(String search, String name, String from) throws Exception {
        String q;
        if (search != null && !search.isEmpty()) {
            q = "*" + search + "* AND (tags.tagFQN:\"" + name + "\")";
        } else {
            q = "** AND (tags.tagFQN:\"" + name + "\")";
        }

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", q);
        queryParams.add("index", "all");
        queryParams.add("from", from);
        queryParams.add("size", "10");

        List<Map<String, ?>> hits = (List<Map<String, ?>>) ((Map<?, ?>) searchClient.getSearchList(queryParams).get("hits")).get("hits");

        List<Object> source = new ArrayList<>();
        for(Map<String, ?> data : hits) {
            source.add(data.get("_source"));
        }

        List<Map<String, Object>> dataList = new ArrayList<>();
        for(Object obj : source) {
            if(obj instanceof Map<?,?>) {
                Map<String, Object> data = (Map<String, Object>) obj;
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("id", data.get("id"));
                tmp.put("modelDesc", data.get("description"));
                tmp.put("modelNm", data.get("name"));

                if(data.containsKey("owner") && data.get("owner") != null) {
                    Map<String, Object> ownerData = (Map<String, Object>) data.get("owner");
                    tmp.put("owner", ownerData);
                    tmp.put("ownerDisplayName", ownerData.get("displayName"));
                }

                String serviceType = Optional.ofNullable(data.get("serviceType"))
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .orElse("");
                String serviceIcon = "type-img type-img-" + serviceType;
                tmp.put("serviceIcon", serviceIcon);

                tmp.put("type", String.valueOf(data.get("tableType")));

                String displayName = (String) data.get("displayName");
                if (displayName == null || "".equals(displayName)) {
                    tmp.put("firModelNm", "");
                } else {
                    tmp.put("firModelNm", data.get("name"));
                }

                List<Map<String, Object>> tags = (List<Map<String, Object>>) data.get("tags");
                List<Map<String, Object>> categoryList = new ArrayList<>();

                if (tags != null && !tags.isEmpty()) {
                    for (Map<String, Object> tag : tags) {
                        Object tagFQN = tag.get("tagFQN");
                        if (tagFQN != null && tagFQN.toString().contains(Constants.OVP_CATEGORY)) {
                            Map<String, Object> categoryData = new HashMap<>();
                            categoryData.put("id", tag.get("id"));
                            categoryData.put("name", tag.get("name"));
                            categoryData.put("tagDisplayName", tag.get("displayName"));
                            categoryData.put("tagFQN", tagFQN);
                            categoryData.put("tagName", tag.get("name"));
                            categoryData.put("tagDescription", tag.get("description"));

                            categoryList.add(categoryData);
                        }
                    }
                }
                tmp.put("category", categoryList);

                String fqn = (String) data.get("fullyQualifiedName");
                tmp.put("fullyQualifiedName", fqn);
                tmp.put("depth", fqn.split("(?<!\\\\)\\.(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

                dataList.add(tmp);
            }
        }
        return dataList;
    }

    /**
     * 데이터 모델 상세
     * @param fqn
     * @return
     */
    public Object getDataModel(String fqn) {
        Map<String, Object> previewData = tablesClient.getSearchPreview(fqn);

        Map<String, Object> modelInfo = createModelInfo(previewData);

        Map<String, Object> result = new HashMap<>();
        result.put("modelInfo", modelInfo);
        result.put("modelType", "structured");
        result.put("tags", extractTags(previewData, "Classification"));
        result.put("glossaries", extractTags(previewData, "Glossary"));
        return result;
    }

    private Map<String, Object> createModelInfo(Map<String, Object> previewData) {
        Map<String, Object> model = new HashMap<>();

        model.put("name", previewData.get("name"));
        model.put("desc", previewData.get("description"));
        model.put("tableType", previewData.get("tableType"));

        List<Map<String, Object>> columns = createColumns(previewData);
        model.put("cnt", columns.size());

        Map<String, Object> modelInfo = new HashMap<>();
        modelInfo.put("columns", columns);
        modelInfo.put("details", "details");
        modelInfo.put("url", "url");
        modelInfo.put("model", model);

        return modelInfo;
    }

    private Map<String, Object> mapToColumn(Map<String, Object> columnData) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnData.get("name"));
        column.put("dataType", columnData.get("dataType"));
        column.put("desc", columnData.get("description"));
        column.put("constraint", columnData.get("constraint"));
        return column;
    }

    private List<Map<String, Object>> createColumns(Map<String, Object> previewData) {
        List<Map<String, Object>> columnsData = (List<Map<String, Object>>) previewData.get("columns");
        return Optional.ofNullable(columnsData)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToColumn)
                .toList();
    }

    private List<Map<String, Object>> extractTags(Map<String, Object> previewData, String sourceType) {
        List<Map<String, Object>> tags = (List<Map<String, Object>>) previewData.get("tags");
        return Optional.ofNullable(tags)
                .orElse(Collections.emptyList())
                .stream()
                .filter(tag -> sourceType.equals(tag.get("source")))
                .toList();
    }
}
