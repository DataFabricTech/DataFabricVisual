<template>
  <div class="select-box" style="width: 300px; height: 30px" v-on-click-outside="closeDropdown">
    <button @click="toggleList" style="width: 100%; height: 100%" :disabled="disabledAll">
      {{ selectedLabel }}
    </button>
    <ul v-show="isShowBox">
      <li
        v-for="(option, index) in data"
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
  nodataMsg: "데이터가 없습니다."
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
