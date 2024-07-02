<template>
  <!--  상단 필터 & 초기화-->
  <div class="filters">
    <!--  template v-for -->
    <filter-item :data="filters" @reset-filters="resetFilters" />
  </div>
  <div class="section-contents">
    <!--  상단 검색 결과 & 우측 필터-->
    <div class="l-top-bar">
      <strong
        >총 <em class="primary">68건</em>의 검색 결과가 있습니다.</strong
      >
      <div class="h-group gap-1">
        <div class="select select-clean">
          <button class="select-button">
            <span class="select-button-title">인기많은순 ↓</span>
            <svg-icon
              class="svg-icon select-indicator"
              name="chevron-down-medium"
            ></svg-icon>
          </button>
        </div>
        <div class="select select-clean">
          <button class="select-button">
            <span class="select-button-title">10개씩 보기</span>
            <svg-icon
              class="svg-icon select-indicator"
              name="chevron-down-medium"
            ></svg-icon>
          </button>
        </div>
        <div class="button-group">
          <input
            type="radio"
            id="button-groupprimary"
            class="button-group-input"
            name="button-group3"
            checked
          />
          <label for="button-groupprimary" class="button-group-label">
            <svg-icon class="svg-icon" name="list"></svg-icon>
            <span class="hidden-text">리스트보기</span>
          </label>
          <input
            type="radio"
            id="button-groupprimary2"
            class="button-group-input"
            name="button-group3"
          />
          <label for="button-groupprimary2" class="button-group-label">
            <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
            <span class="hidden-text">시각화 보기</span>
          </label>
        </div>
      </div>
    </div>
    <div class="l-split">
      <div class="data-page">
        <div class="data-list">
          <!--  목록형 보기-->
          <resource-box-list
            :data-list="searchResult.data"
            :use-list-checkbox="false"
            :show-owner="true"
            :show-category="true"
            @previewClick="previewClick"
            @modelNmClick="modelNmClick"
          />
          <!--  그래프혇 보기 - resource-box-list 안에 resource-box가 존재-->
          <custom-knowledge-graph />
          <Pagination
            :totalCount="60"
            :perPage="10"
            :currentPageNumber="4"
            @change="checkCurrentPage"
          ></Pagination>
        </div>
      </div>
      <!--  우측 미리보기-->
      <preview
        :isShowPreview="false"
        :preview-data="previewData"
        @change="getPreviewCloseStatus"
      ></preview>
    </div>
  </div>
</template>

<script setup lang="ts">
import Pagination from "@extends/pagination/Pagination.vue";
import { useSearchCommonStore } from "@/store/search/common";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const searchCommonStore = useSearchCommonStore();
const { getSearchCommonData, getPreviewData } = searchCommonStore;
const { filters, searchResult, previewData } = storeToRefs(searchCommonStore);

onMounted(async () => {
  // 목록정보 loading
  await getSearchCommonData();
});

// 미리보기의 닫기 버튼 클릭했을 때, 좌측 리소스박스 선택된 상태를 비활성화 시켜야해서 emit 추가 (option 불필요 할수도 있음)
const getPreviewCloseStatus = (option: boolean) => {
  console.log("isPreviewClosed?", option);
};

const checkCurrentPage = (item: number) => {
  console.log("check", item);
};
const previewClick = async (id: string | number) => {
  console.log(`previewClick : ${id}`);
  await getPreviewData();
};
const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};
const resetFilters = () => {
  console.log("resetFilters");
};
</script>

<style lang="scss" scoped></style>
