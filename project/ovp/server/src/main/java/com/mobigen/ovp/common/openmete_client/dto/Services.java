package com.mobigen.ovp.common.openmete_client.dto;

import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Services {
    private String id;
    private String fullyQualifiedName;
    private String name;
    private String description;
    private Owner owner;
    private String serviceType;
    private List<Tag> tags;
}
