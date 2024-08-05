package com.mobigen.ovp.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "tag_id")
    private String tagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<CategoryEntity> children = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "order", nullable = false)
    private int order;

    @Column(name = "desc", nullable = false)
    private String desc;

    public boolean isRoot() {
        return this.id.equals(this.parentId);
    }

    public CategoryEntity(UUID id, UUID parentId, String tagId, String name, int order, String desc) {
        this.id = id;
        this.parentId = parentId;
        this.tagId = tagId;
        this.name = name;
        this.order = order;
        this.desc = desc;
    }
}
