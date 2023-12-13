<template>
  <button
    :class="checkSubmitType ? null : 'button'"
    :type="props.type"
    :title="props.title"
    :id="checkSubmitType ? props.submitId : null"
    :disabled="pDisabled"
    @click="handleClick($event)"
    @mousedown="mouseDown($event)"
  >
    <slot></slot>
  </button>
</template>

<script setup lang="ts">
import { defineEmits, defineProps, computed } from "vue";

const props = defineProps({
  submitId: { type: String, default: "" },
  type: { type: String, default: "button" },
  title: { type: String, default: null },
  disabled: { type: Boolean, default: false }
});

const checkSubmitType = computed(() => {
  return props.type === "submit" && props.submitId !== "";
});
const pDisabled = computed(() => {
  return props.disabled;
})
const emit = defineEmits<{
  (e: "click", evt: PointerEvent): void;
  (e: "mousedown", evt: MouseEvent): void;
}>();
function handleClick($event: PointerEvent) {
  emit("click", $event);
}
function mouseDown($event: MouseEvent) {
  emit("mousedown", $event);
}
</script>
