package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.glossary.client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.Glossary;
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
        this.owner = glossary.getOwner();
        this.tags = new ArrayList<>();
        for(Tag tag : glossary.getTags()) {
            Map<String, String> data = new HashMap<>();
            data.put("label", tag.getName());
            data.put("tagFQN", tag.getTagFQN());
            this.tags.add(data);
        }
    }
}
