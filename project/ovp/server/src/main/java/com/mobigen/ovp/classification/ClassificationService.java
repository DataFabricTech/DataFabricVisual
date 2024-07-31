package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.ClassificationClient;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailByNameResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationResponse;
import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagsResponse;
import com.mobigen.ovp.common.openmete_client.dto.classification.Classification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationClient classificationClient;

    /**
     * 분류 리스트 조회
     * @return
     */
    public Object getClassifications() {
//        log.info(""+ classificationClient.getClassifications());

        return new ClassificationResponse(classificationClient.getClassifications());
    }

    /**
     * 분류 상세 조회
     * @return
     */
    public Object getClassificationDetail(String id) {
        System.out.println(classificationClient.getClassificationDetail(id));

        return new ClassificationDetailResponse(classificationClient.getClassificationDetail(id));
    }

    /**
     * 분류와 매칭되는 태그의 정보 조회 - 위에 2개와 같이 호출
     * @return
     */
    public Object getClassificationDetailByName(String name) {
        System.out.println(classificationClient.getClassificationDetailByName(name));

        return new ClassificationDetailByNameResponse(classificationClient.getClassificationDetailByName(name));
    }

    /**
     * 태그정보 조회(RequestParam 사용)
     * @return
     * @throws Exception
     */
    public Object getClassificationTags(String parentFqn) {
        return new ClassificationTagsResponse(classificationClient.getClassificationTags(parentFqn));
    }

//    /**
//     * 분류 수정
//     * @param id
//     * @param param
//     * @return
//     */
//    public Object editClassification(UUID id, List<JsonPatchOperation> param) {
//        return classificationClient.editClassification(id,param);
//    }
//
//    /**
//     * 분류 삭제
//     * @param id
//     */
//    public void deleteClassification(UUID id) {
//        classificationClient.deleteClassification(id, true, true);
//    }
}
