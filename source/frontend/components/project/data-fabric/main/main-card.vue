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
          <li class="card-simple-item" v-for="simpleCard in simpleCardList_newData">
            <CardSimple
            :model="simpleCard.model"></CardSimple>
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

import { fabricMainStore } from "/store/data-fabric/main/main";
import { storeToRefs } from "pinia";

const store = fabricMainStore();
const { simpleCardContent_newData } = store;
const {
  responseSimpleCard_newData
} = storeToRefs(store);

// 1) 신규데이터를 기준으로한 sorting 파라미터 선언
let param = 'createdAt';

// requestbody 값 설정
const requestSimpleCard_newData = ({
  keyword: '',
  pageable: {
    sort: [
      {
        order: 1,
        field: param,
        direction: 'ASC',
      },
    ],
    page: {
      size: 3,
      selectPage: 1,
    },
  }
});

// monuted되면, 카드리스트의 내용을 가져오기 위한 함수 호출
onMounted(() =>
  simpleCardContent_newData(requestSimpleCard_newData)
)

// response로 받는 리스트 선언
let simpleCardList_newData = reactive([]);
// TODO : 추후 API response값이 들어오면, 수정할 예정
simpleCardList_newData = responseSimpleCard_newData;

</script>
