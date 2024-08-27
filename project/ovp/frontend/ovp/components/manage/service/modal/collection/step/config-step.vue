<template>
  <div class="service-form" :style="props.style">
<!--    <div-->
<!--      class="form form-vertical"-->
<!--      v-show="-->
<!--        props.serviceType === 'databaseService' &&-->
<!--        props.pipelineType === 'metadata'-->
<!--      "-->
<!--    >-->
<!--      <DatabaseMeta />-->
<!--    </div>-->
<!--    <div-->
<!--      class="form form-vertical"-->
<!--      v-show="-->
<!--        props.serviceType === 'databaseService' &&-->
<!--        props.pipelineType === 'profiler'-->
<!--      "-->
<!--    >-->
<!--      <DatabaseProfiler />-->
<!--    </div>-->
<!--    <div-->
<!--      class="form form-vertical"-->
<!--      v-show="-->
<!--        props.serviceType === 'storageService' &&-->
<!--        props.pipelineType === 'metadata'-->
<!--      "-->
<!--    >-->
<!--      <StorageMeta />-->
<!--    </div>-->
    <div
      class="form form-vertical"
      v-show="
        props.serviceType === 'storageService' &&
        props.pipelineType === 'profiler'
      "
    >
      <StorageProfiler />
    </div>
  </div>
</template>
<script setup lang="ts">
// import DatabaseMeta from "./config/database-meta.vue";
// import DatabaseProfiler from "./config/database-profiler.vue";
// import StorageMeta from "./config/storage-meta.vue";
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
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
