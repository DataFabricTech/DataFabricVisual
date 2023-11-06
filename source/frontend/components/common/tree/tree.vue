<script setup lang="ts">
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";


export interface State {
  opened: boolean;
  disabled: boolean;
  editable: boolean;
  draggable: boolean;
  dropable: boolean;
  checked: boolean;
  indeterminate: boolean;
  isLoading: boolean;
}

export interface Node {
  text: string;
  children?: string[];
  state?: State;
}

export interface Icon{
  type: string;
  width: number;
  height: number;
  class: string|string[];
  style: string|object;
  viewbox: string;
  d: string;
  fill: string;
  stroke: string;
}
export interface Config {
  roots: string[];
  padding?: number;
  editable?: boolean;
  checkboxes?: boolean;
  checkMode?: string; // "manual" or "auto"
  dragAndDrop?: boolean;
  disabled?: boolean;
  disabledClass?: string;
  openedIcon?: Icon
  closedIcon?: Icon
  focusClass?: string;
  checkedClass?: string;
}
export interface Props {
  nodes?: { [key: string]: Node },
  config?: Config
}

const props = withDefaults(defineProps<Props>(), {
  nodes: Node => ({
    node1: {
      text: "Node 1",
    }}),
  config: Config => ({
    roots: ["node1"]
  })
})

onMounted(() => {
  console.log("nodes:", props.nodes);
  console.log("config:", props.config);
})
</script>

<template>
  <div>
    <treeview :nodes="nodes" :config="config"></treeview>
  </div>
</template>

<style scoped lang="scss">
</style>
