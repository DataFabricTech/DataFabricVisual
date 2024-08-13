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
            <label for="categoryAddName" class="form-label">
              이름
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <input
                id="categoryAddName"
                class="text-input text-input-lg"
                placeholder="이름을 입력하세요."
                v-model="categoryAddName"
              />
              <div
                class="notification notification-sm notification-error"
                v-if="showAddNameNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름을 입력하세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="categoryAddDesc">
              설명
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <textarea
                id="categoryAddDesc"
                class="textarea h-28"
                placeholder="설명을 입력하세요."
                v-model="categoryAddDesc"
              ></textarea>
              <div
                class="notification notification-sm notification-error"
                v-if="showAddDescNoti"
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
import { storeToRefs } from "pinia";
import { useNuxtApp } from "nuxt/app";
import { useGovernCategoryStore } from "~/store/governance/Category";
import Modal from "@extends/modal/Modal.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const categoryStore = useGovernCategoryStore();
const { $vfm } = useNuxtApp();
const { addNewCategory } = categoryStore;
const {
  categoriesParentId,
  categoryAddName,
  categoryAddDesc,
  showAddNameNoti,
  showAddDescNoti,
} = storeToRefs(categoryStore);

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
    name: categoryAddName.value,
    desc: categoryAddDesc.value,
    children: [],
  };

  addNewCategory(newNodeParam);
};

const onCancel = () => {
  $vfm.close(props.modalId);
};

const onConfirm = async () => {
  showAddNameNoti.value = categoryAddName.value === "";
  showAddDescNoti.value = categoryAddDesc.value === "";

  if (!showAddNameNoti.value && !showAddDescNoti.value) {
    await setNewNodeCategory();

    $vfm.close(props.modalId);
  }
};
</script>

<style scoped></style>
