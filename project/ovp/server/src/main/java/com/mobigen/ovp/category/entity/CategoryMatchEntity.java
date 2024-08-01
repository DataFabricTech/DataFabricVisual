package com.mobigen.ovp.category.entity;

import com.mobigen.ovp.common.entity.ModelIndex;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    @Column(name = "category_id", nullable = false)
    private UUID category_id;

    @NotNull
    @Column(name = "model_index", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ModelIndex modelIndex;
}
