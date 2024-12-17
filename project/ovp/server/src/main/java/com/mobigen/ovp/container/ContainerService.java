package com.mobigen.ovp.container;

import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.openmete_client.ContainerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerClient containerClient;
    private final ModelConvertUtil modelConvertUtil;

    public Object getContainersObject(String id) {
        Map<String, Object> resultMap = modelConvertUtil.convertPreviewData(containerClient.getContainersObject(id, "dataModel,tags, owner"), "unstructured", null);

        return resultMap;
    }

    public Object getContainersMetadata(String fqn) {
        Map<String, Object> resultMap = containerClient.getMetaData(fqn);
        return resultMap.get("rdfs");
    }
}
