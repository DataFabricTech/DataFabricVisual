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
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    /**
     * 모든 카테고리 항목 조회
     * @return
     */
    List<CategoryEntity> findAll();

    /**
     * 카테고리 단일 노드 조회 ( parent 정보 포함 )
     * @param id
     * @return
     */
    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.parent WHERE c.id = :id")
    CategoryEntity findByIdWithParent(@Param("id") UUID id);

    /**
     * 형제 카테고리 노드들 중 제일 큰 order 값 조회
     * @param parentId
     * @return
     */
    @Query("SELECT COALESCE(MAX(c.order), 0) FROM CategoryEntity c WHERE c.parentId = :parentId")
    int findMaxOrderByParentId(@Param("parentId") UUID parentId);

    /**
     * 형제 카테고리 노드 를 order 순으로 조회
     * @param parentId
     * @return
     */
    List<CategoryEntity> findByParentIdOrderByOrderDesc(UUID parentId);
    List<CategoryEntity> findByParentIdOrderByOrderAsc(UUID parentId);

    /**
     * 카테고리 노드 추가/수정
     * @param categoryEntity
     * @return
     */
    default CategoryEntity saveOrUpdate(CategoryEntity categoryEntity) {
        return save(categoryEntity);
    }

    /**
     * 카테고리 노드들 삭제
     * @param ids
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM CategoryEntity c WHERE c.id IN :ids")
    void deleteAllByIds(@Param("ids") List<UUID> ids);
}
