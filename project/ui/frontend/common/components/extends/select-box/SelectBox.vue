<template>
  <div class="select" style="width:200px;" v-on-click-outside="closeDropdown">
    <button class="select-button" @click="toggleList" :disabled="disabledAll">
      <span class="select-button-text">{{ selectedLabel }}</span>
      <svg-icon class="svg-icon select-control" name="chevron-down-medium"></svg-icon>
    </button>
    <ul class="select-list" v-show="isShowBox">
      <li class="select-item"
        v-for="(option, index) in data"
        :key="index"
        @click="selectItem(option)"
        :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { active: isActive(option[valueKey]) }]"
      >
        <button class="select-item-button">{{ option[labelKey] }}</button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { SelectBoxProps } from "@/components/extends/select-box/SelectBoxProps";
import { SelectBoxComposition } from "@/components/extends/select-box/SelectBoxComposition";

const props = withDefaults(defineProps<SelectBoxProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  selectedItem: undefined,
  disabledList: () => [],
  disabledAll: false,
  nodataMsg: "데이터가 없습니다.",
  isFirstSelectedEvent: true,
});

const emit = defineEmits<{ (e: "select", option: number | string): void }>();
function onSelect(value: string | number) {
  emit("select", value);
}

const {
  data,
  labelKey,
  valueKey,
  isShowBox,
  selectedLabel,
  toggleList,
  selectItem,
  isDisabled,
  isActive,
  closeDropdown
} = SelectBoxComposition(props, onSelect);
</script>

<style>
</style>
