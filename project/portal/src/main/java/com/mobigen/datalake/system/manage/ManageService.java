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
  
 
}