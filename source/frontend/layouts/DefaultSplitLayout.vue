<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <TheTab></TheTab>
      <section>
        <TheCategory></TheCategory>
        <slot />
      </section>
    </main>
  </div>
</template>

<script lang="ts">
import TheCategory from "./DataFabric/TheCategory.vue";
import TheHeader from "./DataFabric/TheHeader.vue";
import TheTab from "./DataFabric/TheTab.vue";

export default {
  components: { TheTab, TheCategory, TheHeader }
};
</script>
