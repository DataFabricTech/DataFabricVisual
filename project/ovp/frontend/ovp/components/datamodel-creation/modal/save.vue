<template>
  <Modal
    class="is-modal-no-scroll"
    title="데이터 모델 저장"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="480"
    :height="540"
    :lockScroll="true"
    swipeToClose="none"
    @before-open="onOpenModal"
    @click-outside="onCancelModal"
    @cancel="onCancelModal"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <div class="form-body">
          <div class="form-item">
            <label for="data-model-save-name" class="form-label">
              데이터 모델 이름
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <input
                id="data-model-save-name"
                class="text-input text-input-lg"
                placeholder="데이터 모델 이름을 입력하세요."
                v-model="dataModelName"
                @input="setDataModelName(dataModelName)"
              />
              <div
                class="notification notification-sm notification-error"
                v-show="isDuplicate"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">
                  중복된 데이터 모델 이름입니다.
                </p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="data-model-save-description">
              데이터 모델 설명
            </label>
            <div class="form-detail">
              <textarea
                id="data-model-save-description"
                class="textarea is-textarea-fixed h-20"
                placeholder="데이터 모델 설명을 입력하세요."
                v-model="modelDescription"
                @input="setModelDescription(modelDescription)"
              ></textarea>
            </div>
          </div>
          <div class="form-item">
            <span class="form-label"> 카테고리 </span>
            <div class="form-detail">
              <menu-search-tree
                label-key="name"
                value-key="id"
                :data="categoryList"
                :title="
                  cateTitle
                    ? cateTitle
                    : $constants.SERVICE.CATEGORY_UNDEFINED_NAME
                "
                :isResetToFirst="true"
                :is-multi="false"
                :hideGuideLines="false"
                :firExpandAll="true"
                :show-open-all-btn="false"
                :show-close-all-btn="false"
                :use-draggable="true"
                :selected-items="tree_selectedItem"
                :isShowLength="false"
                mode="view"
                @single-change="editDoneForCategory"
              ></menu-search-tree>
            </div>
          </div>
          <div class="form-item">
            <span class="form-label"> Tag </span>
            <div class="form-detail">
              <menu-search-tag
                :data="tagList"
                :selected-items="tag_selectedItem"
                label-key="displayName"
                value-key="tagFQN"
                :is-multi="true"
                title="태그 없음"
                @multiple-change="changeTags"
              ></menu-search-tag>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-slot:footer>
      <div class="modal-foot-group">
        <div
          class="notification notification-sm notification-error"
          v-show="isNameEmpty"
        >
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">필수값을 입력해주세요.</p>
        </div>
        <div
          class="notification notification-sm notification-error"
          v-show="!isQueryExecuteValid"
        >
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">
            쿼리가 정상적으로 동작하지 않습니다.
          </p>
        </div>
        <button class="button button-primary button-lg" @click="onSaveModal">
          저장
        </button>
      </div>
    </template>
  </Modal>
</template>
<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { useDataModelSaveStore } from "~/store/datamodel-creation/save";
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";
import MenuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import $constants from "~/utils/constant";
const dataModelSaveStore = useDataModelSaveStore();

const {
  dataModelName,
  modelDescription,
  isNameEmpty,
  isDuplicate,
  isQueryExecuteValid,
  cateTitle,
  categoryList,
  tagList,
  tree_selectedItem,
  tag_selectedItem,
} = storeToRefs(dataModelSaveStore);
const {
  setCateTitle,
  setDataModelName,
  setModelDescription,
  setTreeSelectionItem,
  setTagSelectionItem,
  setIsQueryExecuteValid,
  getCategoryList,
  getTagList,
  saveValidation,
  resetValidation,
  saveModel,
  editDoneForCategory,
  changeTags,
} = dataModelSaveStore;

const emit = defineEmits<{
  (e: "close"): void;
}>();

const onOpenModal = async () => {
  setCateTitle("");
  setDataModelName("");
  setModelDescription("");
  setTreeSelectionItem([]);
  setTagSelectionItem([]);
  resetValidation();
  await getCategoryList();
  await getTagList();
};

const onCancelModal = () => {
  emit("close");
};

const onSaveModal = async () => {
  await saveValidation();
  if (isDuplicate.value || isNameEmpty.value) {
    return;
  }
  await saveModel();
  if (!isQueryExecuteValid.value) {
    return;
  }
  emit("close");
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
