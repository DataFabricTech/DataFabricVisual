package com.mobigen.datalake.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    // 좌측 카테고리 리스트 조회
    public List<Map<String, Object>> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    // 카테고리별 테이블 수 전체 조회
    public Object getTableCountsByClassification() {
        return categoryMapper.getTableCountsByClassification();
    }

    // DB의 카테고리 목록 데이터 반환
    public Object getDbClassification() {
        return categoryMapper.getDbClassification();
    }

    // 카테고리 1레벨 목록 조회
    public List getCategoryListForChart() {
        return categoryMapper.getCategoryListForChart();
    }
}
