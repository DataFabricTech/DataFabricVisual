package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class GlossaryActivity {
    private String cardStyle;
    private String createdBy;
    private FeedInfo feedInfo;
    private UUID id;
    private String message;
    private Timestamp updatedAt;
    private String updatedBy;
    private String fieldOperation;
}
