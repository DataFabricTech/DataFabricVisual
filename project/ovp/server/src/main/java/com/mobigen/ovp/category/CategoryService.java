package com.mobigen.ovp.category;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.ModelConvertUtil;
import com.mobigen.ovp.common.openmete_client.ClassificationClient;
import com.mobigen.ovp.common.openmete_client.SearchClient;
import com.mobigen.ovp.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    private final SearchClient searchClient;
    private final ModelConvertUtil modelConvertUtil;
    private final SearchService searchService;
    private final ClassificationClient classificationClient;

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

    public Object addCategory(CategoryDTO dto) throws Exception {
        // TODO : [개발] open meta api 이용해서 tag id 처리 필요
//        dto.setTagId("");
        return insertOrUpdate(dto);
    }

    public Object updateCategory(CategoryDTO dto) throws Exception {
        return insertOrUpdate(dto);
    }

    @Transactional
    public Object insertOrUpdate(CategoryDTO dto) throws Exception {
        CategoryEntity thisNodeEntity = dto.toEntity();
        CategoryEntity targetNodeEntity = categoryRepository.findById(UUID.fromString(dto.getParentId())).get();

        // 추가 여부 확인
        Object checkCategoryValidation = checkValidationNodePosition(targetNodeEntity, thisNodeEntity);
        if (!checkCategoryValidation.equals("VALID")) {
            return checkCategoryValidation;
        }

        CategoryEntity entity = dto.toEntity();
        entity.setOrder(1 + categoryRepository.findMaxOrderByParentId(UUID.fromString(dto.getParentId())));
        categoryRepository.saveOrUpdate(entity);
        return "";
    }

    @Transactional
    public Object deleteCategory(CategoryDTO params) throws Exception {
        CategoryEntity categoryEntity = params.toEntity();
        // 재귀 함수 이용하여 하위 > 하위 > 하위.. 의 categoryId 목록 조회.

        List<UUID> tagIds = new ArrayList<>();
        List<UUID> idsToDelete = new ArrayList<>();
        collectCategoryIdsAndTags(categoryEntity.getId(), idsToDelete, tagIds);

        for (UUID tagId : tagIds) {
            if (getModelListByTagId(tagId).size() > 0) {
                // modelList 가 0이 넘는 tag가 있으면, 삭제할수 없도록 경고 메시지를 return 한다.
                return "HAS_MODEL_LIST";
            }
        }

        // 삭제 진행
        categoryRepository.deleteAllByIds(idsToDelete);
        reorderSiblings(categoryEntity.getParentId());
        return "";
    }

    /**
     * tagName query를 만든다.
     *
     * @param tagFQN
     * @return
     * @throws JSONException
     */
    private String createQueryFilterByTagName(String tagFQN) throws JSONException {
        // 모델 목록 조회에 사용하는 query_filter 생성
        JSONObject queryFilter = new JSONObject();
        JSONObject boolObject = new JSONObject();
        JSONArray mustArray = new JSONArray();
        JSONObject mustObject = new JSONObject();
        JSONArray shouldArray = new JSONArray();

        shouldArray.put(new JSONObject().put("term", new JSONObject().put("tags.tagFQN", tagFQN)));

        // Build the JSON structure
        mustObject.put("bool", new JSONObject().put("should", shouldArray));
        mustArray.put(mustObject);
        boolObject.put("must", mustArray);
        queryFilter.put("query", new JSONObject().put("bool", boolObject));

        return queryFilter.toString();
    }

    /**
     * 하위 category 를 모두 탐색하면서 categoryId 와 tagId 를 list 에 넣어둔다.
     *
     * @param categoryId
     * @param idsToDelete
     * @param tagIds
     */
    private void collectCategoryIdsAndTags(UUID categoryId, List<UUID> idsToDelete, List<UUID> tagIds) {
        List<CategoryEntity> subCategories = categoryRepository.findByParentIdOrderByOrderDesc(categoryId);

        for (CategoryEntity subCategory : subCategories) {
            collectCategoryIdsAndTags(subCategory.getId(), idsToDelete, tagIds);
        }

        categoryRepository.findById(categoryId).ifPresent(category -> {
            idsToDelete.add(category.getId());
            UUID tagId = category.getTagId();
            if (tagId != null) {
                tagIds.add(tagId);
            }
        });
    }

    /**
     * 형제 노드들 order 재설정
     *
     * @param parentId
     */
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
    private int findAncestorsDepth(CategoryEntity category) {
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
    public boolean moveCategory(Map<String, Object> params) throws Exception {
        String dropNodeId = params.get("dropNodeId").toString();
        String targetNodeId = params.get("targetNodeId").toString();
        CategoryEntity dropNodeEntity = categoryRepository.findByIdWithParent(UUID.fromString(dropNodeId));
        CategoryEntity targetNodeEntity = categoryRepository.findByIdWithParent(UUID.fromString(targetNodeId));

        // 조건 처리
        if (!checkValidationNodePosition(targetNodeEntity, dropNodeEntity).equals("VALID")) {
            return false;
        }

        CategoryDTO categoryDto = new CategoryDTO(dropNodeEntity);
        categoryDto.setParentId(targetNodeId);
        insertOrUpdate(categoryDto);
        return true;
    }

    /**
     * 카테고리 등록/수정 시 validation 체크
     *
     * @param targetNodeEntity
     * @param dropNodeEntity
     * @return
     * @throws Exception
     */
    private Object checkValidationNodePosition(CategoryEntity targetNodeEntity, CategoryEntity dropNodeEntity) throws Exception {
        // step1. 노드의 위치가 3 depth 를 넘는지 체크
        int targetNodeDepth = findAncestorsDepth(targetNodeEntity);
        int thisNodeDepth = findChildrenDepth(dropNodeEntity);
        if (targetNodeDepth + thisNodeDepth > 3) {
            return "OVER_DEPTH";
        }

        // step2. targetNode 에 설정된 데이터 모델이 있는지 체크
        if (getModelListByTagId(targetNodeEntity.getTagId()).size() > 1) {
            return "INVALID";
        }

        // step3. thisNode 에 데이터 모델이 설정되어 있는 경우, tagetNode는 최하위 노드일때만 가능함. (중간 노드일때 불가능)
        if (getModelListByTagId(dropNodeEntity.getTagId()).size() > 0 && targetNodeEntity.getChildren().size() > 1) {
            return "INVALID";
        }

        return "VALID";
    }

    /**
     * categoryId 기반 modelList 조회
     *
     * @param categoryId
     * @param params
     * @return
     * @throws Exception
     */
    @Transactional
    public Object getModelByCategoryId(String categoryId, MultiValueMap<String, String> params) throws Exception {
        CategoryEntity categoryEntity = categoryRepository.findById(UUID.fromString(categoryId)).get();
        params.add("query_filter", createQueryFilterByTagName(getTagInfo(categoryEntity.getTagId().toString())));

        return getModelListByTagId(categoryEntity.getTagId(), params);
    }

    /**
     * 메소드 오버로딩
     *
     * @param tagId
     * @return
     * @throws Exception
     */
    private List<Object> getModelListByTagId(UUID tagId) throws Exception {
        return getModelListByTagId(tagId, null);
    }

    private List<Object> getModelListByTagId(UUID tagId, MultiValueMap<String, String> params) throws Exception {
        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }
        params.add("query_filter", createQueryFilterByTagName(getTagInfo(tagId.toString())));
        return (List<Object>) ((Map<String, Object>) (searchService.getAllSearchList(params)).get("data")).get("all");
    }

    // TODO : classfication 쪽으로 이동 필요함.
    public String getTagInfo(String tagId) {
        Map<String, Object> tagInfo = classificationClient.getTag(tagId);
        return tagInfo.get("fullyQualifiedName").toString();
    }
}
