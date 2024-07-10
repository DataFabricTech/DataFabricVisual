package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class SampleData {
    private List<Columns> columns;
    private Sample sampleData;
}
