<template>
  <div id="wrap" class="is-full">
    <header id="header">
      <TheHeader></TheHeader>
    </header>
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
