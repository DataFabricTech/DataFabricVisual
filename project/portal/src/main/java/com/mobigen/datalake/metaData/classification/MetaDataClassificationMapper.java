package com.mobigen.datalake.metaData.classification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("metaDataClassificationMapper")
public interface MetaDataClassificationMapper {
    Object getClassificationDept(String name) throws Exception;
    
    String getClassificationDepth(String name) throws Exception;

    Integer insertClassification(Map<String, Object> params) throws Exception;

    Integer updateClassification(Map<String, Object> params) throws Exception;

    Integer deleteClassification(String name) throws Exception;

    List<Map<String, Object>> getClassificationList(String level) throws Exception;

    Integer updateClassificationOrdNo(Map<String, Object> param) throws Exception;

    // 하위 카테고리 목록 검색
    List<Map<String, Object>> getClassificationChildrenById(String id) throws Exception;

    List<Map<String, Object>> getClassificationListByName(String name) throws Exception;

    // 모든 하위 카테고리 목록 조회
    List<String> getClassificationChildByParentId(String parent_id) throws Exception;

    int deleteMappingClassification(Map<String, Object> map) throws Exception;

    List<String> getClassifications(String guid) throws Exception;
}
