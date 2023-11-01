<template>
  <div id="wrap">
    <header id="header">
      <TheHeader></TheHeader>
      <TheTab></TheTab>
    </header>
    <main id="main">
      <slot />
    </main>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";
import TheTab from "./data-fabric/the-tab.vue";

export default {
  components: { TheHeader, TheTab }
};
</script>
