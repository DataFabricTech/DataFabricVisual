<template>
  <nav id="sidebar">
    <div class="l-sidebar">
      <ul class="sidebar-list">
        <li
          v-for="menu in menuJson"
          class="sidebar-item"
          :class="isSelectedMenu(menu.linkTo) ? 'is-sidebar-item-selected' : ''"
        >
          <nuxt-link
            :to="menu.linkTo"
            class="sidebar-button"
            @click="handleLinkClick(menu.linkTo)"
          >
            <div class="sidebar-icon">
              <svg-icon :name="menu.iconName" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">{{ menu.label }}</span>
          </nuxt-link>
        </li>
      </ul>
      <ul v-if="!_.isEmpty(mgmtMenuJson)" class="sidebar-list">
        <li
          class="sidebar-item"
          :class="
            isSelectedMenu(mgmtMenuJson.linkTo)
              ? 'is-sidebar-item-selected'
              : ''
          "
        >
          <nuxt-link
            :to="mgmtMenuJson.linkTo"
            class="sidebar-button"
            @click="handleLinkClick(mgmtMenuJson.linkTo)"
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

import { useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

import { storeToRefs } from "pinia";
import { useMenuStore } from "@/store/common/menu";
import { useRoute } from "nuxt/app";

const menuStore = useMenuStore();

const { getMenuData } = menuStore;
const { headerUrl, previousUrl, menuJson, mgmtMenuJson } =
  storeToRefs(menuStore);

// 메뉴 데이터 조회
await getMenuData();

// URL 기준 메뉴 하이라이팅 처리
const isSelectedMenu = (pageUrl: string) => {
  // URL depth 특성상 ROOT 페이지의 경우 모든 페이지에서 true를 return 하기 예외처리 한다.
  if (headerUrl.value === "/portal" || headerUrl.value === "/portal/") {
    return false;
  }

  // 사용자관리 페이지에서 사용자 마이페이지로 이동시, 관리메뉴 하이라이팅 처리
  if (
    pageUrl === "/portal/manage" &&
    previousUrl.value === "/portal/manage/user" &&
    _.includes(headerUrl.value, "/portal/my-page")
  ) {
    return true;
  }

  return _.includes(pageUrl, headerUrl.value);
};

const handleLinkClick = (linkTo: string) => {
  if (route.path === linkTo) {
    router.go(0);
  }
};
</script>

<style scoped></style>
