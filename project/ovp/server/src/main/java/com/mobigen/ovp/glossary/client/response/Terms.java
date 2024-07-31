package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.glossary.client.dto.common.Tag;
import com.mobigen.ovp.glossary.client.dto.terms.Term;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Terms {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private List<Map<String, String>> tags;
    private List<String> synonyms;
    private List<Object> relatedTerms;


    public Terms(Term term) {
        this.id = term.getId();
        this.name = term.getName();
        this.displayName = term.getDisplayName();
        this.description = term.getDescription();
        this.fullyQualifiedName = term.getFullyQualifiedName();
        this.tags = new ArrayList<Map<String, String>>();
        for (Tag tag : term.getTags()) {
            Map<String, String> tagMap = new HashMap<String, String>();
            tagMap.put("label", tag.getDisplayName());
            tagMap.put("data", tag.getName());
            this.tags.add(tagMap);
        }
        this.synonyms = term.getSynonyms();
        this.relatedTerms = term.getRelatedTerms();
    }
}
