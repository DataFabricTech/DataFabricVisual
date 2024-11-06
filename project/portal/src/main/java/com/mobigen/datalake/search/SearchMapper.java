package com.mobigen.datalake.search;

import java.util.Map;
import java.util.List;

import org.springframework.stereotype.Repository;
@Repository("SearchMapper")
public interface SearchMapper {
    void insertSearchTextHistory(Map<String, Object> textHistoryParam);
    int getTableTotal();
    List<Map<String, Object>> getExternalDataList(Map<String, Object> params);
    int getExternalDataListTotal(Map<String, Object> params);
}
