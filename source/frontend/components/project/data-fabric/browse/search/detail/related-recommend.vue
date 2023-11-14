<template>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">Knowledge Graph</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <h4 class="page-subtitle">연관데이터 모델</h4>
    <div class="no-result h-[200px]">
      <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
    </div>
  </article>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">데이터리니지</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <div class="h-group w-full gap-[60px]">
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">연관 데이터 모델</h4>
        <div class="no-result h-[200px]">
          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
        </div>
      </div>
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">추천 데이터 모델</h4>
        <div class="no-result h-[200px]">
          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
        </div>
      </div>
    </div>
  </article>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">연간 / 추천 데이터모델</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <div class="h-group w-full gap-[60px]">
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">연관 데이터 모델</h4>
        <ul class="card-list">
          <li class="card-item" v-for="index in 2">
            <Card :show-preview-btn="false" :show-statistics="false"></Card>
          </li>
        </ul>
      </div>
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">추천 데이터 모델</h4>
        <ul class="card-list">
          <li class="card-item" v-for="index in 2">
            <Card :show-preview-btn="false" :show-statistics="false"></Card>
          </li>
        </ul>
      </div>
    </div>
  </article>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">연간 / 추천 데이터모델</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <div class="h-group w-full gap-[60px]">
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">메타 정보</h4>
        <ul class="card-simple-list">
          <li class="card-simple-item" v-for="index in 3">
            <CardSimple></CardSimple>
          </li>
        </ul>
      </div>
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">메타 정보</h4>
        <ul class="card-simple-list">
          <li class="card-simple-item" v-for="index in 3">
            <CardSimple></CardSimple>
          </li>
        </ul>
      </div>
    </div>
  </article>
</template>
