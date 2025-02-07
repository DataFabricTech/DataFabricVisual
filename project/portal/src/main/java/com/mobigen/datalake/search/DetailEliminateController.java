package com.mobigen.datalake.search;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.IpAddressUtil;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/search/detail/eliminate")
@RestController
@RequiredArgsConstructor
public class DetailEliminateController {
    private final SessionManager sessionManager;
    private final DetailEliminateService detailEliminateService;

    @Value("${properties.csvFileUploadPath}")
    private String fileUploadPath;

    /**
     * 데이터추출 : 파일 이력 저장
     * @param param                 파일 이력 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/history/add")
    public Object insertDownloadHistory(@RequestBody Map<String, String> param, HttpServletRequest request) throws Exception {
        // HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // String ip = req.getHeader("X-FORWARDED-FOR");
        // if (ip == null)
        //     ip = req.getRemoteAddr();
        // param.put("user_ip_addr", ip); //사용자 IP 주소
        param.put("user_ip_addr", IpAddressUtil.getClientIp(request));

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        param.put("user_id", user.get("user_id").toString());
        param.put("user_name", user.get("username").toString());
        param.put("dept_id", user.get("dept_id") != null ? user.get("dept_id").toString() : "-");
        param.put("dept_name", user.get("dept_nm").toString());

        return detailEliminateService.insertDownloadHistory(param);
    }

    /**
     * 데이터 추출 : 파일
     * @param typeName              메타 타입 (hive_table, hive_column)         
     * @param qualified_name         메타 고유명
     * @param querySelect           데이터 추출 Trino 쿼리
     * @param queryWhere            데이터 추출 Trino
     * @param fileName              파일명
     * @return
     * @throws SQLException
     */
    @PostMapping(value = "/file/download")
    public void downloadCsv(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());

        if (params.get("querySelect") == null) {
            params.put("querySelect", "*");
        }

        detailEliminateService.getCSVFile(params, request, response);
    }

    /**
     * 대용량 데이터 추출 요청
     * @return
     * @throws SQLException
     */
    @ResponseJsonResult
    @PostMapping(value = "/file/downloadRequest")
    public Object downloadRequest(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        params.put("user_ip", IpAddressUtil.getClientIp(request));
        return detailEliminateService.downloadRequest(params);
    }
}
