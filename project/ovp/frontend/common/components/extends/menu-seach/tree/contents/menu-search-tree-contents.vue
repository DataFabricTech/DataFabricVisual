<template>
  <div v-show="props.isShow" class="menu menu-search" style="width: 100%">
    <!-- TODO: Vue-tree 라이브러리에서 변경 목록 데이터 랜더링 문제로 검색 기능은 추후 개발 -->
    <!--    <div class="menu-head">-->
    <!--      &lt;!&ndash; 검색 기능 &ndash;&gt;-->
    <!--      <div class="search-input">-->
    <!--        <label class="hidden-text" for="text-input-example-11">검색</label>-->
    <!--        <input-->
    <!--          id="text-input-example-11"-->
    <!--          class="text-input"-->
    <!--          :value="searchLabel"-->
    <!--          @input="onSearchText($event.target.value)"-->
    <!--          placeholder="검색어를 입력하세요"-->
    <!--        />-->
    <!--        <svg-icon class="text-input-icon" name="user"></svg-icon>-->
    <!--        <button-->
    <!--          class="search-input-action-button button button-neutral-ghost button-sm"-->
    <!--          type="button"-->
    <!--          @click="onResetSearchText"-->
    <!--        >-->
    <!--          <span class="hidden-text">지우기</span>-->
    <!--          <svg-icon class="button-icon" name="close"></svg-icon>-->
    <!--        </button>-->
    <!--      </div>-->
    <!--    </div>-->
    <div class="menu-list">
      <tree-vue
        :items="listData"
        :checked-ids="selectedListDataIds"
        :isCheckable="props.isMulti"
        :hideGuideLines="props.hideGuideLines"
        :firExpandAll="props.firExpandAll"
        :show-open-all-btn="props.showOpenAllBtn"
        :show-close-all-btn="props.showCloseAllBtn"
        :use-draggable="props.useDraggable"
        :mode="props.mode"
        :dropValidator="props.dropValidator"
        :selected-ids="selectedListDataIds"
        :use-fir-select="false"
        :use-select-only-last-child="props.useSelectOnlyLastChild"
        @onItemChecked="onNodeChecked"
        @onItemSelected="onNodeClicked"
      ></tree-vue>
      <div class="menu-item" v-if="listData.length < 1">
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
import TreeVue from "../../../tree/Tree.vue";
import { TreeViewItem } from "@/components/extends/tree/TreeProps";
import { MenuSearchTreeContentsProps } from "./MenuSearchTreeContentsProps";
import { MenuSearchTreeContentsComposition } from "./MenuSearchTreeContentsComposition";

const props = withDefaults(defineProps<MenuSearchTreeContentsProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",

  // menu-search 고유 props
  selectedItems: () => [] || {},
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색결과가 없습니다.",
  isShow: false,
  isMulti: false,
  outside: false,
  isResetToFirst: false,

  // tree 고유 props
  mode: "view",
  hideGuideLines: false,
  showOpenAllBtn: false,
  showCloseAllBtn: false,
  firExpandAll: false,
  dropValidator: () => {
    return () => false;
  },
  useSelectOnlyLastChild: false
});

const emit = defineEmits<{
  (e: "single-change", value: TreeViewItem): void;
  (e: "multiple-change", value: TreeViewItem[]): void;
  (e: "cancel"): void;
}>();

const applyData: (value: TreeViewItem | TreeViewItem[]) => void = (value) => {
  if (props.isMulti) {
    emit("multiple-change", value as TreeViewItem[]);
  } else {
    emit("single-change", value as TreeViewItem);
  }
};
const cancelData: () => void = () => {
  emit("cancel");
};

const {
  listData,
  searchLabel,
  selectedListDataIds,
  onSearchText,
  onResetSearchText,
  onNodeChecked,
  onNodeClicked,
  onApply,
  onReset,
  onCancel
} = MenuSearchTreeContentsComposition(props, applyData, cancelData);

// emit 생성
</script>
<style scoped>
.select-single {
  color: #016d77;
  font-weight: bold;
}
</style>
