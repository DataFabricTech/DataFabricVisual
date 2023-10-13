<template>
  <div class="wrap">
    <TheHeader></TheHeader>
    <main>
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts">
import TheHeader from "./TheHeader.vue";
import TheFooter from "./TheFooter.vue";

export default {
  components: { TheFooter, TheHeader }
};
</script>
