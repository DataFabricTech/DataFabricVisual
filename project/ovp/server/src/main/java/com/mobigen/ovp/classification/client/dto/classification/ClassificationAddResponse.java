package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationAdd;
import lombok.Data;

@Data
public class ClassificationAddResponse {
    private String name;
    private String description;

    public ClassificationAddResponse(ClassificationAdd classificationAdd) {
        this.name = classificationAdd.getName();
        this.description = classificationAdd.getDescription();
    }
}
