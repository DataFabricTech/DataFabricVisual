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
    int getTablesCount(@Param("categoryId") String categoryId);

    // CRUD
    List<Map<String, Object>> getList(Map<String, Object> params) throws Exception;
    int getListCount(Map<String, Object> params) throws Exception;
    Map<String, Object> getInfo(String id) throws Exception;
    int insert(Map<String, Object> params) throws Exception;
    int update(Map<String, Object> params) throws Exception;
    int delete(String id) throws Exception;
    int countByName(String name) throws Exception;
    List<String> getMappedMetaIds(String categoryId) throws Exception;
}
