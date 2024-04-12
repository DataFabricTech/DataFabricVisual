<template>
  <div class="select" style="width:200px;" v-on-click-outside="closeDropdown">
    <button class="select-button" @click="toggleList" :disabled="disabledAll">
      <svg-icon class="svg-icon select-button-icon" name="setting"></svg-icon>
      <span class="select-button-title">{{ selectedLabel }}</span>
      <svg-icon class="svg-icon select-button-indicator" name="chevron-down-medium"></svg-icon>
    </button>
    <!-- TODO: [개발]
         1.select가 disabled상태일 경우 select-list가 열리지 않아야 함
         2.선택시 class="select-item select-item-selected"클래스 추가
        -->
    <ul class="select-list" v-show="isShowBox">
      <li class="select-item"
          v-for="(option, index) in data"
          :key="index"
          @click="selectItem(option)"
          :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { 'is-select-item-selected': isActive(option[valueKey]) }]"
      >
        <button class="select-item-button">
          <svg-icon class="svg-icon" name="setting"></svg-icon>
          <span class="select-item-button-text">{{ option[labelKey] }}</span>
        </button>
        <button class="button button-neutral-ghost button-xs">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </li>
      <li class="select-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-select" class="checkbox-input" />
          <label for="checkbox-select" class="checkbox-label">
            Checkbox <span class="checkbox-subtext">(Optional)</span></label
          >
        </div>
        <button class="button button-neutral-ghost button-xs">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </li>
      <!-- TODO: [개발] 데이터가 없거나 에러가 날 경우 notificaiton으로 메세지 출력   -->
      <li class="select-item">
        <div class="notification notification-sm notification-error">
          <svg-icon class="notification-icon" name="error"></svg-icon>
          <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
        </div>
      </li>
      <li class="select-item select-item-negative">
        <button class="select-item-button">
          <svg-icon class="svg-icon" name="trash"></svg-icon>
          <span class="select-item-button-text">삭제</span>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { SelectBoxProps } from "@/components/extends/select-box/SelectBoxProps";
import { SelectBoxComposition } from "@/components/extends/select-box/SelectBoxComposition";
import CheckBoxList from "@/components/extends/check-box-list/CheckBoxList.vue";

const props = withDefaults(defineProps<SelectBoxProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  selectedItem: undefined,
  disabledList: () => [],
  disabledAll: false,
  nodataMsg: "데이터가 없습니다.",
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
