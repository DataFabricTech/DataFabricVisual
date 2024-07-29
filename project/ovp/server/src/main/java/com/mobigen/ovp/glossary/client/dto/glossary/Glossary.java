package com.mobigen.ovp.glossary.client.dto.glossary;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import lombok.Data;

import java.util.List;

@Data
public class Glossary {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private Owner owner;
    private List<Object> tags;
}
