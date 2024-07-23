package com.mobigen.ovp.classification.client.dto.common;

import lombok.Data;

import java.util.List;

@Data
public class ClassificationBaseResponse<T>{
    private List<T> data;
    private List<Object> errors;
    private Object padding;
}
