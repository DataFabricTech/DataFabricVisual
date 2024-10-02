<template>
  <div class="alert-overlay" v-if="isModeless"></div>
  <div class="alert center" :style="{ width: width + 'px' }" v-if="isVisible">
    <div class="alert-body">
      <!-- TODO:[개발] 기획변경으로 노티피케이션 생략하고 일반 텍스트 추가하였습니다. -->
      <p class="alert-text">
        {{ message }}
      </p>
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
<style scoped></style>
