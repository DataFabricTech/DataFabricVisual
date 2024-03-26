<template>
  <client-only>
    <div v-for="(option, index) in checkboxList" :key="index">
      <input
        type="checkbox"
        :id="option.id"
        :value="option.value"
        @change="changeList"
        :disabled="option.disabled"
        v-model="option.checked"
      />
      <label :for="option.id"> {{ option[labelKey] }} </label>
    </div>
  </client-only>
</template>

<script setup lang="ts">
import { CheckBoxListProps } from "@/components/extends/check-box-list/CheckBoxListProps";
import { CheckBoxListComposition } from "@/components/extends/check-box-list/CheckBoxListComposition";

const props = withDefaults(defineProps<CheckBoxListProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  checkedList: () => [],
  disabledList: () => [],
  isFirstSelectedEvent: false,
  checkboxList: () => []
});

const emit = defineEmits<{ (e: "change", item: (string | number)[]): void }>();
function onChange(value: (string | number)[]) {
  emit("change", value);
}

const { data, labelKey, valueKey, checkboxList, changeList, isDisabled } = CheckBoxListComposition(props, onChange);
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
