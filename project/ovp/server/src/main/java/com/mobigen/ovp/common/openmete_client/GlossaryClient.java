package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Base;
import com.mobigen.ovp.common.openmete_client.dto.GlossaryActivity;
import com.mobigen.ovp.common.openmete_client.dto.Term;
import com.mobigen.ovp.glossary.client.dto.GlossaryDto;
import com.mobigen.ovp.glossary.client.dto.TermDto;
import com.mobigen.ovp.common.openmete_client.dto.Glossary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "GlossaryClient", url = "${properties.ovp.open-metadata-url}")
public interface GlossaryClient {

    /**
     * 용어 사전 등록
     * @param dto
     * @return
     */
    @PostMapping("/glossaries")
    ResponseEntity<Object> createGlossary(@RequestBody GlossaryDto dto);

    /**
     * 용어 사전 리스트
     * @param params
     * @return
     */
    @GetMapping("/glossaries")
    Base<Glossary> getGlossaries(@RequestParam MultiValueMap<String, String> params);

    /**
     * 용어 조회
     * @param id
     * @param include
     * @return
     */
    @GetMapping("/glossaryTerms/{id}")
    TermDto getGlossaryTermsById(@PathVariable String id, @RequestParam(required = false, defaultValue = "all") String include);

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
    ResponseEntity<Object> deleteGlossary(@PathVariable UUID id,
                        @RequestParam(defaultValue = "true") Boolean recursive,
                        @RequestParam(defaultValue = "true") Boolean hardDelete);

    /**
     * 용어 추가
     * @param dto
     * @return
     */
    @PostMapping("/glossaryTerms")
    ResponseEntity<Object> createTerms(@RequestBody com.mobigen.ovp.common.openmete_client.dto.TermDto dto);

    /**
     * 용어 리스트
     * @param params
     * @return
     */
    @GetMapping("/glossaryTerms")
    Base<Term> getGlossaryTerms(@RequestParam MultiValueMap<String, String> params);

    /**
     * 용어 수정
     * @param id
     * @param param
     * @return
     */
    @PatchMapping(value = "/glossaryTerms/{id}", consumes = "application/json-patch+json")
    Object editGlossaryTerm(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param);

    /**
     * 용어 변경 > 데이터 모델 추가
     *
     * @param id
     * @param body
     * @return
     */
    @PutMapping(value = "/glossaryTerms/{id}/assets/add", consumes = "application/json")
    Object addGlossaryTerm(@PathVariable UUID id, @RequestBody Map<String, Object> body);

    /**
     * 용어 변경 > 데이터 모델 삭제
     * @param id
     * @param body
     * @return
     */
    @PutMapping(value = "/glossaryTerms/{id}/assets/remove")
    Object updateGlossaryTerm(@PathVariable UUID id, @RequestBody Map<String, Object> body);

    /**
     * 용어 삭제
     * @param id
     * @param recursive
     * @param hardDelete
     * @return
     */
    @DeleteMapping("/glossaryTerms/{id}")
    ResponseEntity<Object> deleteGlossaryTerms(@PathVariable UUID id,
                            @RequestParam(defaultValue = "true") boolean recursive,
                            @RequestParam(defaultValue = "true") boolean hardDelete);

    /**
     * 용어 사전 활동 사항
     * @param entityLink
     * @param type
     * @return
     */
    @GetMapping("/feed")
    Base<GlossaryActivity> getGlossaryActivities(@RequestParam String entityLink, @RequestParam(required = false) String after, @RequestParam String type);

    /**
     * 용어 사전 활동 사항 갯수
     * @param entityLink
     * @return
     */
    @GetMapping("/feed/count")
    Map<String, Object> getGlossaryActivitiesCount(@RequestParam String entityLink);
}
