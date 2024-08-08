package com.mobigen.ovp.classification;

import com.mobigen.ovp.common.openmete_client.TagClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationTagService {
    private final TagClient tagClient;

    //TODO : 태그 추가 추가예정

    //TODO : 태그 수정 추가예정

    /**
     * 분류내 태그 삭제
     * @param id
     */
    public Map<String, Object> deleteClassificationTag(String id) throws Exception {

        MultiValueMap<String, String> tagParams = new LinkedMultiValueMap<>();
        tagParams.add("recursive", "true");
        tagParams.add("hardDelete", "true");

        return tagClient.deleteTag(id, tagParams);
    }

}
