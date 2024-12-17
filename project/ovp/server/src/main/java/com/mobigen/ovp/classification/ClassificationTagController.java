package com.mobigen.ovp.classification;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.EditTagRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 분류내 태그 추가
     * @param classificationTagAdd
     * @return
     */
    @ResponseJsonResult
    @PostMapping("/add")
    public Object addClassificationTag(@RequestBody ClassificationTagAdd classificationTagAdd) throws Exception {
        return classificationTagService.addClassificationTag(classificationTagAdd);
    }

    /**
     * 분류내 태그 수정
     */
    @ResponseJsonResult
    @PatchMapping("/edit/{tagId}")
    public Object editClassificationTag(@PathVariable String tagId, @RequestBody EditTagRequest editTagRequest) throws Exception {
        return classificationTagService.editClassificationTag(tagId, editTagRequest);
    }
}
