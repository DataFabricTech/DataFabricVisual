<template>
  <template v-for="(filter, fI) in props.data" :key="fI">
    <menu-search-button
      :data="filter.data"
      :selected-items="[]"
      label-key="text"
      value-key="id"
      :title="filter.text"
      :is-multi="true"
      @multiple-change="changeMultiple"
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
import { defineEmits, ref } from "vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";

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
// TODO : menu-search 구현 완료 되면 아래 기능 구현 -> menu-search 선택함에 따라 index.vue 단으로 옮기기
// const filterSelected = () => {
//   emit("filterSelected");
// };

const changeMultiple: (value: any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
};
</script>

<style lang="scss" scoped></style>
