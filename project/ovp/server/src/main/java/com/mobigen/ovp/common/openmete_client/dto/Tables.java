package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class Tables {
    private String id;
    private String fullyQualifiedName;
    private String name;
    private String description;
    private Owner owner;
    private List<?> followers;
    private Votes votes;
    private List<Columns> columns;
}
