package com.mobigen.datalake.metaData.similarAttrCategory;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/metadata/similar-attr-category")
@RestController
@RequiredArgsConstructor
public class SimilarAttrCategoryController {
    private final SessionManager sessionManager;
    private final SimilarAttrCategoryService similarAttrCategoryService;

    /**
     * 유사 속성 카테고리 목록 조회
     */
    @ResponseJsonResult
    @PostMapping("/list")
    public Object getList(@RequestBody Map<String, Object> params) throws Exception {
        return similarAttrCategoryService.getList(params);
    }

    /**
     * 유사 속성 카테고리 상세 조회
     */
    @ResponseJsonResult
    @GetMapping("/info")
    public Object getInfo(@RequestParam String id) throws Exception {
        return similarAttrCategoryService.getInfo(id);
    }

    /**
     * 유사 속성 카테고리 등록
     */
    @ResponseJsonResult
    @PostMapping("/create")
    public Object create(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return similarAttrCategoryService.create(params, user);
    }

    /**
     * 유사 속성 카테고리 수정
     */
    @ResponseJsonResult
    @PostMapping("/update")
    public Object update(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return similarAttrCategoryService.update(params, user);
    }

    /**
     * 유사 속성 카테고리 삭제 (매핑 데이터 함께 삭제)
     */
    @ResponseJsonResult
    @DeleteMapping("/delete")
    public Object delete(@RequestParam String id) throws Exception {
        return similarAttrCategoryService.delete(id);
    }

    /**
     * 카테고리명 중복 체크
     */
    @ResponseJsonResult
    @GetMapping("/checkNameDuplicated")
    public Object checkNameDuplicated(@RequestParam String name) throws Exception {
        return similarAttrCategoryService.checkNameDuplicated(name);
    }
}
