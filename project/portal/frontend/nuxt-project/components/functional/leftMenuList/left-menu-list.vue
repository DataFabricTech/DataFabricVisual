<template>
  <div class="category-wrap manage">
    <!-- category-options: 메타 데이터 통합 관리 페이지에서 호출 시에만 옵션 버튼 세팅 -->
    <div class="category-options" v-if="calledFrom === 'metaDataManage'">
      <button class="mu-btn mu-btn-sm mu-btn-icon-only" title="카테고리 추가" @click.stop="() => onClickButton('add')">
        <i class="mu-icon-svg add"></i><span>추가</span>
      </button>
      <button class="mu-btn mu-btn-sm mu-btn-icon-only" title="카테고리 초기화" @click.stop="() => onClickButton('refresh')">
        <i class="mu-icon-svg refresh"></i><span>초기화</span>
      </button>
    </div>

    <div class="category-head">
      <h4>카테고리</h4>
    </div>
    <div class="category-body">
      <ul class="category-menu slim-scroll">
        <li v-if="!onlySimilarAttr" :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
          <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']" @click="onToggle('all')">
            <span>펼치기</span>
          </button>
          <a href="javascript:void(0);" :class="[activeFolderSection === 'all' ? 'active' : '', 'item-label']" title="카테고리 전체"
            @click="() => onClick()">
            카테고리 전체
          </a>
          <menu-item :list="categoryList" :toggle-targets="toggleTargets" :selected-id="selectedId"
            @onToggleNode="onToggle" @onClickMenuItem="onClick" @onClickSetup="onToggleTooltip" />
        </li>
        <li v-if="similarAttrCategoryList.length > 0"
          :class="[toggleTargets.similarAttrAll ? 'open' : '', 'has-sub', 'similar-attr-category-top']">
          <button type="button" :class="[toggleTargets.similarAttrAll ? 'on' : '', 'item-toggle']"
            @click="onToggle('similarAttrAll')">
            <span>펼치기</span>
          </button>
          <a href="javascript:void(0);" :class="[activeFolderSection === 'similarAttrAll' ? 'active' : '', 'item-label']"
            title="유사 속성 카테고리 전체" @click="() => onClickSimilarAttrAll()">
            유사 속성 카테고리 전체
          </a>
          <menu-item :list="similarAttrCategoryList" :toggle-targets="toggleTargets" :selected-id="selectedId"
            @onToggleNode="onToggle" @onClickMenuItem="onClick" @onClickSetup="onToggleTooltip" />
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useCategoryStore } from "~/store/category";
import { useEventStore } from "~/store/event.ts";
import MenuItem from "./menu-item";

const emit = defineEmits([
  "onClickMenuItem", // 메뉴 클릭 이벤트 핸들러
  "onClickCategoryOrderChange",
  "onToggleMetaDataTooltip",
  "onClickOptionalButtons", // 메타 데이터 통합 관리 화면용 추가 버튼 클릭 이벤트 핸들러
  "onClickFromTreeMap"
]);

const props = defineProps({
  onlySimilarAttr: { type: Boolean, default: false },
  initialFolderSection: { type: String, default: "all" },
});

const categoryStore = useCategoryStore();
const categoryList = computed(() => categoryStore.getCategoryList);
const similarAttrCategoryList = computed(() => categoryStore.getSimilarAttrCategoryList);
const toggleTargets = categoryStore.getToggleTargets;
const selectedId = categoryStore.getSelectedItemId;
const calledFrom = categoryStore.getCalledFrom;

const eventStore = useEventStore();

// 상단 "전체" 폴더의 active 상태
// "all" = 카테고리 전체, "similarAttrAll" = 유사 속성 카테고리 전체, "" = 개별 항목 선택 중
const activeFolderSection = ref(props.initialFolderSection);

// 메뉴 클릭 이벤트 핸들러
const onClick = (item) => {
  activeFolderSection.value = item && item.id ? "" : "all";
  categoryStore.setSelectedItemId(item && item.id ? item.id : "");
  emit("onClickMenuItem", item);
};

// 유사 속성 카테고리 전체 클릭 핸들러
const onClickSimilarAttrAll = () => {
  activeFolderSection.value = "similarAttrAll";
  categoryStore.setSelectedItemId("");
  emit("onClickMenuItem", { id: "__SIMILAR_ATTR_ALL__" });
};

// 카테고리 세팅 버튼 클릭 이벤트 핸들러
const onToggleTooltip = (e, item, type) => {
  eventStore.emit("onToggleMetaDataTooltip", e, item, type);
};

// 카테고리 목록 상단 추가/새로고침 버튼 클릭 이벤트 핸들러
const onClickButton = (type) => {
  emit("onClickOptionalButtons", type);
};

const onToggle = (value) => {
  categoryStore.setToggleTargetOnClick(value);
};
</script>

<style scoped>
/* 유사 속성 카테고리 전체 폴더 아이콘 강조 색상 (노란색) */
.category-menu > li.similar-attr-category-top > .item-toggle:before {
  filter: hue-rotate(200deg) saturate(1.4);
}
</style>
