<template>
  <div :class="props.class">
    <button v-if="showOpenAllBtn" class="button button-neutral-ghost button-sm" type="button" @click="openAll">
      <span class="button-title">전체 열기</span>
    </button>
    <button v-if="showCloseAllBtn" class="button button-neutral-ghost button-sm" type="button" @click="closeAll">
      <span class="button-title">전체 닫기</span>
    </button>
    <vue-tree
      :items="treeItems"
      :isCheckable="props.isCheckable"
      :hideGuideLines="props.hideGuideLines"
      :draggable="false"
      @onCheck="onItemChecked"
      @dropValidator="dropValidatorHandler"
      @onSelect="onItemSelected"
    >
      <!-- TODO : [퍼블] 자식 여부에 따라 ">" 로 생기는 들여쓰기? 같은거 해결하실때 필요하면 사용하시고, 불필요 하시면 삭제 바랍니다.-->
      <template v-slot:item-prepend="treeViewItem">
        <div style="margin-left: 23px" v-if="treeViewItem.children.length < 1"></div>
      </template>
      <!-- // ---->

      <template v-if="mode === 'edit'" v-slot:item-append="treeViewItem">
        <div class="on-item-hover">
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

/* TODO : [퍼블] 선택한 노드 style 변경시 사용. 사용 안할시 삭제 바랍니다. */
.selected-tree-item {
  background: lightblue !important;
}
</style>
