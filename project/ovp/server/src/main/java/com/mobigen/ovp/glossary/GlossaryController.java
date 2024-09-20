package com.mobigen.ovp.glossary;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.dto.TermDto;
import com.mobigen.ovp.glossary.client.dto.GlossaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/glossary")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GlossaryController {

    private final GlossaryService glossaryService;

    /**
     * 용어 사전 등록
     *
     * @param dto
     * @return
     */
    @ResponseJsonResult
    @PostMapping()
    public Object createGlossary(@RequestBody GlossaryDto dto) throws Exception {
        return glossaryService.createGlossary(dto);
    }

    /**
     * 용어 사전 리스트
     *
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getGlossaries(@RequestParam(required = false) String after) {
        return glossaryService.getGlossaries(after);
    }

    /**
     * 용어 사전 수정
     *
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
     *
     * @param id
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 용어 사전 삭제 오류")
    @DeleteMapping("/{id}")
    public Object deleteGlossary(@PathVariable UUID id) throws Exception {
        return glossaryService.deleteGlossary(id);
    }

    /**
     * 용어 추가
     *
     * @param dto
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/terms")
    public Object createTerm(@RequestBody TermDto dto) throws Exception{
        return glossaryService.createTerm(dto);
    }

    /**
     * 용어 리스트
     *
     * @param term
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/terms")
    public Object getGlossaryTerms(@RequestParam String term, @RequestParam(required = false) String after) {
        return glossaryService.glossaryTerms(term, after);
    }

    /**
     * 용어 수정
     *
     * @param id
     * @param param
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/terms/{id}", consumes = "application/json-patch+json")
    public Object editGlossaryTerm(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) {
        return glossaryService.editGlossaryTerm(id, param);
    }

    /**
     * 용어 변경 > 데이터 모델 삭제
     *
     * @param id
     * @param body
     * @return
     */
    @ResponseJsonResult
    @PutMapping("/terms/{id}/assets/remove")
    public Object updateGlossaryTerm(@PathVariable UUID id, @RequestBody List<Object> body) {
        return glossaryService.updateGlossaryTerm(id, body);
    }

    /**
     * 용어 삭제
     *
     * @param id
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 용어 삭제 오류")
    @DeleteMapping("/terms/{id}")
    public Object deleteGlossaryTerm(@PathVariable UUID id) throws Exception {
        return glossaryService.deleteGlossaryTerm(id);
    }

    /**
     * 용어 사전 활동 기록
     *
     * @param entityLink
     * @param after
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/activities")
    public Object getGlossaryActivities(@RequestParam String entityLink, @RequestParam(required = false) String after) {
        return glossaryService.getGlossaryActivities(entityLink, after);
    }

    /**
     * 용어 사전 활동 기록 개수
     * @param entityLink
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/activities/count")
    public Object getGlossaryActivitiesCount(@RequestParam String entityLink) {
        return glossaryService.getGlossaryActivitiesCount(entityLink);
    }

    /**
     * 태그 리스트 호출
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/all-tags")
    public Object getAllTags() {
        return glossaryService.getAllTags();
    }

    /**
     * 데이터 모델 리스트
     *
     * @param q
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/data-models")
    public Object getDataModels(@RequestParam String search, @RequestParam String name, @RequestParam String from) throws Exception {
        return glossaryService.getDataModels(search, name, from);
    }

    /**
     * 데이터 모델 상세
     *
     * @param fqn
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/data-model")
    public Object getDataModel(@RequestParam String fqn) {
        return glossaryService.getDataModel(fqn);
    }

    /**
     * 용어 변경 > 데이터 모델 추가
     *
     * @param ids
     * @return
     */
    @ResponseJsonResult
    @PutMapping("/terms/{id}/data-models")
    public Object addDataModelsTerm(@PathVariable UUID id, @RequestBody Map<String, List<String>> ids) {
        return glossaryService.addDataModelsTerm(id, ids);
    }
}
