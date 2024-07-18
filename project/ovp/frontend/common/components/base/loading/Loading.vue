<template>
  <div class="loader" role="alert" aria-live="assertive">
    <div class="loader-spinner"></div>
    <p v-if="!hideText" class="loader-message">Loading...</p>
  </div>
</template>

<script>
export default {
  props: {
    isLoading: {
      type: Boolean,
      default: false
    },
    hideText: {
      type: Boolean,
      default: false
    }
  }
};
</script>