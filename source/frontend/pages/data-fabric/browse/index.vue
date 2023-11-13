<template>
  <div class="l-split">
    <h2 class="hidden-text">데이터모델 Browse</h2>
    <section class="l-side">
      <BrowseFilter></BrowseFilter>
    </section>
    <section class="l-split-section">
      <BrowseContent></BrowseContent>
    </section>
  </div>
</template>

<script lang="ts" setup>
definePageMeta({
  layout: "default-tab-layout"
});
</script>
