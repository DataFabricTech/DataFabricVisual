<template>
  <div class="l-common">
    <h2 class="hidden">데이터모델 상세</h2>
    <BaseNotification theme="warning">
      <strong class="notification-title">
        연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다.
      </strong>
      <a href="#" class="notification-link">
        바로가기
      </a>
    </BaseNotification>
    <section class="l-section gap-[20px]">
      <div class="page-subject">
        <h3 class="page-title">
          <span class="hidden">TODO: [개발] '데이터 모델 이름'이 들어가야 합니다.</span></h3>
        <BaseButton class="button-normal">
          <svg-icon name="edit" class="svg-icon" />
          <span class="button-text">수정</span>
        </BaseButton>
      </div>
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
            <Card class="card-primary">
              <template v-slot:img>
                <svg-icon class="svg-icon" name="data"></svg-icon>
              </template>
            </Card>
          </li>
        </ul>
        <BasePagination></BasePagination>
      </article>
    </section>
    <section class="l-section gap-[28px]">
      <div class="anchor" id="all">
        <a class="anchor-link" title="이동" href="#all">전체 (16)</a>
        <a class="anchor-link" title="이동" href="#data-model">데이터모델 (6)</a>
        <a class="anchor-link" title="이동" href="#repository">저장소 (5)</a>
        <a class="anchor-link" title="이동" href="#domain">도메인 (5)</a>
      </div>
      <div class="result-info">
        <h3 class="result-summary-title">"<em class="color-tertiary">키워드</em>"검색 결과는 <em class="color-tertiary">4</em>건
          입니다.</h3>
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
      <article class="category-article ">
        <div class="category-top-bar">
          <h4 class="category-name" id="data-model">데이터모델 (6)</h4>
        </div>
        <ul class="card-list">
          <!--              선택된 데이터모델 is-selected 클래스 추가-->
          <li class="card-item is-selected">
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
      </article>
      <article class="category-article">
        <div class="category-top-bar">
          <h4 class="category-name" id="data-model">데이터모델 (6)</h4>
        </div>
        <div class="no-result">
          <storng class="no-result-title">검색 결과가 없습니다.</storng>
        </div>
      </article>
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
