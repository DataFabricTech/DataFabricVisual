<template>
  <div class="menu menu-data">
    <div class="menu-head">
      <slot name="tab"></slot>
      <!--검색 -->
      <div class="h-group">
        <div class="search-input">
          <label class="hidden-text" for="data-menu-search"
            >데이터 모델 검색</label
          >
          <input
            id="data-menu-search"
            class="text-input"
            :value="searchLabel"
            @input="onSearchText($event.target.value)"
            placeholder="검색어를 입력하세요"
          />
          <svg-icon class="text-input-icon" name="search"></svg-icon>
          <button
            class="search-input-action-button button button-neutral-ghost button-sm"
            type="button"
            @click="onResetSearchText"
          >
            <span class="hidden-text">지우기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
        <button
          class="button button-neutral-ghost button-lg"
          @click="onResetSearchFilter"
        >
          <span class="hidden-text">리셋</span>
          <svg-icon class="svg-icon" name="reset"></svg-icon>
        </button>
      </div>
      <!-- 필터 -->
      <div class="filters">
        <!-- 카테고리, 소유자, 태그 select -->
        <div class="h-group">
          <template v-for="(filterItem, keyName, FI) in props.filter" :key="FI">
            <menu-search-button
              :data="filterItem.data"
              :selected-items="selectedFilter[keyName]"
              label-key="key"
              value-key="key"
              :title="filterItem.text"
              :is-multi="true"
              @multiple-change="onSelectFilter(keyName, $event)"
            ></menu-search-button>
          </template>
        </div>
      </div>
      <!-- TODO: 정렬기능 추후 개발-->
    </div>
    <ul class="menu-list" v-if="checkShowListData">
      <template v-for="(item, idx) in listData" :key="item.value + idx">
        <data-model-list-item
          v-if="item.isShow"
          :data="item"
          :is-multi="props.isMulti"
          :use-delete-btn="props.useItemDeleteBtn"
          :list-type="props.listType"
          @context-menu-click="onShowContextMenu(item.value, $event)"
          @context-menu-btn-click="onShowContextMenuBtn(item.value, $event)"
          @bookmark-change="onChangeBookmark"
          @click="onClickDataModelItem"
          @delete="onDeleteItem"
          @check="onCheckItem(item.value, $event)"
        ></data-model-list-item>
      </template>
    </ul>

    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result" v-else>
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">{{ props.noDataMsg }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import DataModelListItem from "~/components/datamodel-creation/item/data-model-list-item.vue";
import type { DataModelListProps } from "~/components/datamodel-creation/list/base/DataModelListProps";
import { DataModelListComposition } from "~/components/datamodel-creation/list/base/DataModelListComposition";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";

const props = withDefaults(defineProps<DataModelListProps>(), {
  data: () => [],
  sortList: () => [],
  filter: () => {},
  isMulti: false,
  useItemDeleteBtn: false,
  valueKey: "id",
  labelKey: "title",
  noDataMsg: "데이터 모델이 없습니다.",
  listType: "selected",
});

const emit = defineEmits<{
  (e: "delete", value: string): void;
  (e: "bookmark-change", value: string): void;
  (e: "item-click", value: string): void;
  (e: "item-check", value: []): void;
}>();

const emitBookmark = (value: string) => {
  console.log("changeBookmark");
  emit("bookmark-change", value);
};

const emitItemClick = (value: string) => {
  console.log("onClickDataModelItem");
  emit("item-click", value);
};

const emitDeleteItem = (value: string) => {
  console.log("onDeleteItem");
  emit("delete", value);
};
const emitItemCheck = (value: string) => {
  console.log("emitItemCheck", value);
  emit("item-check", value);
};

const {
  listData,
  searchLabel,
  selectedFilter,
  checkShowListData,
  onSearchText,
  onSelectFilter,
  onResetSearchText,
  onResetSearchFilter,
  onShowContextMenu,
  onShowContextMenuBtn,
  onChangeBookmark,
  onClickDataModelItem,
  onDeleteItem,
  onCheckItem,
} = DataModelListComposition(
  props,
  emitBookmark,
  emitItemClick,
  emitDeleteItem,
  emitItemCheck,
);
</script>
