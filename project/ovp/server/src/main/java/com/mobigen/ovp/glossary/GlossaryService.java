package com.mobigen.ovp.glossary;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.glossary.client.GlossaryClient;
import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivityResponse;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import com.mobigen.ovp.glossary.client.dto.GlossaryDto;
import com.mobigen.ovp.glossary.client.dto.TermDto;
import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivity;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import com.mobigen.ovp.glossary.client.dto.glossary.Glossary;
import com.mobigen.ovp.glossary.client.dto.terms.Term;
import com.mobigen.ovp.glossary.client.response.Glossaries;
import com.mobigen.ovp.glossary.client.response.GlossaryActivities;
import com.mobigen.ovp.glossary.client.response.Terms;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final GlossaryClient glossaryClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;

    public Object createGlossary(GlossaryDto dto) throws Exception {
        ResponseEntity<Object> response = glossaryClient.createGlossary(dto);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            throw new Exception();
        }
    }

    /**
     * 용어 사전 리스트
     * @return
     */
    public List<Glossaries> getGlossaries() {
        String fields = "owner,tags,reviewers,votes,domain";
        int limit = 100;
        List<Glossaries> result = new ArrayList<>();
        List<Glossary> response = glossaryClient.getGlossaries(fields, limit).getData();

        for(Glossary glossary : response) {
            result.add(new Glossaries(glossary));
        }
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
    public int deleteGlossary(UUID id) throws Exception {
        ResponseEntity<Void> response = glossaryClient.deleteGlossary(id, true, true);
        if(response.getStatusCode() == HttpStatus.OK ) {
            return 1;
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
        if(response.getStatusCode() == HttpStatus.OK) {
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
    public List<Terms> glossaryTerms(String directChildrenOf) {
        final String FIELDS = "tags,relatedTerms";
        List<Terms> result = new ArrayList<>();
        // TODO: 용어 전체를 가져오는건지 아니면 인피니티 스크롤 이용해서 가져오는지에 따라 after 처리를 해야 함.
        List<Term> response = glossaryClient.getGlossaryTerms(directChildrenOf, FIELDS, 100, "").getData();

        for (Term term : response) {
            result.add(new Terms(term));
        }
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
    public int deleteGlossaryTerm(UUID id) throws Exception {
        ResponseEntity<Void> response = glossaryClient.deleteGlossaryTerms(id, true, true);
        if (response.getStatusCode() == HttpStatus.OK) {
            return 1;
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
        String type = "Conversation";
        Map<String, Object> result = new HashMap<>();

        List<GlossaryActivities> activities = new ArrayList<>();
        GlossaryActivityResponse response = glossaryClient.getGlossaryActivities(entityLink, after, type);
        List<GlossaryActivity> data = response.getData();
        Object paging = response.getPaging();

        for(GlossaryActivity activity : data) {
            activities.add(new GlossaryActivities(activity));
        }
        result.put("activities", activities);
        result.put("paging", paging);
        return result;
    }

    /**
     * 태그 리스트 호출
     * @return
     * @throws Exception
     */
    public List<Tag> getAllTags() throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", "** AND disabled:false");
        queryParams.add("index", "tag_search_index");
        queryParams.add("query_filter", "{}");

        List<Map<String, ?>> hits = (List<Map<String, ?>>) ((Map<?, ?>) searchClient.getSearchList(queryParams).get("hits")).get("hits");
        List<Object> source = new ArrayList<>();
        for(Map<String, ?> data : hits) {
            source.add(data.get("_source"));
        }
        List<Tag> result = new ArrayList<>();

        for(Object obj : source) {
            if(obj instanceof Map<?,?>) {
                Map<String, ?> data = (Map<String, ?>) obj;
                result.add(new Tag(data));
            }
        }
        return result;
    }

    /**
     * 데이터 모델 리스트
     * @param q
     * @return
     * @throws Exception
     */
    public Object getDataModels(String q) throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", q);
        queryParams.add("index", "all");

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
                    tmp.put("owner", ownerData.get("name"));
                }

                tmp.put("type", String.valueOf(data.get("tableType")));
                tmp.put("serviceIcon", "serviceIcon");
                tmp.put("firModelNm", "firModelNm");
                tmp.put("category", "category");
                String fqn = (String) data.get("fullyQualifiedName");
                tmp.put("fullyQualifiedName", fqn);
                tmp.put("depth", fqn.split("\\."));

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
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> extractTags(Map<String, Object> previewData, String sourceType) {
        List<Map<String, Object>> tags = (List<Map<String, Object>>) previewData.get("tags");
        return Optional.ofNullable(tags)
                .orElse(Collections.emptyList())
                .stream()
                .filter(tag -> sourceType.equals(tag.get("source")))
                .collect(Collectors.toList());
    }
}
