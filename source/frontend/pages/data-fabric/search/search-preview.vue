<template>
  <div class="l-common">
    <h2 class="hidden">데이터패브릭 검색</h2>
    <div class="l-inner flex-col">
      <section class="l-section gap-[20px]">
        <h3 class="hidden">데이터 검색</h3>
        <div class="data-search">
          <!--            TODO: [퍼블리싱] search-field 대체 예정-->
          <label for="inp01" class="hidden">데이터 검색</label>
          <BaseTextInput id="inp01" class="w-[522px]"></BaseTextInput>
          <BaseButton class="button-icon button-normal" title="상세 검색">
            <span class="hidden">상세 검색</span>
            <svg-icon name="sort" class="svg-icon"></svg-icon>
          </BaseButton>
        </div>
        <div class="data-search-filter">
          <SearchFilter></SearchFilter>
        </div>
      </section>
      <section class="l-section gap-[28px]">
        <h3 class="hidden">데이터 검색 결과</h3>
        <BaseTab theme="round-tab" class="round-tab-sm"></BaseTab>
        <div class="result-info">
          <strong class="result-summary">검색 결과는 <em>4</em>건 입니다.</strong>
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
        <div class="result-split">
          <div class="result-detail">
            <h4 class="category-name">
              데이터모델 (6)
            </h4>
            <ul class="card-list">
              <li class="card-item is-selected">
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
            <h4 class="category-name">
              데이터모델 (6)
            </h4>
            <div class="no-result">
              <storng class="no-result-title">검색 결과가 없습니다.</storng>
            </div>
          </div>
          <div class="preview">
            <header class="preview-header">
              <baseButton class="button-icon button-sm" title="닫기">
                <svg-icon name="close" class="svg-icon" />
                <span class="hidden">닫기</span>
              </baseButton>
            </header>
            <div class="preview-body">
              <BaseNotification theme="warning">
                <strong class="notification-title">
                  연결정보가 변동되어 데이터모델 정보를 다운로드 받을 수 없습니다.
                </strong>
              </BaseNotification>
            </div>
            <footer class="preview-footer">
              <a href="#" class="preview-link">
                소유자에게 문의하기
              </a>
            </footer>
          </div>
        </div>
        <!--      ...-->
      </section>
    </div>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout"
});


</script>


