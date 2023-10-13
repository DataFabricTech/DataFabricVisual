<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <section>
        <slot />
      </section>
    </main>
  </div>
</template>

<script lang="ts">
import TheHeader from "./DataFabric/TheHeader.vue";

export default {
  components: { TheHeader }
};
</script>
