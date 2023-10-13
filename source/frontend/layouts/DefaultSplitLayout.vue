<template>
  <div class="wrap">
    <TheHeader></TheHeader>
    <TheTab></TheTab>
    <main>
      <TheCategorycategory></TheCategorycategory>
      <slot />
    </main>
    <TheFooter></TheFooter>
  </div>
</template>

<script lang="ts">
import Category from "./TheCategory.vue";
import Header from "./TheHeader.vue";
import TheHeader from "./TheHeader.vue";
import TheCategory from "./TheCategory.vue";
import TheFooter from "./TheFooter.vue";
import TheTab from "./TheTab.vue";

export default {
  components: { TheTab, TheFooter, TheCategory, TheHeader, Header, Category }
};
</script>
