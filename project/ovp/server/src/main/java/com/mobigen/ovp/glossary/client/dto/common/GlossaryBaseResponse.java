package com.mobigen.ovp.glossary.client.dto.common;

import lombok.Data;

import java.util.List;

@Data
public class GlossaryBaseResponse<T> {
    private List<T> data;
    private List<Object> errors;
    private Object padding;
}
