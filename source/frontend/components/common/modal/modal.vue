<template>
  <vue-final-modal class="modal" content-class="modal" :modal-id="props.name">
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

const props = defineProps<{
  title?: string;
  name?: string;
}>();

function onClose() {
  $vfm.close(props.name);
}
const emit = defineEmits<{
  (e: "confirm"): void;
}>();
</script>
