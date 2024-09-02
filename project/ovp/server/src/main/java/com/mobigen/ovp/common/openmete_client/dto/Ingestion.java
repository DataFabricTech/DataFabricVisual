package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Ingestion {
    private String id;
    private String name;
    private String displayName;
    private String fullyQualifiedName;
    private String pipelineType;
    private Owner owner;
    private Map<String, Object> airflowConfig;
    private Map<String, Object> pipelineStatuses;
}
