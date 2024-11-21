package com.mobigen.datalake.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
@Repository("SearchMapper")
public interface SearchMapper {
    void insertSearchTextHistory(Map<String, Object> textHistoryParam);
    int getTableTotal();
    List<String> getSystemtypes();
    List<String> getMetatypes();
}
