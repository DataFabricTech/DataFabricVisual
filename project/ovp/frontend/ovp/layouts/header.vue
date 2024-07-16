<template>
  <header id="header">
    <div class="h-group">
      <h1 class="logo">
        <a href="#" class="logo-link" title="메인 이동">
          Open VDAP Portal
          <span class="hidden-text">logo</span>
        </a>
      </h1>
      <SearchInput @onClickSearch="onClickSearch"></SearchInput>
      <div class="profile ml-auto">
        <span class="profile-avatar">
          <img class="profile-img" src="" alt="프로필 이미지" />
        </span>
        <div class="profile-text">root</div>
        <button class="button button-sm button-neutral-ghost">
          <svg-icon class="svg-icon" name="chevron-down-medium"></svg-icon>
          <span class="hidden-text">내 메뉴</span>
        </button>
        <div class="dropdown" style="top: 40px; right: 16px; display: none">
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">내 정보</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">나의 데이터 현황</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">나의 조직</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">알림</span>
              </button>
            </li>
            <li class="dropdown-item dropdown-item-line">
              <button class="dropdown-button">
                <span class="dropdown-text">로그아웃</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import SearchInput from "@extends/search-input/SearchInput.vue";

import { useSearchCommonStore } from "~/store/search/common";
const searchCommonStore = useSearchCommonStore();
const { setSearchKeyword } = searchCommonStore;

const onClickSearch = (value: string) => {
  setSearchKeyword(value);
};
</script>

<style scoped></style>
