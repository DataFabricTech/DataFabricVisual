<template>
  <template v-for="(filter, keyName, FI) in props.data" :key="FI">
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
import { ref } from "vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import { useGovernCategoryStore } from "~/store/governance/Category/index";
import { storeToRefs } from "pinia";
import _ from "lodash";

const categoryStore = useGovernCategoryStore();
const { resetReloadList } = categoryStore;
const { selectedFilters } = storeToRefs(categoryStore);

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
  setSelectedFilters(keyName, _.map(value, "key"));
};

const setSelectedFilters = (keyName: string, selectedIds: any[]) => {
  if (selectedIds.length === 0) {
    delete selectedFilters.value[keyName];
  } else {
    selectedFilters.value[keyName] = selectedIds;
  }

  resetReloadList();
};
</script>

<style lang="scss" scoped></style>
