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
import { useRouter } from "nuxt/app";

const router = useRouter();

const searchCommonStore = useSearchCommonStore();
const {
  addSearchList,
  getFilters,
  getPreviewData,
  setScrollFrom,
  setIntersectionHandler,
  updateIntersectionHandler,
} = searchCommonStore;
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
  const { id, fqn } = data as { id: string; fqn: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      id: id,
      fqn: fqn,
    },
  });
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

  setScrollFrom(count);
  updateIntersectionHandler(count);

  // 데이터 조회 : 쿼리는 store 에서 처리.
  await addSearchList();

  if (loader) {
    loader.style.display = "none";
  }
};

onMounted(async () => {
  // top-bar 에서 select box (sort) 값이 변경되면 목록을 조회하라는 코드가 구현되어 있는데,
  // select box 가 화면 맨 처음에 뿌릴때 값을 초기에 1번 셋팅하는 부분에서 목록 조회가 이뤄짐.
  // 중복 호출을 피하기 위해서 여기서는 목록 데이터를 조회하지 않음.
  // await getSearchList();

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

  // from 값 변경에 따른 동작을 store 에서 하고 있기 때문에 intersectionHandler 변수를 store 에 저장해둔다.
  setIntersectionHandler(intersectionHandler);
});

onBeforeUnmount(() => {
  if (intersectionHandler) {
    intersectionHandler.disconnect();
  }
});
</script>

<style lang="scss" scoped></style>
