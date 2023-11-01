<template>
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
</template>
<script lang="ts" setup>
</script>
