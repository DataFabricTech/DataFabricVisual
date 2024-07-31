package com.mobigen.ovp.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_category_match")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMatchEntity {

    @Id
    @Column(name = "model_id", nullable = false, unique = true)
    private UUID modelId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @NotNull
    @Column(name = "model_index", nullable = false, length = 10)
    private String modelIndex;
}
