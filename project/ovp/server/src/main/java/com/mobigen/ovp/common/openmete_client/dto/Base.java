package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class Base<T> {
    private List<T> data;
    private List<Object> errors;
    private Paging paging;
}
