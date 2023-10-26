<template>
  <div class="l-common">
    <h2 class="hidden">데이터패브릭 검색</h2>
    <section class="l-section gap-[20px]">
      <h3 class="hidden">데이터 검색</h3>
      <div class="data-search">
        <SearchField></SearchField>
        <BaseButton class="button-icon button-normal" title="상세 검색" @click="state.isOpen = !state.isOpen">
          <span class="hidden">상세 검색</span>
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
      <SearchFilter v-if="state.isOpen"></SearchFilter>
    </section>
    <section class="l-section gap-[28px]">
      <div class="anchor" id="all">
        <a class="anchor-link" title="이동" href="#all">전체 (16)</a>
        <a class="anchor-link" title="이동" href="#data-model">데이터모델 (6)</a>
        <a class="anchor-link" title="이동" href="#repository">저장소 (5)</a>
        <a class="anchor-link" title="이동" href="#domain">도메인 (5)</a>
      </div>
      <div class="result-info">
        <h3 class="result-summary-title">"<em class="color-tertiary">키워드</em>"검색 결과는 <em class="color-tertiary">4</em>건 입니다.</h3>
        <div class="result-sort">
          <div class="toggle toggle-icon toggle-lg">
            <BaseRadio name="rI" id="ic-01">
              <span class="hidden">리스트형 보기</span>
              <svg-icon name="list-view" class="svg-icon"></svg-icon>
            </BaseRadio>
            <BaseRadio name="rI" id="ic-03">
              <span class="hidden">썸네일형 보기</span>
              <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
            </BaseRadio>
          </div>
          <BaseSelect class="select-lg"></BaseSelect>
          <BaseSelect class="select-lg"></BaseSelect>
        </div>
      </div>
      <h4 class="category-name">데이터모델 (6)</h4>
      <ul class="card-list">
        <li class="card-item">
          <Card>
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-primary">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-primary">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-negative">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
      </ul>
      <BasePagination></BasePagination>
      <h4 class="category-name">데이터모델 (6)</h4>
      <div class="no-result">
        <storng class="no-result-title">검색 결과가 없습니다.</storng>
      </div>
      <!--      ...-->
    </section>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout"
});

const state = reactive({
  isOpen: true
});
</script>
