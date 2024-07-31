package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.glossary.client.dto.activity.GlossaryActivity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class GlossaryActivities {
    private String createdBy;
    private String headerMessage;
    private UUID id;
    private String message;
    private Timestamp updatedAt;
    private String updatedBy;

    public GlossaryActivities(GlossaryActivity glossaryActivity) {
        this.createdBy = glossaryActivity.getCreatedBy();
        this.headerMessage = glossaryActivity.getFeedInfo().getHeaderMessage();
        this.id = glossaryActivity.getId();
        this.message = glossaryActivity.getMessage();
        this.updatedAt = glossaryActivity.getUpdatedAt();
        this.updatedBy = glossaryActivity.getUpdatedBy();
    }
}
