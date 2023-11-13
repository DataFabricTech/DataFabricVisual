<template>
<!--  데이터 상세-->
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">
        <span class="hidden-text">선택된 데이터 정보</span>
      </h3>
      <div class="h-group gap-2">
        <baseButton class="button-normal">
          <svg-icon class="svg-icon" name="pen"></svg-icon>
          <span class="button-text">수정</span>
        </baseButton>
        <baseButton class="button-normal">
          <span class="button-text">권한 설정</span>
        </baseButton>
        <baseButton class="button-danger">
          <span class="button-text">삭제</span>
        </baseButton>
      </div>
    </div>
    <article class="page-article">
      <Card class="w-full"></Card>
    </article>
    <article class="page-article">
      <div class="anchor">
        <a class="anchor-link" title="이동" href="#status-info">상태 정보</a>
        <a class="anchor-link" title="이동" href="#default-info">기본 정보</a>
        <a class="anchor-link" title="이동" href="#collect-info">수집 정보</a>
      </div>
      <div class="v-group gap-9 p-6 w-full">
        <div class="page-group">
          <h4 class="page-subtitle" id="default-info">기본 정보</h4>
          <div class="table-col w-full">
            <table class="w-full">
              <caption class="caption-out">
                Description Column
              </caption>
              <colgroup>
                <col style="width:284px;" />
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
              <tr>
                <td>ID</td>
                <td>fooxxx</td>
              </tr>
              <tr>
                <td>Password</td>
                <td>xxxx</td>
              </tr>
              <tr>
                <td>Cert</td>
                <td>xxxx</td>
              </tr>
              <tr>
                <td>상태</td>
                <td>
                  <baseBadge class="bg-marker-cyan">Active</baseBadge>
                </td>
              </tr>
              <tr>
                <td>등록 데이터 / 전체 데이터</td>
                <td>1712 / 2201</td>
              </tr>
              <tr>
                <td>동기화 주기</td>
                <td>3일 / AM 01:30:00</td>
              </tr>
              <tr>
                <td>마지막 동기화 상태</td>
                <td>
                  <baseBadge class="bg-marker-cyan">Success</baseBadge>
                </td>
              </tr>
              <tr>
                <td>마지막 동기화 시간</td>
                <td>2023-01-05  12:10:14</td>
              </tr>
              <tr>
                <td>소유자(등록자)</td>
                <td>User C</td>
              </tr>
              <tr>
                <td>공개 여부</td>
                <td>
                  <div class="h-group gap-3">
                    <baseRadio  id="radio-all" name="disclosure-status">전체 공개</baseRadio>
                    <baseRadio  id="radio-group" name="disclosure-status">그룹 공개</baseRadio>
                    <baseRadio  id="radio-none" name="disclosure-status">비공개</baseRadio>
                  </div>
                </td>
              </tr>
              <tr>
                <td>설명</td>
                <td>김아무개가 만든 MySQL 데이터 소스 저장소</td>
              </tr>
              <tr>
                <td>생성일시</td>
                <td>2023-01-04  12:13:14</td>
              </tr>
              <tr>
                <td>수정자</td>
                <td>홍길동</td>
              </tr>
              <tr>
                <td>수정일시</td>
                <td>2023-01-04  13:13:14</td>
              </tr>
              <tr>
                <td>태그</td>
                <td>
                  <div class="h-group gap-3">
                    <baseTag># 김아무개</baseTag>
                    <baseTag># 김아무개</baseTag>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="page-group">
          <h4 class="page-subtitle" id="collect-info">수집 정보</h4>
          <div class="w-full bg-gray-200 h-[500px]"></div>
        </div>
      </div>
    </article>
  </section>
</template>

<script lang="ts" setup>
</script>
