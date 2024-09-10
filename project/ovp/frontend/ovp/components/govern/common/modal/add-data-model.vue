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
    :height="615"
    :top="240"
    :lockScroll="false"
    :confirm-btn-msg="'저장'"
    :is-disabled-confirm-btn="isDisabledConfirmBtn"
    swipeToClose="none"
    @before-open="beforeOpen"
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
          :inp-value="searchKeyword"
          :inp-id="'modelAddInp1'"
          :label-text="'데이터 모델 검색'"
          @on-input="inputSearchKeyword"
          @reset="resetSearchKeyword"
        ></search-input>
        <div class="filters">
          <data-filter :data="filters"></data-filter>
        </div>
        <Tab
          :data="tabList"
          label-key="labelWithCount"
          value-key="value"
          :current-item="currentTab"
          current-item-type="value"
          :use-tab-contents="false"
          @change="changeTab"
        >
        </Tab>
        <strong
          >선택 <em class="primary">{{ selectedDataModelCount }}개</em></strong
        >
        <div class="no-result" v-if="_.isEmpty(dataModelList)">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">정보가 없습니다.</p>
          </div>
        </div>
        <div
          v-show="!_.isEmpty(dataModelList)"
          class="table-scroll"
          id="dataListModal"
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
                      v-model="handleSelectAll"
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
              <tr v-for="(item, index) in dataModelList" :key="index">
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      :id="item.id"
                      :value="item.id"
                      class="checkbox-input"
                      v-model="
                        selectedDataModelListByTab[
                          currentTab as keyof typeof selectedDataModelListByTab
                        ]
                      "
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
                        @click="gotoModelDetail(item)"
                      >
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full gap-4">
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
            id="loader"
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
import { useRouter } from "nuxt/app";
import { useAddDataModel } from "~/store/governance/common/modal/add-data-model";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import Modal from "@extends/modal/Modal.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import DataFilter from "@/components/govern/common/data-filter.vue";
import Tab from "@extends/tab/Tab.vue";
import Loading from "@base/loading/Loading.vue";
import _ from "lodash";

const router = useRouter();

const addDataModelStore = useAddDataModel();
const {
  clearDataModelModalSettings,
  setSearchKeyword,
  getFilters,
  getDataModelList,
  addDataModelList,
  resetSelectedDataModelListByTab,
  addDataModel,
} = addDataModelStore;
const {
  searchKeyword,
  filters,
  tabList,
  currentTab,
  dataModelList,
  dataModelIdList,
  selectedDataModelListByTab,
} = storeToRefs(addDataModelStore);

const props = defineProps({
  currentPageType: {
    type: String,
    required: true,
  },
});
const emit = defineEmits<{
  (e: "close"): void;
  (e: "confirm"): void;
}>();

const beforeOpen = async () => {
  await getFilters();
};

const inputSearchKeyword = (keyword: string) => {
  setSearchKeyword(keyword);
  resetSelectedDataModelListByTab();
  getDataModelList();
};

const resetSearchKeyword = () => {
  setSearchKeyword("");
  getDataModelList();
};

const changeTab = (item: string) => {
  currentTab.value = item;
  getDataModelList();
};

const selectedDataModelCount = computed(() => {
  return Object.values(selectedDataModelListByTab.value).reduce(
    (acc, arr) => acc + arr.length,
    0,
  );
});

const handleSelectAll = computed({
  get() {
    const selectedDataModelList =
      selectedDataModelListByTab.value[
        currentTab.value as keyof typeof selectedDataModelListByTab.value
      ];
    return (
      !_.isEmpty(dataModelList.value) &&
      _.size(selectedDataModelList) === _.size(dataModelIdList.value)
    );
  },
  set(checked) {
    selectedDataModelListByTab.value[
      currentTab.value as keyof typeof selectedDataModelListByTab.value
    ] = checked ? dataModelIdList.value : [];
  },
});

const gotoModelDetail = (data: object) => {
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

const isDisabledConfirmBtn = computed(() => {
  return selectedDataModelCount.value === 0;
});

const onConfirm = async () => {
  await addDataModel({ currentPageType: props.currentPageType });
  setScrollOptions(0);

  emit("confirm");
};

const onCancel = () => {
  emit("close");
};

const { scrollTrigger, mount, setScrollOptions } = useIntersectionObserver(
  addDataModelList,
  "dataListModal",
);

// 모달의 설정 값 초기화 후 데이터 조회
clearDataModelModalSettings();
await getDataModelList();

const onOpened = async () => {
  // NOTE : modal 사용 방식 변경 후 부터, modal 은 intersectionHelper 의 onMounted 보다 dom 생성이 늦어
  // mount = intersectionObserver 의 생성 순서가 맞지 않아 동작하지 않는 오류가 발생하기 때문에,
  // onOpened 로 이벤트를 받고 dom 이 생성 완료 되면 mount 를 실행해서 intersection observer 를 동작시킨다.
  mount();
};
</script>

<style scoped></style>
