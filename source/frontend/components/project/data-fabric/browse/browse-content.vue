<template>
  <h3 class="hidden-text">데이터모델 브라우저 검색목록</h3>
  <article class="page-article items-start">
    <h4 class="hidden-text">검색</h4>
    <div class="data-search">
      <SearchField></SearchField>
      <BaseButton class="button-icon button-normal" title="상세 검색" @click="state.isOpen = !state.isOpen">
        <span class="hidden-text">상세 검색</span>
        <svg-icon name="sort" class="svg-icon"></svg-icon>
      </BaseButton>
    </div>
    <SearchFilter v-if="state.isOpen"></SearchFilter>
  </article>

  <article class="page-article">
    <div class="result-info">
      <h4 class="result-summary-title">
        "<em class="color-tertiary">키워드</em>"검색 결과는 <em class="color-tertiary">4</em>건 입니다.
      </h4>
      <div class="result-sort">
        <BaseSelect class="select-lg" :data="$constants.FILTER.SORT"></BaseSelect>
        <BaseSelect class="select-lg" :data="$constants.FILTER.PAGE"></BaseSelect>
      </div>
    </div>
    <ul class="card-list">
      <li class="card-item" v-for="index in 10">
        <Card></Card>
      </li>
    </ul>
    <BasePagination></BasePagination>
  </article>
</template>

<script lang="ts" setup>
import $constants from "/utils/constants";

const state = reactive({
  isOpen: true
});
</script>
