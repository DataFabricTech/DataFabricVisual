<template>
  <div class="section-top-bar">
   <filter-item :data="filters" @reset-filters="resetFilters" />
  </div>
  <div class="section-contents">
    <top-bar></top-bar>
    <div class="l-split mt-3">
      <div class="data-page" style="position: relative">
        <div class="data-list" v-if="viewType === 'listView'">
          <resource-box-list
            :data-list="searchResultData"
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
import TopBar from "./top-bar.vue";
import { useSearchCommonStore } from "@/store/search/common";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const searchCommonStore = useSearchCommonStore();
const { getSearchCommonData, getPreviewData } = searchCommonStore;
const {
  filters,
  searchResult,
  previewData,
  viewType,
  isShowPreview,
  isBoxSelectedStyle,
  searchResultLength,
} = storeToRefs(searchCommonStore);

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
};

const previewClick = async (id: string | number) => {
  console.log(`previewClick : ${id}`);
  await getPreviewData();
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
};

const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};

const resetFilters = () => {
  console.log("resetFilters");
};

// Intersection Observer
// 박스 아이템 디폴트 개수
const boxItemDefaultCount: number = 10;
// 데이터 변화 시 시작 값
let changingInitialCount: number = 0;
// 누적될 목록 데이터
const searchResultData = ref<Array<any>>([]);
// 관찰 대상
const scrollTrigger = ref<HTMLElement | null>(null);
let intersectionOb: any = ref("IntersectionObserver");

// 디폴트 개수에 맞춰 데이터 목록 계산 후 누적
const setSearchResultData = (count: number) => {
  const newItems = searchResult.value.data.slice(
    count,
    count + boxItemDefaultCount,
  );
  searchResultData.value.push(...newItems);
};

const setIntersectionObserver = () => {
  intersectionOb = new IntersectionObserver(
    (entries) => {
      // 2-1. 목록 개수가 boxItemDefaultCount(10개) 미만이면 return
      if (searchResult.value.data.length < boxItemDefaultCount) {
        console.log(searchResult.value.data.length);
        return;
      }

      const loader = document.getElementById("loader");

      // 2-2. 목록 개수가 boxItemDefaultCount(10개) 이상이면 실행
      entries.forEach((entry) => {
        // 3-1. scrollTrigger 가 뷰포트에 교차하는 시점(threshold)에 실행한다.
        if (entry.isIntersecting) {
          changingInitialCount += boxItemDefaultCount;
          if (loader) {
            loader.style.display = "flex";
          }

          setTimeout(() => {
            setSearchResultData(changingInitialCount);
            if (loader) {
              loader.style.display = "none";
            }
          }, 1000);
          // 3-2. scrollTrigger 가 뷰포트에 교차하지 않으면 실행한다.
        } else {
          if (loader) {
            loader.style.display = "none";
          }
        }
      });
    },
    {
      root: null,
      rootMargin: "0px",
      threshold: 0.2,
    },
  );

  // 1. 관찰할 요소 (scrollTrigger)가 존재하면, intersection Observer를 실행한다.
  if (scrollTrigger.value) {
    intersectionOb.observe(scrollTrigger.value);
  }
};

onMounted(async () => {
  await getSearchCommonData();
  searchResultLength.value = searchResult.value.data.length;

  setIntersectionObserver();
  setSearchResultData(changingInitialCount);
});

// 컴포넌트 파괴 시 관찰(observe) 중단
onBeforeUnmount(() => {
  if (intersectionOb && scrollTrigger.value) {
    intersectionOb.unobserve(scrollTrigger.value);
  }
});
</script>

<style lang="scss" scoped></style>
