<template>
  <div class="select select-clean select-sm" v-on-click-outside="onClickOutside">
    <button class="select-button" @click="onClickOpenMenuSearch">
      <slot name="button-text-slot">
        <span class="select-button-title">{{ props.title }}</span>
        <div class="badge badge-primary-lighter" v-if="selectedListData.length > 0 && isShowLength">
          <p class="badge-text">{{ selectedListData.length }}</p>
        </div>
      </slot>
      <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    </button>

    <menu-search-tree-contents
      v-if="isMenuSearchShow"
      :is-show="isMenuSearchShow"
      :is-multi="props.isMulti"
      :data="props.data"
      :selected-items="selectedListData"
      :label-key="props.labelKey"
      :value-key="props.valueKey"
      :hideGuideLines="props.hideGuideLines"
      :firExpandAll="props.firExpandAll"
      :show-open-all-btn="props.showOpenAllBtn"
      :show-close-all-btn="props.showCloseAllBtn"
      :use-draggable="props.useDraggable"
      :mode="props.mode"
      :dropValidator="props.dropValidator"
      @cancel="onCancel"
      @single-change="changeMenuSearch"
      @multiple-change="changeMenuSearch"
    ></menu-search-tree-contents>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";

import { TreeViewItem } from "@/components/extends/tree/TreeProps";
import MenuSearchTreeContents from "./contents/menu-search-tree-contents.vue";
import { MenuSearchTreeProps } from "./MenuSearchTreeProps";
import { MenuSearchTreeComposition } from "./MenuSearchTreeComposition";

const props = withDefaults(defineProps<MenuSearchTreeProps>(), {
  title: "",

  data: () => [],
  labelKey: "label",
  valueKey: "value",

  // menu-search 고유 props
  selectedItems: () => [] || {},
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색결과가 없습니다.",
  isMulti: false,
  isShow: false,
  isShowLength: true,

  // tree 고유 props
  mode: "view",
  hideGuideLines: false,
  showOpenAllBtn: false,
  showCloseAllBtn: false,
  firExpandAll: false,
  dropValidator: () => {
    return () => false;
  }
});

const emit = defineEmits<{
  (e: "single-change", value: TreeViewItem): void;
  (e: "multiple-change", value: TreeViewItem[]): void;
  (e: "open"): void;
  (e: "cancel"): void;
  (e: "close"): void;
}>();

const applyData: (value: TreeViewItem | TreeViewItem[]) => void = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as TreeViewItem[]);
  } else {
    emit("single-change", value as TreeViewItem);
  }
};

const openMenuSearch: () => void = () => {
  if (isMenuSearchShow) {
    emit("open");
  }
};

// panel 이 닫힐때 동작함.
const panelClosed = () => {
  emit("close");
};

const { isMenuSearchShow, selectedListData, onCancel, onClickOutside, onClickOpenMenuSearch, changeMenuSearch } =
  MenuSearchTreeComposition(props, applyData, openMenuSearch, panelClosed);
</script>

<style scoped></style>
