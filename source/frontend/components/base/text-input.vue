<template>
  <input class="text-input" :id="props.id" :type="props.type" :value="props.value" :disabled="props.disabled" :placeholder="props.placeholder">
</template>

<script setup lang="ts">

const props = defineProps({
  id: { type: String, default: null},
  type: { type: String, default: "text" },
  value: { type: String, default: null },
  placeholder: { type: String, default: null },
  disabled: { type: Boolean, default: false },
});

</script>
