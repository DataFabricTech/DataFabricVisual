package com.mobigen.ovp.category;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.entity.CategoryMatchEntity;
import com.mobigen.ovp.category.repository.CategoryMatchRepository;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
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

    public Object addCategory(CategoryDTO dto) {
        // TODO : [개발] open meta api 이용해서 tag id 처리 필요
//        dto.setTagId("");
        return insertOrUpdate(dto);
    }

    public Object updateCategory(CategoryDTO dto) {
        return insertOrUpdate(dto);
    }

    @Transactional
    public Object insertOrUpdate(CategoryDTO dto) {
        CategoryEntity thisNodeEntity = dto.toEntity();
        CategoryEntity targetNodeEntity = categoryRepository.findById(UUID.fromString(dto.getParentId())).get();

        // 추가 여부 확인
        Object checkCategoryValidation = checkValidationNodePosition(targetNodeEntity, thisNodeEntity);
        if (!checkCategoryValidation.equals("VALID")) {
            return checkCategoryValidation;
        }

        CategoryEntity entity = dto.toEntity();
        entity.setOrder(1 + getSiblingMaxOrder(UUID.fromString(dto.getParentId())));
        categoryRepository.saveOrUpdate(entity);
        return "";
    }

    private int getSiblingMaxOrder(UUID parentId) {
        return categoryRepository.findMaxOrderByParentId(parentId);
    }

    private void collectCategoryIdsRecursively(UUID categoryId, List<UUID> idsToDelete) {
        List<CategoryEntity> subCategories = categoryRepository.findByParentIdOrderByOrderDesc(categoryId);

        if (!subCategories.isEmpty()) {
            for (CategoryEntity subCategory : subCategories) {
                collectCategoryIdsRecursively(subCategory.getId(), idsToDelete);
            }
        }

        idsToDelete.add(categoryId);
    }

    @Transactional
    public Object deleteCategory(CategoryDTO params) {
        CategoryEntity entity = params.toEntity();
        // 재귀 함수 이용하여 하위 > 하위 > 하위.. 의 categoryId 목록 조회.
        List<UUID> idsToDelete = new ArrayList<>();
        collectCategoryIdsRecursively(entity.getId(), idsToDelete);

        // 하위 categoryId 기반 설정된 modelList 있는지 조회
        List<CategoryMatchEntity> modelList = categoryMatchRepository.findByCategoryIdIn(idsToDelete);

        if (modelList.size() < 1) {
            // 삭제 진행
            categoryRepository.deleteAllByIds(idsToDelete);
            // 선택한 노드의 형제노드들의 order 를 재정렬한다.
            // 같은 형제 내에서 order 를 바꾸는 기능이 없어 하기 동작은 실행하지 않아도 무관함.
            reorderSiblings(entity.getParentId());
            return "";
        } else {
            return "HAS_MODEL_LIST";
        }
    }

    private void reorderSiblings(UUID parentId) {
        List<CategoryEntity> siblingCategories = categoryRepository.findByParentIdOrderByOrderAsc(parentId);
        AtomicInteger orderCounter = new AtomicInteger(1);
        siblingCategories.forEach(category -> category.setOrder(orderCounter.getAndIncrement()));
        categoryRepository.saveAll(siblingCategories);
    }

    /**
     * 조상 (root) 까지의 depth count 를 구함
     *
     * @param category
     * @return
     */
    public int findAncestorsDepth(CategoryEntity category) {
        List<CategoryEntity> ancestors = new ArrayList<>();
        while (category.getParent() != null && !category.isRoot()) {
            category = category.getParent();
            ancestors.add(category);
        }
        return ancestors.size();
    }

    /**
     * 자식 노드들중 가장 최 하위의 자식까지의 depth count 를 구함
     *
     * @param category
     * @return
     */
    private int findChildrenDepth(CategoryEntity category) {
        if (category.getChildren().isEmpty()) {
            return 1;
        }
        int maxDepth = 0;
        for (CategoryEntity child : category.getChildren()) {
            int childDepth = findChildrenDepth(child);
            maxDepth = Math.max(maxDepth, childDepth);
        }
        return maxDepth + 1;
    }

    @Transactional
    public boolean moveCategory(Map<String, Object> params) {
        String thisNodeId = params.get("thisNodeId").toString();
        String targetNodeId = params.get("targetNodeId").toString();
        CategoryEntity targetNodeEntity = categoryRepository.findByIdWithParent(UUID.fromString(targetNodeId));
        CategoryEntity thisNodeEntity = categoryRepository.findByIdWithParent(UUID.fromString(thisNodeId));

        // 조건 처리
        if (!checkValidationNodePosition(targetNodeEntity, thisNodeEntity).equals("VALID")) {
            return false;
        }

        CategoryDTO categoryDto = new CategoryDTO(thisNodeEntity);
        categoryDto.setParentId(targetNodeId);
        insertOrUpdate(categoryDto);
        return true;
    }

    private Object checkValidationNodePosition(CategoryEntity targetNodeEntity, CategoryEntity dropNodeEntity) {
        // step1. 노드의 위치가 3 depth 를 넘는지 체크
        int targetNodeDepth = findAncestorsDepth(targetNodeEntity);
        int thisNodeDepth = findChildrenDepth(dropNodeEntity);
        if (targetNodeDepth + thisNodeDepth > 3) {
            return "OVER_DEPTH";
        }

        // step2. targetNode 에 설정된 데이터 모델이 있는지 체크
        if (targetNodeEntity.getCategoryMatches().size() > 1) {
            return "INVALID";
        }

        // step3. thisNode 에 데이터 모델이 설정되어 있는 경우, tagetNode는 최하위 노드일때만 가능함. (중간 노드일때 불가능)
        if (dropNodeEntity.getCategoryMatches().size() > 0 && targetNodeEntity.getChildren().size() > 1) {
            return "INVALID";
        }

        return "VALID";
    }

    public List<Object> getModelByCategoryId(String categoryId, int page, int size) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.fromString(categoryId));

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryMatchEntity> categoryMatchList = categoryMatchRepository.findByCategoryId(categoryEntity, pageable);

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
