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
    List<Map<String, Object>>  getMetaDataClassifications(Map<String, Object> params) throws Exception;
    List<String> getMetaDataInfoFromClassifications(Map<String, Object> params) throws Exception;
}
