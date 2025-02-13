<template>
  <ul v-if="props.list" class="sub-category-menu">
    <li
      v-for="(item, index) in props.list"
      :key="index"
      :class="getTagStyle('li', item)"
      style="min-width: 186px; width: max-content"
    >
      <div>{{ item.checked }}</div>

      <button type="button" :class="getTagStyle('button', item)" @click="onToggle(item)">
        <span>펼치기</span>
      </button>
      <a
        href="javascript:void(0);"
        :class="getTagStyle('a', item)"
        style="overflow: visible; max-width: max-content"
        :title="item.description"
        :id="item.id"
        @click="() => onClick(item)"
      >
        {{ item.description }}&nbsp;&nbsp;[{{ item.tableCount }}]
      </a>
      <category-item
        v-if="item.subTypes"
        :list="item.subTypes"
        :toggle-targets="toggleTargets"
        :selected-id="selectedId"
        @onToggleNode="onToggle"
        @onClickMenuItem="onClick"
        @onClickSetup="onToggleTooltip"
      />
    </li>
  </ul>
</template>

<script setup lang="ts">
import { useCategoryTreeStore } from "~/store/categoryTree.ts";
import { useEventStore } from "~/store/event.ts";

const emit = defineEmits(["onToggleNode", "onClickMenuItem", "onToggleMetaDataTooltip"]);
const props = defineProps({
  list: {
    type: Array,
    default: () => []
  },
  toggleTargets: {
    type: Object,
    default: () => {}
  },
  selectedId: {
    type: String,
    default: () => ""
  },
  // 하위 메뉴 토글 이벤트 핸들러
  onToggleNode: {
    type: Function
  },
  // 카테고리 메뉴 클릭 이벤트 핸들러
  onClickMenuItem: {
    type: Function
  },
  // 카테고리 설정 버튼 클릭 이벤트 핸들러`
  onClickSetup: {
    type: Function
  }
});

const categoryTreeStore = useCategoryTreeStore();
const eventStore = useEventStore();

// data
const selectedData = ref([]);

// computed
const calledFrom = categoryTreeStore.getCalledFrom; // 카테고리 목록 컴포넌트 호출 페이지
const getSelectedItemId = categoryTreeStore.getSelectedItemId; // 선택된 메뉴 id

// 하위 메뉴 토글 버튼 클릭 이벤트 핸들러
const onToggle = (menuName) => {
  emit("onToggleNode", menuName);
};

// 카테고리 메뉴 클릭 이벤트 핸들러
const onClick = (item) => {
  categoryTreeStore.setSelectedItemId(item && item.id ? item.id : "");
  emit("onClickMenuItem", item);
};

const onClickFromTreeMap = (item) => {
  const styles = [];

  // 선택한 카테고리명 bold
  let guid = "";
  if (item.guid) guid = item.guid;
  else if (item.id) guid = item.id;
  let a = document.getElementById(guid);
  styles.push("active");
  styles.push("item-label");
  let adjustStyles = styles.join(" ");
  a.className = adjustStyles;
};
// defineExpose({onClickFromTreeMap})

// 카테고리 설정 툴팁 토글 핸들러
const onToggleTooltip = (e, item, type) => {
  eventStore.emit("onToggleMetaDataTooltip", e, item, type);
};

// 적용 클래스 계산
const getTagStyle = (tagType, item) => {
  const styles = [];

  switch (tagType) {
    case "li":
      if (item && item.subTypes && item.subTypes.length > 0) {
        styles.push("has-sub");
      }

      if (item && props.toggleTargets && props.toggleTargets[item.id]) {
        styles.push("open");
      }
      break;
    case "button":
      styles.push("item-toggle");

      if (item && props.toggleTargets && props.toggleTargets[item.id]) {
        styles.push("on");
      }

      if (item) {
        // 카테고리 미분류일 경우에만 클래스 추가
        if (item.id === "_NOT_CLASSIFIED") {
          styles.push("no-label");
        }
      }
      break;
    case "a":
      if (item.id == getSelectedItemId.value) {
        styles.push("active");
      }
      styles.push("item-label");

      if (item) {
        // 카테고리 미분류일 경우에만 클래스 추가
        if (item.id === "_NOT_CLASSIFIED") {
          styles.push("no-label");
        }
      }
      break;
  }

  return styles.join(" ");
};
</script>

<style scoped="scoped">
.no-table {
  color: #b4b4b4 !important;
}
</style>
