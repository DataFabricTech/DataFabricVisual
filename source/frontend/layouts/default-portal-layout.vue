<template>
  <div id="wrap">
    <header id="header">
      <TheHeader></TheHeader>
    </header>
    <main id="main">
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts" setup>
import TheFooter from "./data-fabric-portal/the-footer.vue";
import TheHeader from "./data-fabric-portal/the-header.vue";

</script>
