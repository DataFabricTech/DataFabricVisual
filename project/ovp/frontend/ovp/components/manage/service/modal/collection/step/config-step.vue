<template>
  <div class="service-form" :style="props.style">
    <component :is="selectedComponent" />
  </div>
</template>
<script setup lang="ts">
import DatabaseMeta from "./config/database-meta.vue";
import DatabaseProfiler from "./config/database-profiler.vue";
import StorageMeta from "./config/storage-meta.vue";
import StorageProfiler from "./config/storage-profiler.vue";

const props = defineProps({
  style: {
    type: String,
  },
  serviceType: {
    type: String,
  },
  pipelineType: {
    type: String,
  },
});

const selectedComponent = computed(() => {
  const componentMap = {
    databaseService: {
      metadata: DatabaseMeta,
      profiler: DatabaseProfiler,
    },
    storageService: {
      metadata: StorageMeta,
      profiler: StorageProfiler,
    },
  };
  return componentMap[props.serviceType]?.[props.pipelineType] || null;
});
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
