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
        <p class="notification-detail">데이터가 없습니다.</p>
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
</template>

<script setup lang="ts">
import ModalGlossaryDictionary from "@/components/govern/glossary/modal/modal-glossary-dictionary.vue";
import { useModal } from "vue-final-modal";
import { useGlossaryStore } from "~/store/glossary";
import type { Glossary } from "~/type/glossary";
import { _ } from "lodash";
import { onMounted } from "vue";
const { glossaries, glossary, getGlossaries, changeCurrentGlossary } =
  useGlossaryStore();

onMounted(async () => {
  Object.keys(glossary).forEach((key) => {
    delete glossary[key];
  });
  await getGlossaries();
});

const menuListClass = (data: Glossary): string => {
  return _.isEqual(glossary, data)
    ? "menu-item is-menu-item-selected"
    : "menu-item";
};

const MODAL_ID = "modal-glossary-dictionary";

const { open, close } = useModal({
  component: ModalGlossaryDictionary,
  attrs: {
    modalId: MODAL_ID,
    onClose() {
      close();
    },
  },
});

function openModal() {
  open();
}
</script>
