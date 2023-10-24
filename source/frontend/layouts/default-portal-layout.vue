<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main id="main">
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts">
import TheFooter from "./data-fabric-portal/the-footer.vue";
import TheHeader from "./data-fabric-portal/the-header.vue";

export default {
  components: { TheHeader, TheFooter }
};
</script>
