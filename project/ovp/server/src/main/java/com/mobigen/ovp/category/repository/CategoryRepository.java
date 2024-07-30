package com.mobigen.ovp.category.repository;

import com.mobigen.ovp.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findAll();

    List<CategoryEntity> findByParentIdOrderByOrderDesc(UUID parentId);

    // 등록 및 수정을 위한 디폴트 메서드
    default CategoryEntity saveOrUpdate(CategoryEntity categoryEntity) {
        return save(categoryEntity);
    }
}
