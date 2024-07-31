package com.mobigen.ovp.glossary.client.dto.terms;

import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Term {
    private String id;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private String name;
    private List<Tag> tags;
    private List<String> synonyms;
    private List<Object> relatedTerms;
}
