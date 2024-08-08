<template>
  <div class="editable-group">
    <!-- edit 모드 -->
    <template v-if="isEditMode">
      <slot name="edit-slot"></slot>
      <div class="h-group gap-2 shrink-0" v-if="useEditButtons">
        <button class="button button-neutral-lighter" type="button" @click="cancelClick">취소</button>
        <button class="button button-primary-lighter" type="button" @click="doneClick">완료</button>
      </div>
    </template>
    <template v-else>
      <!-- view 모드 -->
      <slot name="view-slot"></slot>
      <template v-if="editable">
        <button class="button button-neutral-ghost button-sm" type="button" @click="editIconClick">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </template>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { EditableGroupProps } from "./EditableGroupProps";

const props = withDefaults(defineProps<EditableGroupProps>(), {
  useEditButtons: true,
  editable: false,
  parentEditMode: false
});

/**
 * isEditMode 변수를 이용하여 해당 컴포넌트 내에서 상태값 관리를 하고 있으나,
 * edit slot 에 특정 컴포넌트를 사용하고, 그 컴포넌트의 특정 동작을 이용하여 상태값 관리를 해야 하는 경우가 있음.
 * 그 경우 상태값을 props 로 받아서 처리하게 한다.
 */
const isEditMode = ref(props.parentEditMode);
watch(
  () => props.parentEditMode,
  (newVal) => {
    isEditMode.value = newVal;
  }
);

const emit = defineEmits<{
  (e: "editCancel", key: string): void;
  (e: "editDone", key: string): void;
  (e: "editIcon", key: string): void;
}>();

const cancelClick = () => {
  isEditMode.value = false;
  emit("editCancel", props.compKey);
};
const doneClick = () => {
  isEditMode.value = false;
  emit("editDone", props.compKey);
};
const editIconClick = () => {
  isEditMode.value = true;
  emit("editIcon", props.compKey);
};
</script>

<style lang="scss" scoped></style>
