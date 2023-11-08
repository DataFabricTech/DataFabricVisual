<script setup lang="ts">
import { onMounted, ref } from "vue";
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";
import { defineEmits } from "vue/dist/vue";

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
  // add 속성
  isRoot?: boolean;
  title?: string;
  icon?: string;
}

export interface Icon {
  type?: string;
  width?: number;
  height?: number;
  class?: string | string[];
  style?: string | object;
  viewBox?: string;
  draw?: string;
  fill?: string;
  stroke?: string;
  strokeWidth?: number;
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
  openedIcon?: Icon;
  closedIcon?: Icon;
  focusClass?: string;
  checkedClass?: string;
}

export interface Props {
  isConfigMode?: boolean,
  nodes?: { [key: string]: Node },
  config?: Config,
  roots?: string[],
  checkboxes?: boolean,
  editable?: boolean,
  addable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  isConfigMode: false,
  nodes: Node => ({
    node1: {
      text: "Node 1",
      isRoot: true
    }}),
  config: Config => ({
    roots: ["node1"]
  }),
  roots: () => [],
  checkboxes: false,
  editable: false,
  addable: false
});

const emit = defineEmits(["save", "delete"]);

const treeNoses = ref<{ [key: string]: Node }>({
  node1: {
    text: "Node 1",
  }});
const treeConfig = ref<Config>({
  roots: ["node1"],
  openedIcon: {
    type: "shape",
    stroke: "black",
    strokeWidth: 3,
    viewBox: "0 0 24 24",
    draw: "M 2 12 L 22 12"
  },
  closedIcon: {
    type: "shape",
    stroke: "black",
    strokeWidth: 3,
    viewBox: "0 0 24 24",
    draw: `M 12 2 L 12 22 M 2 12 L 22 12`
  },
  checkboxes: false,
  editable: true
})
const nodeIconBefore = ref("");
const nodeIconAfter = ref("");

function setConfig(key: string, value: any) {
  console.log("setConfig key:", key, "-", value);
  treeConfig.value[key] = value;
  console.log("treeConfig:", treeConfig.value);
}

onMounted(() => {
  console.log("tree: onMounted - isConfigMode:", props.isConfigMode);

  const isConfigMode = props.isConfigMode;
  for (let key in props) {
    console.log("props -", key, props[key]);
    if (key !== "isConfigMode") {
      if (key === "nodes" && props.nodes) {
        treeNoses.value = props.nodes;
        console.log("set props - key:", key);
      } else if (key === "config" && props.config) {
        if (isConfigMode) {
          treeConfig.value = props.config;
          console.log("set props - key:", key);
        }
      } else if (!isConfigMode) {
        setConfig(key, props[key]);
      }
    }
  }
});
</script>

<template>
  <div>
    <treeview :nodes="treeNoses" :config="treeConfig">

      <template #before-input="props">
        <svg-icon v-if="props.node.icon" :name="props.node.icon" class="svg-icon" />
        <span v-if="props.node.title" class="before"> {{ props.node.title }} </span>
      </template>

      <template v-if="addable" #after-input="props">
        <BaseButton @click="emit('save', props.node)">저장</BaseButton>
        <BaseButton @click="emit('delete', props.node)">삭제</BaseButton>
      </template>
    </treeview>
  </div>
</template>

<style scoped lang="scss">
.before {
  color: blue;
  margin-left: 8px;
}
</style>
