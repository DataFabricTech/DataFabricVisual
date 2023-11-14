<script setup lang="ts">
import { onMounted } from "vue";
import Tree from "~/components/common/tree/tree.vue";
import { Node, Config } from "~/components/common/tree/tree.vue";

definePageMeta({
  layout: "default-layout"
});

const nodes = ref<{ [key: string]: Node }>({
  id1: {
    isRoot: true,
    text: "text1",
    children: ["id11", "id12"]
  },
  id11: {
    text: "text11"
  },
  id12: {
    text: "text12"
  },
  id2: {
    isRoot: true,
    text: "text2"
  },
  id3: {
    isRoot: true,
    text: "text3",
    children: ["id31"],
    state: {
      opened: true
    }
  },
  id31: {
    text: "text31",
    children: ["id311", "id312", "id313"],
    state: {
      opened: true
    }
  },
  id311: {
    text: "text311",
    title: "before",
    icon: "close",
    state: {
      checked: true
    }
  },
  id312: {
    text: "text312",
    state: {
      disabled: true,
      checked: true
    }
  },
  id313: {
    text: "text313",
    state: {
      editable: true
    }
  },
  id4: {
    text: "text4"
  }
});

const config = ref<Config>({
  roots: ["id1", "id2", "id3"],
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
  checkboxes: true,
  editable: true
});

function onSave(value: Node) {
  console.log("save:", value);
}

function onDelete(value: Node) {
  console.log("delete:", value);
}

function nodeClick(nodeId: string) {
  console.log("nodeClick:", nodeId);
}

onMounted(() => {
  // console.log("nodes:", nodes);
  // console.log("config:", config);
});

</script>

<template>
  <div class="l-fixed">
    <h2 class="hidden">Loader/API 샘플</h2>
    <h3>Tree 1</h3>
    <Tree v-if="false"></Tree>
    <h3>Tree 2</h3>
    <Tree v-if="false" :is-config-mode="true" :nodes="nodes" :config="config"></Tree>
    <h3>Tree 3</h3>
    <Tree v-if="false" :nodes="nodes" :roots="config.roots" :checkboxes="true" :addable="true" @save="onSave"
          @delete="onDelete"></Tree>
    <h3>Tree 4</h3>
    <Tree v-if="true" :nodes="nodes" :checkboxes="true" @node-click="nodeClick" @save="onSave"
          @delete="onDelete"></Tree>
  </div>
</template>

<style scoped lang="scss">

</style>
