<!-- TODO: [개발]컴포넌트명 combo-box -> select-search로 변경-->
<template>
  <div class="select select-search" v-on-click-outside="closeDropdown" @click="toggleList" style="width:200px">
    <div class="search-group">
      <label class="hidden-text" for="select-search-ex"></label>
      <input
        id="select-search-ex"
        class="text-input"
        type="text"
        v-model="selectedLabel"
        :placeholder="placeholder"
        :disabled="disabled"
      />
      <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    </div>
    <div class="dropdown" v-show="isShowBox">
      <ul class="dropdown-list">
        <li class="dropdown-item" v-if="props.data.length === 0">
          <div class="notification notification-sm">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">{{ props.nodataMsg }}</p>
          </div>
        </li>
        <li class="dropdown-item" v-if="filteredOptions.length === 0 && props.data.length > 0">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">{{ props.noSearchMsg }}</p>
          </div>
        </li>
        <li class="dropdown-item"
            v-for="(option, index) in filteredOptions"
            :key="index"
            @click="selectItem(option)"
            :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { 'is-dropdown-item-selected': isActive(option[valueKey]) }]"
        >
          <button class="dropdown-button">
            <span class="dropdown-text">{{ option[labelKey] }}</span>
          </button>
        </li>
      </ul>
    </div>

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
