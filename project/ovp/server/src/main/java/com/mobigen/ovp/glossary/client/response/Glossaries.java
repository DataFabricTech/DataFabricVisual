package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.glossary.client.dto.glossary.Glossary;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Glossaries {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private Owner owner;
    private List<Map<String, String>> tags;

    public Glossaries(Glossary glossary) {
        this.id = glossary.getId();
        this.name = glossary.getName();
        this.displayName = glossary.getDisplayName();
        this.description = glossary.getDescription();
        this.fullyQualifiedName = glossary.getFullyQualifiedName();
        this. owner = glossary.getOwner();
        this.tags = new ArrayList<>();
        for(Object objTag : glossary.getTags()) {
            Map<String, String> tag = (Map<String, String>) objTag;
            Map<String, String> data = new HashMap<>();
            data.put("label", tag.get("displayName"));
            data.put("data", tag.get("name"));
            this.tags.add(data);
        }
    }
}
