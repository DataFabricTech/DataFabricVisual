<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">용어사전 목록</span>
      <button class="button button-secondary-stroke" @click="openModal">
        용어사전 추가
      </button>
    </div>
    <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
    <div class="no-result" v-if="glossaries.length === 0">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
    <div class="menu border-none" v-if="glossaries.length > 0">
      <div class="menu-list">
        <li
          :class="menuListClass(glossary)"
          v-for="glossary in glossaries"
          @click="changeCurrentGlossary(glossary)"
        >
          <button class="menu-button">
            <svg-icon class="svg-icon" name="report-tag"></svg-icon>
            <a class="menu-text">{{ glossary.name }}</a>
          </button>
        </li>
      </div>
    </div>
  </div>
  <modal-glossary-dictionary
    :modal-id="MODAL_ID"
    @cancel-model="closeModal"
  ></modal-glossary-dictionary>
</template>

<script setup lang="ts">
import { useGlossaryStore } from "~/store/glossary";
import type { Glossary } from "~/type/glossary";
import { _ } from "lodash";
const { $vfm } = useNuxtApp();
const { glossaries, glossary, getGlossaries, changeCurrentGlossary } =
  useGlossaryStore();
getGlossaries();

const menuListClass = (data: Glossary): string => {
  return _.isEqual(glossary, data)
    ? "menu-item is-menu-item-selected"
    : "menu-item";
};

const MODAL_ID = "modal-glossary-dictionary";
function openModal() {
  $vfm.open(MODAL_ID);
}
function closeModal() {
  $vfm.close(MODAL_ID);
}
</script>
