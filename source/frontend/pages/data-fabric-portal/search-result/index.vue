<!-- 데이터패브릭포털 - 검색 -->
<template>
  <div class="l-common">
    <h2 class="hidden">데이터패브릭포털 검색</h2>
    <section class="l-section">
      <h3 class="hidden">데이터 검색</h3>
      <div class="main-search min-h-[240px]">
        <SearchField class="search-field-lg"></SearchField>
        <div class="search-word">
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
    </section>
    <section class="l-section gap-[28px]">
      <h3 class="result-title">
        <em class="color-tertiary">“주정차CCTV 단속위치”</em> 검색결과는 <em class="color-tertiary">31</em>건 입니다.
      </h3>
      <div class="flex justify-center w-full">
        <div class="round-tab">
          <ul class="round-tab-list">
          <li class="round-tab-item">
            <button type="button" class="round-tab-button is-selected">
              <svg-icon name="list-view" class="svg-icon" />
              <span class="round-tab-title">전체</span>
            </button>
          </li>
          <li class="round-tab-item">
            <button type="button" class="round-tab-button">
              <svg-icon name="mega-menu" class="svg-icon" />
              <span class="round-tab-title">데이터패브릭</span>
            </button>
          </li>
          <li class="round-tab-item">
            <button type="button" class="round-tab-button">
              <svg-icon name="chart-multitype" class="svg-icon" />
              <span class="round-tab-title">분석보고서</span>
            </button>
          </li>
          <li class="round-tab-item">
            <button type="button" class="round-tab-button">
              <svg-icon name="report-tag" class="svg-icon" />
              <span class="round-tab-title">서비스</span>
            </button>
          </li>
          <li class="round-tab-item">
            <button type="button" class="round-tab-button">
              <svg-icon name="bookmark" class="svg-icon" />
              <span class="round-tab-title">태그</span>
            </button>
          </li>
        </ul>
        </div>
      </div>
      <div class="v-group gap-[36px] px-4 w-full">
        <article class="category-article">
          <div class="anchor" id="all">
            <a class="anchor-link" title="이동" href="#all">전체 (16)</a>
            <a class="anchor-link" title="이동" href="#data-model">데이터모델 (6)</a>
            <a class="anchor-link" title="이동" href="#repository">저장소 (5)</a>
            <a class="anchor-link" title="이동" href="#domain">도메인 (5)</a>
          </div>
          <div class="result-info">
            <div class="h-group gap-[20px]">
              <strong class="result-summary-title">데이터 패브릭</strong>
              <strong class="result-summary">검색 결과는 <em class="color-tertiary">4</em>건 입니다.</strong>
            </div>
            <div class="result-sort">
              <BaseSelect class="select-lg"></BaseSelect>
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </div>
          <div class="category-container">
            <div class="v-group gap-[16px]">
              <div class="category-top-bar">
                <h4 class="category-name" id="data-model">데이터모델 (6)</h4>
                <baseButton class="button-primary">
                  <span class="button-text">서비스 이동</span>
                </baseButton>
              </div>
              <ul class="card-list">
                <li class="card-item" v-for="index in 3">
                  <Card></Card>
                </li>
              </ul>
              <BasePagination></BasePagination>
            </div>
            <div class="v-group gap-[16px]">
              <div class="category-top-bar">
                <h4 class="category-name" id="repository">저장소 (5)</h4>
                <baseButton class="button-primary">
                  <span class="button-text">서비스 이동</span>
                </baseButton>
              </div>
              <ul class="card-list">
                <li class="card-item" v-for="index in 3">
                  <Card></Card>
                </li>
              </ul>
              <BasePagination></BasePagination>
            </div>
          </div>
          <baseButton class="button-normal button-lg result-more-button">
            <span class="button-text">검색결과 더보기</span>
          </baseButton>
        </article>
        <article class="category-article">
          <div class="result-info">
            <div class="h-group gap-[20px]">
              <strong class="result-summary-title">데이터 패브릭</strong>
              <strong class="result-summary">검색 결과는 <em class="color-tertiary">4</em>건 입니다.</strong>
            </div>
            <div class="h-group">
              <baseSelect></baseSelect>
              <baseSelect></baseSelect>
            </div>
          </div>
        <div class="category-container">
          <div class="v-group gap-[16px]">
            <div class="category-top-bar">
              <h4 class="category-name">분석보고서 (25)</h4>
              <baseButton class="button-primary">
                <span class="button-text">서비스 이동</span>
              </baseButton>
            </div>
            <ul class="card-list">
              <li class="card-item" v-for="index in 3">
                <Card></Card>
              </li>
            </ul>
            <BasePagination></BasePagination>

          </div>
        </div>
        <baseButton class="button-normal button-lg result-more-button w-[300px] rounded-[35px]">
          <span class="button-text">검색결과 더보기</span>
        </baseButton>
      </article>
      </div>
    </section>
  </div>
</template>
<script lang="ts" setup>

definePageMeta({
  layout: "default-portal-layout",
});

</script>


