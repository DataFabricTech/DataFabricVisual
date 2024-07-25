<template>
  <div class="upload">
    <div class="upload-file">
      <input type="file" class="upload-file-input">
      <button class="button button-neutral-stroke button-lg" :disabled="isDisabled">
        <svg-icon class="button-icon" name="upload"></svg-icon>
        <span class="button-title">File Upload</span>
      </button>
    </div>
    <div v-if="isLoadingVisible" class="upload-status">
      <Loading class="loader-xs" :use-hidden-text="true"></Loading>
    </div>
    <div v-if="isLoadedVisible" class="upload-status">
      <p class="upload-file-name">파일명.cst</p>
      <button class="button link-button button-sm">
        <span class="hidden-text">닫기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineComponent } from 'vue';

const props = defineProps({
  isLoadingVisible: {
    type: Boolean,
    default: false
  },
  isLoadedVisible: {
    type: Boolean,
    default: false
  },
  isDisabled: {
    type: Boolean,
    default: false
  }
});
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
