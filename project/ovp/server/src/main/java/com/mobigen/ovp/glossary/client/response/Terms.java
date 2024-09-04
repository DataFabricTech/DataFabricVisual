package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.glossary.client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.Term;
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
        if(term.getTags() != null) {
            for (Tag tag : term.getTags()) {
                Map<String, String> tagMap = new HashMap<String, String>();
                tagMap.put("label", tag.getName());
                tagMap.put("tagFQN", tag.getTagFQN());
                this.tags.add(tagMap);
            }
        }
        this.synonyms = term.getSynonyms();
        this.relatedTerms = new ArrayList<>();
        for(Object obj : term.getRelatedTerms()) {
            if(obj instanceof Map<?,?>) {
                Map<String, ?> data = (Map<String, ?>) obj;
                Map<String, String> termMap = new HashMap<>();
                termMap.put("label", (String) data.get("name"));
                termMap.put("id", (String) data.get("id"));
                this.relatedTerms.add(termMap);
            }
        }
    }
}
