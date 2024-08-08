package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailByNameResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationDetailResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationEditResponse;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationResponse;
import com.mobigen.ovp.classification.client.dto.classification.tag.ClassificationTagsResponse;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.TagClient;
import com.mobigen.ovp.common.openmete_client.dto.classification.Classification;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationClient classificationClient;
    private final TagClient tagClient;

    /**
     * 분류 리스트 조회
     * @return
     */
    public Object getClassifications() {
        ClassificationData response = classificationClient.getClassifications();

         // classificationList에서 "OVP_category"를 제외한 새로운 리스트 filteredList 생성
        List<Classification> filteredList = response.getData().stream()
                .filter(classification -> !"OVP_category".equals(classification.getDisplayName()))
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
     * @param id
     * @return
     */
    public Object getClassificationDetail(String id) {
        System.out.println(classificationClient.getClassificationDetail(id));

        return new ClassificationDetailResponse(classificationClient.getClassificationDetail(id));
    }

    /**
     * 분류와 매칭되는 태그의 정보 조회
     * @param name
     * @return
     */
    public Object getClassificationDetailByName(String name) {
        System.out.println(classificationClient.getClassificationDetailByName(name));

        return new ClassificationDetailByNameResponse(classificationClient.getClassificationDetailByName(name));
    }

    /**
     * 분류 수정
     * @param id
     * @param param
     * @return
     */
    public Object editClassification(String id, List<JsonPatchOperation> param) {
        return new ClassificationEditResponse(classificationClient.editClassification(id, param));
    }

    /**
     * 태그정보 조회(RequestParam 사용)
     * @return
     * @throws Exception
     */
    public Object getClassificationTags(String parent) {
        return new ClassificationTagsResponse(tagClient.getClassificationTags(parent));
    }

    public String getTagInfo(String tagId) {
        Map<String, Object> tagInfo = tagClient.getTag(tagId);
        return tagInfo.get("fullyQualifiedName").toString();
    }


    /**
     * 분류 삭제
     * @param id
     */
    public int deleteClassification(String id) throws Exception {
        ResponseEntity<Void> response = classificationClient.deleteClassification(id, true, true);
        if(response.getStatusCode() == HttpStatus.OK) {
            return 1;
        } else {
            throw new Exception();
        }
    }
}
