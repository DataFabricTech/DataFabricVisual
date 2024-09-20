package com.mobigen.ovp.glossary.client.response;

import com.mobigen.ovp.common.openmete_client.dto.GlossaryActivity;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

@Data
public class GlossaryActivities {
    private String cardStyle;
    private String createdBy;
    private String headerMessage;
    private UUID id;
    private String message;
    private String updatedAt;
    private String updatedBy;
    private String fieldOperation;
    private Object entitySpecificInfo;

    public GlossaryActivities(GlossaryActivity glossaryActivity) {
        this.cardStyle = glossaryActivity.getCardStyle();
        this.createdBy = glossaryActivity.getCreatedBy();
        if(glossaryActivity.getFeedInfo() != null) {
            this.headerMessage = glossaryActivity.getFeedInfo().getHeaderMessage();
            this.entitySpecificInfo = glossaryActivity.getFeedInfo().getEntitySpecificInfo();
        } else {
            this.headerMessage = null;
            this.entitySpecificInfo = new HashMap<>();
        }
        this.id = glossaryActivity.getId();
        this.message = glossaryActivity.getMessage();
        this.updatedAt = getTimeDifference(glossaryActivity.getUpdatedAt());
        this.updatedBy = glossaryActivity.getUpdatedBy();
        this.fieldOperation = glossaryActivity.getFieldOperation();
    }

    public String getTimeDifference(Timestamp timestamp) {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime updatedAtDateTime = timestamp.toLocalDateTime();

        Duration duration = Duration.between(updatedAtDateTime, now);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        if (days > 0) {
            return days + "days ago";
        } else if (hours > 0) {
            return hours + "hour ago";
        } else if (minutes > 0) {
            return minutes + "minutes ago";
        } else {
            return "0 minutes ago";
        }
    }
}
