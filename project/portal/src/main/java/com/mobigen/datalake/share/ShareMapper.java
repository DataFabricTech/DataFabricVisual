package com.mobigen.datalake.share;

import java.util.List;
import java.util.Map;

public interface ShareMapper {
    List getShareList(Map<String, String> param) throws Exception;
    Integer getShareListTotal(Map<String, String> param) throws Exception;

    Map<String, Object> getShareOne(String id) throws Exception;

    Integer insertShare(Map<String, Object> param) throws Exception;

    Integer deleteShare(Integer id) throws Exception;
    Integer updateShare(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getShareFileInfoList (Integer id) throws Exception;
    List<Map<String, Object>> getShareThumbFileInfoList (Integer id) throws Exception;

    Map<String, Object> getFileInfo(Integer file_id) throws Exception;

}
