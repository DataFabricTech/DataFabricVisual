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
    <menu-search
      :is-show="isMenuSearchShow"
      :data="props.data"
      :selected-items="selectedListData"
      :is-multi="props.isMulti"
      :label-key="props.labelKey"
      :value-key="props.valueKey"
      @cancel="onCancel"
      @multiple-change="changeMenuSearch"
      @single-change="changeMenuSearch"
    ></menu-search>
  </div>
</template>

<script setup lang="ts">
import { MenuSearchItemImpl } from "../MenuSearchComposition";
import { MenuSearchTypeProps } from "../type/MenuSearchTypeProps";
import { MenuSearchTypeComposition } from "../type/MenuSearchTypeComposition";
import MenuSearch from "../menu-search.vue";

const props = withDefaults(defineProps<MenuSearchTypeProps>(), {
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
  (e: "single-change", value: MenuSearchItemImpl): void;
  (e: "multiple-change", value: MenuSearchItemImpl[]): void;
  (e: "open"): void;
  (e: "cancel"): void;
  (e: "close"): void;
}>();

const applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as MenuSearchItemImpl[]);
  } else {
    emit("single-change", value as MenuSearchItemImpl);
  }
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
  MenuSearchTypeComposition(props, applyData, openMenuSearch, panelClosed);
</script>

<style scoped></style>
