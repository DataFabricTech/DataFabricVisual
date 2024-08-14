<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">서비스 목록</span>
      <button class="button button-secondary-stroke" @click="showModal = true">
        서비스 추가
      </button>
    </div>
    <div class="menu menu-data menu-lg">
      <div class="menu-head">
        <div class="h-group">
          <div class="search-input">
            <label class="hidden-text" for="data-menu-search"
              >데이터 모델 검색</label
            >
            <input
              id="data-menu-search"
              class="text-input"
              placeholder="검색어 입력"
            />
            <svg-icon class="text-input-icon" name="search"></svg-icon>
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button class="button button-neutral-ghost">
            <span class="hidden-text">리셋</span>
            <svg-icon class="svg-icon" name="reset"></svg-icon>
          </button>
        </div>
      </div>
      <ul class="menu-list">
        <li class="menu-item is-menu-item-selected">
          <button class="menu-button">
            <svg-icon
              class="svg-icon menu-data-icon"
              name="resource"
            ></svg-icon>
            <span class="menu-text">서비스A</span>
            <span class="menu-subtext">(소유자)</span>
          </button>
        </li>
        <li class="menu-item" v-for="menu in 6" :key="menu">
          <button class="menu-button">
            <svg-icon
              class="svg-icon menu-data-icon"
              name="resource"
            ></svg-icon>
            <span class="menu-text">서비스F</span>
            <span class="menu-subtext">(소유자)</span>
          </button>
        </li>
      </ul>
      <!-- 결과 없을 시 no-result 표시 -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 서비스가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import SelectBox from "@extends/select-box/SelectBox.vue";
</script>
