package com.mobigen.ovp.glossary.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class TermDto {
    private String description;
    private String displayName;
    private String glossary;
    private Boolean mutuallyExclusive;
    private String name;
    private Object owner;
    private List<String> relatedTerms;
    private List<String> synonyms;
    private List<Object> tags;
}
