package com.mobigen.datalake.search;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("categoryMapper")
public interface CategoryMapper {
    List getTableCountsByClassification();

    List getDbClassification();

    List getCategoryListForChart();

    List getCategoryListForFilter();

    List getSystemListForFilter();

    List getTagListForFilter();
}
