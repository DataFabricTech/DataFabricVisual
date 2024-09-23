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
        <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
        <Loading
          id="loader"
          :use-loader-overlay="true"
          class="loader-lg is-loader-inner"
          style="display: none"
        ></Loading>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ModalGlossaryDictionary from "@/components/govern/glossary/modal/modal-glossary-dictionary.vue";
import { useGlossaryStore } from "@/store/glossary";
import type { Glossary } from "@/type/glossary";
import _ from "lodash";
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { useModal } from "vue-final-modal";

const { glossaries, glossary, getGlossaries, changeCurrentGlossary } =
  useGlossaryStore();
getGlossaries();

const menuListClass = (data: Glossary): string => {
  return _.isEqual(glossary, data)
    ? "menu-item is-menu-item-selected"
    : "menu-item";
};

const { scrollTrigger } = useIntersectionObserver(getGlossaries);

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
