package com.mobigen.datalake.search;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/search/category")
@Controller
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 좌측 카테고리 리스트 조회
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getCategoryList() {
        return categoryService.getCategoryList();
    }

    // 카테고리 1레벨 목록 조회 (차트 색상 설정)
    @ResponseJsonResult
    @GetMapping("/categoryList")
    public Object getCategoryListForChart() {
        return categoryService.getCategoryListForChart();
    }

    // DB의 카테고리 목록 데이터 반환
    @ResponseJsonResult
    @GetMapping("/dbList")
    public Object getCategorydbList() {
        return categoryService.getDbClassification();
    }

    // 카탈로그 검색 > 카테고리 검색 > 우측 트리맵 렌더용 데이터 조회
    @ResponseJsonResult
    @GetMapping("/counts")
    public Object getTableCountsByClassification() {
        return categoryService.getTableCountsByClassification();
    }

}
