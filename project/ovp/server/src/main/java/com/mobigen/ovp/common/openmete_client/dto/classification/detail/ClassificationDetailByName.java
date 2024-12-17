package com.mobigen.ovp.common.openmete_client.dto.classification.detail;

import lombok.Data;

@Data
public class ClassificationDetailByName {
    private String name;
    private String description;
    private String fullQualifiedName;
    private String id;
    // private Number termCount;  인피니트 스크롤 ?
}
