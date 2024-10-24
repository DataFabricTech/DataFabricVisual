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
     * 사용자 목록 (전체)
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/user/all")
    Object getUserAll() throws Exception {
        log.info("");

        return searchDetailService.getUserAll();
    }

    /**
     * 태그 목록 (전체)
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

    /**
     * 용어 목록 (전체)
     *
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/glossary/all")
    Object getGlossaryAll() throws Exception {
        log.info("");

        return searchDetailService.getGlossaryAll();
    }

    /**
     * 데이터 모델 상세 (테이블, 스토리지)
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
     * 스키마 (테이블, 스토리지)
     *
     * @param id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/schema/{id}")
    Object getDataModelSchema(@PathVariable String id, @RequestParam String type) throws Exception {
        log.info("");

        return searchDetailService.getDataModelSchema(id, type);
    }

    /**
     * 샘플데이터
     *
     * @param id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/sample-data/{id}")
    Object getDataModelSampleData(@PathVariable String id, @RequestParam String type) throws Exception {
        log.info("");
        return searchDetailService.getDataModelSampleData(id, type);
    }

    /**
     * 프로파일링 - table
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

    /**
     * 프로파일링 - container
     *
     * @param fqn
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/containers/profile/{fqn}")
    Object getContainersTableProfile(@PathVariable String fqn) {
        log.info("");
        return searchDetailService.getContainersTableProfile(fqn);
    }

    /**
     * 쿼리
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/query")
    Object getDataModelQuery(@RequestParam MultiValueMap<String, String> params) throws Exception {
        log.info("");

        return searchDetailService.getDataModelQuery(params);
    }

    /**
     * 리니지 그래프 (테이블, 스토리지)
     *
     * @param params
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/lineage")
    Object getDataModelLineage(@RequestParam MultiValueMap<String, String> params) {
        log.info("");

        return searchDetailService.getDataModelLineage(params);
    }

    /**
     * 추천 (테이블, 스토리지)
     *
     * @param id
     * @param dataModelDetailVote
     * @return
     */
    @ResponseJsonResult
    @PutMapping("/{id}/vote")
    Object changeVote(@PathVariable String id, @RequestParam String type, @RequestBody DataModelDetailVote dataModelDetailVote) {
        log.info("");

        return searchDetailService.changeVote(id, type, dataModelDetailVote);
    }

    /**
     * 비추천 (테이블, 스토리지)
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PutMapping("/{id}/follow")
    Object followDataModel(@PathVariable String id, @RequestParam String type) throws Exception {
        log.info("");

        return searchDetailService.followDataModel(id, type);
    }

    /**
     * 북마크 (테이블, 스토리지)
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @DeleteMapping("/{id}/follow")
    Object unfollowDataModel(@PathVariable String id, @RequestParam String type) throws Exception {
        log.info("");

        return searchDetailService.unfollowDataModel(id, type);
    }

    /**
     * 데이터 모델 변경 (테이블, 스토리지)
     *
     * @param id
     * @param type
     * @param body
     * @return
     */
    @ResponseJsonResult
    @PatchMapping("/{id}")
    Object changeDataModel(@PathVariable String id, @RequestParam String type, @RequestBody List<Map<String, Object>> body) {
        log.info("");

        return searchDetailService.changeDataModel(id, type, body);
    }

    /**
     * 태그 변경 (테이블, 스토리지)
     *
     * @param id
     * @param type
     * @param body
     * @return
     */
    @ResponseJsonResult
    @PatchMapping("/{id}/tag")
    Object changeDataModelTag(@PathVariable String id, @RequestParam String type, @RequestParam String target, @RequestParam boolean isCategory, @RequestBody List<Map<String, Object>> body) {
        log.info("");

        return searchDetailService.ChangeDataModelTag(id, type, target, isCategory, body);
    }

    @ResponseJsonResult
    @DeleteMapping("/{id}")
    Object deleteDataModel(@PathVariable String id, @RequestParam String type) {
        log.info("");

        return searchDetailService.deleteDataModel(id, type);
    }

    /**
     * 추천 데이터 목록 조회 - SAMPLEDATA 없을 경우
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/recommend/clustering/{id}")
    public Object getRecommendDataModelClustering(@PathVariable String id, @RequestParam String type) throws Exception {
        return searchDetailService.getRecommendDataModelClustering(id, type);
    }

    /**
     * 추천 데이터 목록 조회 - SampleData 있을 경우
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/recommend/embedding/{id}")
    public Object getRecommendDataModelEmbedding(@PathVariable String id, @RequestParam String type) throws Exception {
        return searchDetailService.getRecommendDataModelEmbedding(id, type);
    }
}
