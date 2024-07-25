package com.mobigen.ovp.classification;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    /**
//     * 분류 수정
//     * @param id
//     * @param param
//     * @return
//     */
//    @ResponseJsonResult
//    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
//    public Object editClassification(@PathVariable UUID id, @RequestBody List<JsonPatchOperation> param) {
//        return classificationService.editClassification(id, param);
//    }
//
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
