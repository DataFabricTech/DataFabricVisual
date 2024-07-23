package com.mobigen.ovp.glossary.client.dto.activity;

import com.mobigen.ovp.glossary.client.dto.common.FeedInfo;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class GlossaryActivity {
    private String createdBy;
    private FeedInfo feedInfo;
    private UUID id;
    private String message;
    private Timestamp updatedAt;
    private String updatedBy;
}
