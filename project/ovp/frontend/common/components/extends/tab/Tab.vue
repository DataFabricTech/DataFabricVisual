<template>
  <div class="tab">
    <ul class="tab-list">
      <li
        class="tab-item"
        v-for="(item, index) in props.data"
        @click="move(index)"
        :class="{
          'is-tab-item-disabled': isDisabled(item[props.valueKey]),
          'is-tab-item-selected': changeCurrentTabClass(index)
        }"
      >
        <button class="tab-button">
          <p class="tab-button-text">{{ item[props.labelKey] }}</p>
        </button>
      </li>
    </ul>
    <div class="tab-contents" v-if="props.useTabContents">
      <slot :name="props.data[currentIndex][props.valueKey]"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { TabProps } from "~/components/extends/tab/TabProps";
import { TabComposition } from "./TabComposition";

const INDEX = "index";

const props = withDefaults(defineProps<TabProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  currentItem: 0,
  currentItemType: INDEX,
  useTabContents: true,
  disabledList: () => [],
  tabSize: 16
});

const emit = defineEmits<{ (e: "change", item: number | string): void }>();
const onChange = (value: string | number): void => {
  emit("change", value);
};
const { data, labelKey, valueKey, currentIndex, useTabContents, move, isDisabled, changeCurrentTabClass } =
  TabComposition(props, onChange);
</script>

<style lang="css"></style>
