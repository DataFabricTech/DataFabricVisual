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
    @open="onOpened"
    @cancel="onCancel"
    @confirm="onConfirm"
    :is-disabled-confirm-btn="isDisabledSaveButton"
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
        :selected-ids="[selectedNodeId]"
        :disabled-ids="[selectedNodeId]"
        :use-fir-select="false"
        comp-id="modalTreeComponent"
        mode="view"
        @onItemSelected="onCategoryNodeClick"
      />
    </template>
  </Modal>
</template>

<script setup lang="ts">
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { useGovernCategoryStore } from "@/store/governance/Category";
import { useDataModelTag } from "@/store/governance/common/modal/data-model";
import Modal from "@extends/modal/Modal.vue";
import TreeVue from "@extends/tree/Tree.vue";
import _ from "lodash";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const categoryStore = useGovernCategoryStore();
const { addSearchList } = categoryStore;
const { categories, selectedModelList, selectedCategoryId } =
  storeToRefs(categoryStore);
const dataModelTagStore = useDataModelTag();
const { changeDataModelsCategory } = dataModelTagStore;
const { tagId } = storeToRefs(dataModelTagStore);

import { useNuxtApp } from "nuxt/app";
const { $alert } = useNuxtApp();

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

const emit = defineEmits<{
  (e: "close"): void;
  (e: "confirm"): void;
}>();

const isDisabledSaveButton = ref(true);
const selectedNodeId = ref("");

const onOpened = () => {
  selectedNodeId.value = _.cloneDeep(selectedCategoryId.value);
};

const onCategoryNodeClick = (node: TreeViewItem) => {
  isDisabledSaveButton.value =
    _.has(node, "children") && _.size(node.children) > 0;
  tagId.value = node.tagId;
};

const onConfirm = async () => {
  if (selectedModelList.value.length > 20) {
    $alert("선택된 데이터 모델이 많아 시간이 소요될 수 있습니다.", "info");
  }

  await changeDataModelsCategory().then(async (isSuccess) => {
    if (isSuccess) {
      setScrollOptions(0);
      emit("confirm");
    } else {
      $alert("저장 실패했습니다. 잠시 후 다시 시도해주세요.", "error");
    }
  });
};

const onCancel = () => {
  emit("close");
};

const { setScrollOptions } = useIntersectionObserver({
  callback: addSearchList,
});
</script>

<style scoped></style>
