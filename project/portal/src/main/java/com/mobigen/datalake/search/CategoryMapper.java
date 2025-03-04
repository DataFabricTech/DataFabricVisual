package com.mobigen.datalake.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("categoryMapper")
public interface CategoryMapper {

    List<Map<String, Object>> getCategoryList();

    List getTableCountsByClassification();

    List<Map<String, Object>> getDbClassification();

    List getCategoryListForChart();

    List getCategoryListForFilter();
    List getSystemListForFilter();
    List getSystemTypes();
}
