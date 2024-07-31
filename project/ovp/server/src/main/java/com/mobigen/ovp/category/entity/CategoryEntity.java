package com.mobigen.ovp.category.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_category")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "order", nullable = false)
    private int order;

    @Column(name = "desc", nullable = false)
    private String desc;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryMatchEntity> categoryMatches = new ArrayList<>(); // 항상 초기화

    public CategoryEntity(UUID id, UUID parentId, String name, int order, String desc) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.order = order;
        this.desc = desc;
    }
}
