<template>
  <Modal
    title="카테고리 추가"
    class="modal modal-padding-16"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="480"
    :height="400"
    :top="380"
    :lockScroll="false"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <div class="form-body">
          <div class="form-item">
            <label for="categoryName" class="form-label">
              이름
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <input
                id="categoryName"
                class="text-input text-input-lg"
                placeholder="이름을 입력하세요."
                v-model="categoryName"
              />
              <div
                class="notification notification-sm notification-error"
                v-if="isShowCategoryNameNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름을 입력하세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="categoryDesc">
              설명
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <textarea
                id="categoryDesc"
                class="textarea h-28"
                placeholder="설명을 입력하세요."
                v-model="categoryDesc"
              ></textarea>
              <div
                class="notification notification-sm notification-error"
                v-if="isShowCategoryDescNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">설명을 입력하세요.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import { uuid } from "vue3-uuid";
import Modal from "@extends/modal/Modal.vue";
import { useNuxtApp } from "nuxt/app";
import { useGovernCategoryStore } from "~/store/governance/Category";
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { storeToRefs } from "pinia";

const categoryStore = useGovernCategoryStore();
const { $vfm } = useNuxtApp();
const { addNewCategory } = categoryStore;
const { categoriesParentId } = storeToRefs(categoryStore);

const categoryName = ref<string>("");
const isShowCategoryNameNoti = ref<boolean>(false);
const categoryDesc = ref<string>("");
const isShowCategoryDescNoti = ref<boolean>(false);

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

const setNewNodeCategory = () => {
  const newNodeParam: TreeViewItem = {
    id: uuid.v4(),
    parentId: categoriesParentId.value,
    name: categoryName.value,
    desc: categoryDesc.value,
    children: [],
  };
  addNewCategory(newNodeParam);
};

const resetModalStatus = () => {
  categoryName.value = "";
  isShowCategoryNameNoti.value = false;
  categoryDesc.value = "";
  isShowCategoryDescNoti.value = false;
};

const onCancel = () => {
  $vfm.close(props.modalId);
  resetModalStatus();
};

const onConfirm = async () => {
  isShowCategoryNameNoti.value = categoryName.value === "" ? true : false;
  isShowCategoryDescNoti.value = categoryDesc.value === "" ? true : false;

  if (!isShowCategoryNameNoti.value && !isShowCategoryDescNoti.value) {
    setNewNodeCategory();

    $vfm.close(props.modalId);

    resetModalStatus();
  }
};
</script>

<style scoped></style>
