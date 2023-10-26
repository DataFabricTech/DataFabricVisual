<template>
  <div id="wrap" class="is-have-tab">
    <TheHeader></TheHeader>
    <TheTab></TheTab>
    <main id="main">
      <slot />
    </main>
  </div>
</template>

<script lang="ts">
import TheHeader from "./data-fabric/the-header.vue";
import TheTab from "./data-fabric/the-tab.vue";

export default {
  components: { TheTab, TheHeader }
};
</script>
