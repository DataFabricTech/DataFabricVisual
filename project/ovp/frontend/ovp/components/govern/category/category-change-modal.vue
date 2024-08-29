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

const categoryStore = useGovernCategoryStore();
const { patchCategoryTagAPI, setModelIdList, getModelList } = categoryStore;
const {
  categories,
  selectedModelList,
  modelList,
  lastChildIdList,
  selectedCategoryId,
} = storeToRefs(categoryStore);

const isDisabledSaveButton = ref(false);
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
  const storageList = ref([]);
  const tableList = ref([]);

  for (const modelItem of modelList.value) {
    for (const selectedItem of selectedModelList.value) {
      if (modelItem.id === selectedItem) {
        modelItem.type === "table"
          ? tableList.value.push(selectedItem)
          : storageList.value.push(selectedItem);
      }
    }
  }

  if (storageList.value.length > 0) {
    await patchCategoryTagAPI(
      tagIdForCategoryChange.value,
      "storage",
      storageList.value,
    );
  }

  if (tableList.value.length > 0) {
    await patchCategoryTagAPI(
      tagIdForCategoryChange.value,
      "table",
      tableList.value,
    );
  }

  await getModelList();
  setModelIdList();

  emit("close-category-change-modal");
};
const onOpened = () => {
  selectedNodeId.value = _.cloneDeep(selectedCategoryId.value);
};

const onCategoryNodeClick = (node: TreeViewItem) => {
  const checkAddLasChild = !lastChildIdList.value.includes(node.id);
  isDisabledSaveButton.value = checkAddLasChild;
  tagIdForCategoryChange.value = node.tagId;
};
</script>

<style scoped></style>
