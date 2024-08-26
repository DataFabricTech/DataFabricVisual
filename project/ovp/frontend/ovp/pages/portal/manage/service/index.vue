<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>

  <div class="section-contents p-0 bg-white overflow-auto">
    <div class="l-split">
      <service-list
        :add-modal-id="SERVICE_ADD_MODAL_ID"
        @modalOpen="modalOpen"
      ></service-list>
      <service-info></service-info>
    </div>
  </div>
  <modal-collection></modal-collection>
  <modal-log></modal-log>

  <modal-service />
</template>

<style scoped></style>

<script setup lang="ts">
import ModalCollection from "~/components/manage/service/modal/modal-collection.vue";
import ModalLog from "~/components/manage/service/modal/modal-log.vue";
import ModalService from "~/components/manage/service/modal/modal-service/modal-service.vue";
import { useModal } from "vue-final-modal";
import { useServiceStore } from "~/store/manage/service";

const SERVICE_ADD_MODAL_ID: string = "service-add-modal";

const { getServiceList } = useServiceStore();

const { open, close } = useModal({
  component: ModalService,
  attrs: {
    onClose() {
      close();
    },
    onLoadData() {
      getServiceList();
    },
  },
});

const modalOpen = (modalId: string) => {
  if (modalId === SERVICE_ADD_MODAL_ID) {
    open();
  }
};
</script>
