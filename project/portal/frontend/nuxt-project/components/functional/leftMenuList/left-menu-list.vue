<template>
  <div class="category-wrap" style="min-height: 798px;">
    <!-- category-options: 메타 데이터 통합 관리 페이지에서 호출 시에만 옵션 버튼 세팅 -->
    <div class="category-options" v-if="calledFrom === 'metaDataManage'">
      <button
        class="mu-btn mu-btn-sm"
        title="추가"
        @click.stop="() => onClickButton('add')"
      >
        <i class="mu-icon-svg add"></i><span>추가</span>
      </button>
      <button
        class="mu-btn mu-btn-sm"
        title="선택 초기화"
        @click.stop="() => onClickButton('refresh')"
      >
        <i class="mu-icon-svg refresh"></i><span>초기화</span>
      </button>
    </div>

    <div class="category-head">
      <h4>전체</h4>
      <!-- &nbsp; &nbsp; &nbsp; <button v-if="calledFrom === 'metaDataManage'" type="button" @click="onClickCategoryOrderChange()">순서 변경</button> -->
    </div>
    <div class="category-body">
      <ul class="category-menu slim-scroll">
        <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
          <button
            type="button"
            :class="[toggleTargets.all ? 'on' : '', 'item-toggle']"
            @click="onToggle('all')"
          >
            <span>펼치기</span>
          </button>
          <a
            href="javascript:void(0);"
            :class="['' === selectedGuid ? 'active' : '', 'item-label']"
            title="전체"
            @click="() => onClick()"
          >
            전체
          </a>
          <menu-item
            :list="categoryList"
            :toggle-targets="toggleTargets"
            :selected-guid="selectedGuid"
            @onToggleNode="onToggle"
            @onClickMenuItem="onClick"
            @onClickSetup="onToggleTooltip"
          />
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import MenuItem from "./menu-item";
import { useCategoryStore } from "~/store/category";
import { useEventStore  } from "~/store/event.ts";
import { computed } from 'vue';

const emit = defineEmits([
  "onClickMenuItem", // 메뉴 클릭 이벤트 핸들러
  "onClickCategoryOrderChange",
  "onToggleMetaDataTooltip",
  "onClickOptionalButtons", // 메타 데이터 통합 관리 화면용 추가 버튼 클릭 이벤트 핸들러
  "onClickFromTreeMap",
])

const categoryStore = useCategoryStore();
const categoryList = computed(() => categoryStore.getCategoryList);
const toggleTargets = categoryStore.getToggleTargets;
const selectedGuid = categoryStore.getSelectedItemGuid;
const calledFrom = categoryStore.getCalledFrom;

const eventStore = useEventStore();

// 메뉴 클릭 이벤트 핸들러
const onClick = (item) => {
  categoryStore.setSelectedItemGuid(item && item.guid ? item.guid : "");
  emit("onClickMenuItem", item);


  // if (item && item.name != 'CAT' && item.name != 'SYS') {
  //   categoryStore.setSelectedItemGuid(item && item.guid ? item.guid : "");
  //   emit("onClickMenuItem", item);
  // } else {
  //   if (item) {
  //     categoryStore.setToggleTargetOnClick(item.name);
  //   } else {
  //     categoryStore.setSelectedItemGuid("");
  //     categoryStore.setToggleTargetOnClick("");
  //     emit("onClickMenuItem");
  //   }

  // }
}

// 카테고리 순서 변경 클릭 이벤트 핸들러
const onClickCategoryOrderChange = () => {
  emit("onClickCategoryOrderChange");
}

// 카테고리 세팅 버튼 클릭 이벤트 핸들러
const onToggleTooltip = (e, item, type) => {
  eventStore.emit('onToggleMetaDataTooltip', e, item, type);
}

// 카테고리 목록 상단 추가/새로고침 버튼 클릭 이벤트 핸들러
const onClickButton = (type) => {
  emit("onClickOptionalButtons", type);
}

const onToggle = (value) => {
  categoryStore.setToggleTargetOnClick(value);
}
</script>
