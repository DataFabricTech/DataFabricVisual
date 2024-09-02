package com.mobigen.ovp.glossary.client.dto;

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

    public Tag() {}

    public Tag(Map<String,?> map) {
        this.description = (String) map.get("description");
        this.displayName = String.valueOf(map.get("displayName"));
        this.labelType = "Manual";
        this.name = String.valueOf(map.get("name"));
        this.source = "Classification";
        this.state = "Confirmed";
        this.style = map.get("style");
        this.tagFQN = String.valueOf(map.get("fullyQualifiedName"));
    }
}
