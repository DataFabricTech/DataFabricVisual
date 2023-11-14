<!-- 저장소 관리 Overview-->
<template>
  <div class="l-split">
    <h2 class="hidden-text">저장소 관리</h2>
    <storage-tree></storage-tree>
    <storage-overview></storage-overview>
  </div>

  <storage-filter-popup></storage-filter-popup>
</template>

<script lang="ts" setup>
definePageMeta({
  layout: "default-tab-layout"
});
</script>
