package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class Log {
    private String profiler_task;
    private String ingestion_task;
    private Number total;
}
