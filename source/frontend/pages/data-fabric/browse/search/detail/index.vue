<template>
  <div class="l-common">
    <h2 class="hidden-text">데이터모델 상세</h2>
    <BaseNotification theme="warning">
      <strong class="notification-title"> 연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다. </strong>
      <a href="#" class="notification-link"> 바로가기 </a>
    </BaseNotification>
    <section class="l-section">
      <div class="page-subject border-none">
        <h3 class="page-title">
          <span class="hidden-text">TODO: [개발] '데이터 모델 이름'이 들어가야 합니다.</span>
        </h3>
        <div class="h-group">
          <BaseButton class="button-normal">
            <svg-icon name="edit" class="svg-icon" />
            <span class="button-text">수정</span>
          </BaseButton>
          <BaseButton class="button-primary">
            <svg-icon name="save" class="svg-icon" />
            <span class="button-text">저장</span>
          </BaseButton>
          <BaseButton class="button-danger">
            <span class="button-text">초기화</span>
          </BaseButton>
        </div>
      </div>
      <Card class="card-lg w-full"></Card>
    </section>
    <section class="l-section gap-[16px]">
      <div class="anchor">
        <a class="anchor-link" title="이동" href="#status-info">상태 정보</a>
        <a class="anchor-link" title="이동" href="#default-info">기본 정보</a>
        <a class="anchor-link" title="이동" href="#collect-info">수집 정보</a>
      </div>
      <div class="v-group gap-[48px] px-4 w-full">
        <DataModelBaseInfo></DataModelBaseInfo>
        <RelatedRecommend></RelatedRecommend>
        <RatingComment></RatingComment>
      </div>
    </section>
  </div>
  <ScrollTop></ScrollTop>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout"
});

const state = reactive({
  isOpen: true
});
</script>
