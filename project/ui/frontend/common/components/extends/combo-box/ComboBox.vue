<template>
  <div class="combo-box" style="width: 300px; height: 30px" v-on-click-outside="closeDropdown">
    <input
      type="text"
      @click="toggleList"
      v-model="selectedLabel"
      :placeholder="placeholder"
      :disabled="disabled"
      style="width: 100%; height: 100%"
    />
    <ul v-show="isShowBox">
      <li v-if="props.data.length === 0">{{ props.nodataMsg }}</li>
      <li v-if="filteredOptions.length === 0 && props.data.length > 0">
        {{ props.noSearchMsg }}
      </li>
      <li
        v-for="(option, index) in filteredOptions"
        :key="index"
        @click="selectItem(option)"
        :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { active: isActive(option[valueKey]) }]"
      >
        {{ option[labelKey] }}
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
  placeholder: "입력",
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
