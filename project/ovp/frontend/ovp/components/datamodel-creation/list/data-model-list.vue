<template>
  <div class="menu menu-data">
    <div class="menu-head">
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
            <select-box
              class="select-clean select-sm"
              :data="filterItem.data"
              :selected-item="selectedFilter[keyName]"
              :is-multi="false"
              :title="filterItem.text"
              label-key="key"
              value-key="key"
              @select="onSelectFilter(keyName, $event)"
            ></select-box>
          </template>
        </div>
      </div>
      <!-- 정렬 -->
      <select-box
        class="select-sm w-full"
        v-if="props.useSort"
        :data="props.sortList"
        label-key="label"
        value-key="value"
      ></select-box>
    </div>
    <ul class="menu-list">
      <template v-for="(item, idx) in listData">
        <data-model-list-item
          v-if="item.isShow"
          :key="item.value + idx"
          :is-multi="props.isMulti"
          :use-delete-btn="props.useItemDeleteBtn"
          :data="item"
          @context-menu-click="onShowContextMenu(item.value, $event)"
          @context-menu-btn-click="onShowContextMenuBtn(item.value, $event)"
          @bookmark-change="onChangeBookmark"
          @click="onClickDataModelItem"
          @delete="onDeleteItem"
          @select="onSelectItem"
        ></data-model-list-item>
      </template>
    </ul>
    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result" style="display: none">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">{{ props.noDataMsg }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import SelectBox from "@extends/select-box/SelectBox.vue";
import DataModelListItem from "~/components/datamodel-creation/item/data-model-list-item.vue";
import type { DataModelListProps } from "~/components/datamodel-creation/list/DataModelListProps";
import { DataModelListComposition } from "~/components/datamodel-creation/list/DataModelListComposition";

const props = withDefaults(defineProps<DataModelListProps>(), {
  data: () => [],
  sortList: () => [],
  filter: () => {},
  useSort: false,
  useLiveSearch: true,
  useInfinite: false,
  isMulti: false,
  valueKey: "id",
  labelKey: "title",
  noDataMsg: "데이터 모델이 없습니다.",
  listType: "non-selected",
  useItemDeleteBtn: false,
});

const emit = defineEmits<{
  (e: "delete", value: string): void;
  (e: "select", value: string): void;
  (e: "bookmark-change", value: string): void;
  (e: "item-click", value: string): void;
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

const emitSelectItem = (value: string) => {
  console.log("onSelectItem");
  emit("select", value);
};

const {
  listData,
  searchLabel,
  selectedFilter,
  onSearchText,
  onSelectFilter,
  onResetSearchText,
  onResetSearchFilter,
  onShowContextMenu,
  onShowContextMenuBtn,
  onChangeBookmark,
  onClickDataModelItem,
  onDeleteItem,
  onSelectItem,
} = DataModelListComposition(
  props,
  emitBookmark,
  emitItemClick,
  emitDeleteItem,
  emitSelectItem,
);
</script>
