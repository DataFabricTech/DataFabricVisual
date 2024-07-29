package com.mobigen.ovp.category;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @PutMapping("/")
    public Object addCategory(@RequestBody List<JsonPatchOperation> params) {
        return categoryService.addCategory(params);
    }

    @ResponseJsonResult
    @PatchMapping(value = "/", consumes = "application/json-patch+json")
    public Object updateCategory(@RequestBody List<JsonPatchOperation> params) {
        return categoryService.updateCategory(params);
    }

    @ResponseJsonResult
    @GetMapping("/models")
    public Object getModelByCategoryId(@RequestParam MultiValueMap<String, String> params) {
        String categoryId = params.getFirst("categoryId");
        int page = Integer.parseInt(params.getFirst("page"));
        int size = Integer.parseInt(params.getFirst("size"));
        return categoryService.getModelByCategoryId(categoryId, page, size);
    }
}
