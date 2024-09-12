package com.mobigen.ovp.classification.client.dto.classification.tag;

import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTag;
import com.mobigen.ovp.common.openmete_client.dto.tags.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassificationTagsResponse {
    List<Tag> classificationTagList;
    int total;

    public ClassificationTagsResponse(ClassificationTag classificationTag) {
        this.total = classificationTag.getPaging().getTotal();

        List<Tag> tagList = classificationTag.getData();
        if(tagList != null) {
            this.classificationTagList = classificationTag.getData();
        } else {
            this.classificationTagList = new ArrayList<>();
        }
    }
}
