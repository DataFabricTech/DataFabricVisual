<template>
  <div class="data-detail">
    <div class="query" v-for="(query, index) in queryList" :key="index">
      <div class="query-date">{{ query.queryDate }}</div>
      <div class="code-box">{{ query.query }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useDataModelDetailStore } from "@/store/search/detail/index";

const dataModelDetailStore = useDataModelDetailStore();

const { queryList } = storeToRefs(dataModelDetailStore);
</script>

<style lang="scss" scoped></style>
