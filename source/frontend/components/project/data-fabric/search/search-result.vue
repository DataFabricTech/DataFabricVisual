<template>
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
    <div class="l-preview">
      <div class="preview-main">
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
      </div>
      <!--        미리보기 - 아래 컨텐츠가 Toggle-->
      <div class="preview">
        <header class="preview-header">
          <h5 class="preview-title">
            <span class="hidden">데이터 상세 정보</span>
          </h5>
          <baseButton class="button-icon button-sm" title="닫기">
            <svg-icon name="close" class="svg-icon" />
            <span class="hidden">닫기</span>
          </baseButton>
        </header>
        <div class="preview-body">
          <BaseNotification theme="warning">
            <strong class="notification-title">
              연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다.
            </strong>
          </BaseNotification>
          <div class="preview-card">
            <CardSimple class="bg-[#f5f5f5]"></CardSimple>
          </div>
          <article class="preview-article">
            <BaseTab></BaseTab>
            <baseTableRow></baseTableRow>
          </article>
          <article class="preview-article">
            <BaseTab></BaseTab>
            <baseTableColumn></baseTableColumn>
          </article>
          <article class="preview-article">
            <BaseTab></BaseTab>
            <baseTableColumn></baseTableColumn>
          </article>
          <article class="preview-article">
            <BaseTab></BaseTab>
            <div class="chartbox"></div>
          </article>
          <article class="preview-article">
            <BaseTab></BaseTab>
            <ul class="card-simple-list w-full">
              <li class="card-simple-item" v-for="index in 3">
                <CardSimple></CardSimple>
              </li>
            </ul>
          </article>
        </div>
        <footer class="preview-footer">
          <baseButton class="button-lg button-link"><span class="button-text">소유자에게 문의하기</span></baseButton>
        </footer>
      </div>
    </div>
  </section>
</template>
<script lang="ts" setup>

const state = reactive({
  isOpen: true
});
</script>
