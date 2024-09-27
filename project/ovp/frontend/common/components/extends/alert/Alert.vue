<template>
  <div class="alert-overlay" v-if="isModeless"></div>
  <div class="alert center" :style="{ width: width + 'px' }" v-if="isVisible">
    <div class="alert-body">
      <div :class="`notification notification-${type}`">
        <svg-icon class="notification-icon" :name="type"></svg-icon>
        <p class="notification-detail">{{ message }}</p>
      </div>
    </div>
    <div class="alert-foot">
      <div class="alert-foot-group">
        <button v-if="isConfirm" class="button button-neutral-ghost button-lg" @click="handleCancel">
          {{ cancelButtonText }}
        </button>
        <button class="button button-primary button-lg" @click="handleConfirm">{{ confirmButtonText }}</button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useAlert } from "./useAlert";

// useAlert에서 필요한 상태와 메서드를 가져옵니다.
const {
  isModeless,
  isVisible,
  width,
  type,
  message,
  confirmButtonText,
  cancelButtonText,
  isConfirm,
  handleConfirm,
  handleCancel
} = useAlert();
</script>
<style scoped>
.alert-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5);
  z-index: 10000;
}
.center {
  position: absolute;
  z-index: 11000;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
</style>
