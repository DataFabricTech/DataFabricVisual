package com.mobigen.ovp.service_manager.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Ingestion;
import com.mobigen.ovp.common.openmete_client.dto.Owner;
import lombok.Data;

@Data
public class IngestionResponse {
    private String id;
    private String name;
    private String displayName;
    private String fullyQualifiedName;
    private Owner owner;
    private String scheduleInterval;
    private String pipelineState;
    private String pipelineType;
    private Long startDate;
    private Long endDate;
    private Long timestamp;

    public IngestionResponse(Ingestion ingestion) {
        this.id = ingestion.getId();
        this.name = ingestion.getName();
        this.displayName = ingestion.getDisplayName();
        this.fullyQualifiedName = ingestion.getFullyQualifiedName();
        this.owner = ingestion.getOwner();
        this.scheduleInterval = (String) ingestion.getAirflowConfig().get("scheduleInterval");
        if(ingestion.getPipelineStatuses() != null) {
            this.pipelineState = (String) ingestion.getPipelineStatuses().get("pipelineState");
            this.startDate = (Long) ingestion.getPipelineStatuses().get("startDate");
            this.endDate = (Long) ingestion.getPipelineStatuses().get("endDate");
            this.timestamp = (Long) ingestion.getPipelineStatuses().get("timestamp");
        }
        this.pipelineType = ingestion.getPipelineType();
    }
}
