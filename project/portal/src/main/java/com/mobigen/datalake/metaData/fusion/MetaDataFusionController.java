package com.mobigen.datalake.metaData.fusion;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/metadata/fusion")
@RestController
@RequiredArgsConstructor
public class MetaDataFusionController {
    private final SessionManager sessionManager;
    private final MetaDataFusionService metaDataFusionService;

    /**
     * 데이터 융합 관리 > 융합 데이터 대상 목록 조회
     *
     * @params Object 카테고리명
     * @return Object 융합 데이터 대상 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getMetaDataFusionList(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        param.put("userId", user.get("user_id"));
        return metaDataFusionService.getMetaDataFusionList(param);
    }

    /**
     * 데이터 융합 관리 > 융합 추천 테이블 조회
     *
     * @params Object 기준 테이블 meta_id, topN
     * @return Object 추천 테이블 목록(조인 키 포함) 반환
     */
    @ResponseJsonResult
    @PostMapping("/recommend")
    public Object getFusionRecommend(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        param.put("userId", user.get("user_id"));
        return metaDataFusionService.getFusionRecommend(param);
    }

    /**
     * 데이터 융합 관리 > 생성된 융합 뷰 목록 조회
     *
     * @params Object 검색 조건, 페이징 정보
     * @return Object 융합 뷰 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/manageList")
    public Object getFusionViewList(@RequestBody Map<String, Object> param) throws Exception {
        return metaDataFusionService.getFusionViewList(param);
    }

    /**
     * 데이터 융합 관리 > 쿼리 실행
     *
     * @params String 쿼리문
     * @return Object 실행 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/executeQuery")
    public Object executeQuery(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);     
        param.put("user_id", user.get("user_id"));
        return metaDataFusionService.executeQuery(param);
    }

    /**
     * 데이터 융합 관리 > 융합 데이터 저장
     *
     * @params Object 융합 데이터 정보
     * @return Object 등록 결과 반환
     */
    @ResponseJsonResult
    @PostMapping("/saveFusionMetaData")
    public Object saveFusionMetaData(@RequestBody Map<String, Object> metadata, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);       
        return metaDataFusionService.saveFusionMetaData(metadata, user);
    }

    /**
     * 융합 데이터 전용 수집시스템 조회 (Hive)
     */
    @ResponseJsonResult
    @GetMapping("/fusionSystemInfo")
    public Object getFusionSystemInfo() throws Exception {
        return metaDataFusionService.getFusionSystemInfo();
    }

    // ── Trino UDF 관리 ──

    @ResponseJsonResult
    @GetMapping("/udf/list")
    public Object getUdfList() throws Exception {
        return metaDataFusionService.getUdfList();
    }

    @ResponseJsonResult
    @PostMapping("/udf/register")
    public Object registerUdf(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return metaDataFusionService.registerUdf(param, user);
    }

    @ResponseJsonResult
    @PutMapping("/udf/update")
    public Object updateUdf(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return metaDataFusionService.updateUdf(param, user);
    }

    @ResponseJsonResult
    @DeleteMapping("/udf/delete")
    public Object deleteUdf(@RequestParam String udf_id) throws Exception {
        return metaDataFusionService.deleteUdf(udf_id);
    }

    /**
     * 융합 데이터 삭제 (융합 전용 시스템 소속 데이터만 허용)
     */
    @ResponseJsonResult
    @DeleteMapping("/delete")
    public Object deleteFusionMetaData(@RequestParam String meta_id, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return metaDataFusionService.deleteFusionMetaData(meta_id, user);
    }
}
