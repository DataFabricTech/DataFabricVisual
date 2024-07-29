package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class ProfileColumn {
    private String name;
    private String dataTypeDisplay;
    private String description = "";
    private Profile profile;
}
