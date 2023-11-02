<template>
  <div class="l-common">
    <h2 class="hidden">데이터패브릭 검색목록</h2>
    <search-section></search-section>
    <search-result></search-result>
  </div>
</template>
<script lang="ts" setup>
import SearchSection from "../../../components/project/data-fabric/search/search-section.vue";
import SearchResult from "../../../components/project/data-fabric/search/search-result.vue";
definePageMeta({
  layout: "default-layout"
});
</script>
