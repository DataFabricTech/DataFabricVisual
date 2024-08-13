package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class Node {
    String id;
    String displayName;
    String fullyQualifiedName;
    List<LineageData> lineage;
}
