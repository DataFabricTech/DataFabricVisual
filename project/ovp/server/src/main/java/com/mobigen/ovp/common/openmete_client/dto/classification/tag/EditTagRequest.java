package com.mobigen.ovp.common.openmete_client.dto.classification.tag;

import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.Data;

import java.util.List;

@Data
public class EditTagRequest {
    private String classificationName;
    private List<JsonPatchOperation> param;
}
