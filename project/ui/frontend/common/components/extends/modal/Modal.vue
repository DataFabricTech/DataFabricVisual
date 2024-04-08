<template>
  <VueFinalModal
    :background="background"
    :class="modalClass"
    :click-to-close="clickToClose"
    :content-class="contentClass"
    :content-transition="contentTransition"
    :display-directive="displayDirective"
    :esc-to-close="escToClose"
    :hide-overlay="hideOverlay"
    :lock-scroll="lockScroll"
    :modal-id="modalId"
    :overlay-class="overlayClass"
    :overlay-transition="overlayTransition"
    :swipe-to-close="swipeToClose"
    @clickOutside="$emit('click-outside')"
    @closed="$emit('closed')"
    @opened="$emit('open')"
    @before-open="$emit('before-open')"
    @before-close="$emit('before-close')"
  >
    <ClientOnly v-if="dragAndResize">
      <VueDragResize
        :h="position.height"
        :is-active="true"
        :w="position.width"
        class="bg-100"
        @dragging="dragResize"
        @resizing="dragResize"
      >
        <header class="modal-header">
          <slot name="head"></slot>
        </header>
        <body class="modal-body">
          <slot name="body"></slot>
        </body>
        <footer class="modal-footer">
          <slot name="foot"></slot>
        </footer>
        <p>{{ position.top }} х {{ position.left }}</p>
        <p>{{ position.width }} х {{ position.height }}</p>
      </VueDragResize>
    </ClientOnly>
    <template v-if="!dragAndResize">
      <header class="modal-header">
        <slot name="head"></slot>
      </header>
      <body class="modal-body">
        <slot name="body"></slot>
      </body>
      <footer class="modal-footer">
        <slot name="foot"></slot>
      </footer>
    </template>
  </VueFinalModal>
</template>

<script lang="ts" setup>
import { VueFinalModal } from "vue-final-modal";
import { useNuxtApp } from "nuxt/app";
import "vue-final-modal/style.css";
import { ModalProps } from "@/components/extends/modal/ModalProps";
import { ModalComposition } from "@/components/extends/modal/ModalComposition";

const { $vfm } = useNuxtApp();
const props = withDefaults(defineProps<ModalProps>(), {
  modalId: undefined,
  // TODO: 디폴트 값 변경
  modalClass: "custom-modal",
  contentClass: "content-modal",
  displayDirective: "if",
  hideOverlay: undefined,
  overlayTransition: undefined,
  contentTransition: undefined,
  overlayClass: undefined,
  clickToClose: true,
  escToClose: true,
  background: "non-interactive",
  lockScroll: true,
  swipeToClose: "none",
  dragAndResize: false,
  modalElementPosition: () => ({
    width: 300,
    height: 300,
    top: 0,
    left: 0
  })
});

const emit = defineEmits<{
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
  contentClass,
  contentTransition,
  clickToClose,
  escToClose,
  background,
  lockScroll,
  swipeToClose,
  dragAndResize,
  modalElementPosition,
  position,
  dragResize
} = ModalComposition(props);
</script>

<style scoped>
.custom-modal {
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-modal {
  display: flex;
  flex-direction: column;
  max-width: 28rem; /* Tailwind의 max-w-xl은 28rem입니다. */
  margin-left: 1rem; /* Tailwind의 mx-4은 왼쪽/오른쪽 여백에 1rem을 사용합니다. */
  margin-right: 1rem;
  padding: 1rem; /* Tailwind의 p-4는 모든 방향의 padding에 1rem을 사용합니다. */
  background-color: #fff; /* Tailwind의 bg-white는 #fff입니다. */
  border: 1px solid #e5e7eb; /* Tailwind의 border는 1px의 solid #e5e7eb입니다. */
  border-radius: 0.375rem; /* Tailwind의 rounded-lg는 border-radius: 0.375rem입니다. */
  gap: 0.5rem; /* Tailwind의 space-y-2는 요소들 사이의 간격을 0.5rem으로 설정합니다. */
}

.button {
  margin-top: 0.25rem; /* Tailwind의 mt-1은 0.25rem입니다. */
  margin-left: auto; /* Tailwind의 ml-auto는 왼쪽 여백을 자동으로 설정합니다. */
  padding-left: 0.5rem; /* Tailwind의 px-2는 좌우 padding에 0.5rem을 사용합니다. */
  padding-right: 0.5rem;
  border-width: 1px; /* Tailwind의 border는 1px의 테두리를 가집니다. */
  border-style: solid; /* Tailwind의 border는 solid 스타일을 가집니다. */
  border-radius: 0.375rem; /* Tailwind의 rounded-lg는 border-radius: 0.375rem입니다. */
}

.modal-header {
  background-color: deepskyblue; /* 연보라색 배경색 설정 */
  padding: 10px;
}

.modal-body {
  background-color: white; /* 검정색 텍스트 색상 설정 */
}

.modal-footer {
  background-color: deepskyblue; /* 연보라색 배경색 설정 */
  padding: 10px;
}

/* 추가로 헤더와 푸터에 흰색 텍스트 색상을 설정합니다 */
.modal-header h5,
.modal-footer h3 {
  color: #ffffff; /* 흰색 텍스트 색상 설정 */
}
</style>