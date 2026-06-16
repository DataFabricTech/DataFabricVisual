package com.mobigen.datalake.attrcategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttrCategoryService {

    private final AttrCategoryMapper attrCategoryMapper;

    /** 카테고리 목록 + 카테고리별 테이블 수 */
    public List<Map<String, Object>> getCategoryList() {
        return attrCategoryMapper.getCategoryList();
    }

    /** 특정 카테고리의 테이블 목록 */
    public List<Map<String, Object>> getTables(String categoryId) {
        return attrCategoryMapper.getTables(categoryId);
    }
}
