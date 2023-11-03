<script setup lang="ts">
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";

export interface Props {
  node?: any,
  config?: any
}

const props = withDefaults(defineProps<Props>(), {
  node: {},
  config: { "roots": [] }
})
</script>

<template>
  <div>
    <treeview :nodes="node" :config="config"></treeview>
  </div>
</template>

<style scoped lang="scss">
</style>
