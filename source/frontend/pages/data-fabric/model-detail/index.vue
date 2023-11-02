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
      <Card class="card-lg w-full"></Card>
      <div class="anchor" id="all">
        <a class="anchor-link" title="이동" href="#all">전체 (16)</a>
        <a class="anchor-link" title="이동" href="#data-model">데이터모델 (6)</a>
        <a class="anchor-link" title="이동" href="#repository">저장소 (5)</a>
        <a class="anchor-link" title="이동" href="#domain">도메인 (5)</a>
      </div>
      <article class="category-article">
        <h4 class="page-subtitle">최근 등록/변경된 연결정보</h4>
        <div class="table-col w-full">
          <table class="w-full">
            <caption class="caption-out">
              Description Column
            </caption>
            <thead>
            <tr>
              <!-- 세로 => col, 가로 => row -->
              <th scope="col">연결정보 이름</th>
              <th scope="col">데이터 저장소 유형</th>
              <th scope="col">상태</th>
              <th scope="col">소유자(등록자)</th>
              <th scope="col">등록일시</th>
              <th scope="col">변경일시</th>
              <th scope="col">내용</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>Inactive</td>
              <td>User A</td>
              <td>2023-01-02 12:13:14</td>
              <td>2023-01-05 12:13:14</td>
              <td>수정</td>
            </tr>
            <tr>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>Inactive</td>
              <td>User A</td>
              <td>2023-01-02 12:13:14</td>
              <td>2023-01-05 12:13:14</td>
              <td>수정</td>
            </tr>
            <tr>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>Inactive</td>
              <td>User A</td>
              <td>2023-01-02 12:13:14</td>
              <td>2023-01-05 12:13:14</td>
              <td>수정</td>
            </tr>
            <tr>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>Inactive</td>
              <td>User A</td>
              <td>2023-01-02 12:13:14</td>
              <td>2023-01-05 12:13:14</td>
              <td>수정</td>
            </tr>
            </tbody>
          </table>
        </div>
      </article>
      <article class="category-article">
        <h4 class="page-subtitle">히스토리</h4>
        <div class="table-col w-full">
          <table class="w-full">
            <caption class="caption-out">
              Description Column
            </caption>
            <thead>
            <tr>
              <!-- 세로 => col, 가로 => row -->
              <th scope="col">이벤트 발생 시간</th>
              <th scope="col">이벤트</th>
              <th scope="col">연결정보 이름</th>
              <th scope="col">데이터 저장소 유형</th>
              <th scope="col">소유자(등록자)</th>
              <th scope="col">내용</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>2023-01-05 12:13:14</td>
              <td>연결 끊김</td>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>User A</td>
              <td></td>
            </tr>
            <tr>
              <td>2023-01-05 12:13:14</td>
              <td>연결 끊김</td>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>User A</td>
              <td>자동 등록 데이터 xx</td>
            </tr>
            <tr>
              <td>2023-01-05 12:13:14</td>
              <td>연결 끊김</td>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>User A</td>
              <td>자동 등록 데이터 xx</td>
            </tr>
            <tr>
              <td>2023-01-05 12:13:14</td>
              <td>연결 끊김</td>
              <td>연결정보 H</td>
              <td>MySQL</td>
              <td>User A</td>
              <td>자동 등록 데이터 xx</td>
            </tr>
            </tbody>
          </table>
        </div>
      </article>
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
