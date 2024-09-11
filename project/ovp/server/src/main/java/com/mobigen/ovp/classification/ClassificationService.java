package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.dto.classification.ClassificationAddResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailByNameResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationEditResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationResponse;
import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagsResponse;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.ClassificationTagsClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.dto.classification.Classification;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationAdd;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationTagsClient classificationTagsClient;
    private final ClassificationClient classificationClient;

    /**
     * 분류 리스트 조회
     *
     * @return
     */
    public Object getClassifications() {
        ClassificationData response = classificationClient.getClassifications();
        // 제외할 이름들을 리스트로 작성
        List<String> excludedNames = List.of("PersonalData", "PII", "ovp_category", "Tier");

        // classificationList에서 제외할 이름들을 제외한 새로운 리스트 filteredList 생성

        List<Classification> filteredList = response.getData().stream()
                .filter(classification -> {
                    String displayName = classification.getDisplayName();
                    if(displayName == null) {
                        displayName = classification.getName();
                    }
                    return !excludedNames.contains(displayName);
                })
                .collect(Collectors.toList());

        // 필터링된 리스트로 새로운 ClassificationData 객체 생성
        ClassificationData filteredResponse = new ClassificationData();
        filteredResponse.setData(filteredList);
        filteredResponse.setPaging(response.getPaging());  // 기존 페이징 정보 유지

        log.info("" + filteredResponse);

        return new ClassificationResponse(filteredResponse);
    }

    /**
     * 분류 상세 조회
     *
     * @param id
     * @return
     */
    public Object getClassificationDetail(String id) {
        return new ClassificationDetailResponse(classificationClient.getClassificationDetail(id));
    }

    /**
     * 분류와 매칭되는 태그의 정보 조회
     *
     * @param name
     * @return
     */
    public Object getClassificationDetailByName(String name) {
        return new ClassificationDetailByNameResponse(classificationClient.getClassificationDetailByName(name));
    }

    /**
     * 분류 수정
     *
     * @param id
     * @param param
     * @return
     */
    public Object editClassification(String id, List<JsonPatchOperation> param) {
        return new ClassificationEditResponse(classificationClient.editClassification(id, param));
    }

    /**
     * 태그정보 조회(RequestParam 사용)
     *
     * @return
     * @throws Exception
     */
    public Object getClassificationTags(String parent) {
        return new ClassificationTagsResponse(classificationTagsClient.getClassificationTags(parent));
    }


    /**
     * 분류 삭제
     *
     * @param id
     */
    public Object deleteClassification(String id) throws Exception {
        return classificationClient.deleteClassification(id, true, true);
    }

    /**
     * 분류 추가
     *
     * @param classificationAdd
     */
    public Object addClassification(ClassificationAdd classificationAdd) throws Exception {
        // classificationResponse 가져오기
        ClassificationResponse classificationResponse = (ClassificationResponse) getClassifications();

        // classificationList에서 중복 체크할 리스트 가져오기
        List<Classification> classifications = classificationResponse.getClassificationList();

        // 분류 이름 중복 체크
        boolean isDuplicate = classifications.stream().anyMatch(classification -> classification.getName().equalsIgnoreCase(classificationAdd.getName()));

        if(isDuplicate) {
            throw new Exception("Duplicate classification name");
        }


        return new ClassificationAddResponse(classificationClient.addClassification(classificationAdd));
    }
}
