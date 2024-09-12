<template>
  <Modal
    title="비밀번호 변경"
    class="modal modal-padding-16"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="360"
    :height="332"
    :lockScroll="false"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <pw-reset :composition="composition"></pw-reset>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { loginStore } from "~/store/login";
import { PasswordComposition } from "~/components/login/PasswordComposition";
import { useRouter } from "#vue-router";

const composition = PasswordComposition();
const router = useRouter();
const store = loginStore();
const { getPwChangeInMypage } = store;

const emit = defineEmits<{
  (e: "close"): void;
}>();
const onCancel = () => {
  composition.resetValues();
  emit("close");
};

const onConfirm = async () => {
  if (!composition.isValidatePwReset()) {
    return;
  }
  console.log("onSubmit");
  // TODO: 화면에 저장된 로그인 사용자 정보를 가져와야함. -> user store 개발 이후 적용 필요
  let param = composition.getPwResetParam();
  param = {
    ...param,
    username: "js06m12",
  };

  const result = await getPwChangeInMypage(param);
  if (result.result === 1) {
    alert("비밀번호가 변경되었습니다.");
    onCancel();
    router.push("/portal/login");
  } else {
    alert(result.errorMessage.value);
  }
};
</script>

<style scoped></style>
