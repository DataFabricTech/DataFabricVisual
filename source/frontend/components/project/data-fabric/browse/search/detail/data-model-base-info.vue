<template>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">기본 정보</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <h4 class="page-subtitle">메타 정보</h4>
    <div class="table-row w-full">
      <table class="w-full">
        <caption class="caption-out">
          메타 정보
        </caption>
        <tbody>
          <tr>
            <th scope="row">연결정보 이름</th>
            <td>저장소 A</td>
            <th scope="row">Title2</th>
            <td>Description1-2</td>
          </tr>
          <tr>
            <th scope="row">Title3</th>
            <td>Description2-3</td>
            <th scope="row">Title4</th>
            <td>Description1-4</td>
          </tr>
          <tr>
            <th scope="row">Title5</th>
            <td>Description3-5</td>
            <th scope="row">Title6</th>
            <td>Description1-6</td>
          </tr>
          <tr>
            <th scope="row">Title7</th>
            <td>Description4-7</td>
            <th scope="row">Title8</th>
            <td>Description1-8</td>
          </tr>
        </tbody>
      </table>
    </div>
    <h4 class="page-subtitle">수집 정보</h4>
    <div class="table-row w-full">
      <table class="w-full">
        <caption class="caption-out">
          Description Row
        </caption>
        <tbody>
          <tr>
            <th scope="row">Title1</th>
            <td>Description1-1</td>
            <th scope="row">Title2</th>
            <td>Description1-2</td>
          </tr>
        </tbody>
      </table>
    </div>
    <h4 class="page-subtitle">스키마 정보</h4>
    <div class="table-col w-full">
      <table class="w-full">
        <caption class="caption-out">
          Description Column
        </caption>
        <colgroup>
          <col />
          <col />
        </colgroup>
        <thead>
          <tr>
            <!-- 세로 => col, 가로 => row -->
            <th scope="col">항목</th>
            <th scope="col">내용</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>연결정보이름</td>
            <td>연결정보B</td>
          </tr>
          <tr>
            <td>데이터 저장소 유형</td>
            <td>MySQL</td>
          </tr>
          <tr>
            <td>Driver (Adapter)</td>
            <td>MySQL 5</td>
          </tr>
          <tr>
            <td>Host</td>
            <td>foo.mobigen.com</td>
          </tr>
          <tr>
            <td>Port</td>
            <td>3305</td>
          </tr>
        </tbody>
      </table>
    </div>
  </article>
</template>

<script lang="ts" setup></script>
