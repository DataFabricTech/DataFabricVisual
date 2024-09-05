package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagAddResponse;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.openmete_client.ClassificationTagsClient;
import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationTagService {
    private final ClassificationTagsClient classificationTagsClient;



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
    public Object addClassificationTag(ClassificationTagAdd classificationTagAdd) {
        return new ClassificationTagAddResponse(classificationTagsClient.addClassificationTag(classificationTagAdd));
    }
}
