<template>
  <div id="wrap">
    <TheHeader></TheHeader>
     <main class="l-full">
      <TheTab></TheTab>
      <slot />
    </main>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";
import TheTab from "./data-fabric/the-tab.vue";

</script>
