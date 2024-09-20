package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class FeedInfo {
    private String headerMessage;
    private String fieldName;
    private Object entitySpecificInfo;
}
