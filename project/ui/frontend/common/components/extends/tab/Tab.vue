<template>
  <div class="flex-container">
    <div
      v-for="(item, index) in data"
      @click="move(index)"
      class="flex-tab"
      :class="{ 'disabled-tab': isDisabled(item[valueKey]) }"
    >
      <div>
        <span :class="{ 'current-tab': changeCurrentTabClass(index) }">
          {{ item[labelKey] }}
        </span>
      </div>
    </div>
  </div>
  <slot :name="data[currentIndex][valueKey]"></slot>
</template>

<script setup lang="ts">
import { TabProps } from "~/components/extends/tab/TabProps";
import { TabComposition } from "@/components/extends/tab/TabComposition";

const INDEX = "index";

const props = withDefaults(defineProps<TabProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  currentItem: 0,
  currentItemType: INDEX,
  disabledList: () => [],
  tabSize: 16
});

const emit = defineEmits<{ (e: "change", item: number | string): void }>();
const onChange = (value: string | number): void => {
  emit("change", value);
};
const { data, labelKey, valueKey, currentIndex, move, isDisabled, changeCurrentTabClass } = TabComposition(
  props,
  onChange
);
</script>

<style lang="css">
.flex-container {
  display: flex;
  /* Add your flex container styles here */
}

.flex-tab {
  cursor: pointer;
  padding: 30px;
  border: 1px solid #ccc;
  background-color: #f5f5f5;
  /* Add your tab styles here */
}

.current-tab {
  color: deepskyblue;
  border: 5px;
}

.disabled-tab {
  pointer-events: none;
  opacity: 0.5; /* 비활성화된 탭의 투명도를 조절할 수 있습니다. */
  /* 여기에 추가적인 스타일을 적용할 수 있습니다. */
}
</style>
