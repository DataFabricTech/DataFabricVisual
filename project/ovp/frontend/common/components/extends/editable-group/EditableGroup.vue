<template>
  <div class="editable-group">
    <!-- 수정모드 -->
    <template v-if="isEditMode">
      <label class="hidden-text" for="description-modify">내용 입력</label>
      <slot name="edit-slot"></slot>
      <div class="h-group gap-2 shrink-0" v-if="useEditButtons">
        <button class="button button-neutral-lighter button-sm" type="button" @click="cancelClick">취소</button>
        <button class="button button-primary-lighter button-sm" type="button" @click="doneClick">완료</button>
      </div>
    </template>
    <template v-else>
      <!-- view 모드 -->
      <slot name="view-slot"></slot>
      <template>
        <button class="button button-neutral-ghost button-sm" type="button" @click="editIconClick">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </template>
    </template>
  </div>
  <!-- TODO : [개발] text-input > 미입력시에만 적용이 필요합니다. -->
  <div class="notification notification-error" v-if="false">
    <svg-icon class="notification-icon" name="error"></svg-icon>
    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { EditableGroupProps } from "./EditableGroupProps";

const props = withDefaults(defineProps<EditableGroupProps>(), {
  partKey: "",
  useEditButtons: true
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
