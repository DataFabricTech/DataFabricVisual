package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.Lineage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "LineageClient", url = "${properties.ovp.open-metadata-url}/lineage")
public interface LineageClient {

    @GetMapping("/getLineage")
    Lineage getLineage(@RequestParam MultiValueMap<String, String> params);
}
