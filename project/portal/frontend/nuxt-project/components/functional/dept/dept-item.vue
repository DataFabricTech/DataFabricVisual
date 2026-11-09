<template>
  <ul v-if="props.list" class="sub-category-menu">
    <li v-for="(item, index) in props.list" :key="index" :class="getTagStyle('li', item)"
      style="min-width: 186px; width: max-content">
      <div>{{ item.checked }}</div>

      <button type="button" :class="getTagStyle('button', item)" @click="onToggle(item.id)">
        <span>펼치기</span>
      </button>
      <a href="javascript:void(0);" :class="getTagStyle('a', item)" style="overflow: visible; max-width: max-content"
        :title="item.name" :id="item.id" @click="() => onClick(item)">
        {{ item.name }}
      </a>

      <dept-item :list="item.subTypes" :toggle-targets="toggleTargets" :selected-id="selectedId"
        @onToggleNode="onToggle" @onClickMenuItem="onClick" @onClickSetup="onToggleTooltip" />
    </li>
  </ul>
</template>

<script setup lang="ts">
import { useDeptStore } from "@store/dept.ts";
import { useEventStore } from "~/store/event.ts";

const emit = defineEmits(["onToggleNode", "onClickMenuItem", "onToggleMetaDataTooltip"]);
const props = defineProps({
  list: {
    type: Array,
    default: () => []
  },
  toggleTargets: {
    type: Object,
    default: () => { }
  },
  selectedId: {
    type: String,
    default: () => ""
  },
  //
  onToggleNode: {
    type: Function
  },
  //
  onClickMenuItem: {
    type: Function
  },
  //
  onClickSetup: {
    type: Function
  }
});

const deptStore = useDeptStore();
const eventStore = useEventStore();
const getSelectedItemId = deptStore.getSelectedItemId;

//
const onToggle = (menuName) => {
  emit("onToggleNode", menuName);
};

//
const onClick = (item) => {
  deptStore.setSelectedItemId(item && item.id ? item.id : "");
  emit("onClickMenuItem", item);
};

//
const onToggleTooltip = (e, item, type) => {
  eventStore.emit("onToggleMetaDataTooltip", e, item, type);
};

//
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
        //
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
        //
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
