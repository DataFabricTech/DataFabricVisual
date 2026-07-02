package com.mobigen.datalake.standardmapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandardMappingService {

    private final StandardMappingMapper standardMappingMapper;

    /** 표준화율 현황: 전체 요약 + 시스템별. */
    public Map<String, Object> getQuality() {
        Map<String, Object> result = new HashMap<>();
        result.put("overall", standardMappingMapper.getQualityOverall());
        result.put("systems", standardMappingMapper.getQualityBySystem());
        return result;
    }

    /** 테이블별 표준화율 목록. */
    public List<Map<String, Object>> getTables(String systemName) {
        return standardMappingMapper.getTableList(systemName);
    }

    /** 특정 테이블의 컬럼별 판정 목록. */
    public List<Map<String, Object>> getColumns(String metaId, Boolean standardized) {
        return standardMappingMapper.getColumns(metaId, standardized);
    }
}
