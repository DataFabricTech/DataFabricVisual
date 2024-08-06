<template>
  <Modal
    title="카테고리 변경"
    class="modal modal-padding-16"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="350"
    :height="450"
    :top="410"
    :lockScroll="false"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <tree-vue
        :items="categories"
        :isCheckable="false"
        :hideGuideLines="false"
        :firExpandAll="true"
        :show-open-all-btn="true"
        :show-close-all-btn="true"
        :use-draggable="true"
        mode="view"
        :dropValidator="dropValidator"
        @onItemSelected="onNodeClicked"
        @addSibling="addSibling"
        @addChild="addChild"
      />
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { useNuxtApp } from "nuxt/app";
import TreeVue from "@extends/tree/Tree.vue";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { storeToRefs } from "pinia";

const categoryStore = useGovernCategoryStore();

const { $vfm } = useNuxtApp();
const { dropValidator, onNodeClicked, addSibling, addChild } = categoryStore;
const { categories } = storeToRefs(categoryStore);

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

const onCancel = () => {
  $vfm.close(props.modalId);
};

const onConfirm = async () => {
  $vfm.open(props.modalId);
};
</script>

<style scoped></style>
