<!--
  Base 컴포넌트는 앞으로 기능 개발을 하지 않고 디자인 요소만 들어갈 수 있다.
  디자인 요소도 안 들어가고 개발자가 버튼으로 개발시 class를 일일이 넣어 줘야 할 수도 있다.
  아직 결정 된것이 없어서 추후 정책이 결정되면 코드 수정 할 예정이다.
-->
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
    <h1>베이스 버튼</h1>
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
});
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
