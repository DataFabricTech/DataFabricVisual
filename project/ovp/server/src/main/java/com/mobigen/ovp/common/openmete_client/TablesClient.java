package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.SampleData;
import com.mobigen.ovp.common.openmete_client.dto.TableProfile;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "TablesClient", url = "${properties.ovp.open-metadata-url}/tables")
public interface TablesClient {

    @GetMapping("/{id}")
    Tables getTablesName(@PathVariable("id") String id, @RequestParam Map<String, String> params);

    @GetMapping("/{id}/sampleData")
    SampleData getSampleData(@PathVariable("id") String id);

    @GetMapping("/{fqn}/tableProfile/latest")
    TableProfile getTableProfile(@PathVariable("fqn") String fqn);
}
