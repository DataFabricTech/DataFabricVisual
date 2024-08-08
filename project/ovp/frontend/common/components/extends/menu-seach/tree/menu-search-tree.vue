<template>
  <div class="select select-clean select-sm">
    <button class="select-button" @click="onClickOpenMenuSearch">
      <slot name="button-text-slot">
        <span class="select-button-title">{{ props.title }}</span>
        <div class="badge badge-primary-lighter" v-if="selectedListData.length > 0">
          <p class="badge-text">{{ selectedListData.length }}</p>
        </div>
      </slot>
      <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    </button>
    <menu-search-tree-contents
      :is-show="isMenuSearchShow"
      :data="props.data"
      :selected-items="selectedListData"
      :label-key="props.labelKey"
      :value-key="props.valueKey"
      :isCheckable="props.isCheckable"
      :hideGuideLines="props.hideGuideLines"
      :firExpandAll="props.firExpandAll"
      :show-open-all-btn="props.showOpenAllBtn"
      :show-close-all-btn="props.showCloseAllBtn"
      :use-draggable="props.useDraggable"
      :mode="props.mode"
      :dropValidator="props.dropValidator"
      @cancel="onCancel"
      @change="changeMenuSearch"
    ></menu-search-tree-contents>
  </div>
</template>

<script setup lang="ts">
import { TreeViewItem } from "~/components/extends/tree/TreeProps";
import MenuSearchTreeContents from "../tree/contents/menu-search-tree-contents.vue";
import { MenuSearchTreeProps } from "~/components/extends/menu-seach/tree/MenuSearchTreeProps";
import { MenuSearchTreeComposition } from "~/components/extends/menu-seach/tree/MenuSearchTreeComposition";

const props = withDefaults(defineProps<MenuSearchTreeProps>(), {
  title: "컴포넌트",

  data: () => [],
  labelKey: "label",
  valueKey: "value",

  // menu-search 고유 props
  selectedItems: () => [] || {},
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색결과가 없습니다.",
  isShow: false,

  // tree 고유 props
  mode: "view",
  isCheckable: false,
  hideGuideLines: false,
  showOpenAllBtn: false,
  showCloseAllBtn: false,
  firExpandAll: false,
  dropValidator: () => {
    return () => false;
  }
});

const emit = defineEmits<{
  (e: "change", value: TreeViewItem[]): void;
  (e: "open"): void;
  (e: "cancel"): void;
  (e: "close"): void;
}>();

const applyData: (value: TreeViewItem[]) => void = (value) => {
  emit("change", value);
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

const { isMenuSearchShow, selectedListData, onCancel, onClickOpenMenuSearch, changeMenuSearch } =
  MenuSearchTreeComposition(props, applyData, openMenuSearch, panelClosed);
</script>

<style scoped></style>
