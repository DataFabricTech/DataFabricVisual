package com.mobigen.ovp.category;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.category.dto.CategoryDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;


@Slf4j
@RequestMapping("/api/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @ResponseJsonResult
    @GetMapping("/list")
    public Object getModelByCategoryId() {
        return categoryService.getCategories();
    }

    @ResponseJsonResult
    @GetMapping("/list/all")
    public Object getCategoryAllList(@RequestParam MultiValueMap<String, String> params) {
        return categoryService.getCategoryAllList();
    }

    @ResponseJsonResult
    @PutMapping("")
    public Object addCategory(@RequestBody CategoryDTO params) throws Exception {
        return categoryService.addCategory(params);
    }

    @ResponseJsonResult
    @PatchMapping("")
    public Object updateCategory(@RequestBody CategoryDTO params) throws Exception {
        return categoryService.updateCategory(params);
    }

    @ResponseJsonResult
    @DeleteMapping(value = "")
    public Object deleteCategory(@RequestBody CategoryDTO params) throws Exception {
        return categoryService.deleteCategory(params);
    }

    @ResponseJsonResult
    @PutMapping(value = "/move")
    public Object moveCategory(@RequestBody Map<String, Object> params) throws Exception {
        return categoryService.moveCategory(params);
    }

    @ResponseJsonResult
    @GetMapping("/models")
    public Object getModelList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return categoryService.getModelList(params.getFirst("tagId"), params);
    }

    @ResponseJsonResult
    @PatchMapping("/{id}")
    Object addCategoryTagId(@PathVariable String id, @RequestBody Map<String, Object> body) throws Exception {
        return categoryService.addCategoryTagId(id, body);
    }

}
