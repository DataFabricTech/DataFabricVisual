<!-- 저장소 관리 Overview-->
<template>
  <div class="l-split">
    <h2 class="hidden-text">저장소 관리</h2>
    <storage-tree></storage-tree>
    <component :is="getComponent"></component>
  </div>

  <storage-filter-popup></storage-filter-popup>
</template>

<script lang="ts" setup>
definePageMeta({
  layout: "default-tab-layout"
});
const route = useRoute();

const component = {
  overview: resolveComponent("storage-overview"),
  // repository: resolveComponent("repository")
};

const getComponent = computed(() => {
  return component[route.params.id];
});
</script>
