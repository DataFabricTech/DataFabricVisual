package com.mobigen.ovp.glossary;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.glossary.client.GlossaryClient;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import com.mobigen.ovp.glossary.client.dto.glossary.Glossary;
import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final GlossaryClient glossaryClient;
    private final SearchClient searchClient;
    private final TablesClient tablesClient;

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
     * 용어 리스트
     * @param directChildrenOf
     * @return
     */
    public List<Terms> glossaryTerms(String directChildrenOf) {
        final String FIELDS = "tags,relatedTerms";
        List<Terms> result = new ArrayList<>();
        List<Term> response = glossaryClient.getGlossaryTerms(directChildrenOf, FIELDS).getData();

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
     * @return
     */
    public List<GlossaryActivities> getGlossaryActivities(String entityLink) {
        String type = "Conversation";
        List<GlossaryActivities> result = new ArrayList<>();
        List<GlossaryActivity> response = glossaryClient.getGlossaryActivities(entityLink, type).getData();

        for(GlossaryActivity activity : response) {
            result.add(new GlossaryActivities(activity));
        }
        return result;
    }

    /**
     * 태그 리스트 호출
     * @return
     * @throws Exception
     */
    public Object getAllTags() throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", "** AND disabled:false");
        queryParams.add("index", "tag_search_index");
        queryParams.add("query_filter", "{}");

        List<Map<String, ?>> hits = (List<Map<String, ?>>) ((Map<?, ?>) searchClient.getSearchList(queryParams).get("hits")).get("hits");
        List<Object> source = new ArrayList<>();
        for(Map<String, ?> data : hits) {
            source.add(data.get("_source"));
        }

        List<Map<String, String>> menuSearchResult = new ArrayList<>();
        List<Tag> allData = new ArrayList<>();

        for(Object obj : source) {
            if(obj instanceof Map<?,?>) {
                Map<String, ?> data = (Map<String, ?>) obj;
                Map<String, String> menuSearchData = new HashMap<>();
                menuSearchData.put("data", String.valueOf(data.get("name")));
                menuSearchData.put("label", String.valueOf(data.get("displayName")));
                menuSearchResult.add(menuSearchData);

                allData.add(new Tag(data));
            }
        }
        Map<String, List<?>> result = new HashMap<>();
        result.put("menuSearchData", menuSearchResult);
        result.put("allData", allData);
        return result;
    }

    /**
     * 데이터 모델 리스트
     * @param name
     * @return
     * @throws Exception
     */
    public Object getDataModels(String name) throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", name);
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

                Map<String, Object> ownerData = (Map<String, Object>) data.get("owner");
                tmp.put("owner", ownerData.get("name"));

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

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> modelInfo = new HashMap<>();
        Map<String, Object> model = new HashMap<>();

        model.put("name", previewData.get("name"));
        model.put("desc", previewData.get("description"));
        model.put("tableType", previewData.get("tableType"));

        List<Map<String, Object>> columnsData = (List<Map<String, Object>>) previewData.get("columns");
        List<Map<String, Object>> columns = new ArrayList<>();
        if (columnsData != null) {
            for (Map<String, Object> columnData : columnsData) {
                Map<String, Object> column = new HashMap<>();
                column.put("name", columnData.get("name"));
                column.put("dataType", columnData.get("dataType"));
                column.put("desc", columnData.get("description"));
                column.put("constraint", columnData.get("constraint"));
                columns.add(column);
            }
        }

        model.put("cnt", columns.size());

        modelInfo.put("columns", columns);
        modelInfo.put("details", "details");
        modelInfo.put("url", "url");
        modelInfo.put("model", model);

        result.put("modelInfo", modelInfo);
        result.put("modelType", "structured");

        List<Map<String, Object>> tags = (List<Map<String, Object>>) previewData.get("tags");
        List<Map<String, Object>> classifications = new ArrayList<>();
        List<Map<String, Object>> glossaries = new ArrayList<>();
        if (tags != null) {
            for (Map<String, Object> tag : tags) {
                String source = (String) tag.get("source");
                if ("Glossary".equals(source)) {
                    glossaries.add(tag);
                } else if ("Classification".equals(source)) {
                    classifications.add(tag);
                }
            }
        }
        result.put("tags", classifications);
        result.put("glossaries", glossaries);
        return result;
    }
}
