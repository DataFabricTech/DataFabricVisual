package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class Columns {
    private String name;
    private String dataType;
    private String dataTypeDisplay;
    private String description = "";
}
