<template>
  <section class="l-section gap-[20px]">
    <h3 class="hidden-text">검색</h3>
    <div class="data-search" v-if="state.closeSearchField">
      <SearchField :keyword="keyword"></SearchField>
      <BaseButton class="button-icon button-normal" title="상세 검색" @click="viewDetailSearch()">
        <span class="hidden-text">상세 검색</span>
        <svg-icon name="sort" class="svg-icon"></svg-icon>
      </BaseButton>
      <div class="search-word ml-4">
        <span class="search-word-title">연관 검색어</span>
        <div class="h-group">
          <baseButton class="button-normal">
            <span class="button-text">CCTV위치</span>
          </baseButton>
          <baseButton class="button-normal">
            <span class="button-text">행정동별 주정차단속구간</span>
          </baseButton>
          <baseButton class="button-normal">
            <span class="button-text">주정차단속민원</span>
          </baseButton>
          <baseButton class="button-normal">
            <span class="button-text">CCTV민원현황</span>
          </baseButton>
        </div>
      </div>
    </div>
    <SearchFilter
      v-if="state.isDetailFilter"
      @close="isDetailForm"
      :keyword-data="KEYWORD_TYPE"
      :date-data="DATE_TYPE"
      :connection-data="CONNECTOR_NAME"
      :model-type-data="STORAGE_TYPE"
      :model-form-data="DATA_TYPE"
      :model-format-data="DATA_FORMAT"
      ></SearchFilter>
  </section>
</template>
<script lang="ts" setup>
import { storeToRefs } from "pinia";
import { fabricSearchStore } from "/store/data-fabric/search/search";

const store = fabricSearchStore();
const { searchItems } = store;
let { } = storeToRefs(store);

const state = reactive({
  closeSearchField: true,
  isDetailFilter: null,
});

// 상세검색 아이콘 클릭시 실행되는 함수
async function viewDetailSearch() {
  state.closeSearchField = false;
  state.isDetailFilter = true;
}

// 검색어 select 목록
let KEYWORD_TYPE= reactive({});

// 기간 선택 select 목록
let DATE_TYPE= reactive({});

// 연결정보 select 목록
let CONNECTOR_NAME= reactive({});

// 데이터 모델 유형 select 목록
let STORAGE_TYPE= reactive({});

// 데이터 모델 형식 select 목록
let DATA_TYPE= reactive({});

// 데이터 모델 포맷 select 목록
let DATA_FORMAT= reactive({});

onMounted(()=> {
  searchItems()
    .then((res: any) => {
      KEYWORD_TYPE = res.KEYWORD_TYPE.slice();
      DATE_TYPE = res.DATE_TYPE.slice();
      CONNECTOR_NAME = res.CONNECTOR_NAME.slice();
      STORAGE_TYPE = res.STORAGE_TYPE.slice();
      DATA_TYPE = res.DATA_TYPE.slice();
      DATA_FORMAT = res.DATA_FORMAT.slice();
    })
});

// 상세입력 form닫기 클릭시 실행되는 함수
function isDetailForm(val : Boolean) {
  state.closeSearchField = !val;
  state.isDetailFilter = val;
}

//TODO : 추후 페이지 전환을 위한 입력값 받기
// onMounted(() => {
//   const keyValue = useRoute().query;
//   keyword.value = keyValue["keyword"];
// })
</script>
