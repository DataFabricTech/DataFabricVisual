package com.mobigen.ovp.search_detail.dto.request;

import com.mobigen.ovp.common.openmete_client.dto.Tag;
import lombok.Data;

import java.util.Map;

@Data
public class DataModelDetailTagDto {
    String name;
    String displayName;
    String description;
    String tagFQN;
    String source;
    String labelType;
    //    String state;
    Map<String, Object> style;

    public DataModelDetailTagDto() {
    }

    public DataModelDetailTagDto(Tag tag) {
        this.name = tag.getName();
        this.displayName = tag.getDisplayName();
        this.description = tag.getDescription();
        this.tagFQN = tag.getTagFQN();
        this.source = tag.getSource();
        this.labelType = tag.getLabelType();
        this.style = tag.getStyle();
//        this.state = tag.getState();
    }
}
