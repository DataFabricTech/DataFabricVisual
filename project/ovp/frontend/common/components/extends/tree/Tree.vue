<template>
  <div :class="[props.class, props.compId, 'tree']" v-if="showTree">
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
      <template v-slot:item-append="treeViewItem">
        <div
          :class="{
            'node-disabled': isNodeDisabled(treeViewItem),
            'node-fir-selected': treeSelectedIds.length > 0 && treeSelectedIds.includes(treeViewItem.id) && !isCheckable
          }"
        ></div>
        <div v-if="mode === 'edit' && !props.immutableItems.includes(treeViewItem.id)" class="tree-item-buttons">
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
  firExpandAll: false,
  checkedIds: () => [],
  disabledIds: () => [],
  selectedIds: () => [],
  useFirSelect: true,
  compId: "treeComponent",
  immutableItems: () => [],
  useSelectOnlyLastChild: false
});

const emit = defineEmits<{
  (e: "onItemChecked", params: TreeViewItem[]): void;
  (e: "onItemSelected", params: TreeViewItem): void;
  (e: "addSibling", params: TreeViewItem): void;
  (e: "addChild", params: TreeViewItem): void;
}>();

const onItemChecked = (from: TreeViewItem[]) => {
  emit("onItemChecked", from);
};

const onItemSelected = (node: TreeViewItem) => {
  if (node.disabled) {
    return;
  }
  // node-fir-select class 를 삭제해준다.
  try {
    treeSelectedIds.value = treeSelectedIds.value.filter((id) => id !== treeItems.value[0].id);
  } catch {}
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

const isNodeDisabled = (node: TreeViewItem) => {
  const isDisabledNode = props.disabledIds.length > 0 && props.disabledIds.includes(node.id);
  const isLastChildOpt = isNodeLastChildNode(node);
  return isDisabledNode || isLastChildOpt;
};
const {
  showTree,
  treeItems,
  createNewTreeItem,
  openAll,
  closeAll,
  dropValidatorHandler,
  treeSelectedIds,
  isNodeLastChildNode
} = TreeComposition(props);
</script>

<style lang="scss">
/* @import "./index.scss"; */
</style>
