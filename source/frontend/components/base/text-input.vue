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
  placeholder: { type: String, default: null },
  disabled: { type: Boolean, default: false },
});

</script>
