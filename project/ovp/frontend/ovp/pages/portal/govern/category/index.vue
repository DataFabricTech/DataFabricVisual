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
        <div class="p-3" v-else>
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
                placeholder="모델 설명에 대한 영역입니다."
                required
                id="title-modify"
                class="text-input w-1/2"
              />
            </template>
            <template #view-slot>
              <h3 class="editable-group-title">{{ selectedNode.name }}</h3>
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
              <p class="editable-group-desc">{{ selectedNode.desc }}</p>
            </template>
          </editable-group>
          <div>
            <div class="l-top-bar">
              <search-input
                class="w-[541px]"
                :is-search-input-default-type="false"
                :placeholder="'검색어를 입력하세요.'"
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
                <div class="h-group ml-auto gap-2">
                  <button
                    class="button button-secondary-stroke"
                    @click="showCategoryChangeModal"
                  >
                    카테고리 변경
                  </button>
                  <button class="button button-secondary">
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
  <CategoryAddModal :modal-id="CATEGORY_ADD_MODAL_ID"></CategoryAddModal>
  <CategoryChangeModal
    :modal-id="CATEGORY_CHANGE_MODAL_ID"
  ></CategoryChangeModal>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { storeToRefs } from "pinia";
import { useNuxtApp } from "nuxt/app";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import TreeVue from "@extends/tree/Tree.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Loading from "@base/loading/Loading.vue";
import Preview from "~/components/common/preview/preview.vue";
import CategoryAddModal from "~/components/govern/category/category-add-modal.vue";
import CategoryChangeModal from "~/components/govern/category/category-change-modal.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const { $vfm } = useNuxtApp();
const categoryStore = useGovernCategoryStore();

const {
  getCategories,
  addModelList,
  addNewCategory,
  getModelList,
  editCategory,
  deleteCategory,
  getPreviewData,
  moveCategory,
  resetAddModalStatus,
  setSelectedNode,
} = categoryStore;
const {
  selectedModelList,
  categories,
  modelList,
  isCategoriesNoData,
  previewData,
  isBoxSelectedStyle,
} = storeToRefs(categoryStore);

const CATEGORY_ADD_MODAL_ID = "category-add-modal";
const CATEGORY_CHANGE_MODAL_ID = "category-change-modal";

const loader = ref<HTMLElement | null>(null);
const modelIdList = ref([]);
const isDescEditMode = ref(false);
const isTitleEditMode = ref(false);
const isShowPreview = ref<boolean>(false);

let currentPreviewId: string | number = "";
let previewIndex: string = "table";

const selectedNode: Ref<TreeViewItem> = ref<TreeViewItem>({
  id: "",
  name: "",
  desc: "",
  order: 0,
  parentId: "",
  tagId: "",
  expanded: false,
  selected: false,
  disabled: false,
  children: [],
});
const isAllModelListChecked = ref<boolean>(false);
const selectedTitleNodeValue = ref(selectedNode.value.name || "");
const selectedDescNodeValue = ref(selectedNode.value.desc || "");

watch(
  () => selectedNode.value.name,
  (newVal) => {
    selectedTitleNodeValue.value = newVal || "";
  },
  { immediate: true },
);

watch(
  () => selectedNode.value.desc,
  (newVal) => {
    selectedDescNodeValue.value = newVal || "";
  },
  { immediate: true },
);

// tree
const onCategoryNodeClick = async (node: TreeViewItem) => {
  selectedModelList.value = [];
  isDescEditMode.value = false;
  isTitleEditMode.value = false;

  selectedNode.value = node;

  setScrollOptions(0);
  // 선택한 노드정보 저장
  setSelectedNode(node);
  // 선택한 노드 기준 모델 목록을 조회
  await getModelList();
  // 모든 모델 리스트 id 저장
  setModelIdList();
};

const addSibling = (newNode: TreeViewItem) => {
  // TODO : modal 창 띄워서 노드 추가 API  호출 (newNode 에 id(uuid), parentId 포함되어있음)
  console.debug(`형제노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};

const addChild = (newNode: TreeViewItem) => {
  // TODO : modal 창 띄워서 노드 추가 API  호출
  console.debug(`자식노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};

const _editCategory = () => {
  const editNodeParam: TreeViewItem = {
    id: selectedNode.value.id,
    parentId: selectedNode.value.parentId,
    tagId: selectedNode.value.tagId,
    name: selectedNode.value.name,
    desc: selectedNode.value.desc,
    children: [],
  };
  editCategory(editNodeParam);
};

const _deleteCategory = async () => {
  if (confirm("카테고리를 삭제 하시겠습니까?")) {
    const res = await deleteCategory(selectedNode.value.id);
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

// dataModel list
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

const onInput = (value: string) => {
  setScrollOptions(0);
  getModelList(value);
};

const checked = (checkedList: any[]) => {
  selectedModelList.value = checkedList;
};

const { scrollTrigger, setScrollOptions } =
  useIntersectionObserver(addModelList);

// preview
const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

const previewClick = async (data: object) => {
  const { id, fqn } = data as { id: string; fqn: string };
  if (id === currentPreviewId) {
    return;
  }

  await getPreviewData(fqn);
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
  currentPreviewId = id;
  previewIndex = type;
};

// editable-input
const editCancel = (key: string) => {
  switch (key) {
    case "title":
      selectedTitleNodeValue.value = selectedNode.value.name;
      isTitleEditMode.value = false;
      break;
    case "desc":
      selectedDescNodeValue.value = selectedNode.value.desc;
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
      selectedNode.value.name = selectedTitleNodeValue.value;
      isTitleEditMode.value = false;
      break;
    case "desc":
      if (selectedDescNodeValue.value === "") {
        selectedNode.value.desc = "설명 없음";
        return;
      }
      selectedNode.value.desc = selectedDescNodeValue.value;
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

// modal
const showCategoryAddModal = () => {
  $vfm.open(CATEGORY_ADD_MODAL_ID);
  resetAddModalStatus();
};

const showCategoryChangeModal = () => {
  $vfm.open(CATEGORY_CHANGE_MODAL_ID);
};

onMounted(async () => {
  if (loader.value) {
    loader.value.style.display = "block";
  }

  // TODO: [개발] 페이지 진입 시 첫 번째 트리, 모델 리스트 설정 - 첫 번째 트리에 선택된 상태를 추가할 수 있는지 검토 필요
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
