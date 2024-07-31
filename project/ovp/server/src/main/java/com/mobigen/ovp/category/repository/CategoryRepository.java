package com.mobigen.ovp.category.repository;

import com.mobigen.ovp.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findAll();

    @Query("SELECT COALESCE(MAX(c.order), 0) FROM CategoryEntity c WHERE c.parentId = :parentId")
    int findMaxOrderByParentId(@Param("parentId") UUID parentId);

    List<CategoryEntity> findByParentIdOrderByOrderDesc(UUID parentId);

    default CategoryEntity saveOrUpdate(CategoryEntity categoryEntity) {
        return save(categoryEntity);
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM CategoryEntity c WHERE c.id IN :ids")
    void deleteAllByIds(@Param("ids") List<UUID> ids);
}
