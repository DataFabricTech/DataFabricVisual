<template>
  <div class="menu menu-search" v-on-click-outside="onCancel" v-show="props.isShow">
    <div class="menu-head">
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">label</label>
        <input id="text-input-example-11" class="text-input" value="search button" />
        <svg-icon class="text-input-icon" name="user"></svg-icon>
        <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
        <button class="search-input-button button button-neutral-ghost">
          <span class="hidden-text">검색</span>
          <svg-icon class="button-icon" name="search"></svg-icon>
        </button>
      </div>
    </div>
    <ul class="menu-list">
      <!-- 선택 리스트 -->
      <div v-for="(item, index) in selectedListData" :key="index">
        <div class="menu-item" v-if="item.isShow">
          {{item.isChecked}}
          <div class="checkbox" v-if="isMulti">
            <input
                type="checkbox"
                :id="'menu-search-selected-' + item.value"
                :value="item.value"
                v-model="item.isChecked"
                @input="onCancelSelect(item, $event.target.checked)"
            />
            <label :for="'menu-search-selected-' + item.value" class="checkbox-label">
              {{ item.label }}
            </label>
          </div>
          <div class="select-single" v-else @click="onCancelSelect(item, false)">
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
      <!-- 일반 리스트 -->
      <div v-for="(item, index) in listData" :key="index">
        <div class="menu-item" v-if="!item.isChecked && item.isShow">
          <div class="checkbox" v-if="isMulti">
            <input
                type="checkbox"
                :id="'menu-search-data-' + item.value"
                class="checkbox-input"
                :checked="item.isChecked"
                @input="onSelectListData(item, $event.target.checked)"
            />
            <label :for="'menu-search-data-' + item.value" class="checkbox-label">
              {{ item.label }}
            </label>
          </div>
          <div v-else @click="onSelectListData(item, true)">
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
    </ul>
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
import {
} from "~/components/extends/menu-seach/MenuSearchComposition";
import { vOnClickOutside } from "@vueuse/components";
import {MenuSearchPropsTemp} from "~/components/extends/menu-seach-temp/MenuSearchPropsTemp";
import {
  MenuSearchCompositionTemp,
  MenuSearchItemImplTemp
} from "~/components/extends/menu-seach-temp/MenuSearchCompositionTemp";

const props = withDefaults(defineProps<MenuSearchPropsTemp>(), {
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
  isShow: false,
  isMulti: false,
});

const emit = defineEmits<{
  (e: "single-change", value: MenuSearchItemImplTemp): void
  (e: "multiple-change", value: MenuSearchItemImplTemp[]): void
  (e: "cancel"): void
}>()

const applyData : (value: MenuSearchItemImplTemp | MenuSearchItemImplTemp[]) => void  = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as MenuSearchItemImplTemp[]);
  } else {
    emit("single-change", value as MenuSearchItemImplTemp);
  }
}
const cancelData : () => void = () => {
  emit("cancel")
}

const {
  selectedListData,
  listData,
  onCancelSelect,
  onSelectListData,
  onApply,
  onReset,
  onCancel,
} = MenuSearchCompositionTemp(props, applyData, cancelData);

// emit 생성
</script>
<style scoped>
.select-single {
  color: #016d77;
  font-weight: bold;
}
</style>
