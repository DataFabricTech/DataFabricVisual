package com.mobigen.datalake.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("SearchMapper")
public interface SearchMapper {
    void insertSearchTextHistory(Map<String, Object> textHistoryParam);

    int getTableTotal();

    List<String> getSystemTypes();

    List<String> getMetaTypes();

    List<Map<String, Object>> getMetaDataClassifications(Map<String, Object> params) throws Exception;

    List<String> getMetaDataInfoFromClassifications(Map<String, Object> params) throws Exception;

    List<String> getRoleIdsFromUser(String user_id, String dept_id) throws Exception;

    List<Map<String, Object>> getDefaultMetaType() throws Exception;
}
