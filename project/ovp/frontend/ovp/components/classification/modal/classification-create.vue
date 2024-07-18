<template>
  <div class="modal-overlay vfm--fixed vfm--inset">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">분류 추가</span>
        </div>
        <button
            class="button link-button button-sm"
            type="button"
            @click="closeModalClas"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                    id="data-model-save-name"
                    class="text-input text-input-lg"
                    placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                    id="data-model-save-description"
                    class="textarea h-28"
                    placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
              class="button button-neutral-ghost button-lg"
              @click="closeModalClas"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from 'vue';

const emit = defineEmits(['close-modal']);

const closeModalClas = () => {
  emit('close-modal');
};
</script>

<style lang="scss" scoped></style>
