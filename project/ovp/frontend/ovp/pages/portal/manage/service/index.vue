<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>
  <div class="section-top-bar section-top-bar-tab">
    <tab
      class="tab-line"
      :data="tabOptions"
      :labelKey="'label'"
      :valueKey="'value'"
      :useTabContents="false"
      @change="changeTab"
      :current-item="tabOptions[0].value"
      :current-item-type="'value'"
    >
    </tab>
  </div>
  <!--      OVERVIEW-->
  <div class="section-contents overflow-auto" v-if="currentTab === 'overview'">
    <overview></overview>
  </div>
  <!--      SERVICE-->
  <div
    class="section-contents p-0 bg-white overflow-auto"
    v-if="currentTab === 'service'"
  >
    <div class="l-split">
      <service-list
        :add-modal-id="SERVICE_ADD_MODAL_ID"
        @modalOpen="modalOpen"
      ></service-list>
      <service-info></service-info>
    </div>
  </div>

  <modal-log></modal-log>
</template>

<style scoped></style>

<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import Overview from "~/components/manage/service/overview.vue";
import ModalLog from "~/components/manage/service/modal/log.vue";
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

// Tab
const currentTab = ref("overview");
const tabOptions = [
  { label: "Overview", value: "overview" },
  { label: "서비스", value: "service" },
];
const changeTab = async (item: string) => {
  currentTab.value = item;
};
</script>
