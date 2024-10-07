package com.mobigen.ovp.common;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryConvertUtil {

    public CategoryDTO convertCategoryDTO(List<CategoryDTO> categoryDTOs) {
        // 부모-자식 관계를 매핑하기 위한 맵
        Map<String, List<CategoryDTO>> parentChildMap = new HashMap<>();
        List<CategoryDTO> rootCategories = new ArrayList<>();

        for (CategoryDTO category : categoryDTOs) {
            if (category.getParentId() != null && category.getParentId().equals(category.getId())) {
                rootCategories.add(category);
            } else {
                parentChildMap
                        .computeIfAbsent(category.getParentId(), k -> new ArrayList<>())
                        .add(category);
            }
        }

        // 트리 구조를 설정
        for (CategoryDTO category : categoryDTOs) {
            List<CategoryDTO> children = parentChildMap.get(category.getId());
            if (children != null) {
                // order 순으로 정렬
                children.sort(Comparator.comparingInt(CategoryDTO::getOrder));
                category.setChildren(children);
            }
        }
        rootCategories.sort(Comparator.comparingInt(CategoryDTO::getOrder));

        return rootCategories.get(0);

    }

    private List<CategoryDTO> convertCategoryDTOList(List<CategoryEntity> categories) {
        return categories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }
    public CategoryDTO convertCategoryEntities(List<CategoryEntity> categories) {
        return convertCategoryDTO(convertCategoryDTOList(categories));
    }

    public CategoryDTO convertCategoryEntityAndDTO(List<CategoryEntity> categoryEntities, List<CategoryDTO> categoryDTOS) {
        List<CategoryDTO> newEntities = convertCategoryDTOList(categoryEntities);
        newEntities.addAll(categoryDTOS);
        return convertCategoryDTO(newEntities);

    }

}

