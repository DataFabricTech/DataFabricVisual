<template>
  <template v-for="(filter, keyName, FI) in props.data" :key="FI">
    <template v-if="keyName === 'category'">
      <menu-search-tree
        label-key="name"
        value-key="id"
        :title="filter.text"
        :data="filter.data.children"
        :is-multi="true"
        :hideGuideLines="false"
        :firExpandAll="true"
        :show-open-all-btn="false"
        :show-close-all-btn="false"
        :use-draggable="false"
        :selected-items="selectedFilters[FILTER_KEYS.CATEGORY]"
        mode="view"
        @multiple-change="onNodeChecked"
      />
    </template>
    <menu-search-button
      v-else
      v-if="
        !(
          currentTab === 'storage' &&
          (keyName === FILTER_KEYS.DATABASE ||
            keyName === FILTER_KEYS.DATABASE_SCHEMA)
        )
      "
      :data="filter.data"
      :selected-items="selectedFilterItems[keyName]"
      label-key="key"
      value-key="key"
      :title="filter.text"
      :is-multi="true"
      @multiple-change="changeMultiple($event, keyName)"
    ></menu-search-button>
  </template>
  <button
    class="button button-error-lighter button-sm"
    type="button"
    @click="resetFilters"
  >
    <svg-icon class="button-icon" name="reset"></svg-icon>
    <span class="button-title">초기화</span>
  </button>
</template>

<script setup lang="ts">
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";

import { FILTER_KEYS, useSearchCommonStore } from "@/store/search/common";
import { useLayoutHeaderStore } from "~/store/layout/header";
import { storeToRefs } from "pinia";
import _ from "lodash";

import type { TreeViewItem } from "@extends/tree/TreeProps";

const searchCommonStore = useSearchCommonStore();
const { resetReloadList, setEmptyFilter, setSearchKeyword, getGraphData } =
  searchCommonStore;
const {
  selectedFilters,
  currentTab,
  selectedFilterItems,
  isShowPreview,
  viewType,
  showGraphModelListMenu,
} = storeToRefs(searchCommonStore);

const layoutHeaderStore = useLayoutHeaderStore();
const { searchInputValue } = storeToRefs(layoutHeaderStore);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});

const resetFilters = () => {
  isShowPreview.value = false;
  showGraphModelListMenu.value = false;
  setEmptyFilter();
  searchInputValue.value = "";
  setSearchKeyword("");
  resetReloadList();

  if (viewType.value === "graphView") {
    getGraphData();
  }
};

const changeMultiple: (value: any[] | {}, keyName: any) => void = (
  value: any[] | {},
  keyName: string,
) => {
  isShowPreview.value = false;
  setSelectedFilters(keyName, _.map(value, "key"));
  setSelectedFilterItems(keyName, value);
};

const setSelectedFilters = (keyName: string, selectedIds: any[]) => {
  if (selectedIds.length === 0) {
    delete selectedFilters.value[keyName];
  } else {
    selectedFilters.value[keyName] = selectedIds;
  }

  resetReloadList();

  if (viewType.value === "graphView") {
    getGraphData();
  }
};

const setSelectedFilterItems = (keyName: string, value: any[] | {}) => {
  selectedFilterItems.value[keyName] = value;
};

const onNodeChecked = (checkedNodes: TreeViewItem[]) => {
  isShowPreview.value = false;
  setSelectedFilters(FILTER_KEYS.CATEGORY, checkedNodes);
};
</script>

<style lang="scss" scoped></style>
