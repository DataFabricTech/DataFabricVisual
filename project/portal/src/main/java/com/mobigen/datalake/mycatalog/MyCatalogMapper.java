package com.mobigen.datalake.mycatalog;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("myCatalogMapper")
public interface MyCatalogMapper {
    List<HashMap<String, Object>> getSearchDataList(Map<String, String> param);

    List<HashMap<String, Object>> getDataRequestList(Map<String, String> param);
    Integer getDataRequestListCount(Map<String, String> param);


    List<Map<String, Object>> getSynonymDeleteRequestList(Map<String, String> param);

    List<Map<String,String>> getExtractedDataList(Map<String, String> param);

    Integer getExtractedDataListCount(Map<String, String> param);

    List<Map<String,String>> getWishedDataList(Map<String, String> param);

    Integer getWishedDataListCount(Map<String, String> param);

    List<Map<String, String>> getSuggestedDataList(Map<String, String> param);

    Integer getSuggestedDataListCount(Map<String, String> param);

    List<Map<String, Object>> getMySynonymDeleteRequestList(Map<String, String> param);
}
