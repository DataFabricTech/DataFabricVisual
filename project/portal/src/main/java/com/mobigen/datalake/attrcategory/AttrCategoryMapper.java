package com.mobigen.datalake.attrcategory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("attrCategoryMapper")
public interface AttrCategoryMapper {

    /** 카테고리 목록 + 카테고리별 테이블 수 */
    List<Map<String, Object>> getCategoryList();

    List<Map<String, Object>> getTables(@Param("categoryId") String categoryId, @Param("sort") String sort, @Param("limit") int limit);
    int getTablesCount(@Param("categoryId") String categoryId);

    // CRUD
    List<Map<String, Object>> getList(Map<String, Object> params) throws Exception;
    int getListCount(Map<String, Object> params) throws Exception;
    int insert(Map<String, Object> params) throws Exception;
    int update(Map<String, Object> params) throws Exception;
    int delete(String id) throws Exception;
    int countByName(String name) throws Exception;
    List<String> getMappedMetaIds(String categoryId) throws Exception;
}
