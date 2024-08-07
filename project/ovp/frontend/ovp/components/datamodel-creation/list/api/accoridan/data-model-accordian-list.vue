<template>
  <div class="accordion-group">
    <div class="h-group">
      <div class="search-input">
        <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
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
    <div class="accordion-list">
      <Accordion
        v-for="(item, keyName, idx) in listData"
        :key="item[props.valueKey]"
      >
        <template #title>
          {{ $constants.DATAMODEL_CREATION.ADD.ACCORDION[keyName] }}
        </template>
        <template #body>
          <div class="menu menu-data">
            <ul class="menu-list" v-if="checkShowListData(item)">
              <template v-for="(itemValue, idx) in item" :key="idx">
                <data-model-list-item
                  v-if="!itemValue.isSelected && itemValue.isShow"
                  :key="itemValue.value + idx"
                  :is-multi="props.isMulti"
                  :use-delete-btn="props.useItemDeleteBtn"
                  :data="itemValue"
                  @context-menu-click="onShowContextMenu(keyName, $event)"
                  @context-menu-btn-click="
                    onShowContextMenuBtn(keyName, $event)
                  "
                  @bookmark-change="onChangeBookmark(keyName, $event)"
                  @click="onClickDataModelItem"
                  @delete="onDeleteItem"
                  @select="onSelectItem"
                  @check="onCheckItem(keyName, $event)"
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
      </Accordion>
    </div>
  </div>
</template>
<script setup lang="ts">
import Accordion from "@base/accordion/accordion.vue";
import { DataModelAccordianListComposition } from "~/components/datamodel-creation/list/api/accoridan/DataModelAccordianListComposition";
import type { DataModelAccordianListProps } from "~/components/datamodel-creation/list/api/accoridan/DataModelAccoridanListProps";
import $constants from "~/utils/constant";
import DataModelListItem from "~/components/datamodel-creation/item/data-model-list-item.vue";
const props = withDefaults(defineProps<DataModelAccordianListProps>(), {
  data: () => {},
  selectedItems: () => {},
  useLiveSearch: false,
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
  (e: "item-click", value: string): void;
  (e: "bookmark-change", value: string): void;
  (e: "search-change", value: string): void;
}>();

const emitBookmark = (value: string) => {
  emit("bookmark-change", value);
};

const emitItemClick = (value: string) => {
  emit("item-click", value);
};

const emitDeleteItem = (value: string) => {
  emit("delete", value);
};

const emitSelectItem = (value: string) => {
  emit("select", value);
};
const emitSearchChange = (value: string) => {
  emit("search-change", value);
};
const {
  listData,
  searchLabel,
  checkShowListData,
  onSearchText,
  onResetSearchText,
  onResetSearchFilter,
  onShowContextMenu,
  onShowContextMenuBtn,
  onChangeBookmark,
  onClickDataModelItem,
  onDeleteItem,
  onSelectItem,
} = DataModelAccordianListComposition(
  props,
  emitBookmark,
  emitItemClick,
  emitDeleteItem,
  emitSelectItem,
  emitSearchChange,
);
</script>
