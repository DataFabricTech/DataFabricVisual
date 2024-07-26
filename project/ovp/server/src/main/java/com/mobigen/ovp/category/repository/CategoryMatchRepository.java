package com.mobigen.ovp.category.repository;

import com.mobigen.ovp.category.entity.CategoryMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryMatchRepository extends JpaRepository<CategoryMatchEntity, String> {
    List<CategoryMatchEntity> findById(UUID id);
}
