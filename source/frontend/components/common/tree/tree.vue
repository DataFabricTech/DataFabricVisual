<script setup lang="ts">
import { onMounted } from "vue";
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";

export interface Props {
  nodes?: any,
  config?: any
}

const props = withDefaults(defineProps<Props>(), {
  nodes: {},
  config: { "roots": [] }
})

onMounted(() => {
  console.log("nodes", props.nodes);
  console.log("nodes", props.config);
})
</script>

<template>
  <div>
    <treeview :nodes="nodes" :config="config"></treeview>
  </div>
</template>

<style scoped lang="scss">
</style>
