<template>
  <div v-on-click-outside="onCancel" v-show="props.isShow" class="menu menu-search">
    <div class="menu-head">
      <!-- 검색 기능 -->
      <!-- TODO: [개발] 접근성 개선 - label/input의 for/id값 사용되는 용도마다 다르게 적용 필요,
         label안 텍스트도 용도마다 적절히 사용되어야 함 -->
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">검색</label>
        <input
          id="text-input-example-11"
          class="text-input"
          :value="searchLabel"
          @input="onSearchText($event.target.value)"
          placeholder="검색어를 입력하세요"
        />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-xs"
          type="button"
          @click="onResetSearchText"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
    </div>
    <div class="menu-list">
      <!-- 선택 리스트 -->
      <template v-for="(item, index) in selectedListData" :key="item.id + index">
        <div
          class="menu-item"
          :class="{ 'is-menu-item-selected': !props.isMulti }"
          v-if="item.isShow"
          style="z-index: 999"
        >
          <div class="checkbox" v-if="isMulti">
            <input
              type="checkbox"
              class="checkbox-input"
              :id="'menu-search-selected-' + item.id"
              :checked="item.isChecked"
              @input="onCancelSelect(item, $event.target.checked)"
            />
            <label :for="'menu-search-selected-' + item.id" class="checkbox-label">
              <span class="checkbox-text">{{ item.label }}</span>
            </label>
          </div>
          <button class="menu-button" v-else @click="onCancelSelect(item, false)">
            <span class="menu-text">{{ item.label }}</span>
          </button>
        </div>
      </template>
      <!-- 일반 리스트 -->
      <div v-for="(item, index) in listData" :key="index">
        <div class="menu-item" v-if="!item.isChecked && item.isShow">
          <div class="checkbox" v-if="isMulti">
            <input
              type="checkbox"
              class="checkbox-input"
              :id="'menu-search-data-' + item.id"
              :checked="item.isChecked"
              @input="onSelectListData(item, $event.target.checked)"
            />
            <label :for="'menu-search-data-' + item.id" class="checkbox-label">
              <span class="checkbox-text">{{ item.label }}</span>
            </label>
          </div>
          <button class="menu-button" v-else @click="onSelectListData(item, true)">
            <span class="menu-text">{{ item.label }}</span>
          </button>
        </div>
      </div>

      <div class="menu-item" v-if="checkSearchResult()">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">{{ props.noSearchMsg }}</p>
        </div>
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
import { vOnClickOutside } from "@vueuse/components";
import { MenuSearchProps } from "./MenuSearchProps";
import { MenuSearchItemImpl, MenuSearchComposition } from "./MenuSearchComposition";

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
  isShow: false
});

const emit = defineEmits<{
  (e: "single-change", value: MenuSearchItemImpl): void;
  (e: "multiple-change", value: MenuSearchItemImpl[]): void;
  (e: "cancel"): void;
}>();

const applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as MenuSearchItemImpl[]);
  } else {
    emit("single-change", value as MenuSearchItemImpl);
  }
};
const cancelData: () => void = () => {
  emit("cancel");
};

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
  checkSearchResult
} = MenuSearchComposition(props, applyData, cancelData);

// emit 생성
</script>
<style scoped>
.select-single {
  color: #016d77;
  font-weight: bold;
}
</style>
