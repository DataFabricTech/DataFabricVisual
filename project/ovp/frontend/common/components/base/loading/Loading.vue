<template>
  <div class="loader" role="alert" aria-live="assertive">
    <div v-if="useLoaderOverlay" class="overlay"></div>
    <div class="loader-group">
      <div class="loader-spinner"></div>
      <p v-if="useHiddenText" class="loader-message">Loading...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";

const props = defineProps({
  useHiddenText: {
    type: Boolean,
    default: false
  },
  useLoaderOverlay: {
    type: Boolean,
    default: false
  }
});
</script>
