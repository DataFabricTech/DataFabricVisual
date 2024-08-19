<template>
  <li
    class="menu-item"
    :class="props.data.idShowDetail ? ' is-menu-item-selected' : ''"
  >
    <div
      class="checkbox"
      v-if="props.isMulti"
      @contextmenu.prevent="onShowContextMenu"
    >
      <input
        type="checkbox"
        :id="'checkbox-menu' + props.data.value"
        class="checkbox-input"
        :checked="props.data.isChecked"
        @input="onChangeChecked($event.target.checked)"
      />

      <label :for="'checkbox-menu' + props.data.value" class="checkbox-label">
        <span class="hidden-text">label</span>
      </label>
    </div>
    <button
      class="menu-button"
      @click="onClickItem"
      @contextmenu.prevent="onShowContextMenu"
    >
      <div class="type-img type-img-oracle"></div>
      <span class="menu-text">{{ props.data.label }}</span>
      <span class="menu-subtext">{{ owner }}</span>
    </button>
    <div class="menu-button-group">
      <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
      <button
        class="button button-neutral-ghost button-sm"
        @click="changeBookmark"
      >
        <span class="hidden-text">북마크</span>
        <svg-icon
          class="svg-icon secondary"
          name="tag-fill"
          v-if="props.data.bookmarked"
        ></svg-icon>
        <svg-icon class="svg-icon" name="tag" v-else></svg-icon>
      </button>
      <div class="relative">
        <button
          class="button button-neutral-ghost button-sm"
          @click="onShowContextMenuBtn"
        >
          <span class="hidden-text">메뉴보기</span>
          <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
        </button>
        <!-- TODO: [개발] 메뉴 버튼 클릭 시 드롭다운 -->
        <div
          class="dropdown"
          style="right: 0"
          v-if="props.data.isShowContextMenuBtn"
          v-on-click-outside="hideContextMenuBtn"
        >
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <button class="dropdown-button" @click="onOpenDataModelDetail">
                <span class="dropdown-text">데이터 모델 상세 조회</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button
                class="dropdown-button"
                v-if="props.listType === 'selected'"
                @click="onDeleteItem"
              >
                <span class="dropdown-text">데이터 선택 해제</span>
              </button>
              <button
                class="dropdown-button"
                v-else
                @click="onChangeCheckedInContextMenu(!props.data.isChecked)"
              >
                <span class="dropdown-text">데이터 선택 </span>
              </button>
            </li>
          </ul>
        </div>
      </div>
      <button
        class="button button-neutral-ghost button-sm"
        v-if="props.useDeleteBtn"
        @click="onDeleteItem"
      >
        <span class="hidden-text">삭제</span>
        <svg-icon class="svg-icon" name="close"></svg-icon>
      </button>
    </div>
    <div
      class="dropdown"
      style="top: 20px; left: 70px"
      v-if="props.data.isShowContextMenu"
      v-on-click-outside="hideContextMenu"
    >
      <ul class="dropdown-list">
        <li class="dropdown-item">
          <button class="dropdown-button" @click="copyKeyword">
            <span class="dropdown-text">복사 (데이터모델 이름)</span>
          </button>
        </li>
      </ul>
    </div>
  </li>
</template>
<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { defineProps } from "vue";

const props = defineProps({
  data: { type: Object, default: {} },
  isMulti: { type: Boolean, default: false },
  listType: { type: String, default: "selected" },
  useDeleteBtn: { type: Boolean, default: false },
});

const emit = defineEmits<{
  (e: "click", value: string): void;
  (e: "delete", value: string): void;
  (e: "select", value: string): void;
  (e: "check", value: boolean): void;
  (e: "bookmark-change", value: string): void;

  (e: "context-menu-click", value: boolean | null): void;
  (e: "context-menu-btn-click", value: boolean | null): void;
}>();

const changeBookmark: () => void = () => {
  emit("bookmark-change", props.data.value);
};
const onClickItem: () => void = () => {
  emit("click", props.data.value);
};
const onDeleteItem: () => void = () => {
  emit("delete", props.data.value);
  hideContextMenuBtn();
};
const onChangeCheckedInContextMenu: (checked: boolean) => void = (checked) => {
  emit("check", checked);
  hideContextMenuBtn();
};

const onChangeChecked: (checked: boolean) => void = (checked) => {
  emit("check", checked);
};

const onShowContextMenu: () => void = () => {
  emit("context-menu-click", !props.data.isShowContextMenu);
};
const hideContextMenu: () => void = () => {
  emit("context-menu-click", null);
};
const onShowContextMenuBtn: () => void = () => {
  emit("context-menu-btn-click", !props.data.isShowContextMenu);
};
const hideContextMenuBtn: () => void = () => {
  emit("context-menu-btn-click", null);
};
const copyKeyword: () => void = () => {
  window.navigator.clipboard.writeText(`\`${props.data.fqn}\``).then(() => {
    alert("데이터 모델 이름 복사 완료");
    hideContextMenu();
  });
};
const onOpenDataModelDetail: () => void = () => {
  const { id, fqn, type } = props.data as {
    id: string;
    fqn: string;
    type: string;
  };
  window.open(`/portal/search/detail?id=${id}&fqn=${fqn}&type=${type}`);
  hideContextMenuBtn();
};
const owner = computed(() => {
  const nOwner = props.data.owner ? props.data.owner : "-";
  return `(${nOwner})`;
});
</script>

<style scoped></style>
