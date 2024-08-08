package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetail;
import lombok.Data;

@Data
public class ClassificationDetailResponse {
    private String id;
    private String name;
    private String displayName;
    private String description;

    public ClassificationDetailResponse(ClassificationDetail classificationDetail) {
        this.id = classificationDetail.getId();
        this.name = classificationDetail.getName();
        this.displayName = classificationDetail.getDisplayName();
        this.description = classificationDetail.getDescription();
    }
}
