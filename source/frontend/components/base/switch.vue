<template>
  <label class="switch" :for="props.id" :disabled="props.disabled">
    <input class="switch-input" :id="props.id" type="checkbox">
    <span class="switch-handle"></span>
    <span class="hidden"><slot></slot></span>
  </label>
</template>

<script setup lang="ts">

const props = defineProps({
  id: { type: String, default: null},
  disabled: { type: Boolean, default: false }
});

</script>
