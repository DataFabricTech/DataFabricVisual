<template>
  <div class="l-split">
    <h2 class="hidden-text">데이터모델 Browse</h2>
    <section class="l-side">
      <h3 class="hidden-text">카테고리 | 카탈로그 | 연결정보</h3>
      <div class="side-container">
        <!--      <BaseTab theme="fill-tab"></BaseTab> 으로 컴포넌트 사용 가능-->
        <div class="fill-tab">
          <ul class="fill-tab-list">
            <li class="fill-tab-item">
              <button type="button" class="fill-tab-button is-selected">
                <span class="fill-tab-title">카테고리</span>
              </button>
            </li>
            <li class="fill-tab-item">
              <button type="button" class="fill-tab-button">
                <span class="fill-tab-title">카탈로그</span>
              </button>
            </li>
            <li class="fill-tab-item">
              <button type="button" class="fill-tab-button">
                <span class="fill-tab-title">연결정보</span>
              </button>
            </li>
          </ul>
        </div>
        <div class="side-content">
          <div class="side-wrapper">
            <h4 class="hidden-text">카테고리 목록</h4>
            <div class="list border-none">
              <ul class="list-list p-2">
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="object-list"></svg-icon>
                    <span class="list-text">전체 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="data"></svg-icon>
                    <span class="list-text">내가 등록한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                    <span class="list-text">북마크한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="write"></svg-icon>
                    <span class="list-text">최근 분석 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="time"></svg-icon>
                    <span class="list-text">최근 조회한 데이터 모델</span></button>
                </li>
              </ul>
            </div>
          </div>
          <div class="side-wrapper">
            <h4 class="hidden-text">나의 카테고리</h4>
            <div class="list border-none">
              <ul class="list-list p-2">
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="object-list"></svg-icon>
                    <span class="list-text">전체 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="data"></svg-icon>
                    <span class="list-text">내가 등록한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                    <span class="list-text">북마크한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="write"></svg-icon>
                    <span class="list-text">최근 분석 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="time"></svg-icon>
                    <span class="list-text">최근 조회한 데이터 모델</span></button>
                </li>
              </ul>
            </div>
          </div>
          <div class="side-wrapper">
            <h4 class="hidden-text">나의 카테고리</h4>
            <div class="list border-none">
              <ul class="list-list p-2">
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="object-list"></svg-icon>
                    <span class="list-text">전체 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="data"></svg-icon>
                    <span class="list-text">내가 등록한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                    <span class="list-text">북마크한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="write"></svg-icon>
                    <span class="list-text">최근 분석 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="time"></svg-icon>
                    <span class="list-text">최근 조회한 데이터 모델</span></button>
                </li>
              </ul>
            </div>
          </div>
          <div class="side-wrapper">
            <h4 class="hidden-text">나의 카테고리</h4>
            <div class="list border-none">
              <ul class="list-list p-2">
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="object-list"></svg-icon>
                    <span class="list-text">전체 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="data"></svg-icon>
                    <span class="list-text">내가 등록한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                    <span class="list-text">북마크한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="write"></svg-icon>
                    <span class="list-text">최근 분석 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="time"></svg-icon>
                    <span class="list-text">최근 조회한 데이터 모델</span></button>
                </li>
              </ul>
            </div>
          </div>
          <div class="side-wrapper">
            <h4 class="hidden-text">나의 카테고리</h4>
            <div class="list border-none">
              <ul class="list-list p-2">
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="object-list"></svg-icon>
                    <span class="list-text">전체 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="data"></svg-icon>
                    <span class="list-text">내가 등록한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                    <span class="list-text">북마크한 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="write"></svg-icon>
                    <span class="list-text">최근 분석 데이터 모델</span></button>
                </li>
                <li class="list-item">
                  <button class="list-button" type="button">
                    <svg-icon class="svg-icon" name="time"></svg-icon>
                    <span class="list-text">최근 조회한 데이터 모델</span></button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="l-split-section">
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
          <h4 class="result-summary-title">"<em class="color-tertiary">키워드</em>"검색 결과는 <em class="color-tertiary">4</em>건
            입니다.</h4>
          <div class="result-sort">
            <BaseSelect class="select-lg"></BaseSelect>
            <BaseSelect class="select-lg"></BaseSelect>
          </div>
        </div>
        <ul class="card-list">
          <li class="card-item" v-for="index in 10">
            <Card></Card>
          </li>
        </ul>
        <BasePagination></BasePagination>
      </article>
    </section>
  </div>
</template>

<script lang="ts" setup>
definePageMeta({
  layout: "default-tab-layout"
});

const state = reactive({
  isOpen: true
});

</script>
