package com.mobigen.datalake.metaData.manage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("metaDataManageMapper")
public interface MetaDataManageMapper {
    // 매핑된 메타 데이터 목록
    List<Map<String, Object>> getMappedMetaDataManageListByClassificationId(Map<String, Object> params)
            throws Exception;

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

    // 메타 데이터 검색 필터옵션 조회
    List<Map<String, Object>> getFilterMetaType() throws Exception;
    List<Map<String, Object>> getFilterSystemType() throws Exception;
    List<Map<String, Object>> getFilterFileFormat() throws Exception;

    // 메타 데이터 권한 수정
    int updateMetaDataRelease(Map<String, Object> params) throws Exception;

    // 메타 데이터 유형 목록 조회
    List<Map<String, Object>> getMetaTypeList() throws Exception;

    // 메타데이터 ID 중복확인
    int metaIdDupCheck(String system_id) throws Exception;

    // 메타데이터 qualified_name 중복확인
    Integer metaQualifiedNameDupCheck(String qualified_name) throws Exception;

    // 메타데이터 이벤트 저장
    Integer saveEvent(Map<String, Object> eventParams) throws Exception;

    // 메타데이터 저장
    Integer saveMetadata(Map<String, Object> metadata) throws Exception;

    // 메타데이터 업데이트
    Integer updateMetadata(Map<String, Object> metadata) throws Exception;

    // MinIO FabricStorage 코드 조회
    List<Map<String, Object>> getCodeForFabricStorage() throws Exception;

    // MinIO FabricStorage 정보 조회
    Map<String, Object> getFabricStorageInfoFromSystemName(String system_name) throws Exception;

    String getMetaIdFromQualifiedName(String qualified_name) throws Exception;

    List<LinkedHashMap<String, Object>> getExtractMetaList(Map<String, Object> params) throws Exception;

    Integer getMetadataTotal(Map<String, Object> params) throws Exception;

    List<String> getMetaDataManageIds(Map<String, Object> params) throws Exception;
}
