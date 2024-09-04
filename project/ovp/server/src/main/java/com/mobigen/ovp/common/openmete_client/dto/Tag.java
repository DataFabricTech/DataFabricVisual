package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Tag {
    String id;
    String name;
    String displayName;
    String description;
    String tagFQN;
    String fullyQualifiedName;
    String source;
    Map<String, Object> style;
    String labelType;
    String state;
    Tag classification;
}
