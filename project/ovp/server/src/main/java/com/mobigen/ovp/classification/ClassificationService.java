package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.ClassificationClient;
import com.mobigen.ovp.classification.client.dto.classification.ClassificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationClient classificationClient;

    /**
     * 분류 리스트
     * @return
     */
    public Object getClassifications() {
//        log.info(""+ classificationClient.getClassifications());

        return new ClassificationResponse(classificationClient.getClassifications());
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
