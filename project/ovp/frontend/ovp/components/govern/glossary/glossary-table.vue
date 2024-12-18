<template>
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="openModal">
      용어추가
    </button>
  </div>
  <table class="align-center">
    <colgroup>
      <col style="width: 25%" />
      <col style="width: 25%" />
      <col style="width: 25%" />
    </colgroup>
    <tr>
      <th>용어</th>
      <th>설명</th>
      <th>관리</th>
    </tr>
    <tr v-if="terms.length !== 0" v-for="term in terms">
      <td>{{ term.name }}</td>
      <td>{{ term.description }}</td>
      <td>
        <div class="button-group">
          <button
            class="button button button-secondary-stroke"
            @click="onClickTerm(term)"
          >
            편집
          </button>
          <button
            class="button button button-error-stroke"
            @click="removeTerm(term.id)"
          >
            삭제
          </button>
        </div>
      </td>
    </tr>
    <tr v-else>
      <td colspan="3">
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </td>
    </tr>
  </table>
  <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
  <Loading
    id="termLoader"
    :use-loader-overlay="true"
    class="loader-lg is-loader-inner"
    style="display: none"
  ></Loading>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import ModalGlossary from "@/components/govern/glossary/modal/modal-glossary.vue";
import { useGlossaryStore } from "@/store/glossary";
import type { Term } from "@/type/glossary";
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { useModal } from "vue-final-modal";

import { useNuxtApp } from "nuxt/app";
const { $alert, $confirm } = useNuxtApp();

const { terms, openEditTermComponent, deleteTerm, getTerms, getTerm } =
  useGlossaryStore();

onMounted(() => getTerms());

async function onClickTerm(source: Term): void {
  await getTerm(source.name);
  openEditTermComponent("term");
}

async function removeTerm(id: string): void {
  if (await $confirm("용어를 삭제 하시겠습니까?")) {
    deleteTerm(id).then((res) => {
      if (res.result === 1) {
        getTerms();
      } else {
        $alert(res.errorMessage, "error");
      }
    });
  }
}

const MODAL_ID = "modal-glossary";

const { open, close } = useModal({
  component: ModalGlossary,
  attrs: {
    modalId: MODAL_ID,
    onClose() {
      close();
    },
  },
});

function openModal(): void {
  open();
}

const { scrollTrigger } = useIntersectionObserver({
  callback: getTerms,
  targetId: "termList",
  loaderId: "termLoader",
});
</script>
