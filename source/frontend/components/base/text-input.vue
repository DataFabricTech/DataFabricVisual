<template>
  <input
    class="text-input"
    :id="props.id"
    :type="props.type"
    :value="modelValue"
    :disabled="props.disabled"
    :placeholder="props.placeholder"
    @input="$emit('update:modelValue', $event.target.value)"
  >
</template>

<script setup lang="ts">
import { defineProps } from "vue";
const props = defineProps({
  id: { type: String, default: null},
  type: { type: String, default: "text" },
  modelValue: { type: String, default: null },
  placeholder: { type: String, default: '검색어를 입력하세요.' },
  disabled: { type: Boolean, default: false },
});

</script>
