<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <slot />
    </main>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";

</script>
