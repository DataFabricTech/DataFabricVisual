<template>
  <div id="wrap" class="is-split-full">
    <header id="header">
      <TheHeader></TheHeader>
<!--      <TheTab></TheTab>-->
    </header>
    <main id="main">
      <slot />
    </main>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";
import TheTab from "./data-fabric/the-tab.vue";

</script>
