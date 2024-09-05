package com.mobigen.ovp.category;

import com.mobigen.ovp.category.dto.CategoryDTO;
import com.mobigen.ovp.category.entity.CategoryEntity;
import com.mobigen.ovp.category.repository.CategoryRepository;
import com.mobigen.ovp.common.constants.Constants;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.ClassificationTagsClient;
import com.mobigen.ovp.common.openmete_client.ContainersClient;
import com.mobigen.ovp.common.openmete_client.TablesClient;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.search_detail.dto.request.DataModelDetailTagDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ContainersClient containersClient;
    private final TablesClient tablesClient;
    private final CategoryRepository categoryRepository;
    private final SearchService searchService;
    private final ClassificationTagsClient classificationTagsClient;

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

    public List<CategoryDTO> getCategoryAllList() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        return categories.stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList());
    }

    @Transactional
    public Object addCategory(CategoryDTO dto) throws Exception {
        dto.setTagId(createTagInfo(dto.getId()));
        return insertOrUpdate(dto);
    }

    public Object updateCategory(CategoryDTO dto) throws Exception {
        return insertOrUpdate(dto);
    }

    @Transactional
    public Object insertOrUpdate(CategoryDTO dto) throws Exception {
        CategoryEntity undefinedTagEntity = getUndefinedTag();

        if (undefinedTagEntity != null && dto.getName().equals(Constants.UNDEFINED_TAG_NAME)) {
            return "NOT_ALLOWED_NAME";
        }
        if (undefinedTagEntity != null && dto.getParentId().equals(undefinedTagEntity.getId().toString())) {
            return "NOT_ALLOWED_PARENT_ID";
        }

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
        CategoryEntity undefinedTagEntity = getUndefinedTag();
        if (params.getId().equals(undefinedTagEntity.getId().toString())) {
            return "NOT_ALLOWED_ID";
        }

        CategoryEntity categoryEntity = params.toEntity();
        // 재귀 함수 이용하여 하위 > 하위 > 하위.. 의 categoryId 목록 조회.

        List<UUID> tagIds = new ArrayList<>();
        List<UUID> idsToDelete = new ArrayList<>();
        collectCategoryIdsAndTags(categoryEntity.getId(), idsToDelete, tagIds);

        // 삭제 진행
        // step1. openMeta 에서 tag 삭제
        MultiValueMap<String, String> tagParams = new LinkedMultiValueMap<>();
        tagParams.add("recursive", "true");
        tagParams.add("hardDelete", "true");

        for (UUID tagId : tagIds) {
            classificationTagsClient.deleteTag(tagId.toString(), tagParams);
        }

        // step2. db 에서 category 삭제
        categoryRepository.deleteAllByIds(idsToDelete);

        // step3. 형제 category 들 reorder 진행
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

        CategoryEntity undefinedTagEntity = getUndefinedTag();
        if (undefinedTagEntity.getId().equals(dropNodeId)) {
            return false;
        }

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
        if (getModelListByTagId(targetNodeEntity.getTagId()).size() > 0) {
            return "INVALID";
        }

        // step3. 같은 형제 노드중에 중복된 카테고리 명이 있는지 체크 필요함.
        if (hasSameNameInSiblings(dropNodeEntity.getId(), targetNodeEntity.getId(), dropNodeEntity.getName())) {
            return "HAS_SAME_NAME";
        }

        return "VALID";
    }

    private boolean hasSameNameInSiblings(UUID id, UUID parentId, String name) {
        return categoryRepository.findByParentIdAndIdNotAndName(parentId, id, name).size() > 0;
    }

    /**
     * categoryId 기반 modelList 조회
     *
     * @param tagId
     * @param params
     * @return
     * @throws Exception
     */
    @Transactional
    public Object getModelList(String tagId, MultiValueMap<String, String> params) throws Exception {
        params.add("query_filter", createQueryFilterByTagName(getTagInfo(tagId.toString())));
        return getModelListByTagId(UUID.fromString(tagId), params);
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
        Map<String, Object> tagInfo = classificationTagsClient.getTag(tagId);
        return tagInfo.get("fullyQualifiedName").toString();
    }

    // TODO : classification 쪽으로 이동 필요함.
    // TODO : classification 명 상수 처리 필요.
    public String createTagInfo(String categoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("classification", Constants.OVP_CATEGORY);
        params.put("description", "OVP Category Matched Tag");
        params.put("displayName", categoryId);
        params.put("name", categoryId);
        Map<String, Object> response = (Map<String, Object>) classificationTagsClient.createTag(params);
        return response.get("id").toString();
    }

    /**
     * 데이터 모델 변경 (테이블, 스토리지)
     *
     * @param id
     * @param type
     * @param body
     * @return
     */
    public Object changeDataModel(String id, String type, List<Map<String, Object>> body) {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "owner,followers,tags,votes");

        if (!ModelType.STORAGE.getValue().equals(type)) {
            return tablesClient.changeDataModel(id, params, body);
        } else {
            return containersClient.changeStorage(id, params, body);
        }
    }

    private List<Map<String, Object>> makeRemoveBody(int tagLength) {
        List<Map<String, Object>> removedBody = new ArrayList<>();

        for (int i = (tagLength - 1); i >= 0; i--) {
            Map<String, Object> row = new HashMap<>();
            row.put("op", "remove");
            row.put("path", new StringBuffer("/tags/").append(i).toString());
            removedBody.add(row);
        }

        return removedBody;
    }

    private List<Map<String, Object>> makeAddBody(List<Tag> tags, String tagId) {
        List<Map<String, Object>> addedBody = new ArrayList<>();
        List<DataModelDetailTagDto> bodyList = new ArrayList<>();
        List<DataModelDetailTagDto> tagList = new ArrayList<>();

        // 데이터 모델의 태크 목록에서 카테고리, 태그, 용어를 분리해서 저장.
        List<DataModelDetailTagDto> glossaryList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains(Constants.OVP_CATEGORY) && "Glossary".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());
        ;
        List<DataModelDetailTagDto> classificationList = tags.stream()
                .filter(tag -> !tag.getTagFQN().contains(Constants.OVP_CATEGORY) && "Classification".equals(tag.getSource()))
                .map(tag -> new DataModelDetailTagDto(tag))
                .collect(Collectors.toList());

        // { op, path, value }
        // 클라이언트로 받은 변경해야 할 데이터(태그, 카테고라, 용어)를 각각 단일 조회 후에 value 를 셋팅한다.
        Map<String, Object> tempTag = classificationTagsClient.getTag(tagId);

        DataModelDetailTagDto tag = new DataModelDetailTagDto();
        tag.setName(tempTag.get("name").toString());
        tag.setDisplayName(tempTag.get("displayName").toString());
        tag.setDescription(tempTag.get("description").toString());
        tag.setTagFQN(tempTag.get("fullyQualifiedName").toString());
        tag.setSource("Classification");
        tag.setLabelType("Manual");
        tag.setStyle((Map<String, Object>) tempTag.get("style"));

        tagList.add(tag);

        // value가 셋팅이 완료 되면 모든 데이터(카테고리, 태그, 용어)를 하나로 합친다.
        bodyList = Stream.concat(glossaryList.stream(), classificationList.stream()).collect(Collectors.toList());
        bodyList = Stream.concat(bodyList.stream(), tagList.stream()).collect(Collectors.toList());

        // 모두 합쳐진 value를 가지고 patch할 body 데이터를 만든다.
        int bodySize = bodyList.size();
        for (int i = 0; i < bodySize; i++) {
            Map<String, Object> operationMap = new HashMap<>();
            operationMap.put("op", "add");
            operationMap.put("path", new StringBuffer("/tags/").append(i).toString());
            operationMap.put("value", bodyList.get(i));
            addedBody.add(operationMap);
        }

        return addedBody;
    }

    public Object ChangeDataModelTag(String tagId, String type, List<String> body) {

        Tables tables = null;
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("fields", "tags");
        params.add("include", "all");
        int excuteCount = 0;
        int successCount = 0;
        for (String dataModelId : body) {
            if (!ModelType.STORAGE.getValue().equals(type)) {
                tables = tablesClient.getTablesName(dataModelId, params);
            } else {
                tables = containersClient.getStorageById(dataModelId, params);
            }

            List tags = tables.getTags();
            List removeBody = makeRemoveBody(tags.size());
            List addBody = makeAddBody(tags, tagId);

            Object removeResult = changeDataModel(dataModelId, type, removeBody);
            Object addResult = changeDataModel(dataModelId, type, addBody);

            excuteCount++;

            if (removeResult != null && addResult != null) {
                successCount++;
            }
        }

        if (excuteCount != successCount) {
            throw new RuntimeException("업데이트 실패: " + (excuteCount - successCount) + "개의 데이터 모델이 업데이트되지 않았습니다.");
        }

        return true;
    }

    private CategoryEntity getUndefinedTag() {
        return categoryRepository.findByName(Constants.UNDEFINED_TAG_NAME);
    }
}
