<template>
  <div class="l-header">
    <h1 class="logo">
      데이터 패브릭
    </h1>
    <nav>
      <ul class="gnb">
        <li class="gnb-item">
          <button class="gnb-button is-selected" type="button">
            데이터 모델 Browse
          </button>
        </li>
        <li class="gnb-item">
          <button class="gnb-button" type="button">
            저장소 관리
          </button>
        </li>
        <li class="gnb-item">
          <button class="gnb-button" type="button">
            Knowledge Graph
          </button>
        </li>
      </ul>
    </nav>
    <div class="header-setting">
      <BaseButton class="button-normal">
        <span class="button-text">데이터 패브릭 관리</span>
      </BaseButton>
      <BaseButton class="button-primary">
        <span class="button-text">데이터 모델 생성</span>
      </BaseButton>
    </div>
    <div class="header-management">
      <BaseButton class="button-icon">
        <svg-icon name="app-switcher" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="setting" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="user" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="arrow-down" class="svg-icon" />
      </BaseButton>
    </div>
  </div>
</template>

<script setup lang="ts"></script>
