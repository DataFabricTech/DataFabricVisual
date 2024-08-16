package com.mobigen.ovp.service;

import com.mobigen.ovp.common.openmete_client.ServiceCollectionLogClient;
import com.mobigen.ovp.service.dto.response.ServiceCollectionLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceCollectionLogClient serviceCollectionLogClient;
    /**
     * service : Service - 수집 - 동작 [log] 조회
     * @param id
     * @return
     * **/
    public Object getServiceCollectionLog(String id) {
        return new ServiceCollectionLogResponse(serviceCollectionLogClient.getServiceCollectionLog(id));
    }
}
