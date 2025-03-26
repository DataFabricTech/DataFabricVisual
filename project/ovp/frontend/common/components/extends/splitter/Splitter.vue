<template>
  <q-splitter v-model="splitterValue" :limits="props.limits" :unit="props.unit" :horizontal="props.horizontal">
    <template #before>
      <slot name="before">
        {{ props.limits }}
        <div class="default-panel">Before Panel</div>
      </slot>
    </template>
    <!-- 기본 separator로 q-avatar 제공, 하지만 사용자가 직접 제공 가능 -->
    <template v-slot:separator>
      <slot name="separator">
        <q-avatar text-color="white" size="40px" icon="" />
      </slot>
    </template>

    <template #after>
      <slot name="after">
        <div class="default-panel">After Panel</div>
      </slot>
    </template>
  </q-splitter>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, useSlots, computed } from "vue";
import { QSplitter, QAvatar } from "quasar";

import type { SplitterProps } from "./SplitterProps.ts";

// Props 정의
const props = withDefaults(defineProps<SplitterProps>(), {
  modelValue: 0, // v-model로 전달받을 값
  limits: [30, 70], // 크기 제한
  unit: "%", // 단위 (예: %, px)
  horizontal: false // 방향 (true면 가로)
});

// Emits 정의 (v-model 대응)
const emit = defineEmits(["update:modelValue"]);

// v-model 변경 이벤트 처리
const splitterValue = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value)
});

// Slots
const slots = useSlots();
</script>

<style scoped>
/* 기본 패널 스타일 */
.default-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  background: #f5f5f5;
  border: 1px solid #ccc;
}
</style>
