package com.mobigen.datalake.system.manage;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("ManageMapper")
public interface ManageMapper {
  List getConfigList(Map<String, String> param) throws Exception;
  Integer updateConfigInfo(Map<String, String> param) throws Exception;  
}
