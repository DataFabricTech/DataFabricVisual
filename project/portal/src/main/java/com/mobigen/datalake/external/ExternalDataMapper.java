package com.mobigen.datalake.external;

import java.util.List;
import java.util.Map;

public interface ExternalDataMapper {
    List getExternalDataList(Map<String, String> params) throws Exception;
    int getExternalDataListTotal(Map<String, String> params) throws Exception;
    List getClassificationList() throws Exception;
    Map<String, Object> getExternalData(String id) throws Exception;
    int deleteData(String id) throws Exception;
    List<Map<String, Object>> getCategoryList() throws Exception;
    List<Map<String, Object>> getSystemList() throws Exception;
    String getSystem(String id) throws Exception;
    List<Map<String, Object>> getTagList() throws Exception;
    int registerExternalData(Map<String, Object> params) throws Exception;
    int updateExternalData(Map<String, Object> params) throws Exception;

}