package com.mobigen.datalake.system.manage;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin/system/manage")
@Controller
@AllArgsConstructor

public class ManageController {
    private final ManageService manageService;
  
    /**
     * 샘플데이터 카운트수 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/sampleDataCount")
    public Object getSampleDataCount() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("id", 1);
        configMap.put("config_name", "sampleDataCount");
        configMap.put("config_value", 10);
        configMap.put("config_desc", "샘플데이터 조회 개수");
        configMap.put("use_yn", true);
        return configMap;
    }
}
