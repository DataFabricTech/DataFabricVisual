package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class StorageColumn {
    List<Columns> columns;
    boolean isPartitioned;
}
