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
     * config 수정
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/update")
    @ResponseBody
    public Object updateConfigInfo(@RequestBody Map<String, String> param, HttpServletRequest request) throws Exception {
        return manageService.updateConfigInfo(param, request);
    }

    /**
     * config 목록 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getConfigList() throws Exception {
        List<Map> list = manageService.getConfigList(null);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);        

        return resultMap;
    }

    /**
     * 샘플데이터 카운트수 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/sampleDataCount")
    public Object getSampleDataCount() throws Exception {
        Map param = new HashMap<>();
        param.put("config_name", "sampleDataCount");
        List<Map> list = manageService.getConfigList(param);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return new HashMap<>();
        }
    }
}
