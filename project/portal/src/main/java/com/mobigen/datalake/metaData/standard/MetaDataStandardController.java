package com.mobigen.datalake.metaData.standard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobigen.datalake.metaData.manage.MetaDataManageController;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/metadata/standard")
@RestController
@RequiredArgsConstructor
public class MetaDataStandardController {
    private final SessionManager sessionManager;
    private final MetaDataStandardService metaDataStandardService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataManageController.class);

     /**
     * 표준 데이터 목록 조회
     * 
     * @param param 조회 정보
     * @return 표준 데이터 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getMetaDataStandardList(@RequestBody Map<String, Object> params, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("select_type", params.get("select_type").toString());
        param.put("searchType", params.get("searchType"));
        param.put("searchWord", params.get("searchWord"));
        param.put("used", params.get("used"));
        param.put("formChecked", String.valueOf(params.get("formChecked")));
        param.put("limit", String.valueOf(params.get("limit")));
        param.put("offset", String.valueOf(Integer.parseInt(params.get("limit").toString()) * (Integer.parseInt(params.get("offset").toString()) - 1)));

        return metaDataStandardService.getMetaDataStandardList(param);
    }

    /**
     * 표준 데이터 정보 조회
     * 
     * @param param 조회 정보
     * @return 표준 데이터 정보
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/info")
    public Object getMetaDataStandardInfo(@RequestParam String id, @RequestParam String select_type, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return metaDataStandardService.getMetaDataStandardInfo(id, select_type);
    }

     /**
     * 표준 용어, 단어, 도메인 삭제처리
     * 
     * @return 표준 용어, 단어, 도메인 삭제 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @DeleteMapping("/deleteSoft")
    public Object deleteSoft(@RequestParam String id, @RequestParam String select_type) throws Exception {
        return metaDataStandardService.deleteSoft(id, select_type);
    }

    /**
     * 용어 중복체크
     *
     * @param word 용어명/영문약어명
     * @param type KOR/ENG_SIGN
     */
    @GetMapping("/checkTermDuplicated")
    public Object checkTermDuplicated(@RequestParam String word, @RequestParam String type) throws Exception {
        return metaDataStandardService.checkTermDuplicated(word, type);
    }

    /**
     * 단어 중복체크
     *
     * @param word 한글명/영문약어명/단어영문명
     * @param type KOR/ENG_SIGN/ENG
     */
    @GetMapping("/checkWordDuplicated")
    public Object checkDuplicatedWord(@RequestParam String word, @RequestParam String type) throws Exception {
        return metaDataStandardService.checkWordDuplicated(word, type);
    }

    /**
     * 도메인 중복체크
     *
     * @param word 한글명
     * @param type KOR
     */
    @GetMapping("/checkDomainDuplicated")
    public Object checkDomainDuplicated(@RequestParam String word, @RequestParam String type) throws Exception {
        return metaDataStandardService.checkDomainDuplicated(word, type);
    }

    /**
     * 용어 등록
     *
     * @param params 용어 등록 데이터
     */
    @ResponseJsonResult
    @PostMapping("/createTerm")
    public Object createTerm(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // UUID
        UUID id = UUID.randomUUID();
        params.put("id", id);

        // 작성자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String creator = "";
        if (user != null) {
            creator = user.get("user_id").toString();
        }        
        params.put("creator", creator);

        return metaDataStandardService.createTerm(params);
    }

    /**
     * 용어 수정
     *
     * @param params 용어 수정 데이터
     */
    @ResponseJsonResult
    @PostMapping("/updateTerm")
    public Object updateTerm(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 수정자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String modifier = "";
        if (user != null) {
            modifier = user.get("user_id").toString();
        }        
        params.put("modifier", modifier);

        return metaDataStandardService.updateTerm(params);
    }

    /**
     * 용어 정보 수정
     *
     * @param params 용어 정보 수정 데이터
     */
    @ResponseJsonResult
    @PostMapping("/updateTermInfo")
    public Object updateTermInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 수정자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String modifier = "";
        if (user != null) {
            modifier = user.get("user_id").toString();
        }        
        params.put("modifier", modifier);

        return metaDataStandardService.updateTermInfo(params);
    }

    /**
     * 용어 도메인 수정
     *
     * @param params 용어 도메인 수정 데이터
     */
    @ResponseJsonResult
    @PostMapping("/updateTermDomain")
    public Object updateTermDomain(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 수정자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String user_id = "";
        if (user != null) {
            user_id = user.get("user_id").toString();
        }        
        params.put("creator", user_id);
        params.put("modifier", user_id);

        return metaDataStandardService.updateTermDomain(params);
    }

    /**
     * 단어 등록
     *
     * @param params 단어 등록 데이터
     */
    @ResponseJsonResult
    @PostMapping("/createWord")
    public Object createWord(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // UUID
        UUID id = UUID.randomUUID();
        params.put("id", id);

        // 작성자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String creator = "";
        if (user != null) {
            creator = user.get("user_id").toString();
        }        
        params.put("creator", creator);

        return metaDataStandardService.createWord(params);
    }

    /**
     * 단어 수정
     *
     * @param params 용어 수정 데이터
     */
    @ResponseJsonResult
    @PostMapping("/updateWord")
    public Object updateWord(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 수정자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String modifier = "";
        if (user != null) {
            modifier = user.get("user_id").toString();
        }        
        params.put("modifier", modifier);

        return metaDataStandardService.updateWord(params);
    }

    /**
     * 도메인 등록
     *
     * @param params 도메인 등록 데이터
     */
    @ResponseJsonResult
    @PostMapping("/createDomain")
    public Object createDomain(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // UUID
        UUID id = UUID.randomUUID();
        params.put("id", id);

        // 작성자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String creator = "";
        if (user != null) {
            creator = user.get("user_id").toString();
        }        
        params.put("creator", creator);

        return metaDataStandardService.createDomain(params);
    }

    /**
     * 도메인 수정
     *
     * @param params 도메인 수정 데이터
     */
    @ResponseJsonResult
    @PostMapping("/updateDomain")
    public Object updateDomain(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        // 수정자
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String modifier = "";
        if (user != null) {
            modifier = user.get("user_id").toString();
        }        
        params.put("modifier", modifier);

        return metaDataStandardService.updateDomain(params);
    }

    /**
     * 엑셀 업로드
     * @param file 업로드 파일
     * @param search_type 표준 데이터 타입
     */
    @ResponseJsonResult
    @PostMapping("/uploadExcel")
    public Object uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam String search_type, HttpServletRequest request) throws Exception {
        Map<String, Object> params = new HashMap<>();
        try {
            // 업로드 등록자
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);
            String user_id = "";
            if (user != null) {
                user_id = user.get("user_id").toString();
            }        
            params.put("modifier", user_id);
            params.put("creator", user_id);

            // 업로드 타입(용어, 단어, 도메인)
            params.put("search_type", search_type);
        } catch (Exception e) {
            log.error("fail to uploadExcel !!");
            return ResponseEntity.internalServerError().body("엑셀 업로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }

        return metaDataStandardService.uploadExcel(file, params);
    }

    /**
     * 엑셀 다운로드
     * @param params 표준 데이터 조회 쿼리관련 파라미터
     */
    @PostMapping("/downloadStandardData")
	public ResponseEntity<byte[]> downloadExcel(@RequestBody Map<String, Object> params) throws Exception {
		return metaDataStandardService.downloadExcelFile(params);
	}
}
