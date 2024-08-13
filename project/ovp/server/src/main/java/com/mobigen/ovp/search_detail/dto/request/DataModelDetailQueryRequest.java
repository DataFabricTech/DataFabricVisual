package com.mobigen.ovp.search_detail.dto.request;

import lombok.Data;

@Data
public class DataModelDetailQueryRequest {
    String q;
    String index;
    String from;
    String size;
    String query_filter;
    String sort_field;
    String sort_order;
}
