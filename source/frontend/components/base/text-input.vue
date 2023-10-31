<template>
  <input class="text-input" :id="props.id" :type="props.type" :disabled="props.disabled" :placeholder="props.placeholder" :value="modelValue"
         @input="$emit('update:modelValue', $event.target.value)">
</template>

<script setup lang="ts">
defineEmits(['update:modelValue'])

const props = defineProps({
  id: { type: String, default: null},
  type: { type: String, default: "text" },
  placeholder: { type: String, default: null },
  disabled: { type: Boolean, default: false },
  modelValue: {type: String, default:null},
});

</script>
