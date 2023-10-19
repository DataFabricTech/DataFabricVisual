<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts" setup>
import TheFooter from "./data-fabric-portal/the-footer.vue";
import TheHeader from "./data-fabric-portal/the-header.vue";

</script>
