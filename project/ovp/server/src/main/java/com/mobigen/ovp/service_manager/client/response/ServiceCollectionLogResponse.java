package com.mobigen.ovp.service_manager.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Log;
import lombok.Data;

@Data
public class ServiceCollectionLogResponse {
    private String log;

    public ServiceCollectionLogResponse(Log log) {
        this.log = log.getProfiler_task();
    }
}
