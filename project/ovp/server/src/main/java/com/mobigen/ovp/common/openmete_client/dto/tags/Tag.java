package com.mobigen.ovp.common.openmete_client.dto.tags;

import lombok.Data;

@Data
public class Tag {
    private String id;
    private String name;
    private String displayName;
    private String fullyQualifiedName;
    private String description;
    private String tagFQN;
    private Object changeDescription;
}
