<script setup lang="ts">
import { VueFinalModal } from "vue-final-modal";

defineProps<{
  title?: string
}>();

const emit = defineEmits<{
  (e: "confirm"): void
}>();
</script>

<template>
    <VueFinalModal
      class="modal-fixed"
      content-class="modal modal-padding-32"
      overlay-transition="vfm-fade"
      content-transition="vfm-fade"
    >
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">{{ title }}</span>
          <span class="modal-head-subtitle">Sub title</span>
        </div>
        <button class="button link-button button-sm" type="button">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <slot />
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-ghost button-lg">
            취소
          </button>
          <button class="button button-primary button-lg">
            확인
          </button>
        </div>
      </div>
    </VueFinalModal>
</template>

<style>

</style>
