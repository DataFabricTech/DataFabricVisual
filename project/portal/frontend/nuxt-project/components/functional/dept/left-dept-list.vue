<template>
  <div class="category-wrap manage">
    <div class="category-body">
      <ul class="category-menu slim-scroll">
        <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
          <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']" @click="onToggle('all')">
            <span>펼치기</span>
          </button>
          <a href="javascript:void(0);" :class="['' === selectedId ? 'active' : '', 'item-label']" title="조직 전체"
            @click="() => onClick()">
            조직 전체
          </a>
          <menu-item :list="deptList" :toggle-targets="toggleTargets" :selected-id="selectedId" @onToggleNode="onToggle"
            @onClickMenuItem="onClick" @onClickSetup="onToggleTooltip" />
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import MenuItem from "@/pages/admin/manager/system/user/menu-item";
import { useDeptStore } from "@store/dept.ts";
import { computed, onMounted } from 'vue';
import { useEventStore } from "~/store/event.ts";

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

const eventStore = useEventStore();

//
onMounted(() => {
  fetchDepartments();
});

//
const fetchDepartments = async () => {
  await deptStore.fetchDepts();
};

//
const onClick = (item) => {
  deptStore.setSelectedItemId(item && item.id ? item.id : "");

  if (item && item.id) {
    eventStore.emit('deptSelected', { dept_cd: item.name });
  } else {
    eventStore.emit('deptSelected', { dept_cd: '' });
  }

  emit("onClickMenuItem", item);
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
  emit("onClickOptionalButtons", type);
};

const onToggle = (value) => {
  deptStore.setToggleTargetOnClick(value);
};
</script>
