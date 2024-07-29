package com.mobigen.ovp.category.dto;

import com.mobigen.ovp.category.entity.CategoryEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {
    private String id;
    private String parentId;
    private String name;
    private int order;
    private String desc;
    private Boolean expanded;
    private Boolean selected;
    private Boolean disabled;
    private List<CategoryDTO> children;

    public CategoryDTO(CategoryEntity entity) {
        this.id = entity.getId().toString();
        this.parentId = entity.getParentId() != null ? entity.getParentId().toString() : null;
        this.name = entity.getName();
        this.order = entity.getOrder();
        this.desc = entity.getDesc();
        this.expanded = false;
        this.selected = false;
        this.disabled = false;
        this.children = new ArrayList<>();
    }
}
