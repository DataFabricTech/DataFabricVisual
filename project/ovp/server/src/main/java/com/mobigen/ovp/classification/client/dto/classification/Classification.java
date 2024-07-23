package com.mobigen.ovp.classification.client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import lombok.Data;

@Data
public class Classification {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private Owner owner;
}
