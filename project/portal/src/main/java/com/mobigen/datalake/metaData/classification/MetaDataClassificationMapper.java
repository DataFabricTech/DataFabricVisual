package com.mobigen.datalake.metaData.classification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("metaDataClassificationMapper")
public interface MetaDataClassificationMapper {
    Object getClassificationDept(String name) throws Exception;
    
    String getClassificationDepth(String name) throws Exception;

    Integer insertClassification(Map<String, Object> params) throws Exception;

    Integer createClassificationCount(Map<String, Object> params) throws Exception;

    Integer updateClassification(Map<String, Object> params) throws Exception;

    Integer mapperCount(Map<String, Object> entity) throws Exception;

    Integer mapperCountMulti(Map<String, Object> params) throws Exception;

    Integer supperMapperCount(Map<String, Object> entity) throws Exception;

    Integer deleteClassification(String name) throws Exception;

    Integer deleteClassificationCount(String name) throws Exception;

    Integer mapperCountDown (String id) throws Exception;

    List<Map<String, Object>> getClassificationList(String level) throws Exception;

    Integer updateClassificationOrdNo(Map<String, Object> param) throws Exception;

    // 하위 카테고리 목록 검색
    List<Map<String, Object>> getClassificationChildrenById(String id) throws Exception;

    List<Map<String, Object>> getClassificationListByName(String name) throws Exception;

}
