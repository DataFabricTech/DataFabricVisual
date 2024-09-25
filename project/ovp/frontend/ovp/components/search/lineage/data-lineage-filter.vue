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
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";

import { FILTER_KEYS } from "@/store/search/common";
import _ from "lodash";

import type { TreeViewItem } from "@extends/tree/TreeProps";
import { useLineageStore } from "~/store/search/detail/lineage";
import { useDataModelDetailStore } from "~/store/search/detail";

const lineageStore = useLineageStore();
const { setEmptyFilter, getLineageData } = lineageStore;
const { selectedFilters, selectedFilterItems, isShowPreview, lineageRef } =
  storeToRefs(lineageStore);

const dataModelDetailStore = useDataModelDetailStore();
const { getDataModelFqn } = dataModelDetailStore;
const { dataModelType } = storeToRefs(dataModelDetailStore);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});

// treeFilter event
const onNodeChecked = (checkedNodes: TreeViewItem[]) => {
  isShowPreview.value = false;
  setSelectedFilters(FILTER_KEYS.CATEGORY, checkedNodes);
};

// buttonFilter event
const changeMultiple: (
  value: { key: string }[] | {},
  keyName: string,
) => void = async (value: any[] | {}, keyName: string) => {
  isShowPreview.value = false;
  setSelectedFilters(keyName, _.map(value, "key"));
  setSelectedFilterItems(keyName, value);
  await getLineageData(dataModelType.value, getDataModelFqn());
};

const setSelectedFilters = (keyName: string, selectedIds: any[]) => {
  if (_.isEmpty(selectedIds)) {
    delete selectedFilters.value[keyName];
  } else {
    selectedFilters.value[keyName] = selectedIds;
  }
};

const setSelectedFilterItems = (keyName: string, value: any[] | {}) => {
  selectedFilterItems.value[keyName] = value;
};

const resetFilters = async () => {
  isShowPreview.value = false;
  if (lineageRef.value) {
    lineageRef.value.reset();
  }
  setEmptyFilter();
  await getLineageData(dataModelType.value, getDataModelFqn());
};
</script>

<style lang="scss" scoped></style>
