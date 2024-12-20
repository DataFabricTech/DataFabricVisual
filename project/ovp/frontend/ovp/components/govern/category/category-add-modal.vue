<template>
  <Modal
    title="카테고리 추가"
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
    :confirm-btn-msg="'저장'"
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
                maxlength="20"
              />
              <div
                class="notification notification-sm notification-error"
                v-if="showAddNameNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">{{ categoryNameMsg }}</p>
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
import { useGovernCategoryStore } from "~/store/governance/Category";
import $constants from "@/utils/constant";
import Modal from "@extends/modal/Modal.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const categoryStore = useGovernCategoryStore();
const { addNewCategory } = categoryStore;
const {
  categoriesParentId,
  categoriesId,
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

const emit = defineEmits<{
  (e: "close-category-add-modal"): void;
}>();

const categoryNameMsg = ref("");

const setNewNodeCategory = () => {
  const newNodeParam: TreeViewItem = {
    id: categoriesId.value !== "" ? categoriesId.value : uuid.v4(),
    parentId: categoriesParentId.value,
    name: categoryAddName.value,
    desc: categoryAddDesc.value,
    children: [],
  };

  addNewCategory(newNodeParam);
};

const onCancel = () => {
  emit("close-category-add-modal");
};

const onConfirm = async () => {
  if (categoryAddName.value === "") {
    categoryNameMsg.value = $constants.GOVERNANCE.TITLE.EMPTY_ERROR_MSG;
    showAddNameNoti.value = true;
    return;
  }

  if (categoryAddName.value.length === 1) {
    categoryNameMsg.value =
      $constants.GOVERNANCE.TITLE.MINIMUM_LENGTH_ERROR_MSG;
    showAddNameNoti.value = true;
    return;
  }

  if (!$constants.GOVERNANCE.TITLE.REGEX.test(categoryAddName.value)) {
    categoryNameMsg.value = $constants.GOVERNANCE.TITLE.REGEX_ERROR_MSG;
    showAddNameNoti.value = true;
    return;
  }

  showAddNameNoti.value = false;

  if (categoryAddDesc.value === "") {
    showAddDescNoti.value = true;
    return;
  }

  showAddDescNoti.value = false;

  await setNewNodeCategory();
  emit("close-category-add-modal");
};
</script>

<style scoped></style>
