package com.mobigen.ovp.glossary.client.dto.terms;

import lombok.Data;

@Data
public class GlossaryTerms {
    private String id;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private String name;
}
