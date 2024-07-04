<template>
  <div class="resource-box-group">
    <!-- 수정모드 -->
    <template v-if="isEditMode">
      <label class="hidden-text" for="description-modify"
        >데이터 모델 설명 입력</label
      >
      <slot name="edit-slot"></slot>
      <div class="h-group gap-2 shrink-0" v-if="useEditButtons">
        <button
          class="button button-neutral-lighter button-sm"
          type="button"
          @click="cancelClick"
        >
          취소
        </button>
        <button
          class="button button-primary-lighter button-sm"
          type="button"
          @click="doneClick"
        >
          완료
        </button>
      </div>
    </template>
    <template v-else>
      <!-- view 모드 -->
      <slot name="view-slot"></slot>
      <template v-if="props.editable">
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="editIconClick"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </template>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { ResourceBoxProps } from "./resource-box-props";
const props = withDefaults(defineProps<ResourceBoxProps>(), {
  partKey: "",
  useEditButtons: true,
});

const emit = defineEmits<{
  (e: "editCancel", id: string): void;
  (e: "editDone", id: string): void;
  (e: "editIcon", id: string): void;
}>();

const cancelClick = () => {
  emit("editCancel", props.partKey);
};
const doneClick = () => {
  emit("editDone", props.partKey);
};
const editIconClick = () => {
  emit("editIcon", props.partKey);
};
</script>

<style lang="scss" scoped></style>
