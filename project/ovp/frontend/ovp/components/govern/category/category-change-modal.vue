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
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { ref } from "vue";

const categoryStore = useGovernCategoryStore();

const { $vfm } = useNuxtApp();
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

const onNodeClicked = () => {
  console.log("onNodeClick");
};

// TODO: [개발] 임시 추가 - dropValidator 기능 불필요(warning으로 추가해 둠)tree 컴포넌트에서 미사용할 수 있는지 검토요청
let nodeMoved: Ref<boolean> = ref(false);
let dropMsg: Ref<any> = ref(null);
watch(
  () => nodeMoved.value,
  (newVal) => {
    if (newVal) {
      if (dropMsg.value !== null) {
        alert(dropMsg.value);
      }
    }
    nodeMoved.value = false;
    dropMsg.value = null;
  },
);
const dropValidator = async (
  dropNode: TreeViewItem,
  targetNode: TreeViewItem,
): Promise<boolean> => {
  // 조건 처리 backend 에서 진행
  dropMsg.value = null;
  nodeMoved.value = true;
  // tree lib가 async-await 처리를 지원하지 않기 때문에 여기서는 true 로 던지고,
  // backend 동작이 끝나면 그때 결과에 따라 watch 항목에서 alert 처리, 목록을 갱신 or 유지 한다
  return true;
};
</script>

<style scoped></style>
