package com.mobigen.ovp.admin.service.client.response;

import lombok.Data;

import java.util.List;

@Data
public class ServiceBaseResponse<T> {
    private List<T> data;
    private List<Object> errors;
    private Object paging;
}
