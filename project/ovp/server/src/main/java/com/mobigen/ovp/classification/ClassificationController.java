package com.mobigen.ovp.classification;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/classifications")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClassificationController {
    private  final ClassificationService classificationService;

    /**
     * 분류 리스트 조회
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getClassification() {
        return classificationService.getClassifications();
    }

    /**
     * 분류 상세 조회
     * @oaran id
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/list/{id}")
    public Object getClassificationDetail(@PathVariable String id) {
        log.info("");

        return classificationService.getClassificationDetail(id);
    }

    /**
     * 분류와 매칭되는 태그의 정보 조회 - 위에 2개와 같이 호출
     * @oaran name
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/name/{name}")
    public Object getClassificationDetailByName(@PathVariable String name) {
        log.info("");

        return classificationService.getClassificationDetailByName(name);
    }

    /**
     * 분류 수정
     * @param id
     * @return
     */
    @ResponseJsonResult
    @PatchMapping(value = "/{id}")
    public Object editClassification(@PathVariable String id, @RequestBody List<JsonPatchOperation> param) {

        return classificationService.editClassification(id, param);
    }

    /**
     * 선택한 분류를 클릭할 때, 할때마다 해당되는 태그목록이 조회되어 불러옴
     * @param parent
     * @return
     */
    @ResponseJsonResult
    @GetMapping("/tags")
    public Object getClassificationTags(@RequestParam String parent) throws Exception{
        log.info("");

        return classificationService.getClassificationTags(parent);
    }

//    /**
//     * 분류 삭제
//     * @param id
//     */
//    @ResponseJsonResult
//    @DeleteMapping("/{id}")
//    public void deleteClassification(@PathVariable UUID id) {
//        classificationService.deleteClassification(id);
//    }
}
