package com.mobigen.datalake.standardmapping;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("standardMappingMapper")
public interface StandardMappingMapper {

    /** 전체 표준화율 요약 (단일 행). */
    Map<String, Object> getQualityOverall();

    /** 시스템별 표준화율. */
    List<Map<String, Object>> getQualityBySystem();

    /** 특정 테이블의 컬럼별 판정 목록. standardized: null=전체 / true=준수 / false=미준수 */
    List<Map<String, Object>> getColumns(@Param("metaId") String metaId,
                                         @Param("standardized") Boolean standardized);
}
