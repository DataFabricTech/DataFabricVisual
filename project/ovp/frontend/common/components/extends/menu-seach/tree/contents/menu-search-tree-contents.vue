<template>
  <div v-on-click-outside="onCancel" v-show="props.isShow" class="menu menu-search" style="width: 100%">
    <div class="menu-head">
      <!-- 검색 기능 -->
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">검색</label>
        <input
          id="text-input-example-11"
          class="text-input"
          :value="searchLabel"
          @input="onSearchText($event.target.value)"
          placeholder="검색어를 입력하세요"
        />
        <svg-icon class="text-input-icon" name="user"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
          @click="onResetSearchText"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
    </div>
    <div class="menu-list">
      <tree-vue
        :items="props.data"
        :isCheckable="props.isCheckable"
        :hideGuideLines="props.hideGuideLines"
        :firExpandAll="props.firExpandAll"
        :show-open-all-btn="props.showOpenAllBtn"
        :show-close-all-btn="props.showCloseAllBtn"
        :use-draggable="props.useDraggable"
        :checked-ids="firCheckedIds"
        :mode="props.mode"
        :dropValidator="props.dropValidator"
      ></tree-vue>
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
import { vOnClickOutside } from "@vueuse/components";
import TreeVue from "~/components/extends/tree/Tree.vue";
import { MenuSearchTreeContentsProps } from "~/components/extends/menu-seach/tree/contents/MenuSearchTreeContentsProps";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";
import { MenuSearchTreeContentsComposition } from "~/components/extends/menu-seach/tree/contents/MenuSearchTreeContentsComposition";

const props = withDefaults(defineProps<MenuSearchTreeContentsProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",

  // menu-search 고유 props
  selectedItems: () => [] || {},
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색결과가 없습니다.",
  isShow: false,

  // tree 고유 props
  mode: "view",
  isCheckable: false,
  hideGuideLines: false,
  showOpenAllBtn: false,
  showCloseAllBtn: false,
  firExpandAll: false,
  dropValidator: () => {
    return () => false;
  }
});

const emit = defineEmits<{
  (e: "change", value: TreeViewItem[]): void;
  (e: "cancel"): void;
}>();

const applyData: (value: TreeViewItem[]) => void = (value) => {
  emit("change", value);
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
  firCheckedIds,
  onApply,
  onReset,
  onCancel,
  checkSearchResult
} = MenuSearchTreeContentsComposition(props, applyData, cancelData);

// emit 생성
</script>
<style scoped>
.select-single {
  color: #016d77;
  font-weight: bold;
}
</style>
