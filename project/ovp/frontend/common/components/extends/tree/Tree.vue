<template>
  <div :class="props.class" class="tree">
    <div class="tree-top-buttons">
      <button v-if="showOpenAllBtn" class="button button-neutral-stroke button-sm" type="button" @click="openAll">
        <span class="button-title">전체 열기</span>
      </button>
      <button v-if="showCloseAllBtn" class="button button-neutral-stroke button-sm" type="button" @click="closeAll">
        <span class="button-title">전체 닫기</span>
      </button>
    </div>
    <vue-tree
      class="tree-list"
      :items="treeItems"
      :isCheckable="props.isCheckable"
      :hideGuideLines="props.hideGuideLines"
      :draggable="false"
      @onCheck="onItemChecked"
      @dropValidator="dropValidatorHandler"
      @onSelect="onItemSelected"
    >

      <template v-if="mode === 'edit'" v-slot:item-append="treeViewItem">
        <div class="tree-item-buttons">
          <button class="button button-neutral-ghost button-sm" type="button" @click="addSibling(treeViewItem)">
            <span class="button-title">추가</span>
          </button>
          <button class="button button-neutral-ghost button-sm" type="button" @click="addChild(treeViewItem)">
            <span class="button-title">하위에 추가</span>
          </button>
        </div>
      </template>
    </vue-tree>
  </div>
</template>

<script setup lang="ts">
import VueTree from "vue3-tree-vue";
import "vue3-tree-vue/dist/style.css";

import { TreeProps, TreeViewItem } from "./TreeProps";
import { TreeComposition } from "./TreeComposition";
const props = withDefaults(defineProps<TreeProps>(), {
  mode: "view",
  isCheckable: false,
  hideGuideLines: false,
  showOpenAllBtn: false,
  showCloseAllBtn: false,
  firExpandAll: false
});

const emit = defineEmits<{
  (e: "onItemChecked", params: { from: TreeViewItem; to: TreeViewItem }): void;
  (e: "onItemSelected", params: TreeViewItem): void;
  (e: "addSibling", params: TreeViewItem): void;
  (e: "addChild", params: TreeViewItem): void;
}>();

const onItemChecked = (from: TreeViewItem, to: TreeViewItem) => {
  emit("onItemChecked", { from, to } as { from: TreeViewItem; to: TreeViewItem });
};

const onItemSelected = (node: TreeViewItem) => {
  emit("onItemSelected", node);
};

const addSibling = (node: TreeViewItem) => {
  emit("addSibling", createNewTreeItem(node.parentId));
};
const addChild = (node: TreeViewItem) => {
  emit("addChild", createNewTreeItem(node.id));
};

onMounted(() => {
  if (props.firExpandAll) {
    openAll();
  }
});

const { treeItems, createNewTreeItem, openAll, closeAll, dropValidatorHandler } = TreeComposition(props);
</script>

<style lang="scss">
/* @import "./index.scss"; */

</style>
