package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetailByName;
import lombok.Data;

@Data
public class ClassificationDetailByNameResponse {
    private String name;
    private String description;
    private String fullyQualifiedName;
    private String id;
//    private Number termCount;  인피니트 스크롤 ?

    public ClassificationDetailByNameResponse(ClassificationDetailByName classificationDetailByName) {
        this.name = classificationDetailByName.getName();
        this.description = classificationDetailByName.getDescription();
        this.fullyQualifiedName = classificationDetailByName.getFullQualifiedName();
        this.id = classificationDetailByName.getId();
    }
}
