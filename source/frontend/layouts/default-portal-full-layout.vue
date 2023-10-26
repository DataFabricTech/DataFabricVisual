<template>
  <div id="wrap" class="is-full">
    <TheHeader></TheHeader>
    <main id="main">
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts">
import TheHeader from "./data-fabric-portal/the-header.vue";
import TheFooter from "./data-fabric-portal/the-footer.vue";

export default {
  components: { TheHeader, TheFooter }
};
</script>
