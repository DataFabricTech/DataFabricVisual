package com.mobigen.ovp.admin.service.client.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class ServiceEntity {
    private String id;
    private String name;
    private String fullyQualifiedName;
    private String serviceType;
    private String description;
    private List<Map<String, Object>> tags;
    private List<Map<String, Object>> relatedTerms;
    private Owner owner;

    public ServiceEntity(ServiceResponse service) {
        this.id = service.getId();
        this.name = service.getName();
        this.fullyQualifiedName = service.getFullyQualifiedName();
        this.serviceType = service.getServiceType();
        this.description = service.getDescription();
        this.tags = new ArrayList<Map<String, Object>>();
        this.relatedTerms = new ArrayList<Map<String, Object>>();

        List<Tag> tags = service.getTags();
        if(tags != null) {
            for(Tag tag : tags) {
                if ("Classification".equals(tag.getSource())) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("label", tag.getName());
                    tagData.put("tagFQN", tag.getTagFQN());
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.getSource())) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("label", tag.getName());
                    termData.put("id", tag.getTagFQN());
                    this.relatedTerms.add(termData);
                }
            }
        }
        this.owner = service.getOwner();
    }

    public ServiceEntity(ResponseEntity<ServiceResponse> service) {
        this.id = Objects.requireNonNull(service.getBody()).getId();
        this.name = service.getBody().getName();
        this.fullyQualifiedName = service.getBody().getFullyQualifiedName();
        this.serviceType = service.getBody().getServiceType();
        this.description = service.getBody().getDescription();
        this.tags = new ArrayList<Map<String, Object>>();
        this.relatedTerms = new ArrayList<Map<String, Object>>();

        List<Tag> tags = service.getBody().getTags();
        if(tags != null) {
            for(Tag tag : tags) {
                if ("Classification".equals(tag.getSource())) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("label", tag.getName());
                    tagData.put("tagFQN", tag.getTagFQN());
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.getSource())) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("label", tag.getName());
                    termData.put("id", tag.getTagFQN());
                    this.relatedTerms.add(termData);
                }
            }
        }
        this.owner = service.getBody().getOwner();
    }

    public ServiceEntity(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.name = (String) map.get("name");
        this.fullyQualifiedName = (String) map.get("fullyQualifiedName");
        this.serviceType = (String) map.get("serviceType");
        this.description = (String) map.get("description");
        this.tags = new ArrayList<Map<String, Object>>();
        this.relatedTerms = new ArrayList<Map<String, Object>>();

        List<Map<String, Object>> tags = (List<Map<String, Object>>) map.get("tags");
        if(tags != null) {
            for(Map<String, Object> tag : tags) {
                if ("Classification".equals(tag.get("source"))) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("label", tag.get("name"));
                    tagData.put("tagFQN", tag.get("tagFQN"));
                    this.tags.add(tagData);
                } else if ("Glossary".equals(tag.get("source"))) {
                    Map<String, Object> termData = new HashMap<>();
                    termData.put("label", tag.get("name"));
                    termData.put("id", tag.get("tagFQN"));
                    this.relatedTerms.add(termData);
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
