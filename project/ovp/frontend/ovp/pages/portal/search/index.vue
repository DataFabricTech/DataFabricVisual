<template>
  <div class="section-top-bar">
    <data-filter :data="filters"></data-filter>
  </div>
  <div class="section-contents">
    <top-bar></top-bar>
    <div class="l-split mt-3">
      <div class="data-page" style="position: relative">
        <div id="dataList" class="data-list" v-if="viewType === 'listView'">
          <resource-box-list
            :data-list="searchResult"
            :use-list-checkbox="false"
            :show-owner="true"
            :show-category="true"
            :is-box-selected-style="isBoxSelectedStyle"
            @previewClick="previewClick"
            @modelNmClick="modelNmClick"
          />
          <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
          <!--                TODO: [퍼블리싱] loader UI 컴포넌트 추가 및 로딩 위치 검토 필요 -->
          <div
            id="loader"
            style="
              display: none;
              position: fixed;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background-color: rgba(255, 255, 255, 0.5);
              align-items: center;
              justify-content: center;
              font-size: 20px;
              color: #333;
            "
          >
            loader
          </div>
        </div>
        <div class="data-list" v-if="viewType === 'graphView'">
          <custom-knowledge-graph />
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
import { onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useSearchCommonStore } from "@/store/search/common";
import { IntersectionObserverHandler } from "@/utils/intersection-observer";

import TopBar from "./top-bar.vue";

const searchCommonStore = useSearchCommonStore();
const { getSearchList, getFilters, getPreviewData, setScrollFrom } =
  searchCommonStore;
const {
  from,
  size,
  filters,
  searchResult,
  previewData,
  viewType,
  isShowPreview,
  isBoxSelectedStyle,
} = storeToRefs(searchCommonStore);

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

let currentPreviewId: string | number = "";

const previewClick = async (id: string | number) => {
  if (id === currentPreviewId) {
    return;
  }

  await getPreviewData();
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
  currentPreviewId = id;
};

const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};

// TODO: intersection observer 옵션이 부족해 보임. 데이터가 1000가 넘으면 UI가 버벅거림.

// intersection observer 타겟
const targetId = "dataList";
// 스크롤 트리거
const scrollTrigger = ref<HTMLElement | null>(null);
// 로딩 스피너 아이디
const loaderId = "loader";
// intersection observer instance
let intersectionHandler: IntersectionObserverHandler | null = null;
// 스크롤 이동시 데이터 로딩 시점에 실행되는 callback
const getDataCallback = async (count: number, loader: HTMLElement | null) => {
  // loader start
  if (loader) {
    loader.style.display = "flex";
  }

  // 데이터 조회
  // 조회 쿼리는 store 에서 처리.
  setScrollFrom(count);
  await getSearchList();

  if (loader) {
    loader.style.display = "none";
  }
};

onMounted(async () => {
  await getFilters();

  // intersection observer instance 생성
  intersectionHandler = new IntersectionObserverHandler(
    targetId,
    scrollTrigger.value,
    loaderId,
    from.value,
    size.value,
    getDataCallback,
  );
});

onBeforeUnmount(() => {
  if (intersectionHandler) {
    intersectionHandler.disconnect();
  }
});
</script>

<style lang="scss" scoped></style>
