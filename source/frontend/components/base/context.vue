<template>
  <div class="context">
    <ul class="context-list">
      <li class="context-item" v-for="(item, key) in props.items" :key="key">
        <button class="context-button" type="button" :class="selectedClass(item)" @click="onClickItem(item)">
          <span class="context-text">{{ item.name }}</span>
          <svg-icon class="svg-icon" :name="item.icon"></svg-icon>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import type { ContextItem } from "~/components/base/context";
import { defineEmits } from "vue";

const props = defineProps({
  items: {
    type: Array<ContextItem>,
    default: () => {
      return [];
    }
  }
});

function selectedClass(item: ContextItem) {
  return item.selected ? "is-selected" : "";
}

const emit = defineEmits<{
  (e: "click", evt: ContextItem): void;
}>();
function onClickItem(item: ContextItem) {
  emit("click", item);
}
</script>
