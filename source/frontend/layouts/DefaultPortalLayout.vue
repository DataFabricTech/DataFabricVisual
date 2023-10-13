<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <section>
        <slot />
      </section>
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts">
import TheFooter from "./DataFabricPortal/TheFooter.vue";
import TheHeader from "./DataFabricPortal/TheHeader.vue";

export default {
  components: { TheHeader, TheFooter }
};
</script>
