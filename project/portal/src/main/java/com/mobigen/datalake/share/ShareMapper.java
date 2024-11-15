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

    List<Map<String, Object>> getShareFileInfoList (int post_id) throws Exception;

}
