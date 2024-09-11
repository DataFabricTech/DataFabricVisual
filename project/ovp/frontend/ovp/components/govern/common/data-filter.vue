<template>
  <template v-for="(filter, keyName, fi) in props.data" :key="fi">
    <menu-search-button
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
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import { useDataModelTag } from "@/store/governance/common/modal/data-model";
import _ from "lodash";

const dataModelTagStore = useDataModelTag();
const {
  getDataModelList,
  setSearchKeyword,
  setEmptyFilter,
  resetDataModelIdListByTab,
  resetSelectedDataModelListByTab,
} = dataModelTagStore;
const { selectedFilters, selectedFilterItems } = storeToRefs(dataModelTagStore);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});

const resetFilters = () => {
  setSearchKeyword("");
  setEmptyFilter();
  resetDataModelIdListByTab();
  resetSelectedDataModelListByTab();
  getDataModelList();
};

const changeMultiple = (value: { [key: string]: any }[], keyName: string) => {
  setSelectedFilters(keyName, _.map(value, "key"));
};

const setSelectedFilters = (keyName: string, selectedIds: string[]) => {
  if (selectedIds.length === 0) {
    delete selectedFilters.value[keyName];
  } else {
    selectedFilters.value[keyName] = selectedIds;
  }

  resetDataModelIdListByTab();
  resetSelectedDataModelListByTab();
  getDataModelList();
};
</script>

<style lang="scss" scoped></style>
