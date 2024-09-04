<template>
  <Modal
    :title="'로그'"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="1080"
    :height="800"
    :lockScroll="true"
    :useCancelBtn="false"
    @before-open="onBeforeOpenModal"
    @click-outside="onCancelModal"
    @cancel="onCancelModal"
    @confirm="onCancelModal"
  >
    <template v-slot:body>
      <div class="flex justify-end w-full gap-2">
        <button class="button button-neutral-stroke" @click="refresh">
          새로고침
        </button>
        <button class="button button-neutral-stroke" @click="copyLog">
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
import { useClipboard } from "@vueuse/core";
import { storeToRefs } from "pinia";

import { useServiceCollectionLogStore } from "@/store/manage/service/collection-log/index";

import Modal from "@extends/modal/Modal.vue";

const serviceCollectionLogStore = useServiceCollectionLogStore();
const { collectionLog } = storeToRefs(serviceCollectionLogStore);
const { getCollectionLogData } = serviceCollectionLogStore;

const { copy } = useClipboard({ collectionLog });

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
  getCollectionLogData();
}
function copyLog() {
  copy(collectionLog.value);
  alert("로그를 복사했습니다.");
}
</script>
