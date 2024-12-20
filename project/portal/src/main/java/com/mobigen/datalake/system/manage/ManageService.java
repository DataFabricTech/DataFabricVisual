package com.mobigen.datalake.system.manage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.AtlasErrorMap;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ManageService {
  private final ManageMapper manageMapper;    
  
  /**
   * config 수정
   */
  // public Object updateConfigInfo(Map<String, String> param, HttpServletRequest request) throws Exception {    
  //   Object result = manageMapper.updateConfigInfo(param);
  //   return result;
  // }

  /**
   * config 목록 조회
   */
  // public List<Map> getConfigList(Map<String, String> param) throws Exception {
  //   List<Map> list = (List<Map>) manageMapper.getConfigList(param);
  //   return list;
  // }
 
}