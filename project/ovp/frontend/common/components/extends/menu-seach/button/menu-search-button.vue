<template>
  <div class="select select-clean">
    <button class="select-button button-lg" style="width: 360px" @click="onClickOpenMenuSearch">
      <span class="select-button-title">{{props.title}}</span>
      <div class="badge badge-primary-lighter">
        <p class="badge-text">{{selectedListData.length}}</p>
      </div>
      <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    </button>
    <menu-search
      v-on-click-outside="() => {isShow = false}"
      v-show="isShow"
      :data="props.data"
      :selected-items="selectedListData"
      :is-multi="props.isMulti"
      :label-key="props.labelKey"
      :value-key="props.valueKey"
      @cancel="onClickOpenMenuSearch"
      @multiple-change="changeMenuSearch"
      @single-change="changeMenuSearch"
    ></menu-search>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";

import { MenuSearchButtonProps } from "~/components/extends/menu-seach/button/MenuSearchButtonProps";
import { MenuSearchItemImpl } from "~/components/extends/menu-seach/MenuSearchComposition";
import { MenuSearchButtonComposition } from "~/components/extends/menu-seach/button/MenuSearchButtonComposition";

const props = withDefaults(defineProps<MenuSearchButtonProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  selectedItem: undefined,
  disabledAll: false,
  disabledList: () => [],

  // menu-search 고유 props
  selectedItems: () => [] || {},
  idKey: "id",
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색결과가 없습니다.",
  isMulti: false,

  // menu-search-button 고유 props
  title: "컴포넌트"
});


const emit = defineEmits<{
  (e: "single-change", value: MenuSearchItemImpl): void
  (e: "multiple-change", value: MenuSearchItemImpl[]): void
  (e: "cancel"): void
}>()

const applyData : (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void  = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as MenuSearchItemImpl[]);
  } else {
    emit("single-change", value as MenuSearchItemImpl);
  }
}

const {
  isShow,
  selectedListData,
  onClickOpenMenuSearch,
  changeMenuSearch
} = MenuSearchButtonComposition(props, applyData);
</script>

<style scoped>

</style>
