<template>
  <div class="menu menu-search" v-on-click-outside="onCancel">
    <div class="menu-head">
      <!-- 검색 기능 -->
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">검색</label>
        <input
          id="text-input-example-11"
          class="text-input"
          :value="searchLabel"
          @input="onSearchText($event.target.value)"
          placeholder="검색어를 입력하세요"/>
        <svg-icon class="text-input-icon" name="user"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
          @click="onResetSearchText">
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
        <button class="search-input-button button button-neutral-ghost">
          <span class="hidden-text">검색</span>
          <svg-icon class="button-icon" name="search"></svg-icon>
        </button>
      </div>
    </div>
    <div class="menu-list">
      <div v-for="(item, key) in selectedListData" :key="key">
        <div class="menu-item" v-if="item.isChecked && item.isShow">
          <div class="checkbox" v-if="isMulti">
            <input
              type="checkbox"
              :id="'menu-search-selected-' + item.id"
              class="checkbox-input"
              :checked="item.isChecked"
              @input="onCancelSelect(item, $event.target.checked)"
            />
            <label :for="'menu-search-selected-' + item.id" class="checkbox-label">
              {{ item.label }}
            </label>
          </div>
          <div class="select-single" v-else @click="onCancelSelect(item, false)">
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
      <!-- 일반 리스트 -->
      <div v-for="(item, key) in listData" :key="key">
        <div class="menu-item" v-if="!item.isChecked && item.isShow">
          <div class="checkbox" v-if="isMulti">
            <input
              type="checkbox"
              :id="'menu-search-data-' + item.id"
              class="checkbox-input"
              :checked="item.isChecked"
              @input="onSelectListData(item, $event.target.checked)"
            />
            <label :for="'menu-search-data-' + item.id" class="checkbox-label">
              {{ item.label }}
            </label>
          </div>
          <div v-else @click="onSelectListData(item, true)">
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
      <div class="menu-item" v-if="checkSearchResult()">
        <span> {{ props.noSearchMsg }} </span>
      </div>
    </div>
    <div class="menu-foot">
      <button class="button button-error-ghost" @click="onReset">초기화</button>
      <div class="menu-foot-group">
        <button class="button button-neutral-ghost" @click="onCancel">취소</button>
        <button class="button button-primary" @click="onApply">적용</button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { MenuSearchProps } from "~/components/extends/menu-seach/MenuSearchProps";
import {
  MenuSearchItemImpl, MenuSearchComposition
} from "~/components/extends/menu-seach/MenuSearchComposition";
import { vOnClickOutside } from "@vueuse/components";

const props = withDefaults(defineProps<MenuSearchProps>(), {
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
const cancelData : () => void = () => {
  emit("cancel")
}

const {
  selectedListData,
  listData,
  searchLabel,
  onSearchText,
  onResetSearchText,
  onCancelSelect,
  onSelectListData,
  onApply,
  onReset,
  onCancel,
  checkSearchResult,
} = MenuSearchComposition(props, applyData, cancelData);

// emit 생성
</script>
<style scoped>
.select-single {
  color: #016d77;
  font-weight: bold;
}
</style>
