package com.mobigen.datalake.metaData.similarAttrCategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface SimilarAttrCategoryMapper {
    List<Map<String, Object>> getList(Map<String, Object> params) throws Exception;

    int getListCount(Map<String, Object> params) throws Exception;

    Map<String, Object> getInfo(String id) throws Exception;

    int insert(Map<String, Object> params) throws Exception;

    int update(Map<String, Object> params) throws Exception;

    int delete(String id) throws Exception;

    int countByName(String name) throws Exception;

    List<String> getMappedMetaIds(String categoryId) throws Exception;
}
