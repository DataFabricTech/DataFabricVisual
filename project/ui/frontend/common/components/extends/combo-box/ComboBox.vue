<!-- TODO: [게빌]컴포넌트명 combo-box -> select-search로 변경-->
<template>
  <div class="select select-search" v-on-click-outside="closeDropdown">
    <input
      class="text-input"
      type="text"
      @click="toggleList"
      v-model="selectedLabel"
      :placeholder="placeholder"
      :disabled="disabled"
    />
    <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    <ul class="select-list" v-show="isShowBox">
      <li class="select-item" v-if="props.data.length === 0">
        <div class="notification notification-sm">
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">{{ props.nodataMsg }}</p>
        </div>
      </li>
      <li class="select-item" v-if="filteredOptions.length === 0 && props.data.length > 0">
        <div class="notification notification-sm notification-error">
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">{{ props.noSearchMsg }}</p>
        </div>
      </li>
      <li class="select-item"
          v-for="(option, index) in filteredOptions"
          :key="index"
          @click="selectItem(option)"
          :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { 'is-select-item-selected': isActive(option[valueKey]) }]"
      >
        <button class="select-item-button">

          <span class="select-item-button-text">{{ option[labelKey] }}</span>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { ComboBoxProps } from "@/components/extends/combo-box/ComboBoxProps";
import { ComboboxComposition } from "@/components/extends/combo-box/ComboBoxComposition";

const props = withDefaults(defineProps<ComboBoxProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  selectedItem: undefined,
  disabledList: () => [],
  disabled: false,
  nodataMsg: "데이터가 없습니다.",
  noSearchMsg: "검색 결과가 없습니다.",
  placeholder: "검색",
  isFirstSelectedEvent: true
});

const emit = defineEmits<{ (e: "select", option: number | string): void }>();

function onSelect(value: string | number) {
  emit("select", value);
}

const {
  data,
  labelKey,
  valueKey,
  filteredOptions,
  isShowBox,
  selectedLabel,
  toggleList,
  selectItem,
  isDisabled,
  placeholder,
  disabled,
  isActive,
  closeDropdown
} = ComboboxComposition(props, onSelect);
</script>

<style>
.select-box {
  background-color: #9ca3af;
}

.disabled-option {
  pointer-events: none;
  opacity: 0.5;
}

.active {
  color: burlywood;
  font-weight: bold;
}
</style>
