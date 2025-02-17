package com.mobigen.datalake.metaData.classification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import lombok.AllArgsConstructor;

@RequestMapping("/api/metadata/classification")
@RestController
@AllArgsConstructor
public class MetaDataClassificationController {

    private final MetaDataClassificationService metaDataClassificationService;

    /**
     * 메타 데이터 관리 > 카테고리 담당부서 조회
     *
     * @param  name         카테고리명
     * @return Object       카테고리 담당부서 정보 반환
     */
    @ResponseJsonResult
    @GetMapping("/dept/{name}")
    public Object getClassificationDept(@PathVariable("name") String name) throws Exception {
        return metaDataClassificationService.getClassificationDept(name);
    }

    /**
     * 메타 데이터 관리 > 카테고리 Depth 조회
     *
     * @param  name         카테고리명
     * @return Object       카테고리 Depth 반환
     */
    @ResponseJsonResult
    @GetMapping("/depth")
    public Object getClassificationDepth(@RequestParam("name") String name) throws Exception {
        return metaDataClassificationService.getClassificationDepth(name);
    }

    /**
     * 메타 데이터 관리 > 카테고리 추가
     *
     * @params Map          처리 대상 데이터 Map
     * @return Object       처리 성공 데이터 수
     */
    @ResponseJsonResult
    @PostMapping("/insert")
    public Object insertClassification(@RequestBody Map<String, Object> params) throws Exception {
        return metaDataClassificationService.insertClassification(params);
    }

    /**
     * 메타 데이터 관리 > 카테고리 수정
     *
     * @params Map          처리 대상 데이터 Map
     * @return Object       처리 성공 데이터 수
     */
    @ResponseJsonResult
    @PutMapping
    public Object updateClassification(@RequestBody Map<String, Object> params) throws Exception {
        return metaDataClassificationService.updateClassification(params);
    }

    /**
     * 메타 데이터 관리 > 카테고리 삭제
     *
     * @param  id         카테고리 아이디
     * @return Map          API 호출 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/{id}")
    public Object deleteClassification(@PathVariable("id") String id) throws Exception {
        return metaDataClassificationService.deleteClassification(id);
    }

    /**
     * 메타 데이터 관리 > 카테고리 맵핑
     *
     * @params Map          처리 대상 데이터 Map
     * @return Object       API 호출 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/map")
    public Object mapClassification(@RequestBody Map<String, Object> params) throws Exception {
        return metaDataClassificationService.mapClassification(params);
    }

    /**
     * 메타 데이터 관리 > 카테고리 맵핑 > ES Sync Updates API
     *
     * @params Map          처리 대상 데이터 Map
     * @return Object       API 호출 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/essyncUpdates")
    public Object essyncUpdates(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> restResponse = metaDataClassificationService.essyncUpdates(params);
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {                    
                    result.put("esResult", true);
                } else {
                    result.put("esResult", false);
                }
            }
        } catch (Exception e) {
            result.put("esResult", false);
        }
        
        return result;
    }


    /**
     * 메타 데이터 관리 > 카테고리 맵핑 제거
     *
     * @param id          삭제할 classification 아이디
     * @param params        카테고리 맵핑 제거 대상 엔티티의 id, 테이블 타입 여부 flag
     * @return Object       처리 성공 데이터 수
     */
    @ResponseJsonResult
    @PutMapping("/map/{id}")
    public Object deleteMapClassification(@PathVariable("id") String id,
                                          @RequestBody Map<String, Object> params) throws Exception {
        return metaDataClassificationService.deleteMapClassification(id, params);
    }

    /**
     * 메타 데이터 관리 > 상세조회 > 카테고리 맵핑 제거
     *
     * @param params        카테고리 맵핑 제거 대상 엔티티의 id, 테이블 타입 여부 flag
     * @return Object       처리 성공 데이터 수
     */
    @ResponseJsonResult
    @PostMapping("/deleteMapping")
    public Object deleteMapClassification(@RequestBody Map<String, Object> params) throws Exception {
        return metaDataClassificationService.deleteMappingClassification(params);
    }

    /**
     * 메타 데이터 관리 >       순서 변경을 위한 카테고리 목록 조회
     *
     * @return Object       카테고리 목록 반환
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getClassificationList(@RequestParam("level") String level) throws Exception {
        return metaDataClassificationService.getClassificationList(level);
    }

    /**
     * 메타 데이터 관리 >       순서 변경
     *
     * @return Object       카테고리 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/updateClassificationOrdNo")
    public Object updateClassificationOrdNo(@RequestBody List<Map<String, Object>> params) throws Exception {
        return metaDataClassificationService.updateClassificationOrdNo(params);
    }

    /**
     * 메타 데이터 관리 > 카테고리 중복 여부 조회
     *
     * @param  name         카테고리명
     * @return Object       카테고리 중복 여부 반환
     */
    @ResponseJsonResult
    @GetMapping("/check/{name}")
    public Object checkDuplicateClassificationName(@PathVariable("name") String name) throws Exception {
        return metaDataClassificationService.checkDuplicateClassificationName(name);
    }

}
