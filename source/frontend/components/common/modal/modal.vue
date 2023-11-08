<template>
  <vue-final-modal
    class="modal"
    content-class="modal"
    :modal-id="props.name"
    :background="clickOutside"
    @beforeOpen="onBeforeOpen"
  >
    <header class="modal-header">
      <slot name="head">
        <h5 class="modal-title">
          {{ title }}
        </h5>
        <baseButton class="button-icon button-sm" title="닫기" @click="onClose">
          <svg-icon name="close" class="svg-icon" />
          <span class="hidden">닫기</span>
        </baseButton>
      </slot>
    </header>
    <slot class="modal-body" name="body"></slot>
    <footer class="modal-footer">
      <slot name="foot">
        <baseButton class="button-primary button-lg" @click="onClose">
          <span class="button-text">다음</span>
        </baseButton>
      </slot>
    </footer>
  </vue-final-modal>
</template>

<script setup lang="ts">
import { VueFinalModal } from "vue-final-modal";
import { useNuxtApp } from "nuxt/app";
const { $vfm } = useNuxtApp();

const props = defineProps({
  title: {
    type: String,
    default: ""
  },
  name: {
    type: String,
    default: ""
  },
  useClickOutside: {
    type: Boolean,
    default: true
  }
});
const clickOutside = computed(() => {
  let result = props.useClickOutside ? "non-interactive" : "interactive";
  console.log("useClickOutside", result);
  return result;
});
function onClose() {
  emit("close");
}
function onBeforeOpen() {
  emit("before-open");
}
const emit = defineEmits<{
  (e: "confirm"): void;
  (e: "close"): void;
  (e: "before-open"): void;
  (e: "clickOutside"): void;
}>();
</script>
