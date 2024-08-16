package com.mobigen.ovp.service.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.ServiceCollectionLog.ServiceCollectionLog;
import lombok.Data;

@Data
public class ServiceCollectionLogResponse {
    private String log;

    public ServiceCollectionLogResponse(ServiceCollectionLog serviceCollectionLog) {
        this.log = serviceCollectionLog.getProfiler_task();
    }
}
