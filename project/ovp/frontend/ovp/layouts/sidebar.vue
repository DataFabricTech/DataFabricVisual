<template>
  <nav id="sidebar">
    <div class="l-sidebar">
      <ul class="sidebar-list">
        <li
          v-for="menu in menuJson"
          class="sidebar-item"
          :class="isSelectedMenu(menu.linkTo) ? 'is-sidebar-item-selected' : ''"
        >
          <nuxt-link :to="menu.linkTo" class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon :name="menu.iconName" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">{{ menu.label }}</span>
          </nuxt-link>
        </li>
      </ul>
      <ul class="sidebar-list">
        <li>
          <nuxt-link
            :to="mgmtMenuJson.linkTo"
            :class="
              isSelectedMenu(mgmtMenuJson.linkTo)
                ? 'is-sidebar-item-selected'
                : ''
            "
            class="sidebar-button"
          >
            <div class="sidebar-icon">
              <svg-icon
                :name="mgmtMenuJson.iconName"
                class="svg-icon"
              ></svg-icon>
            </div>
            <span class="sidebar-text">{{ mgmtMenuJson.label }}</span>
          </nuxt-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup lang="ts">
import _ from "lodash";

import { storeToRefs } from "pinia";
import { useMenuStore } from "@/store/common/menu";
const menuStore = useMenuStore();

const { getMenuData } = menuStore;
const { headerUrl, menuJson, mgmtMenuJson } = storeToRefs(menuStore);

// 메뉴 데이터 조회
await getMenuData();

// URL 기준 메뉴 하이라이팅 처리
const isSelectedMenu = (pageUrl: string) => {
  // URL depth 특성상 ROOT 페이지의 경우 모든 페이지에서 true를 return 하기 예외처리 한다.
  if (headerUrl.value === "/portal" || headerUrl.value === "/portal/") {
    return false;
  }
  return _.includes(pageUrl, headerUrl.value);
};
</script>

<style scoped></style>
