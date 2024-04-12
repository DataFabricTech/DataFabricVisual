<template>
  <VueFinalModal
    :background="background"
    :class="dynamicModalClass"
    :click-to-close="clickToClose"
    :content-style="dynamicModalPosition"
    :content-transition="contentTransition"
    :display-directive="displayDirective"
    :esc-to-close="escToClose"
    :hide-overlay="hideOverlay"
    :lock-scroll="lockScroll"
    :modal-class="modalClass"
    :modal-id="modalId"
    :overlay-class="overlayClass"
    :overlay-transition="overlayTransition"
    :swipe-to-close="swipeToClose"
    content-class="modal modal-padding-32"
    @clickOutside="$emit('click-outside')"
    @closed="$emit('closed')"
    @opened="$emit('open')"
    @before-open="$emit('before-open')"
    @before-close="$emit('before-close')"
  >
    <div class="modal-head">
      <div class="modal-head-text">
        <span class="modal-head-title">{{ title }}</span>
        <span class="modal-head-subtitle">{{ subTitle }}</span>
      </div>
      <button class="button link-button button-sm" type="button" @click="closeModal(modalId)">
        <span class="hidden-text">닫기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
    <div class="modal-body">
      <slot />
      <p>test</p>
    </div>
    <div class="modal-foot">
      <div class="modal-foot-group">
        <button class="button button-ghost button-lg" @click="$emit('cancel', modalId)">취소</button>
        <button class="button button-primary button-lg" @click="$emit('confirm', modalId)">확인</button>
      </div>
    </div>
  </VueFinalModal>
</template>

<script lang="ts" setup>
import { VueFinalModal } from "vue-final-modal";
import { useNuxtApp } from "nuxt/app";
import { ModalProps } from "./ModalProps";
import { ModalComposition } from "./ModalComposition";

const { $vfm } = useNuxtApp();
const props = withDefaults(defineProps<ModalProps>(), {
  modalId: undefined,
  modalClass: undefined,
  displayDirective: "if",
  hideOverlay: undefined,
  overlayTransition: "vfm-fade",
  contentTransition: "vfm-fade",
  overlayClass: undefined,
  clickToClose: true,
  escToClose: true,
  background: "non-interactive",
  lockScroll: true,
  swipeToClose: "none",
  modalPosition: undefined,
  title: null,
  subTitle: null
});

const emit = defineEmits<{
  (e: "confirm", id: string | number | symbol): void;
  (e: "cancel", id: string | number | symbol): void;
  (e: "click-outside"): void;
  (e: "before-open"): void;
  (e: "open"): void;
  (e: "before-close"): void;
  (e: "closed"): void;
}>();

const {
  modalId,
  modalClass,
  displayDirective,
  hideOverlay,
  overlayClass,
  overlayTransition,
  contentTransition,
  clickToClose,
  escToClose,
  background,
  lockScroll,
  swipeToClose,
  modalPosition,
  position,
  title,
  subTitle,
  dynamicModalClass,
  dynamicModalPosition,
  closeModal
} = ModalComposition(props);
</script>