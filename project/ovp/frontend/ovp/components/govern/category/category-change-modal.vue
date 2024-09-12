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
import Modal from "@extends/modal/Modal.vue";
import TreeVue from "@extends/tree/Tree.vue";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { storeToRefs } from "pinia";
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { ref } from "vue";
import _ from "lodash";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";

const categoryStore = useGovernCategoryStore();
const { patchCategoryTagAPI, setModelIdList, getModelList, addSearchList } =
  categoryStore;
const {
  categories,
  selectedModelList,
  modelList,
  selectedCategoryId,
  isShowPreview,
} = storeToRefs(categoryStore);

const isDisabledSaveButton = ref(true);
const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});
const tagIdForCategoryChange = ref("");
const selectedNodeId = ref("");

const emit = defineEmits<{
  (e: "close-category-change-modal"): void;
}>();

const onCancel = () => {
  emit("close-category-change-modal");
};

const onConfirm = async () => {
  let storageList: string[] = [];
  let tableList: string[] = [];

  for (const modelItem of modelList.value) {
    for (const selectedItem of selectedModelList.value) {
      if (modelItem.id === selectedItem) {
        if (modelItem.type === "table") {
          tableList.push(selectedItem);
        } else {
          storageList.push(selectedItem);
        }
      }
    }
  }

  if (storageList.length > 0) {
    await patchCategoryTagAPI(
      tagIdForCategoryChange.value,
      "storage",
      storageList,
    );
  }

  if (tableList.length > 0) {
    await patchCategoryTagAPI(tagIdForCategoryChange.value, "table", tableList);
  }

  setScrollOptions(0);
  await getModelList();
  setModelIdList();

  isShowPreview.value = false;
  emit("close-category-change-modal");
};
const onOpened = () => {
  selectedNodeId.value = _.cloneDeep(selectedCategoryId.value);
};

const onCategoryNodeClick = (node: TreeViewItem) => {
  isDisabledSaveButton.value =
    _.has(node, "children") && _.size(node.children) > 0;
  tagIdForCategoryChange.value = node.tagId;
};

const { setScrollOptions } = useIntersectionObserver({
  callback: addSearchList,
});
</script>

<style scoped></style>
