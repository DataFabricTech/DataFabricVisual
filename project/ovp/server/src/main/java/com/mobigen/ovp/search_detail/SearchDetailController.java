package com.mobigen.ovp.search_detail;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search/detail")
public class SearchDetailController {

    private final SearchDetailService searchDetailService;

    /**
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/filter/user")
    Object getUserFilter() throws Exception {
        log.info("");

        return searchDetailService.getUserFilter();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/tag/all")
    Object getTagAll() throws Exception {
        log.info("");

        return searchDetailService.getTagAll();
    }

    @ResponseJsonResult
    @GetMapping("/glossary/all")
    Object getGlossaryAll() throws Exception {
        log.info("");

        return searchDetailService.getGlossaryAll();
    }

    /**
     * 데이터 모델 상세
     *
     * @param id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/{id}")
    Object getDataModelDetail(HttpServletRequest request, @PathVariable String id, @RequestParam String type) throws Exception {
        log.info("");

        return searchDetailService.getDataModelDetail(id, type);
    }

    /**
     * 스키마
     *
     * @param id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/schema/{id}")
    Object getDataModelSchema(@PathVariable String id) {
        log.info("");

        return searchDetailService.getDataModelSchema(id);
    }

    /**
     * 샘플데이터
     *
     * @param id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/sample-data/{id}")
    Object getDataModelSampleData(@PathVariable String id) {
        log.info("");
        return searchDetailService.getDataModelSampleData(id);
    }

    /**
     * 프로파일링
     *
     * @param fqn
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/profile/{fqn}")
    Object getTableProfile(@PathVariable String fqn) {
        log.info("");

        return searchDetailService.getTableProfile(fqn);
    }

    @ResponseJsonResult
    @GetMapping("/query")
    Object getDataModelQuery(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("");

        return searchDetailService.getDataModelQuery(params);
    }

    @ResponseJsonResult
    @GetMapping("/lineage/{type}")
    Object getDataModelLineage(@PathVariable String type, @RequestParam MultiValueMap<String, String> params) {
        log.info("");

        return searchDetailService.getDataModelLineage(type, params);
    }

    @ResponseJsonResult
    @PutMapping("/{id}/vote")
    Object changeVote(@PathVariable String id, @RequestBody DataModelDetailVote dataModelDetailVote) {
        log.info("");

        return searchDetailService.changeVote(id, dataModelDetailVote);
    }

    @ResponseJsonResult
    @PutMapping("/{id}/follow")
    Object followDataModel(@PathVariable String id) throws Exception {
        log.info("");

        return searchDetailService.followDataModel(id);
    }

    @ResponseJsonResult
    @DeleteMapping("/{id}/follow")
    Object unfollowDataModel(@PathVariable String id) throws Exception {
        log.info("");

        return searchDetailService.unfollowDataModel(id);
    }

    @ResponseJsonResult
    @PatchMapping("/{id}")
    Object changeDataModel(@PathVariable String id, @RequestBody List<Map<String, String>> body) {
        log.info("");

        return searchDetailService.changeDataModel(id, body);
    }
}
