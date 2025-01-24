package com.mobigen.datalake.metaData.manage;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("metaDataManageMapper")
public interface MetaDataManageMapper {
    // 매핑된 메타 데이터 목록
    List<Map<String, Object>> getMappedMetaDataManageListByClassificationId(Map<String, Object> params) throws Exception;

    // 메타 데이터 목록
    List<Map<String, Object>> getMetaDataManageList(Map<String, Object> params) throws Exception;

    // 메타 데이터 목록 count
    int getMetaDataManageListTotCnt(Map<String, Object> params) throws Exception;

    // 하위 메타 데이터 목록
    List<Map<String, Object>> getMetaDataListByParentId(@Param("parent_id") String parent_id) throws Exception;

    // 메타 데이터 classification 업데이트
    int updateMetaDataClassification(Map<String, Object> params) throws Exception;

    // 메타 데이터 classification 매핑 삭제
    int deleteMetaDataClassification(Map<String, Object> params) throws Exception;

    // 검색 필터옵션 조회
    List<String> getFilterOptions() throws Exception;

    // 메타 데이터 권한 수정 
    int updateMetaDataRelease(Map<String, Object> params) throws Exception;

}
