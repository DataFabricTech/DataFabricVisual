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
    :is-disabled-confirm-btn="isDisabledSaveButton"
  >
    <template v-slot:body>
      <!--      TODO: [개발] 현재 내 카테고리(selectedCategoryId)가 비활성화 상태로 보여져야 함-->
      <!--      TODO: [개발] 변경 시 카테고리 화면의 트리도 함께 이동함-->
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
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { ref } from "vue";

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

const onCategoryNodeClick = (node: TreeViewItem) => {
  const checkAddLasChild = !lastChildIdList.value.includes(node.id);
  isDisabledSaveButton.value = checkAddLasChild;
  tagIdForCategoryChange.value = node.tagId;
};
</script>

<style scoped></style>
