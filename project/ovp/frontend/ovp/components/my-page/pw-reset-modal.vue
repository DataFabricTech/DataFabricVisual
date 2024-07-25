<template>
  <!--  <div class="modal-overlay vfm&#45;&#45;fixed vfm&#45;&#45;inset" v-if="showModalPwChange">-->
  <!--    -->
  <!--    -->
  <!--  </div>-->
  <Modal
    title="비밀번호 변경"
    class="modal modal-padding-16"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="350"
    :lockScroll="false"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <div class="form-body">
          <div class="form-item">
            <label for="new-password" class="form-label">
              신규 비밀번호
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <div class="search-input">
                <input
                  id="new-password"
                  class="text-input text-input-lg"
                  type="password"
                  placeholder="비밀번호 입력"
                />
                <!-- TODO : [개발] 비밀번호 보기와 보기해제 on/off 필요(hidden-text와 아이콘 체인지)-->
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">비밀번호 보기 해제</span>
                  <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                </button>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">안내문구 출력</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label for="password-check" class="form-label">
              비밀번호 확인
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <div class="search-input">
                <input
                  id="password-check"
                  class="text-input text-input-lg"
                  type="password"
                  placeholder="비밀번호 확인"
                />
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">비밀번호 보기</span>
                  <svg-icon class="button-icon" name="eye"></svg-icon>
                </button>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">안내문구 출력</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";

const onCancel = () => {
  console.log("onCancel");
};
const onConfirm = () => {
  console.log("onConfirm");
};

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});
</script>

<style scoped></style>
