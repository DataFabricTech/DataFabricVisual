<template>
  <div class="l-common flex-grow">
    <h2 class="hidden-text">데이터패브릭</h2>
    <section class="l-section flex-grow">
      <MainSearch></MainSearch>
      <MainCard></MainCard>
    </section>
  </div>
</template>

<script lang="ts" setup>
import MainSearch from "/components/project/data-fabric/main/main-search.vue";
import MainCard from "/components/project/data-fabric/main/main-card.vue";
definePageMeta({
  layout: "default-full-layout"
});
</script>
