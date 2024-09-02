package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class Paging {
    private String after;
    private String before;
    private int limit;
    private int offset;
    private int total;
}
