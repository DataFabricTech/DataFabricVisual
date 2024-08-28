package com.mobigen.ovp.common.openmete_client;

import com.mobigen.ovp.common.openmete_client.dto.SampleData;
import com.mobigen.ovp.common.openmete_client.dto.TableProfile;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailVote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "TablesClient", url = "${properties.ovp.open-metadata-url}/tables")
public interface TablesClient {

    @GetMapping("/{id}")
    Tables getTablesName(@PathVariable("id") String id, @RequestParam Map<String, String> params);

    @GetMapping("/{id}/sampleData")
    SampleData getSampleData(@PathVariable("id") String id);

    @GetMapping("/{fqn}/tableProfile/latest")
    TableProfile getTableProfile(@PathVariable("fqn") String fqn);

    @GetMapping("/{fqn}/tableProfile/latest")
    Map<String, Object> getSearchPreview(@PathVariable("fqn") String fqn);

    @PutMapping("/{id}/vote")
    Tables changeVote(@PathVariable("id") String id, @RequestBody DataModelDetailVote dataModelDetailVote);

    @PutMapping("/{modelId}/followers")
    Object follow(@PathVariable("modelId") String modelId, @RequestBody UUID id);

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    Map<String, Object> changeDataModel(@PathVariable("id") String id, @RequestParam Map<String, String> params, @RequestBody List<Map<String, Object>> body);

    @DeleteMapping("/{id}/followers/{userId}")
    Object unfollow(@PathVariable("id") String id, @PathVariable("userId") String userId);

    @DeleteMapping("{id}")
    Object delete(@PathVariable("id") String id,
                  @RequestParam(defaultValue = "true") Boolean recursive,
                  @RequestParam(defaultValue = "true") Boolean hardDelete);
}
