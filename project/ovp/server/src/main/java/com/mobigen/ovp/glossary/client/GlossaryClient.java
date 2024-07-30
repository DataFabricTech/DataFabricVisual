package com.mobigen.ovp.glossary.client;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivityResponse;
import com.mobigen.ovp.glossary.client.dto.glossary.Glossary;
import com.mobigen.ovp.glossary.client.dto.glossary.GlossaryResponse;
import com.mobigen.ovp.glossary.client.dto.terms.GlossaryTermsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "GlossaryClient", url = "http://192.168.105.26:8585/api/v1")
public interface GlossaryClient {

    /**
     * 용어 사전 리스트
     * @param fields
     * @return
     */
    @GetMapping("/glossaries")
    GlossaryResponse getGlossaries(@RequestParam(required = false, defaultValue = "owner,tags,reviewers,votes,domain") String fields);

    /**
     * 용어 사전 수정
     * @param id
     * @param param
     * @return
     */
    @PatchMapping(value = "/glossaries/{id}", consumes = "application/json-patch+json")
    Glossary editGlossary(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 용어 사전 삭제
     * @param id
     * @param recursive
     * @param hardDelete
     * @return
     */
    @DeleteMapping("/glossaries/{id}")
    ResponseEntity<Void> deleteGlossary(@PathVariable UUID id,
                        @RequestParam(defaultValue = "true") Boolean recursive,
                        @RequestParam(defaultValue = "true") Boolean hardDelete);

    /**
     * 용어 리스트
     * @param directChildrenOf
     * @return
     */
    @GetMapping("/glossaryTerms")
    GlossaryTermsResponse getGlossaryTerms(@RequestParam String directChildrenOf);

    /**
     * 용어 수정
     * @param id
     * @param param
     * @return
     */
    @PatchMapping(value = "/glossaryTerms/{id}", consumes = "application/json-patch+json")
    Object editGlossaryTerms(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 용어 삭제
     * @param id
     * @param recursive
     * @param hardDelete
     * @return
     */
    @DeleteMapping("/glossaryTerms/{id}")
    ResponseEntity<Void> deleteGlossaryTerms(@PathVariable UUID id,
                            @RequestParam(defaultValue = "true") boolean recursive,
                            @RequestParam(defaultValue = "true") boolean hardDelete);

    /**
     * 용어 사전 활동 사항
     * @param entityLink
     * @param type
     * @return
     */
    @GetMapping("/feed")
    GlossaryActivityResponse getGlossaryActivities(@RequestParam String entityLink, @RequestParam String type);
}
