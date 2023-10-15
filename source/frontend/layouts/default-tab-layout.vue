<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <TheTab></TheTab>
      <section>
        <slot />
      </section>
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
