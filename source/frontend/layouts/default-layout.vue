<template>
  <div id="wrap">
    <header id="header">
      <TheHeader></TheHeader>
    </header>
    <main id="main">
      <slot />
    </main>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";

</script>
