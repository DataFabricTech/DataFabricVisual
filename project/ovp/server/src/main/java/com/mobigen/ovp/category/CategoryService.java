package com.mobigen.ovp.category;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.entity.CategoryMatchEntity;
import com.mobigen.ovp.category.repository.CategoryMatchRepository;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.openmete_client.JsonPatchOperation;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMatchRepository categoryMatchRepository;
    private final SearchClient searchClient;
    private final ModelConvertUtil modelConvertUtil;

    public CategoryDTO getCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        // CategoryEntity 리스트를 CategoryDTO 리스트로 변환
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());

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

    private int getSiblingCnt(UUID parentId) {
        List<CategoryEntity> siblings = categoryRepository.findByParentIdOrderByOrderDesc(parentId);
        return siblings.isEmpty() ? 1 : siblings.get(0).getOrder() + 1;
    }

    public Object addCategory(CategoryDTO paramsDTO) {
        // parentId 기준 자식 노드가 몇인지 확인해서 order 값 부여 (가장 마지막 노드로 추가)
        CategoryEntity categoryEntity = paramsDTO.toEntity();
        int order = getSiblingCnt(categoryEntity.getParentId());
        categoryEntity.setOrder(order);

        return categoryRepository.saveOrUpdate(categoryEntity);
    }

    public Object updateCategory(CategoryDTO paramsDTO) {
        return categoryRepository.saveOrUpdate(paramsDTO.toEntity());
    }

    public Object deleteCategory(Map<String, String> params) {
        return null;
    }

    public Object moveCategory(List<JsonPatchOperation> params) {
        return null;
    }

    public List<Object> getModelByCategoryId(String categoryId, int page, int size) {
        UUID uuid;
        try {
            uuid = UUID.fromString(categoryId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID string: " + categoryId, e);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryMatchEntity> categoryMatchList = categoryMatchRepository.findByCategoryId(uuid, pageable);

        List<Object> resultList = new ArrayList<>();
        for (CategoryMatchEntity item : categoryMatchList) {
            String modelId = item.getModelId().toString();
            String modelIndex = item.getModelIndex().toString();
            try {
                Map<String, Object> modelData = searchClient.getSearchOne(modelIndex, modelId);
                if (modelData != null && modelData.containsKey("_source")) {
                    Map<String, Object> source = (Map<String, Object>) modelData.get("_source");
                    resultList.add(modelConvertUtil.convertSourceDataOne(modelIndex, source));
                }
            } catch (Exception e) {
                // 각 요청의 예외를 로깅하고 continue (필요 시 다른 방법으로 처리 가능)
                System.err.println("Error fetching model data for model ID: " + modelId + ": " + e.getMessage());
            }
        }

        return resultList;
    }
}
