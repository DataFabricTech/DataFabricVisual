package com.mobigen.ovp.glossary.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class GlossaryDto {
    private String name;
    private String displayName;
    private String description;
    private Object owner;
    private List<String> tags;
    private boolean mutuallyExclusive;
}
