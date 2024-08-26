package com.mobigen.ovp.search_detail.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class DataModelDetailCategory {
    UUID id;
    String name;
    String tagName;
    String tagDisplayName;
    String tagDescription;
    String tagFQN;
}
