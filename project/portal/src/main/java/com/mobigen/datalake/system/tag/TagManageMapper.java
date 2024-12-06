package com.mobigen.datalake.system.tag;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("tagManageMapper")
public interface TagManageMapper {
  Integer insertTagInfo(Map<String, String> param) throws Exception;  
  Integer getTagListTotal(Map<String, String> param) throws Exception;
  List getTagList(Map<String, String> param) throws Exception;  
  Integer deleteTagInfo(String tag_name) throws Exception;  
  Integer checkTagCount(String tag_name) throws Exception;
  Integer insertTagCount(String tag_name) throws Exception;  
  Integer mapperCount(String tag_name) throws Exception;
  Integer mapperCountDown (String tag_name) throws Exception;
  int updateMetaDataTag(Map<String, Object> params) throws Exception;
  int upsertMetaDataTag(Map<String, Object> params) throws Exception;
}
