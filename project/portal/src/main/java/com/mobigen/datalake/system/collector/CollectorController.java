package com.mobigen.datalake.system.collector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.service.file.FileService;
import com.mobigen.framework.service.file.model.FileUploadRequest;
import com.mobigen.framework.service.file.model.FileUploadResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/admin/system/collector")
@Controller
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;
    private final FileService fileService;
    private final SessionManager sessionManager;

    @Value("${properties.systemConfigPath}")
    public String systemConfigPath;

    /**
     * 수집대상 목록 조회
     * 
     * @param searchType 검색 종류
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/collectorList")
    public Object getCollectorListInfo(@RequestParam int pageSize,
            @RequestParam int pnum,
            @RequestParam String searchType,
            @RequestParam String searchWord) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchWord", searchWord);
        params.put("limit", String.valueOf(pageSize));
        params.put("offset", String.valueOf(pageSize * (pnum - 1)));

        return collectorService.getCollectorListInfo(params);
    }

    /**
     * 수집대상 시스템 ID 중복확인
     * 
     * @param system_id 시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/systemIdDupCheck")
    public Object systemIdDupCheck(@RequestParam String system_id) throws Exception {
        return collectorService.systemIdDupCheck(system_id);
    }

    /**
     * 수집대상 등록
     * 
     * @param params 수집대상 시스템
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/add")
    public Object addCollector(@RequestBody Map<String, Object> params) throws Exception {
        return collectorService.addCollector(params);
    }

    /**
     * 수집대상 수정
     * 
     * @param params 수집대상 시스템
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/update")
    public Object updateCollector(@RequestBody Map<String, Object> params) throws Exception {
        return collectorService.updateCollector(params);
    }

    /**
     * 수집대상 삭제
     * 
     * @param system_id 시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/delete")
    public Object deleteCollector(@RequestParam String system_id) throws Exception {
        return collectorService.deleteCollector(system_id);
    }

    /**
     * 수동 수집 실행
     * 
     * @param system_id 시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/directCollect")
    public Object directCollect(@RequestParam String system_id) throws Exception {
        return collectorService.directCollect(system_id);
    }

    /**
     * 시스템 타입 조회
     * 
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/getSystemTypes")
    public Object getSystemTypes() throws Exception {
        return collectorService.getSystemTypes();
    }

    /**
     * 수집대상 조회
     * 
     * @param system_id 검색 종류
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/collectorOne")
    public Object getCollectorOneInfo(@RequestParam String system_id) throws Exception {
        return collectorService.getCollectorOneInfo(system_id);
    }

    /**
     * 비정형 전용 수집시스템 조회 (minIO)
     * 
     * @param system_name 검색 종류
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/minioSystemInfo")
    public Object getMinioSystemInfo() throws Exception {
        return collectorService.getMinioSystemInfo();
    }

    /**
     * Hive 수집대상 파일저장
     * 
     * @param uploadForm 파일
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult insertFile(@ModelAttribute FileUploadRequest uploadForm, HttpServletRequest request) {
        JsonResult js = new JsonResult();

        try {
            // 세션의 사용자 ID 확인
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);

            if (user != null) {
                // 파일 업로드 경로 숨기기
                List<FileUploadResponse> uploadResult = fileService.fileHiveConfigUpload(uploadForm, systemConfigPath,
                        request);
                if (uploadResult.size() != 0)
                    uploadResult.get(0).setSaveFileName(null);

                js.setData(uploadResult);
            } else {
                js.setData(0);
                js.setErrorMessage("Hive 설정파일 저장에 실패하였습니다.");
            }

        } catch (Exception e) {
            log.error("Fail to update file {}", e);
            js.setErrorMessage("파일 업데이트에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }

        return js;
    }

    /**
     * Airflow 조회
     * 
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/airflowUrl")
    public Object getAirflowUrl() throws Exception {
        return collectorService.getAirflowUrl();
    }
}
