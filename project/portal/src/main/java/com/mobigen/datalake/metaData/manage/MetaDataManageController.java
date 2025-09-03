package com.mobigen.datalake.metaData.manage;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.datalake.metaData.manage.DTO.UploadMinioFileForm;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/metadata/manage")
@RestController
@RequiredArgsConstructor
public class MetaDataManageController {
    private final SessionManager sessionManager;
    private final MetaDataManageService metaDataManageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataManageController.class);

    /**
     * 메타 데이터 통합 관리 > 메타 데이터 통합 관리 대상 목록 조회
     *
     * @params Object 카테고리명
     * @return Object 메타 데이터 통합 관리 대상 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getMetaDataList(@RequestBody Map<String, Object> param) throws Exception {
        return metaDataManageService.getMetaDataManageList(param);
    }

    /**
     * 메타 데이터 통합 관리 > 메타 데이터 통합 관리 대상 목록 조회
     *
     * @params Object
     * @return Object 메타 데이터 통합 관리 대상 목록 반환
     */
    @ResponseJsonResult
    @GetMapping("/getFilterOptions")
    public Object getFilterOptions() throws Exception {
        return metaDataManageService.getFilterOptions();
    }

    /**
     * 메타 데이터 통합 관리 > 데이터 공개설정 권한 처리
     *
     * @params Object
     * @return Object 성공여부
     */
    @ResponseJsonResult
    @PutMapping("/setDataAuth")
    public Object setDataAuth(@RequestBody Map<String, Object> param) throws Exception {
        return metaDataManageService.setDataAuth(param);
    }

    /**
     * 메타데이터 ID 중복확인
     * 
     * @param meta_id 메타데이터 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/metaIdDupCheck")
    public Object metaIdDupCheck(@RequestParam String meta_id) throws Exception {
        return metaDataManageService.metaIdDupCheck(meta_id);
    }

    /**
     * 메타데이터 qualified_name 중복확인
     * 이미 메타로 등록된 파일 중복확인
     * 
     * @param meta_id 메타데이터 qualified_name
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/metaQualifiedNameDupCheck")
    public Object metaQualifiedNameDupCheck(@RequestParam String qualified_name) throws Exception {
        return metaDataManageService.metaQualifiedNameDupCheck(qualified_name);
    }

    /**
     * 메타 데이터 통합 관리 > 비정형 데이터 메타 등록
     *
     * @params Object 비정형 데이터 정보
     * @return Object 등록 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/saveFileMetaData")
    public Object saveFileMetaData(@RequestBody Map<String, Object> metadata, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);       
        return metaDataManageService.saveFileMetaData(metadata, user);
    }

    /**
     * 메타 데이터 통합 관리 > 비정형 데이터 메타 파일 Minio 업로드
     *
     * @params Multipart 비정형 파일 데이터
     * @return Object minio 업로드 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/uploadFileToMinio")
    public Object uploadFileToMinio(@ModelAttribute UploadMinioFileForm fileForm, HttpServletRequest request) throws Exception {
        return metaDataManageService.uploadFile(fileForm);
    }

    /**
     * 수동 수집 실행
     * 
     * @param system_id 시스템 ID
     * @param qualified_name 메타 고유명
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/executeCollect")
    public Object executeCollect(@RequestBody Map<String, Object> executeBody) throws Exception {
        return metaDataManageService.executeCollect(executeBody);
    }

    /**
     * 수동 프로파일링 실행
     * 
     * @param system_id 시스템 ID
     * @param qualified_name 메타 고유명
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/executeProfile")
    public Object executeProfile(@RequestBody Map<String, Object> executeBody) throws Exception {
        return metaDataManageService.executeProfile(executeBody);
    }

    /**
     * 메타 추출
     * 
     * @param meta_id 메타 ID
     * @param qualified_name 메타 고유명
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/extractMetadata")
    public void extractMetadata(@RequestBody Map<String, Object> extractBody, HttpServletResponse response) throws Exception {
        metaDataManageService.extractMetadata(extractBody, response);
    }
}
