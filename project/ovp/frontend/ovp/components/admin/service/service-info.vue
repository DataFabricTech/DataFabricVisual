<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="h-group gap-2">
        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
        <h4 class="service-title">서비스A</h4>
      </div>
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <div class="v-group gap-4">
        <div class="h-group gap-2">
          <div class="text-neutral-700 font-semibold w-14">소유자</div>
          <div class="editable-group w-auto">
            <div class="text-neutral-700">owner</div>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
        <div class="h-group gap-8">
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">태그</div>
            <div class="editable-group w-auto">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">DATA-tag</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">용어</div>
            <div class="editable-group w-auto">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">term 01</span>
              </div>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">term 04</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="tab tab-line">
        <ul class="tab-list">
          <li
            :class="tabSelectedClass(tabItem.value)"
            v-for="tabItem in tabList"
            @click="changeTab(tabItem.value)"
          >
            <button class="tab-button">
              <p class="tab-button-text">{{ tabItem.label }}</p>
            </button>
          </li>
        </ul>
      </div>
      <repository v-if="store.tab === 'repository'"></repository>
      <collection v-if="store.tab === 'collection'"></collection>
      <connection-info v-if="store.tab === 'connection-info'"></connection-info>
    </div>
  </div>
  <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
  <div class="no-result" style="display: none">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 서비스가 없습니다.</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useServiceStore } from "@/store/admin/service";
import $constants from "~/utils/constant";
const { changeTab } = useServiceStore();
const store = useServiceStore();

const tabList = $constants.SERVICE.TAB;
const tabSelectedClass = computed(() => (data: string): string => {
  return store.tab === data ? "tab-item is-tab-item-selected" : "tab-item";
});
</script>
