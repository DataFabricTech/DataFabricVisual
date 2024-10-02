package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagsResponse;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.openmete_client.ClassificationTagsClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.EditTagRequest;
import com.mobigen.ovp.common.openmete_client.dto.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationTagService {
    private final ClassificationTagsClient classificationTagsClient;
    private final ClassificationService classificationService;

    //TODO : 태그 수정 추가예정

    /**
     * 분류내 태그 삭제
     *
     * @param id
     */
    public Map<String, Object> deleteClassificationTag(String id) throws Exception {

        MultiValueMap<String, String> tagParams = new LinkedMultiValueMap<>();
        tagParams.add("recursive", "true");
        tagParams.add("hardDelete", "true");

        return classificationTagsClient.deleteTag(id, tagParams);
    }

    public String getTagInfo(String tagId) {
        Map<String, Object> tagInfo = classificationTagsClient.getTag(tagId);
        return tagInfo.get("fullyQualifiedName").toString();
    }

    public String createTagInfo(String categoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("classification", Constants.OVP_CATEGORY);
        params.put("description", "OVP Category Matched Tag");
        params.put("displayName", categoryId);
        params.put("name", categoryId);
        Map<String, Object> response = (Map<String, Object>) classificationTagsClient.createTag(params);
        return response.get("id").toString();
    }

    /**
     * 분류내 태그 추가
     * @param classificationTagAdd
     * @return
     */
    public Object addClassificationTag(ClassificationTagAdd classificationTagAdd) throws Exception {
        // 해당 태그의 분류name값 가져오기
        String classificationName = classificationTagAdd.getClassification();
        ClassificationTagsResponse classificationTagsResponse = (ClassificationTagsResponse) classificationService.getClassificationTags(classificationName);

        // 중복 체크할 리스트 가져오기
        List<Tag> classificationTags = classificationTagsResponse.getClassificationTagList();

        // 태그 이름 중복 체크
        boolean isDuplicate = classificationTags.stream().anyMatch(classificationTag -> classificationTag.getName().equalsIgnoreCase(classificationTagAdd.getName()));

        if(isDuplicate) {
            throw new Exception("Duplicate classification name");
        }
        return classificationTagsClient.addClassificationTag(classificationTagAdd);
    }

    /**
     * 분류내 태그 수정
     */
    public Object editClassificationTag(String tagId, EditTagRequest editTagRequest) throws Exception {

          // 수정된 이름 가져오기
        String newTagName = editTagRequest.getParam().stream()
                .filter(op -> op.getPath().equals("/name"))
                .findFirst()
                .map(op -> op.getValue().toString())
                .orElse(null);

        // 태그 이름 수정이 있는 경우 중복 체크
        if(newTagName != null) {
            // 중복 체크할 리스트 가져오기
            ClassificationTagsResponse classificationTags = (ClassificationTagsResponse) classificationService.getClassificationTags(editTagRequest.getClassificationName());
            List<Tag> classificationTagsList = classificationTags.getClassificationTagList();

            // 태그 이름 중복 체크
            boolean isDuplicate = classificationTagsList.stream()
                    .anyMatch(classificationTag -> classificationTag.getName().equalsIgnoreCase(newTagName));

            if(isDuplicate) {
                throw new Exception("DUPLICATE_TAG_NAME");
            }
        }

        return classificationTagsClient.editClassificationTag(tagId, editTagRequest.getParam());
    }
}
