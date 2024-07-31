package com.mobigen.ovp.glossary;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
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

    /**
     * 용어 사전 리스트
     * @return
     */
    public List<Glossaries> getGlossaries() {
        String fields = "owner,tags,reviewers,votes,domain";
        List<Glossaries> result = new ArrayList<>();
        List<Glossary> response = glossaryClient.getGlossaries(fields).getData();

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
        final String FIELDS = "tags";
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
        return glossaryClient.editGlossaryTerms(id, param);
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
}
