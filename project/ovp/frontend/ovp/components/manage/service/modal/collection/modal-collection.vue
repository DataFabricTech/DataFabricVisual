<template>
  <Modal
    :title="modalTitle"
    class="modal modal-padding-16"
    :lockScroll="false"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="620"
    :height="580"
    swipeToClose="none"
    @before-open="onBeforeOpen"
    @cancel="onCancel"
    @closed="onClosed"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <Step
        :data="stepOptions"
        label-key="label"
        value-key="value"
        :parent-index="currentStep"
        :current-item="currentStep"
        :current-item-type="'index'"
        :comparison="'equal'"
        :show-btn="false"
        :use-button-click="false"
        @change="changeStep"
      >
      </Step>
      <config-step
        :style="{ display: currentStep === 1 ? 'block' : 'none' }"
        :serviceType="serviceType"
        :pipelineType="pipelineType"
      />
      <schedule-step
        :style="{ display: currentStep === 2 ? 'block' : 'none' }"
      />
    </template>
    <template v-slot:footer>
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
          v-show="currentStep > 1"
          class="button button-primary-stroke button-lg"
          @click="gotoPrev"
        >
          이전
        </button>
        <button class="button button-primary button-lg" @click="gotoNext">
          {{ currentStep === 2 ? "저장" : "다음" }}
        </button>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Modal from "@extends/modal/Modal.vue";
import Step from "@extends/step/Step.vue";
import ConfigStep from "./step/config-step.vue";
import ScheduleStep from "./step/schedule-step.vue";
const { $vfm } = useNuxtApp();

import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add";
const collectionAddStore = useServiceCollectionAddStore();
const { pipelineType, serviceType, modalTitle, isValid, inValidMsg } =
  storeToRefs(collectionAddStore);
const {
  setIsValidCronParsedMessage,
  setIsValidCronParedMessage,
  setInvalidMessage,
  setIsValid,
  setCronExpression,
  resetData,
  checkValidation,
  createIngestion,
} = collectionAddStore;

const emit = defineEmits<{
  (e: "close"): void;
}>();

const onCancel = () => {
  resetData();
  emit("close");
};

const onClosed = () => {
  currentStep.value = 1;
  resetData();
  emit("close");
};

const onConfirm = () => {
  console.log("onConfirm");
};

const onBeforeOpen = () => {
  currentStep.value = 1;
};

/**
 * STEP
 */
const stepOptions: any = [
  { label: "컨피그수집", value: 1 },
  { label: "스케줄구간수정", value: 2 },
];

const currentStep = ref<number>(1); // 첫 번째 스텝부터 시작

const changeStep = (value: number) => {
  currentStep.value = Number(value) - 1;
};

const gotoPrev = () => {
  if (currentStep.value === 2) {
    setIsValidCronParsedMessage(true);
    setInvalidMessage("");
    setIsValid(true);
    setCronExpression("0 0 * * *");
  }
  currentStep.value = currentStep.value - 1;
};

const gotoNext = async () => {
  // validation 체크
  await checkValidation();

  if (!isValid.value) {
    return;
  }

  if (currentStep.value === 2) {
    await createIngestion();
    if(!isValid.value) {
      return;
    }
    onCancel();
    return;
  } else {
    currentStep.value = currentStep.value + 1;
  }
};
</script>
