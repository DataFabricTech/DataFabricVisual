package com.mobigen.ovp.service_manager.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.common.openmete_client.dto.Services;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class ServiceResponse {
    private String id;
    private String name;
    private String fullyQualifiedName;
    private String serviceType;
    private String description;
    private List<Map<String, Object>> tags;
    private List<Map<String, Object>> terms;
    private Owner owner;
    private String type;

    public ServiceResponse(Services service, String type) {
        this.id = service.getId();
        this.name = service.getName();
        this.fullyQualifiedName = service.getFullyQualifiedName();
        this.serviceType = service.getServiceType();
        this.description = service.getDescription();
        this.tags = new ArrayList<Map<String, Object>>();
        this.terms = new ArrayList<Map<String, Object>>();

        List<Tag> tags = service.getTags();
        if(tags != null) {
            for(Tag tag : tags) {
                String displayName = tag.getDisplayName();
                if (displayName == null || displayName.isEmpty()) {
                    displayName = tag.getName();
                }
                if ("Classification".equals(tag.getSource())) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("displayName", displayName);
                    tagData.put("tagFQN", tag.getTagFQN());
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.getSource())) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("displayName", displayName);
                    termData.put("tagFQN", tag.getTagFQN());
                    this.terms.add(termData);
                }
            }
        }
        this.owner = service.getOwner();
        this.type = type;
    }

    public ServiceResponse(ResponseEntity<Services> service, String type) {
        this.id = Objects.requireNonNull(service.getBody()).getId();
        this.name = service.getBody().getName();
        this.fullyQualifiedName = service.getBody().getFullyQualifiedName();
        this.serviceType = service.getBody().getServiceType();
        this.description = service.getBody().getDescription();
        this.tags = new ArrayList<Map<String, Object>>();
        this.terms = new ArrayList<Map<String, Object>>();

        List<Tag> tags = service.getBody().getTags();
        if(tags != null) {
            for(Tag tag : tags) {
                String displayName = tag.getDisplayName();
                if (displayName == null || displayName.isEmpty()) {
                    displayName = tag.getName();
                }
                if ("Classification".equals(tag.getSource())) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("displayName", displayName);
                    tagData.put("tagFQN", tag.getTagFQN());
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.getSource())) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("displayName", displayName);
                    termData.put("tagFQN", tag.getTagFQN());
                    this.terms.add(termData);
                }
            }
        }
        if(service.getBody().getOwner() != null) {
            this.owner = service.getBody().getOwner();
        }
        this.type = type;
    }

    public ServiceResponse(Map<String, Object> map, String type) {
        this.id = (String) map.get("id");
        this.name = (String) map.get("name");
        this.fullyQualifiedName = (String) map.get("fullyQualifiedName");
        this.serviceType = (String) map.get("serviceType");
        this.description = (String) map.get("description");
        this.tags = new ArrayList<Map<String, Object>>();
        this.terms = new ArrayList<Map<String, Object>>();
        this.type = type;

        List<Map<String, Object>> tags = (List<Map<String, Object>>) map.get("tags");
        if(tags != null) {
            for(Map<String, Object> tag : tags) {
                String displayName = "";
                if (tag.get("displayName") == null || "".equals(tag.get("displayName"))) {
                    displayName = tag.get("name").toString();
                } else {
                    displayName = tag.get("displayName").toString();
                }

                if ("Classification".equals(tag.get("source"))) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("displayName", displayName);
                    tagData.put("tagFQN", tag.get("tagFQN"));
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.get("source"))) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("displayName", displayName);
                    termData.put("tagFQN", tag.get("fullyQualifiedName"));
                    this.terms.add(termData);
                }
            }
        }
        Map<String, Object> ownerMap = (Map<String, Object>) map.get("owner");
        if (ownerMap != null) {
            this.owner = new Owner();
            this.owner.setName((String) ownerMap.get("name"));
            this.owner.setId((String) ownerMap.get("id"));
            this.owner.setType((String) ownerMap.get("type"));
            this.owner.setFullyQualifiedName((String) ownerMap.get("fullyQualifiedName"));
            this.owner.setDisplayName((String) ownerMap.get("displayName"));
        } else {
            this.owner = new Owner();
        }
    }
}
