<template>
  <template v-for="(filter, keyName, FI) in props.data" :key="FI">
    <template v-if="keyName === 'category'">
      <menu-search-tree
        label-key="name"
        value-key="id"
        :data="categories"
        :is-multi="true"
        :hideGuideLines="false"
        :firExpandAll="true"
        :show-open-all-btn="false"
        :show-close-all-btn="false"
        :use-draggable="false"
        :selected-items="treeSelectedItem"
        mode="view"
        @multiple-change="onNodeChecked"
      />
    </template>
    <menu-search-button
      v-else
      :data="filter.data"
      :selected-items="selectedFilterItems"
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
import { ref } from "vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";

import { useSearchCommonStore } from "@/store/search/common";
import { storeToRefs } from "pinia";
import _ from "lodash";
import { useGovernCategoryStore } from "~/store/governance/Category";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const searchCommonStore = useSearchCommonStore();
const { resetReloadList } = searchCommonStore;
const { selectedFilters } = storeToRefs(searchCommonStore);

const categoryStore = useGovernCategoryStore();
const { getCategories } = categoryStore;
const { categories } = storeToRefs(categoryStore);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});

const selectedFilterItems: Ref<any> = ref([]);

const resetFilters = () => {
  selectedFilterItems.value = [];
  selectedFilters.value = {};

  resetReloadList();
};

const changeMultiple: (value: any[] | {}, keyName: any) => void = (
  value: any[] | {},
  keyName: string,
) => {
  let selectedIds = _.map(value, "key");

  if (selectedIds.length === 0) {
    delete selectedFilters.value[keyName];
  } else {
    selectedFilters.value[keyName] = selectedIds;
  }

  resetReloadList();
};

const treeSelectedItem: Ref<any[]> = ref<any[]>([]);
const onNodeChecked = (checkedNodeIds: TreeViewItem[]) => {
  treeSelectedItem.value = checkedNodeIds;
};

getCategories();
</script>

<style lang="scss" scoped></style>
