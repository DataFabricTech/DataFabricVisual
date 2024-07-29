<template>
  <client-only>
    <div class="select select-clean">
      <button class="select-button" @click.stop="onClickOpenMenuSearch">
        <span class="select-button-title" v-if="selectedListData.length < 1">{{ props.title }}</span>
        <div class="tag tag-primary tag-sm" v-else v-for="item in selectedListData">
          <span class="tag-text">{{ item[props.labelKey] }}</span>
          <button class="tag-delete-button" @click.stop="onDeleteTag(item)">
            <span class="hidden-text">삭제</span>
            <svg-icon class="svg-icon" name="close"></svg-icon>
          </button>
        </div>

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
  </client-only>
</template>

<script setup lang="ts">
import { MenuSearchItemImpl } from "../MenuSearchComposition";
import { MenuSearchTagComposition } from "./MenuSearchTagComposition";
import { MenuSearchTypeProps } from "../type/MenuSearchTypeProps";
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
  title: "값을 선택하세요"
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

const { isMenuSearchShow, selectedListData, onCancel, onDeleteTag, onClickOpenMenuSearch, changeMenuSearch } =
  MenuSearchTagComposition(props, applyData, openMenuSearch, panelClosed);
</script>

<style scoped></style>
