<template>
  <Modal
    title="서비스 추가"
    class="modal modal-padding-16"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="900"
    :height="674"
    :lockScroll="false"
    swipeToClose="none"
    @closed="onClosed"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template #body>
      <Step
        :data="stepOptions"
        :label-key="'label'"
        :value-key="'value'"
        :parent-index="currentStep"
        :current-item="currentStep"
        :current-item-type="'index'"
        :comparison="'equal'"
        :show-btn="false"
        @change="changeStep"
        style="width: 100%"
      />

      <step1 :style="{ display: currentStep === 1 ? 'block' : 'none' }" />

      <step2 :style="{ display: currentStep === 2 ? 'block' : 'none' }" />

      <step3 :style="{ display: currentStep === 3 ? 'block' : 'none' }" />
    </template>
    <template #footer>
      <button class="button button-neutral-ghost button-lg" @click="onCancel">
        취소
      </button>
      <div class="modal-foot-group">
        <div
          class="notification notification-sm notification-error"
          v-if="!isValid"
        >
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">{{ inValidMsg }}</p>
        </div>
        <button
          class="button button-primary-stroke button-lg"
          @click="gotoPrev"
        >
          이전
        </button>
        <button class="button button-primary button-lg" @click="gotoNext">
          {{ currentStep === 3 ? "저장" : "다음" }}
        </button>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import Step from "@extends/step/Step.vue";
import Step1 from "./step/step1.vue";
import Step2 from "./step/step2.vue";
import Step3 from "./step/step3.vue";
import _ from "lodash";

import { useNuxtApp } from "nuxt/app";
const { $vfm } = useNuxtApp();

import { ServiceAddModalComposition } from "./ServiceAddModalComposition";
import type { ServiceAddModalProps } from "./ServiceAddModalProps";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const {
  currentStep,
  inValidMsg,
  isValid,
  resetServiceObj,
  submit,
  checkValidation,
} = ServiceAddModalComposition(props);

const stepOptions: any[] = [
  { label: "서비스 타입 선택", value: 1 },
  { label: "서비스 컨피그 입력", value: 2 },
  { label: "연결 세부 정보 입력", value: 3 },
];

const changeStep = (value: number | string) => {
  // value 를 숫자로만 했기 때문에 ts 오류 방지를 위해 Number 로 형변환 한다.
  currentStep.value = Number(value) - 1;
};
const onCancel = () => {
  $vfm.close(props.modalId);
};
const onConfirm = () => {
  console.log("onConfirm");
};
const onClosed = () => {
  resetServiceObj(); // store 에 저장하고 있던 데이터 리셋
  isValid.value = true;
  inValidMsg.value = "";
  currentStep.value = 1;
};

const gotoPrev = () => {
  isValid.value = true;
  inValidMsg.value = "";
  currentStep.value = currentStep.value - 1;
};
const gotoNext = async () => {
  if (!(await checkValidation())) {
    return;
  }

  if (currentStep.value === 3) {
    gotoSave();
  } else {
    // 다음 단계로 넘어가기 전에 validation 체크를 해야함.
  }
  currentStep.value = currentStep.value + 1;
};
const gotoSave = async () => {
  if (!(await checkValidation())) {
    return;
  }

  // 저장한다.
  submit();
};
</script>

<style scoped></style>
