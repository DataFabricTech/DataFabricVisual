package com.mobigen.datalake.standardmapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 표준화매핑 - FE용 조회 API.
 * - quality : 표준화율 현황(전체 + 시스템별)
 * - list    : 특정 테이블의 컬럼별 표준 준수 판정 목록
 * (판정/적재는 standard_mapping 배치가 수행, 본 API는 조회만)
 */
@Slf4j
@RequestMapping("/api/standard-mapping")
@Controller
@AllArgsConstructor
public class StandardMappingController {

    private final StandardMappingService standardMappingService;

    /** 표준화율 현황 (전체 요약 + 시스템별). */
    @ResponseJsonResult
    @GetMapping("/quality")
    public Object getQuality() {
        return standardMappingService.getQuality();
    }

    /**
     * 특정 테이블의 컬럼별 표준 준수 판정 목록.
     * @param metaId       테이블 meta_id (필수)
     * @param standardized 필터: null=전체, true=준수만, false=미준수만
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getColumns(@RequestParam(name = "metaId") String metaId,
                             @RequestParam(name = "standardized", required = false) Boolean standardized) {
        return standardMappingService.getColumns(metaId, standardized);
    }
}
