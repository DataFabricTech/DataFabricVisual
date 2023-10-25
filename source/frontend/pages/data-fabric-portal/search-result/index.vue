<!-- 데이터패브릭포털 - 검색 -->
<template>
  <div class="l-common h-full justify-start overflow-hidden">
    <h2 class="hidden">데이터패브릭포털 검색</h2>
    <section class="l-section mt-[28px]">
      <h3 class="hidden">데이터 검색</h3>
      <div class="main-search">
      <SearchField class="search-field-lg"></SearchField>
        <div class="relate-keyword">
          <span class="relate-keyword-title">연관 검색어</span>
          <ul class="relate-keyword-list">
            <li class="relate-keyword-item">
              <baseButton class="button-normal">
                <span class="button-text">CCTV위치</span>
              </baseButton>
            </li>
            <li class="relate-keyword-item">
              <baseButton class="button-normal">
                <span class="button-text">행정동별 주정차단속구간</span>
              </baseButton>
            </li>
            <li class="relate-keyword-item">
              <baseButton class="button-normal">
                <span class="button-text">주정차단속민원</span>
              </baseButton>
            </li>
            <li class="relate-keyword-item">
              <baseButton class="button-normal">
                <span class="button-text">CCTV민원현황</span>
              </baseButton>
            </li>
          </ul>
        </div>
      </div>
    </section>
    <section class="l-section mt-[28px] h-full justify-start">
      <div class="portal-result">
        <div class="result-info justify-center">
          <h3 class="result-summary text-2xl">
            <em>“주정차CCTV 단속위치”</em> 검색결과는 <em>31</em>건 입니다.
          </h3>
        </div>
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
        <article class="portal-article portal-article-scroll">
        <div class="w-full">
          <div class="category-anchor" id="all">
            <a class="anchor" title="이동" href="#all">전체 (16)</a>
            <a class="anchor" title="이동" href="#data-model">데이터모델 (6)</a>
            <a class="anchor" title="이동" href="#repository">저장소 (5)</a>
            <a class="anchor" title="이동" href="#domain">도메인 (5)</a>
          </div>
          <div class="result-info">
            <strong class="result-summary-title">데이터 패브릭</strong>
            <strong class="result-summary">검색 결과는 <em>4</em>건 입니다.</strong>
          </div>
          <div class="category-container">
            <div class="category-wrapper">
              <div class="category-top-bar">
                <h4 class="category-name" id="data-model">데이터모델 (6)</h4>
                <baseButton class="button-primary">
                  <span class="button-text">서비스 이동</span>
                </baseButton>
              </div>
              <ul class="card-list">
                <li class="card-item" v-for="index in 3">
                  <Card class="card-primary">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </li>
              </ul>
              <BasePagination></BasePagination>
            </div>
            <div class="category-wrapper">
              <div class="category-top-bar">
                <h4 class="category-name" id="repository">저장소 (5)</h4>
                <baseButton class="button-primary">
                  <span class="button-text">서비스 이동</span>
                </baseButton>
              </div>
              <ul class="card-list">
                <li class="card-item" v-for="index in 3">
                  <Card class="card-primary">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </li>
              </ul>
              <BasePagination></BasePagination>
            </div>
          </div>
          <div class="result-more-button">
            <baseButton class="button-normal button-lg">
              <span class="button-text">검색결과 더보기</span>
            </baseButton>
          </div>
        </div>
        <div class="w-full">
          <div class="result-info">
            <strong class="result-summary-title">분석보고서</strong>
            <strong class="result-summary">검색 결과는 <em>25</em>건 입니다.</strong>
          </div>
          <div class="category-container">
            <div class="category-wrapper">
              <div class="category-top-bar">
                <h4 class="category-name">분석보고서 (25)</h4>
                <baseButton class="button-primary">
                  <span class="button-text">서비스 이동</span>
                </baseButton>
              </div>
              <ul class="card-list">
                <li class="card-item" v-for="index in 3">
                  <Card class="card-primary">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </li>
              </ul>
              <BasePagination></BasePagination>
              <div class="result-more-button">
                <baseButton class="button-normal button-lg">
                  <span class="button-text">검색결과 더보기</span>
                </baseButton>
              </div>
            </div>
          </div>

        </div>
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


