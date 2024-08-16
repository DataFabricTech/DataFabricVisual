package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.ServiceCollectionLog.ServiceCollectionLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ServiceClient", url = "${properties.ovp.open-metadata-url}/services")
public interface ServiceCollectionLogClient {
    /**
     * service : Service - 수집 - 동작 [log] 조회
     * @param id
     * @return
     * **/
    @GetMapping("/ingestionPipelines/logs/{id}/last")
    ServiceCollectionLog getServiceCollectionLog(@PathVariable("id") String id);
}
