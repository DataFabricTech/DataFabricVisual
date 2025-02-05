<template>
  <div class="category-wrap">
    <div class="category-options">
      <button class="mu-btn mu-btn-sm mu-btn-icon-only" title="조직 추가" @click.stop="() => onClickButton('add')">
        <i class="mu-icon-svg add"></i><span>추가</span>
      </button>
      <button
        class="mu-btn mu-btn-sm mu-btn-icon-only"
        title="조직 초기화"
        @click.stop="() => onClickButton('refresh')"
      >
        <i class="mu-icon-svg refresh"></i><span>초기화</span>
      </button>
    </div>

    <div class="category-head">
      <h4>조직</h4>
    </div>
    <div class="category-body">
      <ul class="category-menu slim-scroll">
        <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
          <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']" @click="onToggle('all')">
            <span>펼치기</span>
          </button>
          <a
            href="javascript:void(0);"
            :class="['' === selectedId ? 'active' : '', 'item-label']"
            title="조직 전체"
            @click="() => onClick()"
          >
            조직 전체
          </a>
          <menu-item
            :list="deptList"
            :toggle-targets="toggleTargets"
            :selected-id="selectedId"
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
import { computed } from "vue";
import { useDeptStore } from "@store/dept.ts";
import { useEventStore } from "~/store/event.ts";
import MenuItem from "@/pages/admin/manager/system/user/menu-item";

const emit = defineEmits([
  "onClickMenuItem",
  "onClickCategoryOrderChange",
  "onToggleMetaDataTooltip",
  "onClickOptionalButtons",
  "onClickFromTreeMap"
]);

const deptStore = useDeptStore();
const deptList = computed(() => deptStore.getDeptList);
const toggleTargets = deptStore.getToggleTargets;
const selectedId = deptStore.getSelectedItemId;
const calledFrom = deptStore.getCalledFrom;

const eventStore = useEventStore();

//
const findMaxOrdNo = (item, deptList, isSubDept = false) => {
  const level = isSubDept ? parseInt(item.level) + 1 : item.level;
  const parentId = isSubDept ? item.id : (item.parent_id == null ? '' : item.parent_id);
  let maxOrdNo = 0;

  const traverse = (list) => {
    list.forEach(dept => {
      let parent_id = dept.parent_id == null ? '' : dept.parent_id;

      if (parent_id === parentId && parseInt(dept.level) === parseInt(level)) {
        maxOrdNo = Math.max(maxOrdNo, dept.ord_no);
      }
      if (dept.subTypes && dept.subTypes.length > 0) {
        traverse(dept.subTypes);
      }
    });
  };

  traverse(deptList);

  return maxOrdNo;
};

// 
const onClick = (item) => {
  if (item) {
    onSelectedDeptMaxNo(item);
  }

  deptStore.setSelectedItemId(item && item.id ? item.id : "");
  emit("onClickMenuItem", item);
};

//
const onSelectedDeptMaxNo = (item) => {
  const maxOrdNo = findMaxOrdNo(item, deptList.value);
  const maxSubDeptOrdNo = findMaxOrdNo(item, deptList.value, true);

  deptStore.setSelectedDeptLevelMaxOrdNo(maxOrdNo);
  deptStore.setSelectedSubDeptLevelMaxOrdNo(maxSubDeptOrdNo);
};

// 
const onClickCategoryOrderChange = () => {
  emit("onClickCategoryOrderChange");
};

// 
const onToggleTooltip = (e, item, type) => {
  eventStore.emit("onToggleMetaDataTooltip", e, item, type);
};

// 
const onClickButton = (type) => {
  if(deptList.value.length != 0 && type === "add") {
    onSelectedDeptMaxNo(deptList.value[0]);
  }

  emit("onClickOptionalButtons", type);
};

// 
const onToggle = (value) => {
  deptStore.setToggleTargetOnClick(value);
};
</script>
