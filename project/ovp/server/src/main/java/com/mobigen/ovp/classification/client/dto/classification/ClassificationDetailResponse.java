package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.classification.detail.ClassificationDetail;
//import com.mobigen.ovp.common.openmete_client.dto.tags.Tags;
import lombok.Data;

//import java.util.ArrayList;
//import java.util.List;

@Data
public class ClassificationDetailResponse {
    private String id;
    private String name;
    private String displayName;
    private String description;
//    private List<Tags> tags;
//
    public ClassificationDetailResponse(ClassificationDetail classificationDetail) {
        this.id = classificationDetail.getId();
        this.name = classificationDetail.getName();
        this.displayName = classificationDetail.getDisplayName();
        this.description = classificationDetail.getDescription();
//        List<Tags> tagList = classificationDetail.getTags();
//        if (tagList != null) {
//            this.tags = classificationDetail.getTags();
//        } else {
//            this.tags = new ArrayList<>();
//        }

    }
}
