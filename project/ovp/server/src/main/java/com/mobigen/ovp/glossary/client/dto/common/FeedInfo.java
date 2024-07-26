package com.mobigen.ovp.glossary.client.dto.common;

import lombok.Data;

@Data
public class FeedInfo {
    private String headerMessage;
    private String fieldName;
    private Object entitySpecificInfo;
}
