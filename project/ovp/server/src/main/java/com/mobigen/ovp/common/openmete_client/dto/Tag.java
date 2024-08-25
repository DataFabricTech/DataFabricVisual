package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class Tag {
    String name;
    String displayName;
    String description;
    String tagFQN;
    String fullyQualifiedName;
    String source;
}
