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
import TheHeader from "/layouts/data-fabric/the-header.vue";
import TheTab from "/layouts/data-fabric/the-tab.vue";
</script>
