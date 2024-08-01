package com.mobigen.ovp.glossary;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.glossary.client.GlossaryClient;
import com.mobigen.ovp.glossary.client.dto.GlossaryDto;
import com.mobigen.ovp.glossary.client.dto.glossary.Glossary;
import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivity;
import com.mobigen.ovp.glossary.client.dto.terms.GlossaryTerms;
import com.mobigen.ovp.glossary.client.response.GlossaryActivities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final GlossaryClient glossaryClient;

    public Object createGlossary(GlossaryDto dto) {
        return glossaryClient.createGlossary(dto);
    }

    /**
     * 용어 사전 리스트
     * @return
     */
    public List<Glossary> getGlossaries() {
        String fields = "owner,tags,reviewers,votes,domain";
        return  glossaryClient.getGlossaries(fields).getData();
    }

    /**
     * 용어 사전 수정
     * @param id
     * @param param
     * @return
     */
    public Object editGlossary(UUID id, List<JsonPatchOperation> param) {
        return glossaryClient.editGlossary(id, param);
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
    public List<GlossaryTerms> glossaryTerms(String directChildrenOf) {
        return glossaryClient.getGlossaryTerms(directChildrenOf).getData();
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
}
