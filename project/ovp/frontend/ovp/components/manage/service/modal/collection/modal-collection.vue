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
    :height="560"
    swipeToClose="none"
    @before-open="onBeforeOpen"
    @closed="onClosed(false)"
    @cancel="onClosed(false)"
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
        :is-show="currentStep === 1"
        :serviceType="serviceType"
        :pipelineType="pipelineType"
      />
      <schedule-step :is-show="currentStep === 2" />
    </template>
    <template v-slot:footer>
      <button
        class="button button-neutral-ghost button-lg"
        @click="onClosed(false)"
      >
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

import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add";
const collectionAddStore = useServiceCollectionAddStore();
const {
  isEditModalStatus,
  pipelineType,
  serviceType,
  modalTitle,
  isValid,
  inValidMsg,
} = storeToRefs(collectionAddStore);
const {
  setIsValidCronParsedMessage,
  setInvalidMessage,
  setIsValid,
  setCronExpression,
  resetData,
  checkValidation,
  createIngestion,
  editIngestion,
} = collectionAddStore;

const emit = defineEmits<{
  (e: "close"): void;
  (e: "onLoadData"): void;
}>();

const onClosed = (data: any) => {
  currentStep.value = 1;
  resetData();
  emit("close", data);
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
    if (isEditModalStatus.value) {
      await editIngestion();
    } else {
      await createIngestion();
    }

    if (!isValid.value) {
      return;
    }
    onClosed(true);
  } else {
    currentStep.value = currentStep.value + 1;
  }
};
</script>
