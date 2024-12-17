package com.mobigen.ovp.glossary.client.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TermDto {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private Map<String, Object> glossary;
    private Boolean mutuallyExclusive;
    private Object owner;
    private List<String> relatedTerms;
    private List<String> synonyms;
    private List<Object> tags;
    private Map<String, Object> style;
}
