<template>
  <Modal
    title="카테고리 변경"
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
        :use-draggable="false"
        mode="view"
        @onItemSelected="onCategoryNodeClick"
      />
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import TreeVue from "@extends/tree/Tree.vue";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { storeToRefs } from "pinia";

const categoryStore = useGovernCategoryStore();

const { categories, selectedModelList } = storeToRefs(categoryStore);

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});
const emit = defineEmits<{
  (e: "close-category-change-modal"): void;
}>();
const onCancel = () => {
  emit("close-category-change-modal");
};

const onConfirm = async () => {
  emit("close-category-change-modal");
};

const onCategoryNodeClick = () => {
  // TODO: [API 개발] 모델 리스트 삭제/변경 API 수정
  // NOTE: 변경하려고 하는 카테고리의 tag 정보를, 내가 선택한 모델 리스트 쿼리에 저장한다.
};
</script>

<style scoped></style>
