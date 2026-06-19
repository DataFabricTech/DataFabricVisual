package com.mobigen.datalake.attrcategory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유사 속성 카테고리 차트 - FE용 조회 API.
 * - 차트: 카테고리 20개 + 테이블 수
 * - 상세화면: 특정 카테고리에 속한 테이블 목록
 * (매핑 산정/적재는 recommender 배치가 수행, 본 API는 조회만)
 */
@Slf4j
@RequestMapping("/api/attr-category")
@Controller
@AllArgsConstructor
public class AttrCategoryController {

    private final AttrCategoryService attrCategoryService;

    /** 카테고리 목록 + 카테고리별 테이블 수 (차트 초기 20노드용) */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getCategoryList() {
        return attrCategoryService.getCategoryList();
    }

    /**
     * 특정 카테고리에 속한 테이블 목록 (카테고리 클릭 시 상세화면용).
     * @param sort 정렬 기준: similarity(대표성 유사도 내림차순, 기본) | name(테이블명 오름차순)
     */
    @ResponseJsonResult
    @GetMapping("/{categoryId}/tables")
    public Object getTables(@PathVariable String categoryId,
                            @RequestParam(name = "sort", defaultValue = "similarity") String sort) {
        return attrCategoryService.getTables(categoryId, sort);
    }
}
