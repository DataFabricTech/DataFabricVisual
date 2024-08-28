<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">카테고리</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="loader-wrap" ref="loader" style="display: none">
      <Loading :use-loader-overlay="true" class="loader-lg"></Loading>
    </div>
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">카테고리 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showCategoryAddModal"
          >
            카테고리 추가
          </button>
        </div>
        <div class="no-result" v-if="isCategoriesNoData">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
        <div class="work-list-tree" v-else>
          <tree-vue
            v-if="categories && categories.length"
            mode="edit"
            :items="categories"
            :isCheckable="false"
            :hideGuideLines="false"
            :firExpandAll="true"
            :show-open-all-btn="true"
            :show-close-all-btn="true"
            :use-draggable="true"
            :dropValidator="dropValidator"
            @onItemSelected="onCategoryNodeClick"
            @addSibling="addSibling"
            @addChild="addChild"
          />
        </div>
      </div>
      <div class="work-page" v-if="isCategoriesNoData">
        <div class="l-top-bar"></div>
        <div class="work-contents">
          <div class="no-result">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 정보가 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
      <div class="work-page" v-else>
        <div class="l-top-bar">
          <editable-group
            compKey="title"
            :editable="true"
            :parent-edit-mode="isTitleEditMode"
            @editCancel="editCancel"
            @editDone="editDone"
            @editIcon="editIcon"
          >
            <template #edit-slot>
              <label class="hidden-text" for="title-modify"
                >카테고리 이름 수정</label
              >
              <input
                v-model="selectedTitleNodeValue"
                placeholder="모델명에 대한 영역입니다."
                required
                id="title-modify"
                class="text-input w-1/2"
              />
            </template>
            <template #view-slot>
              <h3 class="editable-group-title">
                {{ selectedNodeCategory.name }}
              </h3>
            </template>
          </editable-group>
          <button class="button button-error-lighter" @click="_deleteCategory">
            삭제
          </button>
        </div>
        <div class="work-contents">
          <editable-group
            compKey="desc"
            :editable="true"
            :parent-edit-mode="isDescEditMode"
            @editCancel="editCancel"
            @editDone="editDone"
            @editIcon="editIcon"
          >
            <template #edit-slot>
              <label class="hidden-text" for="textarea-modify"
                >textarea 입력</label
              >
              <textarea
                class="textarea"
                v-model="selectedDescNodeValue"
                placeholder="모델 설명에 대한 영역입니다."
                required
                id="textarea-modify"
              ></textarea>
            </template>
            <template #view-slot>
              <p class="editable-group-desc">{{ selectedNodeCategory.desc }}</p>
            </template>
          </editable-group>
          <div>
            <div class="l-top-bar">
              <search-input
                class="w-[541px]"
                :is-search-input-default-type="false"
                :placeholder="'검색어를 입력하세요.'"
                :inp-value="searchInputValue"
                :inp-id="'categoryInp1'"
                :label-text="'데이터 모델 검색'"
                @update:value="updateSearchInputValue"
                @on-input="onInput"
              ></search-input>
              <div class="h-group w-full">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="allCheck"
                    value="all"
                    v-model="allModelList"
                    class="checkbox-input"
                  />
                  <label for="allCheck" class="checkbox-label">
                    전체선택
                  </label>
                </div>
                <div class="h-group ml-auto gap-2" v-if="isModalButtonShow">
                  <button
                    class="button button-secondary-stroke"
                    @click="showCategoryChangeModal"
                  >
                    카테고리 변경
                  </button>
                  <button
                    class="button button-secondary"
                    @click="showDataModelAddModal"
                  >
                    데이터모델추가
                  </button>
                </div>
              </div>
            </div>
            <div v-show="modelList.length === 0" class="no-result mt-3">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">등록된 정보가 없습니다.</p>
              </div>
            </div>
            <div
              v-show="modelList && modelList.length > 0"
              class="l-resource-box l-split mt-3"
            >
              <div class="data-page" style="position: relative">
                <div id="dataList" class="data-list">
                  <resource-box-list
                    :data-list="modelList || []"
                    :use-list-checkbox="true"
                    :show-owner="true"
                    :show-category="true"
                    :is-box-selected-style="isBoxSelectedStyle"
                    :useDataNmLink="false"
                    :selected-model-list="selectedModelList"
                    @previewClick="previewClick"
                    @checkedValueChanged="checked"
                  />
                  <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
                  <Loading
                    id="loader"
                    :use-loader-overlay="true"
                    class="loader-lg is-loader-inner"
                    style="display: none"
                  ></Loading>
                </div>
              </div>
              <Preview
                :isShowPreview="isShowPreview"
                :preview-data="previewData"
                :model-type="previewIndex"
                @change="getPreviewCloseStatus"
              ></Preview>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { storeToRefs } from "pinia";
import { useModal } from "vue-final-modal";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import TreeVue from "@extends/tree/Tree.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Loading from "@base/loading/Loading.vue";
import Preview from "~/components/common/preview/preview.vue";
import CategoryAddModal from "~/components/govern/category/category-add-modal.vue";
import CategoryChangeModal from "~/components/govern/category/category-change-modal.vue";
import DataModelAddModal from "~/components/govern/category/data-model-add-modal.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const categoryStore = useGovernCategoryStore();

const {
  getCategories,
  addModelList,
  getModelList,
  editCategory,
  deleteCategory,
  getContainerPreviewData,
  getPreviewData,
  moveCategory,
  resetAddModalStatus,
  setSelectedNode,
  setSearchKeyword,
  getFilters,
  changeTab,
  setEmptyFilter,
} = categoryStore;
const {
  selectedModelList,
  categories,
  modelList,
  isCategoriesNoData,
  previewData,
  isBoxSelectedStyle,
  selectedDataModelList,
  addSearchInputValue,
  checkReachedCount,
  selectedCategoryId,
  selectedCategoryTagId,
  selectedTitleNodeValue,
  selectedNodeCategory,
  dupliSelectedTitleNodeValue,
  lastChildIdList,
  defaultCategoriesParentId,
  categoriesParentId,
  categoriesId,
} = storeToRefs(categoryStore);

const CATEGORY_ADD_MODAL_ID = "category-add-modal";
const CATEGORY_CHANGE_MODAL_ID = "category-change-modal";
const DATA_MODEL_ADD_MODAL_ID = "data-modal-add-modal";

const loader = ref<HTMLElement | null>(null);
const modelIdList = ref([]);
const isDescEditMode = ref(false);
const isTitleEditMode = ref(false);
const isShowPreview = ref<boolean>(false);
const isModalButtonShow = ref<boolean>(false);

let currentPreviewId: string | number = "";
let previewIndex: string = "table";

const selectedDescNodeValue = ref(selectedNodeCategory.value.desc || "");
const isAllModelListChecked = ref<boolean>(false);
watch(
  () => selectedNodeCategory.value.name,
  (newVal) => {
    selectedTitleNodeValue.value = newVal || "";
  },
  { immediate: true },
);

watch(
  () => selectedNodeCategory.value.desc,
  (newVal) => {
    selectedDescNodeValue.value = newVal || "";
  },
  { immediate: true },
);

// TREE
const onCategoryNodeClick = async (node: TreeViewItem) => {
  selectedModelList.value = [];
  isShowPreview.value = false;
  isBoxSelectedStyle.value = false;
  isDescEditMode.value = false;
  isTitleEditMode.value = false;
  selectedNodeCategory.value = node;
  dupliSelectedTitleNodeValue.value = node.name;
  selectedCategoryId.value = node.id;
  selectedCategoryTagId.value = <string>node.tagId;

  checkModalButton(node.id);

  setScrollOptions(0);
  // 선택한 노드정보 저장
  setSelectedNode(node);
  // 선택한 노드 기준 모델 목록을 조회
  await getModelList();
  // 모든 모델 리스트 id 저장
  setModelIdList();
};

const checkModalButton = (id: string) => {
  isModalButtonShow.value = lastChildIdList.value.some(
    (lastChildId: string) => lastChildId === id,
  );
};

const addSibling = (newNode: TreeViewItem) => {
  categoriesParentId.value = newNode.parentId;
  categoriesId.value = newNode.id;
  openCategoryAddModal();
  resetAddModalStatus();
};

const addChild = (newNode: TreeViewItem) => {
  const checkAddLasChild = lastChildIdList.value.some(
    (lastChildId: string) => lastChildId === newNode.parentId,
  );

  // 이곳에서 3depth 체크
  if (checkAddLasChild) {
    alert("카테고리는 최대 3depth 까지만 추가할 수 있습니다.");
    return;
  }

  categoriesParentId.value = newNode.parentId;
  categoriesId.value = newNode.id;
  openCategoryAddModal();
  resetAddModalStatus();
};

const _editCategory = () => {
  const editNodeParam: TreeViewItem = {
    id: selectedNodeCategory.value.id,
    parentId: selectedNodeCategory.value.parentId,
    tagId: selectedNodeCategory.value.tagId,
    name: selectedNodeCategory.value.name,
    desc: selectedNodeCategory.value.desc,
    children: [],
  };
  editCategory(editNodeParam);
};

const _deleteCategory = async () => {
  if (confirm("카테고리를 삭제 하시겠습니까?")) {
    const res = await deleteCategory(selectedNodeCategory.value.id);
    if (res.result === 1) {
      alert("삭제 되었습니다.");
      await getCategories();
      await onCategoryNodeClick(categories.value[0]);
    } else {
      alert("삭제가 실행되지 않았습니다.");
    }
  }
};

let nodeMoved: Ref<boolean> = ref(false);
let dropMsg: Ref<any> = ref(null);

watch(
  () => nodeMoved.value,
  (newVal) => {
    if (newVal) {
      if (dropMsg.value !== null) {
        alert(dropMsg.value);
      }
      getCategories();
    }
    nodeMoved.value = false;
    dropMsg.value = null;
  },
);
const dropValidator = async (
  dropNode: TreeViewItem,
  targetNode: TreeViewItem,
): Promise<boolean> => {
  // 조건 처리 backend 에서 진행
  const resultMsg = await moveCategory(dropNode.id, targetNode.id);
  dropMsg.value = resultMsg ? null : "이동이 불가 합니다.";
  nodeMoved.value = true;
  // tree lib가 async-await 처리를 지원하지 않기 때문에 여기서는 true 로 던지고,
  // backend 동작이 끝나면 그때 결과에 따라 watch 항목에서 alert 처리, 목록을 갱신 or 유지 한다
  return true;
};

// DATAMODEL LIST
const allModelList = computed({
  get() {
    return modelIdList.value.length === 0
      ? false
      : modelIdList.value.length === selectedModelList.value.length;
  },
  set(event) {
    if (event) {
      isAllModelListChecked.value = true;
      selectedModelList.value = modelIdList.value;
    } else {
      isAllModelListChecked.value = false;
      selectedModelList.value = [];
    }
  },
});

const setModelIdList = () => {
  modelIdList.value = [];
  for (const element of modelList.value) {
    modelIdList.value.push(element.id);
  }
};

const searchInputValue = ref("");
const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};
const onInput = (value: string) => {
  setScrollOptions(0);
  getModelList(value);
};

const checked = (checkedList: any[]) => {
  selectedModelList.value = checkedList;
};

const { scrollTrigger, setScrollOptions } =
  useIntersectionObserver(addModelList);

// PREVIEW
const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

const previewClick = async (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  if (id === currentPreviewId) {
    return;
  }

  type === "storage"
    ? await getContainerPreviewData(id)
    : await getPreviewData(fqn);

  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
  currentPreviewId = id;
  previewIndex = type;
};

// EDITABLE-INPUT
const editCancel = (key: string) => {
  switch (key) {
    case "title":
      selectedTitleNodeValue.value = selectedNodeCategory.value.name;
      isTitleEditMode.value = false;
      break;
    case "desc":
      selectedDescNodeValue.value = selectedNodeCategory.value.desc;
      isDescEditMode.value = false;
      break;
  }
};
const editDone = (key: string) => {
  switch (key) {
    case "title":
      if (selectedTitleNodeValue.value === "") {
        return;
      }
      selectedNodeCategory.value.name = selectedTitleNodeValue.value;
      isTitleEditMode.value = false;
      break;
    case "desc":
      if (selectedDescNodeValue.value === "") {
        selectedNodeCategory.value.desc = "설명 없음";
        return;
      }
      selectedNodeCategory.value.desc = selectedDescNodeValue.value;
      isDescEditMode.value = false;
      break;
  }

  _editCategory();
};
const editIcon = (key: string) => {
  switch (key) {
    case "title":
      isTitleEditMode.value = true;
      break;
    case "desc":
      isDescEditMode.value = true;
      break;
  }
};

// MODAL
const { open: openCategoryAddModal, close: closeCategoryAddModal } = useModal({
  component: CategoryAddModal,
  attrs: {
    modalId: CATEGORY_ADD_MODAL_ID,
    onCloseCategoryAddModal() {
      closeCategoryAddModal();
    },
  },
});
const { open: openCategoryChangeModal, close: closeCategoryChangeModal } =
  useModal({
    component: CategoryChangeModal,
    attrs: {
      modalId: CATEGORY_CHANGE_MODAL_ID,
      onCloseCategoryChangeModal() {
        closeCategoryChangeModal();
      },
    },
  });
const { open: openDataModelAddModal, close: closeDataModelAddModal } = useModal(
  {
    component: DataModelAddModal,
    attrs: {
      modalId: DATA_MODEL_ADD_MODAL_ID,
      onCloseDataModelAddModal() {
        closeDataModelAddModal();
      },
      onBeforeOpen() {
        beforeOpenModal();
      },
      onOpen() {
        openModal();
      },
    },
  },
);

const showCategoryAddModal = () => {
  categoriesParentId.value = defaultCategoriesParentId.value;
  categoriesId.value = "";
  openCategoryAddModal();
  resetAddModalStatus();
};

const showCategoryChangeModal = () => {
  openCategoryChangeModal();
};

const showDataModelAddModal = () => {
  openDataModelAddModal();
};

const beforeOpenModal = () => {
  selectedDataModelList.value = [];
  addSearchInputValue.value = "";
  checkReachedCount.value = false;
  setSearchKeyword("");
  getFilters();
};

const openModal = () => {
  setEmptyFilter();
  changeTab("table");
};

onMounted(async () => {
  if (loader.value) {
    loader.value.style.display = "block";
  }

  await getCategories();

  if (categories.value && categories.value.length > 0) {
    await onCategoryNodeClick(categories.value[0]);
  }

  if (loader.value) {
    loader.value.style.display = "none";
  }
});
</script>

<style scoped></style>
