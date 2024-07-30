package com.mobigen.ovp.glossary.client.dto.common;

import lombok.Data;

import java.util.Map;

@Data
public class Tag {
    private String description;
    private String displayName;
    private String labelType;
    private String name;
    private String source;
    private String state;
    private Object style;
    private String tagFQN;

    public Tag(Map<String,?> map) {
        this.description = (String) map.get("description");
        this.displayName = String.valueOf(map.get("displayName"));
        this.labelType = String.valueOf(map.get("labelType"));
        this.name = String.valueOf(map.get("name"));
        this.source = String.valueOf(map.get("source"));
        this.state = String.valueOf(map.get("state"));
        this.style = map.get("style");
        this.tagFQN = String.valueOf(map.get("tagFQN"));
    }
}
