<template>
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
                <span class="list-text">전체 데이터 모델</span>
              </button>
            </li>
            <li class="list-item">
              <button class="list-button" type="button">
                <svg-icon class="svg-icon" name="data"></svg-icon>
                <span class="list-text">내가 등록한 모델</span>
              </button>
            </li>
            <li class="list-item">
              <button class="list-button" type="button">
                <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                <span class="list-text">북마크한 모델</span>
              </button>
            </li>
            <li class="list-item">
              <button class="list-button" type="button">
                <svg-icon class="svg-icon" name="write"></svg-icon>
                <span class="list-text">최근 분석 데이터 모델</span>
              </button>
            </li>
            <li class="list-item">
              <button class="list-button" type="button">
                <svg-icon class="svg-icon" name="time"></svg-icon>
                <span class="list-text">최근 조회한 데이터 모델</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup></script>
