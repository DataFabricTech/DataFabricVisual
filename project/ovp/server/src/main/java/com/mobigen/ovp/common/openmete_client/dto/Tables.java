package com.mobigen.ovp.common.openmete_client.dto;

import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Tables {
    String id;
    String fullyQualifiedName;
    String name;
    String displayName;
    String description;
    Owner owner;
    List<Followers> followers;
    Votes votes;
    List<Columns> columns;
    Service service;
    Entity entity;
    List<Tag> tags;
}
