package com.mobigen.ovp.classification.client.dto.classification.tag;

import com.mobigen.ovp.common.openmete_client.dto.classification.tag.ClassificationTag;
import com.mobigen.ovp.common.openmete_client.dto.tags.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationTagsResponse {
    List<Tag> classificationTagList;
    int total;

    // 생성자
    public ClassificationTagsResponse(ClassificationTag classificationTag) {
        this.classificationTagList = classificationTag.getData();
        this.total = classificationTag.getPaging().getTotal();
    }
}
