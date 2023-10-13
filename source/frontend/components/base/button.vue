<template>
  <button class="button" :type="props.type" :title="props.title" :disabled="props.disabled">
    <slot></slot>
  </button>
</template>

<script setup lang="ts">

const props = defineProps({
  type: { type: String, default: "button" },
  title: { type: String, default: null },
  disabled: { type: Boolean, default: false },
});

</script>
