package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.classification.ClassificationEdit;
import lombok.Data;

@Data
public class ClassificationEditResponse {
    private String displayName;
    private String description;

    public ClassificationEditResponse(ClassificationEdit classificationEdit) {
        this.displayName = classificationEdit.getDisplayName();
        this.description = classificationEdit.getDescription();
    }
}
