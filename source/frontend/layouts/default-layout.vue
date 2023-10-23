<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main id="main">
      <slot />
    </main>
  </div>
</template>

<script lang="ts">
import TheHeader from "./data-fabric/the-header.vue";

export default {
  components: { TheHeader }
};
</script>
