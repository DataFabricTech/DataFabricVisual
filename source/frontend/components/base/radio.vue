<template>
  <div class="radio">
    <input
      class="radio-input"
      type="radio"
      :id="props.id"
      :name="props.name"
      :disabled="props.disabled"
      :checked="props.checked"
      @change="$emit('change', props.value)"
    />
    <label :for="props.id" class="radio-label"><slot></slot></label>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue/dist/vue";
const props = defineProps({
  id: { type: String, default: null },
  name: { type: String, default: null },
  disabled: { type: Boolean, default: false },
  checked: { type: Boolean, default: false },
  value: { type: String, default: null }
});
const emit = defineEmits<{
  change: [value: string];
}>();
</script>
