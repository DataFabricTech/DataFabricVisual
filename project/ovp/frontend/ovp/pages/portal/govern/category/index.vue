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
        <div class="work-list-contents" v-else>
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
            :use-fir-select="true"
            :dropValidator="dropValidator"
            :immutable-items="[undefinedTagIdManager.get() ?? '']"
            @onItemSelected="onCategoryNodeClick"
            @addSibling="addSibling"
            @addChild="addChild"
          />
        </div>
      </div>
      <div class="work-page" v-if="isCategoriesNoData">
        <div class="l-top-bar h-[48.8px]"></div>
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
        <div class="l-top-bar h-[48.8px]">
          <div class="h-group gap-2">
            <editable-group
              class="w-auto"
              compKey="title"
              :editable="isEditableNode"
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
                  placeholder="카테고리명에 대한 영역입니다."
                  maxlength="20"
                  required
                  id="title-modify"
                  class="text-input"
                />
              </template>
              <template #view-slot>
                <h3 class="editable-group-title">
                  {{ selectedNodeCategory.name }}
                </h3>
              </template>
            </editable-group>
            <div
              class="notification notification-sm notification-error"
              v-if="showSelectedTitleNodeNoti"
            >
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">
                {{ selectedTitleNodeMsg }}
              </p>
            </div>
          </div>
          <button
            class="button button-error-lighter"
            @click="_deleteCategory"
            v-if="isEditableNode"
          >
            삭제
          </button>
        </div>
        <div class="work-contents">
          <div class="v-group gap-2">
            <div class="font-semibold text-neutral-700">설명</div>
            <editable-group
              compKey="desc"
              :editable="isEditableNode"
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
                  placeholder="카테고리 설명에 대한 영역입니다."
                  required
                  id="textarea-modify"
                ></textarea>
              </template>
              <template #view-slot>
                <p class="editable-group-desc">
                  {{ selectedNodeCategory.desc }}
                </p>
              </template>
            </editable-group>
          </div>
          <div class="category-search" v-if="isModalButtonShow">
            <div class="l-top-bar">
              <search-input
                :is-search-input-default-type="false"
                :placeholder="'검색어를 입력하세요.'"
                :inp-value="searchInputValue"
                :inp-id="'categoryInp1'"
                :label-text="'데이터 모델 검색'"
                @update:value="updateSearchInputValue"
                @on-input="onInput"
                @reset="getAllModelList"
              ></search-input>
              <div class="h-group w-full h-8">
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
                <div class="h-group ml-auto gap-2">
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
                    :selected-model-list="selectedModelList"
                    @modelNmClick="modelNmClick"
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
import { useNuxtApp, useRouter } from "nuxt/app";
import TreeVue from "@extends/tree/Tree.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Loading from "@base/loading/Loading.vue";
import Preview from "~/components/common/preview/preview.vue";
import CategoryAddModal from "~/components/govern/category/category-add-modal.vue";
import CategoryChangeModal from "~/components/govern/category/category-change-modal.vue";
import DataModelAddModal from "@/components/govern/common/modal/add-data-model.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";
import _ from "lodash";
import $constants from "~/utils/constant";
import ResourceBox from "~/components/common/resource-box/resource-box.vue";
const router = useRouter();

const { $alert, $confirm } = useNuxtApp();
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
  setModelIdList,
  setSelectedNode,
  undefinedTagIdManager,
} = categoryStore;
const {
  selectedNode,
  selectedModelList,
  categories,
  modelList,
  modelIdList,
  isCategoriesNoData,
  previewData,
  isBoxSelectedStyle,
  selectedCategoryId,
  selectedCategoryTagId,
  selectedTitleNodeValue,
  selectedNodeCategory,
  dupliSelectedTitleNodeValue,
  lastChildIdList,
  defaultCategoriesParentId,
  categoriesParentId,
  categoriesId,
  isShowPreview,
  searchInputValue,
} = storeToRefs(categoryStore);

const CATEGORY_ADD_MODAL_ID = "category-add-modal";
const CATEGORY_CHANGE_MODAL_ID = "category-change-modal";
const DATA_MODEL_ADD_MODAL = "data-model-add-modal";

const loader = ref<HTMLElement | null>(null);

const isDescEditMode = ref(false);
const isTitleEditMode = ref(false);
const isModalButtonShow = ref<boolean>(false);

let currentPreviewId: string | number = "";
let previewIndex: string = "table";

const selectedDescNodeValue = ref(selectedNodeCategory.value.desc || "");
const isAllModelListChecked = ref<boolean>(false);

const showSelectedTitleNodeNoti = ref(false);
const selectedTitleNodeMsg = ref("");

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
  showSelectedTitleNodeNoti.value = false;
  searchInputValue.value = "";
  selectedModelList.value = [];
  isShowPreview.value = false;
  isBoxSelectedStyle.value = false;
  isDescEditMode.value = false;
  isTitleEditMode.value = false;
  selectedNodeCategory.value = node;
  dupliSelectedTitleNodeValue.value = node.name;
  selectedCategoryId.value = node.id;
  selectedCategoryTagId.value = <string>node.tagId;
  searchInputValue.value = "";
  checkModalButton(node);
  setScrollOptions(0);
  // 선택한 노드정보 저장
  selectedNode.value = node;
  // 선택한 노드 기준 모델 목록을 조회
  await getModelList();
  // 모든 모델 리스트 id 저장
  setModelIdList();
};

const getAllModelList = async () => {
  isAllModelListChecked.value = false;
  selectedModelList.value = [];
  setScrollOptions(0);
  setSelectedNode(selectedNodeCategory.value);
  await getModelList();
  setModelIdList();
};

const checkModalButton = (node: TreeViewItem) => {
  isModalButtonShow.value =
    !_.has(node, "children") || _.size(node.children) < 1;
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
    $alert("카테고리는 최대 3depth 까지만 추가할 수 있습니다.", "warning");
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
  if (await $confirm("카테고리를 삭제 하시겠습니까?")) {
    const res = await deleteCategory(selectedNodeCategory.value.id);
    if (res.result === 1) {
      if (res.data === "NOT_ALLOWED_ID") {
        $alert(
          `${$constants.SERVICE.CATEGORY_UNDEFINED_NAME} 카테고리는 삭제가 불가능합니다.`,
          "warning",
        );
        return;
      }
      $alert("삭제 되었습니다.", "info");
      await getCategories();
      await onCategoryNodeClick(categories.value[0]);
    } else {
      $alert("삭제가 실행되지 않았습니다.", "info");
    }
  } else {
    return;
  }
};

let nodeMoved: Ref<boolean> = ref(false);
let dropMsg: Ref<any> = ref(null);

watch(
  () => nodeMoved.value,
  (newVal) => {
    if (newVal) {
      if (dropMsg.value !== null) {
        $alert(dropMsg.value, "error");
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

const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};
const onInput = async (value: string) => {
  searchInputValue.value = value;
  isAllModelListChecked.value = false;
  selectedModelList.value = [];
  setScrollOptions(0);
  await getModelList();
  setModelIdList();
};

const checked = (checkedList: any[]) => {
  selectedModelList.value = checkedList;
};

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

const modelNmClick = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      type: type,
      id: id,
      fqn: fqn,
    },
  });
};

// EDITABLE-INPUT
const editCancel = (key: string) => {
  switch (key) {
    case "title":
      selectedTitleNodeValue.value = selectedNodeCategory.value.name;
      isTitleEditMode.value = false;
      showSelectedTitleNodeNoti.value = false;
      break;
    case "desc":
      selectedDescNodeValue.value = selectedNodeCategory.value.desc;
      isDescEditMode.value = false;
      break;
  }
};
const editDone = (key: string) => {
  const hasError = ref(false);

  switch (key) {
    case "title":
      if (selectedTitleNodeValue.value === "") {
        selectedTitleNodeMsg.value =
          $constants.GOVERNANCE.TITLE.EMPTY_ERROR_MSG;
        hasError.value = true;
      } else if (selectedTitleNodeValue.value.length === 1) {
        selectedTitleNodeMsg.value =
          $constants.GOVERNANCE.TITLE.MINIMUM_LENGTH_ERROR_MSG;
        hasError.value = true;
      } else if (
        !$constants.GOVERNANCE.TITLE.REGEX.test(selectedTitleNodeValue.value)
      ) {
        selectedTitleNodeMsg.value =
          $constants.GOVERNANCE.TITLE.REGEX_ERROR_MSG;
        hasError.value = true;
      } else {
        selectedNodeCategory.value.name = selectedTitleNodeValue.value;
      }

      showSelectedTitleNodeNoti.value = hasError.value;
      isTitleEditMode.value = hasError.value;
      break;

    case "desc":
      if (selectedDescNodeValue.value === "") {
        selectedNodeCategory.value.desc = "-";
      } else {
        selectedNodeCategory.value.desc = selectedDescNodeValue.value;
      }

      isDescEditMode.value = false;
      break;
  }

  if (!hasError.value) {
    _editCategory();
  }
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
      onConfirm() {
        resetDataModelList();
        closeCategoryChangeModal();
      },
      onClose() {
        closeCategoryChangeModal();
      },
    },
  });

const { open: openDataModelAddModal, close: closeDataModelAddModal } = useModal(
  {
    component: DataModelAddModal,
    attrs: {
      modalId: DATA_MODEL_ADD_MODAL,
      currentPageType: "category",
      onConfirm() {
        resetDataModelList();
        closeDataModelAddModal();
      },
      onClose() {
        getModelList();
        closeDataModelAddModal();
      },
    },
  },
);

const resetDataModelList = async () => {
  searchInputValue.value = "";
  selectedModelList.value = [];
  isShowPreview.value = false;
  await getModelList();
  setModelIdList();
};

const showCategoryAddModal = () => {
  categoriesParentId.value = defaultCategoriesParentId.value;
  categoriesId.value = "";
  openCategoryAddModal();
  resetAddModalStatus();
};

const showCategoryChangeModal = () => {
  if (selectedModelList.value.length === 0) {
    $alert("데이터모델을 선택해주세요", "warning");
    return;
  }
  openCategoryChangeModal();
};

const showDataModelAddModal = () => {
  openDataModelAddModal();
};

const isEditableNode = computed(() => {
  return undefinedTagIdManager.get() !== selectedNodeCategory.value.id;
});

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

const { scrollTrigger, setScrollOptions } = useIntersectionObserver({
  callback: addModelList,
});
</script>

<style scoped></style>
