package com.mobigen.datalake.attrcategory;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유사 속성 카테고리 차트 - FE용 조회 API.
 * - 차트: 카테고리 20개 + 테이블 수
 * - 상세화면: 특정 카테고리에 속한 테이블 목록
 * (매핑 산정/적재는 recommender 배치가 수행, 본 API는 조회만)
 */
@Slf4j
@RequestMapping("/api/attr-category")
@Controller
@AllArgsConstructor
public class AttrCategoryController {

    private final AttrCategoryService attrCategoryService;
    private final SessionManager sessionManager;

    // ── 차트 조회 ──────────────────────────────────────────────────────────────
    /** 카테고리 목록 + 카테고리별 테이블 수 (차트 초기 20노드용) */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getCategoryList() {
        return attrCategoryService.getCategoryList();
    }

    /**
     * 특정 카테고리에 속한 테이블 목록 (카테고리 클릭 시 상세화면용).
     * @param sort 정렬 기준: similarity(대표성 유사도 내림차순, 기본) | name(테이블명 오름차순)
     */
    @ResponseJsonResult
    @GetMapping("/{categoryId}/tables")
    public Object getTables(@PathVariable String categoryId,
                            @RequestParam(name = "sort", defaultValue = "similarity") String sort) {
        return attrCategoryService.getTables(categoryId, sort);
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────

    @ResponseJsonResult
    @PostMapping("/api/metadata/similar-attr-category/list")
    public Object getList(@RequestBody Map<String, Object> params) throws Exception {
        return attrCategoryService.getList(params);
    }

    @ResponseJsonResult
    @GetMapping("/api/metadata/similar-attr-category/info")
    public Object getInfo(@RequestParam String id) throws Exception {
        return attrCategoryService.getInfo(id);
    }

    @ResponseJsonResult
    @PostMapping("/api/metadata/similar-attr-category/create")
    public Object create(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return attrCategoryService.create(params, user);
    }

    @ResponseJsonResult
    @PostMapping("/api/metadata/similar-attr-category/update")
    public Object update(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return attrCategoryService.update(params, user);
    }

    @ResponseJsonResult
    @DeleteMapping("/api/metadata/similar-attr-category/delete")
    public Object delete(@RequestParam String id) throws Exception {
        return attrCategoryService.delete(id);
    }

    @ResponseJsonResult
    @GetMapping("/api/metadata/similar-attr-category/checkNameDuplicated")
    public Object checkNameDuplicated(@RequestParam String name) throws Exception {
        return attrCategoryService.checkNameDuplicated(name);
    }
}
