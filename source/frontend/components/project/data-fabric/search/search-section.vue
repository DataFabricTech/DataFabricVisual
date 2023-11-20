<template>
  <section class="l-section gap-[20px]">
    <h3 class="hidden-text">검색</h3>
    <div class="data-search" v-if="state.closeSearchField">
      <SearchField ></SearchField>
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
      @close="isDetailForm"></SearchFilter>
  </section>
</template>
<script lang="ts" setup>

const state = reactive({
  closeSearchField: true,
  isDetailFilter: null,
});

// 상세검색 아이콘 클릭시 실행되는 함수
function viewDetailSearch() {
  state.closeSearchField = false;
  state.isDetailFilter = true;
}

// 상세입력 form닫기 클릭시 실행되는 함수
function isDetailForm(val : Boolean) {
  state.closeSearchField = !val;
  state.isDetailFilter = val;
}
</script>
