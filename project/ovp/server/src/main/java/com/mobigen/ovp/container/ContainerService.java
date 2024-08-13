package com.mobigen.ovp.container;

import com.mobigen.ovp.common.openmete_client.ContainerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerClient containerClient;

    public Object getContainersName(String id) {
        Map<String, Object> result = containerClient.getContainersName((id));
        return result;
    }

}
