<template>
  <div class="l-split">
    <div class="knowledge">knowledge</div>
    <div class="data-list">
      <div class="menu menu-data w-full">
        <ul class="menu-list">
          <li class="menu-item is-menu-item-selected">
            <button class="menu-button">
              <svg-icon
                class="svg-icon menu-data-icon"
                name="resource"
              ></svg-icon>
              <span class="menu-text">데이터 모델</span>
              <span class="menu-subtext">(소유자)</span>
            </button>
            <div class="menu-button-group">
              <div class="relative">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">메뉴보기</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
                <div class="dropdown" style="right: 0">
                  <ul class="dropdown-list">
                    <li class="dropdown-item">
                      <button class="dropdown-button">
                        <span class="dropdown-text">데이터 모델 상세 조회</span>
                      </button>
                    </li>
                    <li class="dropdown-item">
                      <button class="dropdown-button">
                        <span class="dropdown-text">데이터 선택(해제)</span>
                      </button>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </li>
          <li class="menu-item" v-for="item in 5" :key="item">
            <button class="menu-button">
              <svg-icon
                class="svg-icon menu-data-icon"
                name="resource"
              ></svg-icon>
              <span class="menu-text">데이터 모델</span>
              <span class="menu-subtext">(소유자)</span>
            </button>
            <div class="menu-button-group">
              <div class="relative">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">메뉴보기</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">샘플 데이터가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped></style>
