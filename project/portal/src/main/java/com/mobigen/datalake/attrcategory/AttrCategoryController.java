package com.mobigen.datalake.attrcategory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/attr-category")
@AllArgsConstructor
public class AttrCategoryController {

    private final AttrCategoryService attrCategoryService;
    private final SessionManager sessionManager;

    // ── 차트 조회 ──────────────────────────────────────────────────────────────

    @ResponseJsonResult
    @GetMapping("/list")
    public Object getCategoryList() {
        return attrCategoryService.getCategoryList();
    }

    @ResponseJsonResult
    @GetMapping("/{categoryId}/tables")
    public Object getTables(@PathVariable String categoryId,
            @RequestParam(defaultValue = "similarity") String sort,
            @RequestParam(defaultValue = "10") int limit) {
        return attrCategoryService.getTables(categoryId, sort, limit);
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────

    @ResponseJsonResult
    @GetMapping
    public Object getList(
            @RequestParam(required = false) String used,
            @RequestParam(required = false) String searchWord,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("used", used);
        params.put("searchWord", searchWord);
        params.put("limit", limit);
        params.put("offset", offset);
        return attrCategoryService.getList(params);
    }

    @ResponseJsonResult
    @GetMapping("/name-check")
    public Object checkNameDuplicated(@RequestParam String name) throws Exception {
        return attrCategoryService.checkNameDuplicated(name);
    }

    @ResponseJsonResult
    @PostMapping
    public Object create(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return attrCategoryService.create(params, user);
    }

    @ResponseJsonResult
    @PutMapping("/{id}")
    public Object update(@PathVariable String id, @RequestBody Map<String, Object> params,
            HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("id", id);
        return attrCategoryService.update(params, user);
    }

    @ResponseJsonResult
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable String id) throws Exception {
        return attrCategoryService.delete(id);
    }
}
