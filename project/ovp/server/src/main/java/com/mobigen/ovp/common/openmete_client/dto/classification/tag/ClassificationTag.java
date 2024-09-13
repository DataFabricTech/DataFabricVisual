package com.mobigen.ovp.common.openmete_client.dto.classification.tag;

import com.mobigen.ovp.common.openmete_client.dto.Paging;
import com.mobigen.ovp.common.openmete_client.dto.tags.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationTag {
    List<Tag> data;
    Paging paging;
}
