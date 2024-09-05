package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagAddResponse;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.ClassificationClient2;
//import com.mobigen.ovp.common.openmete_client.TagClient;
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
//    private final TagClient tagClient;
    private final ClassificationClient classificationClient;
    private final ClassificationClient2 classificationClient2;



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

        return classificationClient.deleteTag(id, tagParams);
    }

    public String getTagInfo(String tagId) {
        Map<String, Object> tagInfo = classificationClient.getTag(tagId);
        return tagInfo.get("fullyQualifiedName").toString();
    }

    public String createTagInfo(String categoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("classification", "OVP_category");
        params.put("description", "OVP Category Matched Tag");
        params.put("displayName", categoryId);
        params.put("name", categoryId);
        Map<String, Object> response = (Map<String, Object>) classificationClient.createTag(params);
        return response.get("id").toString();
    }

    /**
     * 분류내 태그 추가
     * @param classificationTagAdd
     * @return
     */
    public Object addClassificationTag(ClassificationTagAdd classificationTagAdd) {
        return new ClassificationTagAddResponse(classificationClient.addClassificationTag(classificationTagAdd));
    }
}
