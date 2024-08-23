<template>
  <VueFinalModal
    :background="background"
    :class="dynamicModalClass"
    :click-to-close="clickToClose"
    :content-style="dynamicModalStyle"
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
    content-class="modal"
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
      <slot name="body" />
    </div>
    <div class="modal-foot">
      <slot name="footer">
        <div class="modal-foot-group">
          <button class="button button-ghost button-lg" @click="$emit('cancel', modalId)" v-if="useCancelBtn">
            취소
          </button>
          <button class="button button-primary button-lg" @click="$emit('confirm', modalId)">{{ confirmBtnMsg }}</button>
        </div>
      </slot>
    </div>
  </VueFinalModal>
</template>

<script lang="ts" setup>
import { VueFinalModal } from "vue-final-modal";
import { ModalProps } from "./ModalProps";
import { ModalComposition } from "./ModalComposition";

const props = withDefaults(defineProps<ModalProps>(), {
  modalId: undefined,
  modalClass: "",
  displayDirective: "if",
  hideOverlay: false,
  overlayTransition: "vfm-fade",
  contentTransition: "vfm-fade",
  overlayClass: "",
  clickToClose: true,
  escToClose: true,
  background: "non-interactive",
  lockScroll: true,
  swipeToClose: "none",
  width: 620,
  height: 180,
  title: "",
  subTitle: "",
  useCancelBtn: true,
  confirmBtnMsg: "확인"
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

const onClose = (): void => {
  emit("closed");
};

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
  title,
  subTitle,
  dynamicModalClass,
  dynamicModalStyle,
  closeModal
} = ModalComposition(props, onClose);
</script>
