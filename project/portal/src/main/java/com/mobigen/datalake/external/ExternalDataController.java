package com.mobigen.datalake.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ExternalDataController {
    
private final Environment env;
    private final ExternalDataService externalDataService;
    private final SessionManager sessionManager;

    @ResponseJsonResult
    @GetMapping("/dataList")
    public Object getExternalDataList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchWord", searchWord);
        params.put("limit", String.valueOf(pageSize));
        params.put("offset", String.valueOf(pageSize * (pnum - 1)));

        return externalDataService.getExternalDataList(params);
    }

    @ResponseJsonResult
    @GetMapping("/data")
    public Object getExternalDataList(@RequestParam("id") String id) throws Exception {
        return externalDataService.getExternalData(id);
    }

    @ResponseJsonResult
    @DeleteMapping("/deleteData")
    public Object deleteData(@RequestParam("id") String id) throws Exception {
        return externalDataService.deleteData(id);
    }

    @ResponseJsonResult
    @GetMapping("/externalInfo")
    public Object getExternalInfo() throws Exception {
        return externalDataService.getExternalInfo();
    }

    @ResponseJsonResult
    @PostMapping("/register")
    public Object registerExternalData(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        params.put("writer", userId);

        return externalDataService.registerExternalData(params);
    }

    @ResponseJsonResult
    @PostMapping("/update")
    public Object updateExternalData(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        params.put("writer", userId);

        return externalDataService.updateExternalData(params);
    }
}
