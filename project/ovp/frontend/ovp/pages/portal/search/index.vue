<template>
  <div class="section-top-bar">
    <data-filter :data="filters"></data-filter>
  </div>
  <div class="section-top-bar section-top-bar-tab">
    <Tab
      class="tab-line"
      :data="tabOptions"
      :label-key="'label'"
      :value-key="'value'"
      :current-item="initTab"
      :current-item-type="'value'"
      :use-tab-contents="false"
      @change="changeTab"
    >
    </Tab>
  </div>
  <div class="section-contents overflow-auto">
    <top-bar></top-bar>
    <div class="l-split mt-3">
      <div class="data-page" style="position: relative">
        <div
          id="dataList"
          class="data-list"
          v-show="viewType === 'listView' && !isSearchResultNoData"
        >
          <resource-box-list
            :data-list="searchResult"
            :use-list-checkbox="false"
            :show-owner="true"
            :show-category="true"
            :is-box-selected-style="isBoxSelectedStyle"
            @previewClick="previewClick"
            @modelNmClick="modelNmClick"
          />
          <!-- NOTE "scrollTrigger" -> useIntersectionObserver 가 return 하는 변수병과 동일해야함. -->
          <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
          <Loading
            id="loader"
            :use-loader-overlay="true"
            class="loader-lg is-loader-inner"
            style="display: none"
          ></Loading>
        </div>
        <div
          class="data-list"
          v-if="viewType === 'graphView' && !isSearchResultNoData"
        >
          <custom-knowledge-graph />
        </div>
        <div class="no-result" v-if="isSearchResultNoData">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
          </div>
        </div>
      </div>
      <preview
        v-if="viewType === 'listView'"
        :isShowPreview="isShowPreview"
        :preview-data="previewData"
        @change="getPreviewCloseStatus"
      ></preview>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useSearchCommonStore } from "@/store/search/common";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import Loading from "@base/loading/Loading.vue";
import Tab from "@extends/tab/Tab.vue";

import TopBar from "./top-bar.vue";
import { useRouter } from "nuxt/app";

const router = useRouter();

const searchCommonStore = useSearchCommonStore();
const { addSearchList, getFilters, getPreviewData, changeTab } =
  searchCommonStore;
const {
  filters,
  searchResult,
  previewData,
  viewType,
  isShowPreview,
  isBoxSelectedStyle,
  isSearchResultNoData,
} = storeToRefs(searchCommonStore);

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

let currentPreviewId: string | number = "";

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

const initTab: string = "table";

const tabOptions = [
  { label: "테이블", value: "table", type: "table" },
  { label: "스토리지", value: "storage", type: "storage" },
  { label: "융합모델", value: "model", type: "model" },
];

// top-bar 에서 select box (sort) 값이 변경되면 목록을 조회하라는 코드가 구현되어 있는데,
// select box 가 화면 맨 처음에 뿌릴때 값을 초기에 1번 셋팅하는 부분에서 목록 조회가 이뤄짐.
// 중복 호출을 피하기 위해서 여기서는 목록 데이터를 조회하지 않음.
// await getSearchList();

await getFilters();

const { scrollTrigger } = useIntersectionObserver(addSearchList);
</script>

<style lang="scss" scoped></style>
