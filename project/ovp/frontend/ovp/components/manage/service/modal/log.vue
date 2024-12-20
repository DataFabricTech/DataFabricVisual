<template>
  <Modal
    :title="'로그'"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="1180"
    :height="610"
    :lockScroll="true"
    :useCancelBtn="false"
    @before-open="onBeforeOpenModal"
    @click-outside="onCancelModal"
    @cancel="onCancelModal"
    @confirm="onCancelModal"
  >
    <template v-slot:body>
      <div class="flex justify-end w-full gap-2 mb-3">
        <button class="button button-neutral-stroke" @click="refresh">
          <svg-icon class="button-icon" name="refresh"></svg-icon>
          새로고침
        </button>
        <button
          class="button button-neutral-stroke"
          ref="copyButton"
          @click="copyLog"
        >
          복사
        </button>
      </div>
      <!-- TODO: [개발] 로그 화면 구현 -->
      <div class="log-view" style="height: 100%">
        <MonacoEditor
          v-model="collectionLog"
          lang="shell"
          :options="options"
          style="height: 100%"
        />
      </div>
    </template>
  </Modal>
</template>
<script setup lang="ts">
import { defineEmits } from "vue";
import { useClipboard, usePermission } from "@vueuse/core";
import { storeToRefs } from "pinia";

import { useNuxtApp } from "nuxt/app";
const { $alert } = useNuxtApp();

import { useServiceCollectionLogStore } from "@/store/manage/service/collection-log/index";

import Modal from "@extends/modal/Modal.vue";

const serviceCollectionLogStore = useServiceCollectionLogStore();
const { collectionLog } = storeToRefs(serviceCollectionLogStore);
const { getCollectionLogData } = serviceCollectionLogStore;

const { copy } = useClipboard();
const permissionRead = usePermission("clipboard-read");
const permissionWrite = usePermission("clipboard-write");

const options = {
  theme: "vs-dark",
  minimap: {
    enabled: false,
  },
  find: {
    addExtraSpaceOnTop: true,
    autoFindInSelection: "multiline",
  },
  readOnly: true,
  domReadOnly: true,
};

const emit = defineEmits<{
  (e: "close"): void;
}>();

const onBeforeOpenModal = async () => {
  collectionLog.value = "";
  await getCollectionLogData();
};

const onCancelModal = () => {
  emit("close");
};

function refresh() {
  collectionLog.value = "";
  getCollectionLogData();
}

const copyLog = () => {
  console.log("start copy log");
  if (
    permissionWrite.value.toLowerCase() !== "denied" &&
    permissionRead.value.toLowerCase() !== "denied"
  ) {
    copy(collectionLog.value);
  } else {
    const textarea = document.createElement("textarea");
    textarea.value = collectionLog.value;
    document.body.appendChild(textarea);
    textarea.select();
    textarea.focus(); // 포커스 강제 설정
    document.execCommand("copy");
    document.body.removeChild(textarea);
  }

  $alert("로그가 복사되었습니다.", "success");
};
</script>
