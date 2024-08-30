<template>
  <div class="form-body">
    <div class="form-item">
      <label for="new-password" class="form-label">
        신규 비밀번호
        <span class="required">*</span>
      </label>
      <div class="form-detail flex flex-col">
        <div class="text-input-group w-full">
          <input
            id="inpNewResetPw"
            :type="composition.inputPasswordType.value"
            class="text-input text-input-lg"
            placeholder="비밀번호 입력"
            v-model="composition.newPassword.value"
          />
          <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
          <button
            class="text-input-group-action-button button button-neutral-ghost button-sm"
            type="button"
            @click="composition.isHidePw"
          >
            <span class="hidden-text">비밀번호 보기 해제</span>
            <svg-icon
              class="button-icon"
              :name="
                getPwdIconName({
                  inputType: composition.inputPasswordType.value,
                })
              "
            ></svg-icon>
          </button>
        </div>
        <div
          class="notification notification-sm notification-error"
          v-if="composition.errorMsgPassword.value"
        >
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">
            {{ composition.errorMsgPassword.value }}
          </p>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="password-check" class="form-label">
        비밀번호 확인
        <span class="required">*</span>
      </label>
      <div class="form-detail flex flex-col">
        <div class="text-input-group w-full">
          <input
            id="inpConfirmPw"
            :type="composition.inputConfirmPasswordType.value"
            class="text-input text-input-lg"
            placeholder="비밀번호 입력"
            v-model="composition.confirmPassword.value"
          />
          <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
          <button
            class="text-input-group-action-button button button-neutral-ghost button-sm"
            type="button"
            @click="composition.isHideConfirmPw"
          >
            <span class="hidden-text">비밀번호 보기</span>
            <svg-icon
              class="button-icon"
              :name="
                getPwdIconName({
                  inputType: composition.inputConfirmPasswordType.value,
                })
              "
            ></svg-icon>
          </button>
        </div>
        <div
          class="notification notification-sm notification-error"
          v-if="composition.errorMsgConfirmPassword.value"
        >
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">
            {{ composition.errorMsgConfirmPassword.value }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useCommonUtils } from "@/composables/commonUtils";

const { getPwdIconName } = useCommonUtils();

const props = defineProps({
  composition: {
    type: Object,
    default: true,
  },
});
</script>
<style lang="scss" scoped></style>
