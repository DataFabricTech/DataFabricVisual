<template>
  <vue-final-modal
    class="modal"
    content-class="modal"
    @update:model-value="(val) => emit('update:modelValue', val)"
  >
    <header class="modal-header">
      <h5 class="modal-title">
        {{ title }}
      </h5>
      <baseButton class="button-icon button-sm" title="닫기">
        <svg-icon name="close" class="svg-icon" />
        <span class="hidden-text">닫기</span>
      </baseButton>
    </header>
    <slot class="modal-body" name="body">
      <!-- 샘플코드 -->
      <p>
        Vue Final Modal is a renderless, stackable, detachable and lightweight
        modal component.
      </p>
    </slot>
    <footer class="modal-footer">
      <slot name="foot">
        <baseButton class="button-primary button-lg" @click="emit('update:modelValue', false)">
          <span class="button-text">다음</span>
        </baseButton>
      </slot>
    </footer>
  </vue-final-modal>
</template>

<script setup lang="ts">
import { VueFinalModal } from "vue-final-modal";

defineProps<{
  title?: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", modelValue: boolean): void;
}>();
</script>
