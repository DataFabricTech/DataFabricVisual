<template>
  <VueFinalModal
    @update:model-value="(val) => emits('update:modelValue', val)"
    :background="options.background"
    :escToClose="options.escToClose"
    class="modal"
    content-class="modal"
  >
    <div class="alert" :class="'alert-' + props.type">
      <div class="alert-body">
        <svg-icon :name="props.type" class="svg-icon" />
        <div class="alert-contents">
          <p class="alert__text" v-html="props.message"></p>
        </div>
      </div>
      <div class="alert-foot">
        <div class="alert-foot-main-buttons">
          <div class="alert-foot-main-buttons">
            <base-button class="button-lg button-normal" v-if="props.type === AlertType.confirm" @click="onCancel">
              <span class="button-text">취소</span>
            </base-button>
            <slot>
              <base-button class="button-lg button-primary" @click="onConfirm">
                <span class="button-text">확인</span>
              </base-button>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </VueFinalModal>
</template>

<script lang="ts" setup>
import { VueFinalModal } from "vue-final-modal";
import type { IAlertProp } from "./alert-default";
import { AlertType } from "./alert-default";
import BaseButton from "~/components/base/button.vue";

const props = defineProps<IAlertProp>();

const getInitialValues = () => ({
  teleportTo: "body",
  displayDirective: "if",
  hideOverlay: false,
  overlayTransition: "vfm-fade",
  contentTransition: "vfm-fade",
  clickToClose: true,
  escToClose: true,
  background: "interactive",
  lockScroll: true,
  reserveScrollBarGap: true,
  swipeToClose: "none"
});

const options = ref(getInitialValues());

const emits = defineEmits<{
  (e: "update:modelValue", modelValue: boolean): void;
  (e: "confirm", selectValue: boolean): void;
}>();

function onClose() {
  emits("update:modelValue", false);
}

function onConfirm() {
  emits("confirm", true);
  onClose();
}

function onCancel() {
  emits("confirm", false);
  onClose();
}
</script>
