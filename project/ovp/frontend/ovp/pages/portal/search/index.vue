<template>
  <div class="section-top-bar">
    <data-filter :data="filters" @reset-filters="resetFilters"></data-filter>
  </div>
  <div class="section-contents">
    <top-bar></top-bar>
    <div class="l-split mt-3">
      <div class="data-page" style="position: relative">
        <div id="dataList" class="data-list" v-if="viewType === 'listView'">
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
import { onMounted, onBeforeMount } from "vue";
import _ from "lodash";
import { storeToRefs } from "pinia";
import { useSearchCommonStore } from "@/store/search/common";
import { IntersectionObserverHandler } from "@/utils/intersection-observer";

import TopBar from "./top-bar.vue";

const searchCommonStore = useSearchCommonStore();
const { getSearchCommonData, getPreviewData } = searchCommonStore;
const {
  filters,
  // searchResult,
  previewData,
  viewType,
  isShowPreview,
  isBoxSelectedStyle,
  searchResultLength,
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

const resetFilters = () => {
  console.log("resetFilters");
};

// TODO: 데이터 테스트 임시 코드, 나중에 제거
export interface searchResult {
  data: any[];
  totalCount: number;
}
const searchResult: Ref<searchResult> = ref({} as searchResult);

// TODO: intersection observer 옵션이 부족해 보임. 데이터가 1000가 넘으면 UI가 버벅거림.

// intersection observer 타겟
const targetId = "dataList";
// 스크롤 트리거
const scrollTrigger = ref<HTMLElement | null>(null);
// 로딩 스피너 아이디
const loaderId = "loader";
// 박스 아이템 디폴트 개수
const boxItemDefaultCount: number = 100;
// 데이터 변화 시 시작 값
let changingInitialCount: number = 0;
// 누적될 목록 데이터
const searchResultData = ref<Array<any>>([]);
// intersection observer instance
let intersectionHandler: IntersectionObserverHandler | null = null;
// 스크롤 이동시 데이터 로딩 시점에 실행되는 callback
const getDataCallback = (count: number, loader: HTMLElement | null) => {
  // loader start
  if (loader) {
    loader.style.display = "flex";
  }
  // TODO: 데이터 로딩 부분은 임시 코드, 나중에 setTimeout 제거, store에서 async await로 데이터를 가져와야 함
  setTimeout(() => {
    // 데이터 추가
    setSearchResultData(count);
    // loader stop
    if (loader) {
      loader.style.display = "none";
    }
  }, 300);
};
// TODO: 데이터 로딩 부분은 임시 코드, 나중에 제거
const setSearchResultData = (count: number) => {
  const newItems = searchResult.value.data.slice(
    count,
    count + boxItemDefaultCount,
  );
  searchResultData.value.push(...newItems);
};

onBeforeMount(async () => {
  // TODO: 데이터 테스트 임시 코드, 나중에 제거
  const array = Array.from({ length: 100000 }, (_, i) => i + 1);
  let data = {
    totalCount: 100000,
    data: _.map(array, (value) => {
      return {
        id: value + "",
        serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
        depth: ["1depth", "2depth", "3depth", "데이터모델"],
        firModelNm: "최초 데이터모델 명",
        modelNm: "세종특별자치시 상하수도요금표" + value,
        modelDesc:
          "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
        owner: "Owner",
        category: "Domain",
      };
    }),
  };
  searchResult.value = data;
});

onMounted(async () => {
  await getSearchCommonData();
  // TODO: searchResultLength 는 store 에서 데이터를 요청해서 응답했을 때 store에서 셋팅해야 함.
  searchResultLength.value = searchResult.value.data.length;

  // intersection observer instance 생성
  intersectionHandler = new IntersectionObserverHandler(
    targetId,
    scrollTrigger.value,
    loaderId,
    changingInitialCount,
    boxItemDefaultCount,
    getDataCallback,
  );

  // TODO: 데이터 테스트 임시 코드, store 연결 후에 필요 없음.
  setSearchResultData(changingInitialCount);
});

onBeforeUnmount(() => {
  if (intersectionHandler) {
    intersectionHandler.disconnect();
  }
});
</script>

<style lang="scss" scoped></style>
