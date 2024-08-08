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
            @click="showModal = true"
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
            mode="edit"
            :items="categories"
            :isCheckable="false"
            :hideGuideLines="false"
            :firExpandAll="true"
            :show-open-all-btn="true"
            :show-close-all-btn="true"
            :use-draggable="true"
            :dropValidator="dropValidator"
            @onItemSelected="onNodeClicked"
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
          <!-- NOTE : v-if 로 할 경우, intersectionObserver 이 element 의 변화를 catch 하지 못해서 동작하지 않는 현상이 있어서 v-show 로 변환함. -->
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
                    @click="showModalChange = true"
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
              v-show="modelList.length > 0"
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
                @change="getPreviewCloseStatus"
              ></Preview>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 추가-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">카테고리 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModal = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModal = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 변경-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalChange">
    <div class="modal modal-padding-16" style="width: 350px; height: 450px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">카테고리 변경</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalChange = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <tree-vue
          :items="items"
          :isCheckable="false"
          :hideGuideLines="false"
          :firExpandAll="true"
          :show-open-all-btn="true"
          :show-close-all-btn="true"
          :use-draggable="true"
          mode="view"
          :dropValidator="dropValidator"
          @onItemSelected="onNodeClicked"
          @addSibling="addSibling"
          @addChild="addChild"
        />
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModalChange = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">선택</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import _ from "lodash";
import TreeVue from "@extends/tree/Tree.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Loading from "@base/loading/Loading.vue";
import Preview from "~/components/common/preview/preview.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

import { storeToRefs } from "pinia";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";

const categoryStore = useGovernCategoryStore();

const {
  getCategories,
  addModelList,
  getModelList,
  setSelectedNode,
  addCategory,
  editCategory,
  moveCategory,
  deleteCategory,
  setModelIdList,
  getPreviewData,
} = categoryStore;
const {
  categories,
  modelList,
  modelIdList,
  isCategoriesNoData,
  previewData,
  isBoxSelectedStyle,
} = storeToRefs(categoryStore);

const loader = ref<HTMLElement | null>(null);
const showModal = ref(false);
const showModalChange = ref(false);
const isDescEditMode = ref(false);
const isTitleEditMode = ref(false);
const isShowPreview = ref<boolean>(false);
let currentPreviewId: string | number = "";
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
const selectedModelList = ref([]);
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

const onNodeClicked = async (node: TreeViewItem) => {
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
  // 형제 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출 (newNode 에 id(uuid), parentId 포함되어있음)
  console.log(`형제노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};
const addChild = (newNode: TreeViewItem) => {
  // 자식 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출
  console.log(`자식노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};
// TODO : [개발] 카테고리 등록 예 (등록, 수정 같은 코드 사용합니다.)
const addNewCategory = (newNode: TreeViewItem) => {
  const addNodeParam: TreeViewItem = {
    id: "f6a91e15-18c1-4920-ab2b-dd20a68f75bc",
    parentId: selectedNode.value.id,
    name: "카테고리 01 - 01",
    desc: "카테고리 설명이여요",
    children: [],
  };
  addCategory(addNodeParam);
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
  deleteCategory(selectedNode.value.id);
  await onNodeClicked(categories.value[0]);
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
// 데이터 모델 리스트
const onInput = (value: string) => {
  setScrollOptions(0);
  getModelList(value);
};

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

onMounted(async () => {
  if (loader.value) {
    loader.value.style.display = "block";
  }

  // TODO: [개발] 페이지 진입 시 첫 번째 트리, 모델 리스트 설정 - 첫 번째 트리에 선택된 상태를 추가할 수 있는지 검토 필요
  await getCategories();

  if (categories.value && categories.value.length > 0) {
    await onNodeClicked(categories.value[0]);
  }

  if (loader.value) {
    loader.value.style.display = "none";
  }
  //   selectedNode
});
</script>

<style scoped></style>
