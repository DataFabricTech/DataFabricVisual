package com.mobigen.datalake.metaData.fusion;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("metaDataFusionMapper")
public interface MetaDataFusionMapper {
    // 메타 데이터 목록
    List<Map<String, Object>> getMetaDataFusionList(Map<String, Object> params) throws Exception;

    // 메타 데이터 목록 count
    int getMetaDataFusionListTotCnt(Map<String, Object> params) throws Exception;

    // 메타데이터 이벤트 저장
    Integer saveEvent(Map<String, Object> eventParams) throws Exception;

    // 메타데이터 저장
    Integer saveMetadata(Map<String, Object> metadata) throws Exception;

    // 융합 데이터 저장소 시스템 정보 코드 조회
    List<Map<String, Object>> getFusionCodeInfo() throws Exception;

    // 융합 데이터 저장소 시스템 정보 조회
    Map<String, Object> getFusionSystemInfo(String system_name, String system_type) throws Exception;

    // ── Trino UDF 관리 ──
    List<Map<String, Object>> getUdfList() throws Exception;
    List<Map<String, Object>> getUdfListByNames(List<String> names) throws Exception;
    int insertUdf(Map<String, Object> param) throws Exception;
    int insertUdfParam(Map<String, Object> param) throws Exception;
    int updateUdf(Map<String, Object> param) throws Exception;
    int deleteUdfParams(String udfId) throws Exception;
    int deleteUdf(String udfId) throws Exception;

    // 융합 뷰-UDF 의존성 관리
    int insertFusionViewUdf(Map<String, Object> param) throws Exception;
    List<Map<String, Object>> getUdfListByMetaId(String metaId) throws Exception;
    int deleteFusionViewUdfs(String metaId) throws Exception;
    int getUdfViewCount(String udfId) throws Exception;
}
