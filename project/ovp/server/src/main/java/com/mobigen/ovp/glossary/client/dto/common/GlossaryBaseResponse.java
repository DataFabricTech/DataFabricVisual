package com.mobigen.ovp.glossary.client.dto.common;

import com.mobigen.ovp.common.openmete_client.dto.Paging;
import lombok.Data;

import java.util.List;

@Data
public class GlossaryBaseResponse<T> {
    private List<T> data;
    private List<Object> errors;
    // NODE: @강이정 padding -> paging
    private Paging paging;
}
