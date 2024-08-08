package com.mobigen.ovp.classification;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tags")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClassificationTagController {
    private final ClassificationTagService classificationTagService;

    /**
     * 분류내 태그 삭제
     * @param id
     */
    @ResponseJsonResult(errorMessage = "Open-metadata 분류내 태그 삭제 오류")
    @DeleteMapping("/{id}")
    public Object deleteClassification(@PathVariable String id) throws Exception {
        return classificationTagService.deleteClassificationTag(id);
    }
}
