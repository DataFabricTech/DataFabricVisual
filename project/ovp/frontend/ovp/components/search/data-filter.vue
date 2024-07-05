<template>
  <template v-for="(filter, title, FI) in props.data" :key="FI">
    <menu-search-button
      :data="filter.data"
      :selected-items="[]"
      label-key="text"
      value-key="id"
      :title="filter.text"
      :is-multi="true"
      @multiple-change="changeMultiple($event, title)"
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
import { defineEmits } from "vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import { useSearchCommonStore } from "@/store/search/common";
import { storeToRefs } from "pinia";
import _ from "lodash";

const searchCommonStore = useSearchCommonStore();
const { selectedFilters } = storeToRefs(searchCommonStore);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
});

const emit = defineEmits<{
  (e: "resetFilters"): void;
  // TODO : menu-search 구현 완료 되면 아래 기능 구현 -> menu-search 선택함에 따라 index.vue 단으로 옮기기
  // (e: "filterSelected", params: object): void;
}>();

const resetFilters = () => {
  emit("resetFilters");
};

const changeMultiple: (value: any[] | {}, title: any) => void = (
  value: any[] | {},
  title: string,
) => {
  let selectedIds = _.map(value, "id");
  if (selectedIds.length === 0) {
    delete selectedFilters.value[title];
  } else {
    selectedFilters.value[title] = selectedIds;
  }

  console.log("selectedFilters", selectedFilters.value);
};
</script>

<style lang="scss" scoped></style>
