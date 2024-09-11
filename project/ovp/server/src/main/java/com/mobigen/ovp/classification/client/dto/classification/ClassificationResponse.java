package com.mobigen.ovp.classification.client.dto.classification;
import com.mobigen.ovp.common.openmete_client.dto.classification.Classification;
import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationData;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationResponse {
    List<Classification> classificationList;
    int total;

    // 생성자
    public ClassificationResponse(ClassificationData classificationData) {
        this.classificationList = classificationData.getData();
        this.total = classificationData.getPaging().getTotal();
    }
}
