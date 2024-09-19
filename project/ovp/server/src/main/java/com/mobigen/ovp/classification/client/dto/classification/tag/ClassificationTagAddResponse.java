package com.mobigen.ovp.classification.client.dto.classification.tag;

import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTagAdd;
import lombok.Data;

@Data
public class ClassificationTagAddResponse {
    private String name;
    private String description;
    private String classification;

    public ClassificationTagAddResponse(ClassificationTagAdd classificationTagAdd) {
        this.name = classificationTagAdd.getName();
        this.description = classificationTagAdd.getDescription();
        this.classification =  classificationTagAdd.getClassification();
    }
}
