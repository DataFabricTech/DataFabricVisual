package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Sample {
    private List<String> columns;
    private List<ArrayList<?>> rows;
}
