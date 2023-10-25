<!-- 데이터패브릭 - 검색 -->
<template>
  <div class="l-common flex-grow">
    <h2 class="hidden">데이터패브릭 검색</h2>
    <section class="l-section flex-grow">
      <div class="main-search">
        <SearchField class="search-field-lg"></SearchField>
        <div class="search-word">
          <strong class="search-word-title">최근 검색어</strong>
          <div class="h-group gap-5">
            <baseTag># CCTV위치</baseTag>
            <baseTag># 주정차댠속</baseTag>
            <baseTag># 기후변화</baseTag>
            <baseTag># 안심지역</baseTag>
          </div>
        </div>
      </div>
      <div class="main-information">
        <!-- TODO:[퍼블리싱] 데이터패브릭 메인과 ui가 동일할경우 클래스명 article로 통일  -->
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">신규데이터 모델</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="card-simple-list">
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
            </ul>
          </div>
        </article>
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">최근 분석중 데이터 모델</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="card-simple-list">
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
            </ul>
          </div>
        </article>
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">추천 데이터 모델</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="card-simple-list">
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
            </ul>
          </div>
        </article>
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">최근 업데이트 연결정보</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="card-simple-list">
              <li class="card-simple-item">
                <CardSimple hasStatus></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple hasStatus></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple hasStatus></CardSimple>
              </li>
            </ul>
          </div>
        </article>
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">도메인 분류</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="domain-list">
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
              <li class="domain-item">
                <button class="domain-button" type="button">
                  <svg-icon name="data" class="svg-icon"></svg-icon>
                  <span class="domain-title">일반행정</span>
                </button>
              </li>
            </ul>
          </div>
        </article>
        <article class="main-article">
          <div class="main-article-head">
            <h3 class="main-article-title">북마크한 데이터 모델</h3>
            <BaseButton class="button-link-tertiary"><span class="button-text">더보기</span></BaseButton>
          </div>
          <div class="main-article-body">
            <ul class="card-simple-list">
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
              <li class="card-simple-item">
                <CardSimple></CardSimple>
              </li>
            </ul>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout"
});
</script>
