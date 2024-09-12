<template>
  <Modal
    title="데이터 모델 추가"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="1180"
    :height="560"
    :top="240"
    :lockScroll="false"
    :confirm-btn-msg="'저장'"
    :is-disabled-confirm-btn="isDisabledConfirmBtn"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
    @open="onOpened"
  >
    <template v-slot:body>
      <div class="data-info max-h-[460px]">
        <search-input
          class="search-input-lg"
          :is-search-input-default-type="false"
          :placeholder="'검색어를 입력하세요.'"
          :inp-value="addSearchInputValue"
          :inp-id="'modelAddInp1'"
          :label-text="'데이터 모델 검색'"
          @update:value="updateSearchInputValue"
          @on-input="onInput"
          @reset="getAllDataModelList"
        ></search-input>
        <div class="filters">
          <data-filter :data="filters"></data-filter>
        </div>
        <Tab
          :data="tabOptions"
          :label-key="'label'"
          :value-key="'value'"
          :current-item="initTab"
          :current-item-type="'value'"
          :use-tab-contents="false"
          @change="changeTab"
        >
        </Tab>
        <strong
          >선택
          <em class="primary">{{ selectedDataModelListLength }}개</em></strong
        >
        <div class="no-result" v-if="isSearchResultNoData">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">정보가 없습니다.</p>
          </div>
        </div>
        <div
          class="table-scroll"
          id="dataListModal"
          v-show="!isSearchResultNoData"
        >
          <table class="table-fixed">
            <colgroup>
              <col style="width: 42px" />
              <col />
            </colgroup>
            <thead>
              <tr>
                <th>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-all-select"
                      class="checkbox-input"
                      v-model="allDataModalList"
                    />
                    <label for="checkbox-all-select" class="checkbox-label">
                      <span class="hidden-text"> 전체 선택 </span>
                    </label>
                  </div>
                </th>
                <th class="text-center">데이터 모델 정보</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in searchResult" :key="index">
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      :id="item.id"
                      :value="item.id"
                      class="checkbox-input"
                      v-model="selectedDataModelList"
                    />
                    <label :for="item.id" class="checkbox-label">
                      <span class="hidden-text"> 첫번째 데이터 모델 </span>
                    </label>
                  </div>
                </td>
                <td>
                  <div class="table-data">
                    <div class="l-between">
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li
                            class="breadcrumb-item"
                            v-for="(breadItem, idx) in item.depth"
                            :key="idx"
                          >
                            <span class="breadcrumb-text">{{ breadItem }}</span>
                          </li>
                        </ul>
                      </div>
                      <button
                        class="button link-button-support button-sm"
                        @click="goToModelDetail(item)"
                      >
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full gap-4">
                      <!--          TODO: [개발] type 이 table / storage / model 로 오는데 어떤 이미지를 넣는지 확인 필요 -->
                      <div
                        class="type-img type-img-mysql"
                        :class="`type-img-${item.serviceIcon}`"
                      ></div>
                      <span class="table-data-title">{{ item.modelNm }}</span>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
          <Loading
            id="dataModelAddModalLoader"
            :use-loader-overlay="true"
            class="loader-lg is-loader-inner"
            style="display: none"
          ></Loading>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "nuxt/app";
import { useGovernCategoryStore } from "~/store/governance/Category/index";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { storeToRefs } from "pinia";
import Modal from "@extends/modal/Modal.vue";
import DataFilter from "./data-filter.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Tab from "@extends/tab/Tab.vue";
import Loading from "@base/loading/Loading.vue";

const router = useRouter();

const categoryStore = useGovernCategoryStore();
const {
  changeTab,
  setSearchKeyword,
  resetReloadList,
  addSearchList,
  getSearchList,
  getModelList,
  patchModelAddItemAPI,
  setModelIdList,
} = categoryStore;
const {
  initTab,
  filters,
  searchResult,
  searchResultLength,
  isSearchResultNoData,
  dataModelIdList,
  selectedDataModelList,
  addSearchInputValue,
  isShowPreview,
  searchInputValue,
} = storeToRefs(categoryStore);

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});
const emit = defineEmits<{
  (e: "close-data-model-add-modal"): void;
}>();

// SEARCH INPUT
const updateSearchInputValue = (newValue: string) => {
  addSearchInputValue.value = newValue;
};
const onInput = (value: string) => {
  setSearchKeyword(value);
  resetReloadList();
};

const getAllDataModelList = () => {
  setSearchKeyword("");
  resetReloadList();
};

// DATA MODEL
// checkbox
const selectedDataModelListLength = computed(() => {
  return selectedDataModelList.value.length;
});

const allDataModalList = computed({
  get() {
    return dataModelIdList.value.length === 0
      ? false
      : dataModelIdList.value.length === selectedDataModelListLength.value;
  },
  set(event) {
    if (event) {
      selectedDataModelList.value = dataModelIdList.value;
    } else {
      selectedDataModelList.value = [];
    }
  },
});

const goToModelDetail = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };

  const url = router.resolve({
    path: "/portal/search/detail",
    query: {
      type: type,
      id: id,
      fqn: fqn,
    },
  }).href;

  window.open(url, "_blank");
};

// TAB
const tabOptions = ref([
  {
    label: `테이블 (${searchResultLength.value.table})`,
    value: "table",
    type: "table",
  },
  {
    label: `스토리지 (${searchResultLength.value.storage})`,
    value: "storage",
    type: "storage",
  },
  {
    label: `융합모델 (${searchResultLength.value.model})`,
    value: "model",
    type: "model",
  },
]);

watchEffect(() => {
  tabOptions.value[0].label = `테이블 (${searchResultLength.value.table})`;
  tabOptions.value[1].label = `스토리지 (${searchResultLength.value.storage})`;
  tabOptions.value[2].label = `융합모델 (${searchResultLength.value.model})`;
});

// MODAL
const isDisabledConfirmBtn = computed(() => {
  return selectedDataModelList.value.length === 0;
});
const onCancel = () => {
  emit("close-data-model-add-modal");
};

const onConfirm = async () => {
  await patchModelAddItemAPI();
  setScrollOptions(0);
  searchInputValue.value = "";

  await getModelList();
  setModelIdList();

  isShowPreview.value = false;
  emit("close-data-model-add-modal");
};

await getSearchList();

const { scrollTrigger, mount, setScrollOptions } = useIntersectionObserver({
  callback: addSearchList,
  targetId: "dataListModal",
  loaderId: "dataModelAddModalLoader",
});

const onOpened = () => {
  // NOTE : modal 사용 방식 변경 후 부터, modal 은 intersectionHelper 의 onMounted 보다 dom 생성이 늦어
  // mount = intersectionObserver 의 생성 순서가 맞지 않아 동작하지 않는 오류가 발생하기 때문에,
  // onOpened 로 이벤트를 받고 dom 이 생성 완료 되면 mount 를 실행해서 intersection observer 를 동작시킨다.
  mount();
};
</script>

<style scoped></style>
