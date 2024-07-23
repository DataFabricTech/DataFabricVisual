package com.mobigen.ovp.classification;

import com.mobigen.ovp.classification.client.ClassificationClient;
import com.mobigen.ovp.classification.client.dto.classification.Classification;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationClient classificationClient;

    /**
     * 분류 리스트
     * @return
     */
    public List<Classification> getClassifications() {
        String fields = "owner,tags,reviewers,votes,domain";
        return classificationClient.getClassifications(fields).getData();
    }

    /**
     * 분류 수정
     * @param id
     * @param param
     * @return
     */
    public Object editClassification(UUID id, List<JsonPatchOperation> param) {
        return classificationClient.editClassification(id,param);
    }

    /**
     * 분류 삭제
     * @param id
     */
    public void deleteClassification(UUID id) {
        classificationClient.deleteClassification(id, true, true);
    }
}
