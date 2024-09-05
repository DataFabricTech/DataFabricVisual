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
            @keydown.enter="onSearchText($event.target.value)"
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
            <template v-if="keyName === 'category'">
              <menu-search-tree
                label-key="name"
                value-key="id"
                :title="filterItem.text"
                :data="filterItem.data.children"
                :is-multi="true"
                :hideGuideLines="false"
                :firExpandAll="true"
                :show-open-all-btn="false"
                :show-close-all-btn="false"
                :use-draggable="false"
                :selected-items="props.selectedFilters[keyName]"
                mode="view"
                @multiple-change="onSelectFilter(keyName, $event)"
              />
            </template>
            <template v-else>
              <menu-search-button
                :data="filterItem.data"
                :selected-items="props.selectedFilters[keyName]"
                label-key="key"
                value-key="key"
                :title="filterItem.text"
                :is-multi="true"
                @multiple-change="onSelectFilter(keyName, $event)"
              ></menu-search-button>
            </template>
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
        :selected-item="selectedSort"
        @select="onChangeSort"
      ></select-box>
    </div>

    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result" v-if="!isDoneFirModelListLoad">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">{{ props.noDataMsg }}</p>
      </div>
    </div>

    <ul id="dataListModal" class="menu-list">
      <template v-for="(item, idx) in listData" :key="item.value + idx">
        <data-model-list-item
          v-if="!item.isSelected"
          checked-key="checked-menu-api-list-"
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
      <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
      <Loading
        id="dataModelApiListLoader"
        :use-loader-overlay="true"
        class="loader-lg is-loader-inner"
        style="display: none"
      ></Loading>
    </ul>
  </div>
</template>

<script setup lang="ts">
import SelectBox from "@extends/select-box/SelectBox.vue";
import DataModelListItem from "~/components/datamodel-creation/item/data-model-list-item.vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import { DataModelApiListComposition } from "~/components/datamodel-creation/list/api/DataModelApiListComposition";
import type { DataModelApiListProps } from "~/components/datamodel-creation/list/api/DataModelApiListProps";
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";

const props = withDefaults(
  defineProps<
    DataModelApiListProps & {
      onAddTransfer?: () => void;
      isDoneFirModelListLoad?: boolean;
    }
  >(),
  {
    data: () => [],
    selectedItems: () => [],
    selectedFilters: () => [],
    sortList: () => [],
    filter: () => {},
    addSearchList: () => {
      return () => {};
    },
    useSort: false,
    useLiveSearch: true,
    useInfinite: false,
    isMulti: false,
    valueKey: "id",
    labelKey: "title",
    noDataMsg: "데이터 모델이 없습니다.",
    listType: "non-selected",
    useItemDeleteBtn: false,
    isDoneFirModelListLoad: false,
  },
);

const emit = defineEmits<{
  (e: "delete", value: any[]): void;
  (e: "item-check", value: any[]): void;
  (e: "item-click", value: string): void;
  (e: "bookmark-change", value: string): void;
  (e: "filter-change", value: string): void;
  (e: "sort-change", value: string): void;
  (e: "search-change", value: string): void;
  (e: "filter-reset", value: string): void;
}>();

const emitBookmark = (value: string) => {
  emit("bookmark-change", value);
};

const emitItemClick = (value: string) => {
  emit("item-click", value);
};

const emitItemCheck = (value: any[]) => {
  emit("item-check", value);
};

const emitDeleteItem = (value: any[]) => {
  emit("delete", value);
};

const emitFilterChange = (value: {}) => {
  emit("filter-change", value);
};
const emitSortChange = (value: string) => {
  emit("sort-change", value);
};
const emitSearchChange = (value: string) => {
  emit("search-change", value);
};

const { scrollTrigger } = useIntersectionObserver({
  callback: props.addSearchList,
  targetId: "dataListModal",
  loaderId: "dataModelApiListLoader",
});

const {
  listData,
  searchLabel,
  selectedSort,
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
  onChangeSort,
  onCheckItem,
} = DataModelApiListComposition(
  props,
  emitBookmark,
  emitItemClick,
  emitDeleteItem,
  emitFilterChange,
  emitSortChange,
  emitSearchChange,
  emitItemCheck,
);
</script>
