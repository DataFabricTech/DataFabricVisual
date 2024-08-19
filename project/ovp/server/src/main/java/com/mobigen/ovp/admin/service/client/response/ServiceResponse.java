package com.mobigen.ovp.admin.service.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ServiceResponse {
    private String id;
    private String fullyQualifiedName;
    private String name;
    private String description;
    private Owner owner;
    private String serviceType;
    private List<Tag> tags;
}
