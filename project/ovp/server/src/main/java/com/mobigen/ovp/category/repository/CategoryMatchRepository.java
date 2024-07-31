package com.mobigen.ovp.category.repository;

import com.mobigen.ovp.category.entity.CategoryMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryMatchRepository extends JpaRepository<CategoryMatchEntity, UUID> {

    @Query("SELECT c FROM CategoryMatchEntity c WHERE c.category = :categoryId")
    Page<CategoryMatchEntity> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    List<CategoryMatchEntity> findByCategoryIdIn(List<UUID> ids);
}
