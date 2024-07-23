<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">용어사전 목록</span>
      <button
        class="button button-secondary-stroke"
        @click="showModalDic = true"
      >
        용어사전 추가
      </button>
    </div>
    <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
    <div class="no-result" style="display: none">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
    <div class="menu border-none">
      <div class="menu-list">
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="report-tag"></svg-icon>
            <a href="" class="menu-text">용어사전 01</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="report-tag"></svg-icon>
            <a href="" class="menu-text">용어사전 02</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="report-tag"></svg-icon>
            <a href="" class="menu-text">용어사전 03</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="report-tag"></svg-icon>
            <a href="" class="menu-text">용어사전 04</a>
          </button>
        </li>
      </div>
    </div>
  </div>
  <modal-glossary-dictionary></modal-glossary-dictionary>
</template>

<script setup lang="ts"></script>
