<template>
  <div class="checkbox">
    <input
      class="checkbox-input"
      type="checkbox"
      :id="props.id"
      :name="props.name"
      :disabled="props.disabled"
      :checked="props.checked"
      :value="props.value"
      @change="$emit('change', $event.target.checked)"
    >
    <label :for="props.id" class="checkbox-label">
      <slot>{{ props.name }}</slot>
    </label>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue/dist/vue";

const props = defineProps({
  id: { type: String || Number, default: null },
  name: { type: String, default: null},
  disabled: { type: Boolean, default: false },
  checked: { type: Boolean, default: false },
  value: { default: null}
});
const emit = defineEmits<{
  change: [checked: boolean]
}>()

</script>
