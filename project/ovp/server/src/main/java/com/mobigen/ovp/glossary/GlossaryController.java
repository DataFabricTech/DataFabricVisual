package com.mobigen.ovp.glossary;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/glossary")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GlossaryController {

    private final GlossaryService glossaryService;

    /**
     * 용어 사전 리스트
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getGlossaries() {
        return glossaryService.getGlossaries();
    }

    /**
     * 용어 사전 수정
     * @param id
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public Object editGlossary(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) {
        return glossaryService.editGlossary(id, param);
    }

    /**
     * 용어 사전 삭제
     * @param id
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 용어 사전 삭제 오류")
    @DeleteMapping("/{id}")
    public Object deleteGlossary(@PathVariable UUID id) throws Exception {
        return glossaryService.deleteGlossary(id);
    }

    /**
     * 용어 리스트
     * @param term
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/terms")
    public Object getGlossaryTerms(@RequestParam String term) {
        return glossaryService.glossaryTerms(term);
    }

    /**
     * 용어 수정
     * @param id
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/terms/{id}", consumes = "application/json-patch+json")
    public Object editGlossaryTerms(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) {
        return glossaryService.editGlossaryTerm(id, param);
    }

    /**
     * 용어 삭제
     * @param id
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 용어 삭제 오류")
    @DeleteMapping("/terms/{id}")
    public Object deleteGlossaryTerm(@PathVariable UUID id) throws Exception {
        return glossaryService.deleteGlossaryTerm(id);
    }

    /**
     * 용어 사전 활동 기록
     * @param entityLink
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/activities")
    public Object getGlossaryActivities(@RequestParam String entityLink) {
        return glossaryService.getGlossaryActivities(entityLink);
    }

}
