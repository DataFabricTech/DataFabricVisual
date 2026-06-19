package com.mobigen.datalake.attrcategory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("attrCategoryMapper")
public interface AttrCategoryMapper {

    /** 카테고리 목록 + 카테고리별 테이블 수 */
    List<Map<String, Object>> getCategoryList();

    /** 특정 카테고리의 테이블 목록 (sort: similarity | name) */
    List<Map<String, Object>> getTables(@Param("categoryId") String categoryId, @Param("sort") String sort);
}
