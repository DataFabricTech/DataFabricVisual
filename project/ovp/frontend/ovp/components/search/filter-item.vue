<template>
  <!--  셀렉트 + 드롭다운 메뉴로 구성-->
  <div class="select select-clean" v-for="(filter, fI) in props.data" :key="fI">
    <button class="select-button">
      <span class="select-button-title">{{ filter.text }}</span>
      <div class="badge badge-primary-lighter">
        <p class="badge-text">{{ filter.data.length }}</p>
      </div>
      <svg-icon
        class="svg-icon select-indicator"
        name="chevron-down-medium"
      ></svg-icon>
    </button>
  </div>
  <button
    class="button button-error-lighter button-sm"
    type="button"
    @click="resetFilters"
  >
    <svg-icon class="button-icon" name="reset"></svg-icon>
    <span class="button-title">초기화</span>
  </button>

  <!-- TODO: menu-search component 추가-->
</template>

<script setup lang="ts">
import { defineEmits } from "vue";

const props = defineProps({
  data: { type: Object, default: () => {} },
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
</script>

<style lang="scss" scoped></style>
