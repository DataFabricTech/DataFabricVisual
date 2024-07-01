<template>
  <!-- TODO: [개발]select 오픈시 class="select is-select-open" 클래스 추가
  (open시에는 select-list가 보이게 스타일 처리 했습니다.인라인스타일은 생략해주세요.)-->
  <div class="select" v-on-click-outside="closeDropdown">
    <button class="select-button" @click="toggleList" :disabled="disabledAll">
      <svg-icon class="svg-icon select-button-icon" name="setting"></svg-icon>
      <span class="select-button-title">{{ selectedLabel }}</span>
      <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
    </button>
    <!-- TODO: [개발]
         1.select가 disabled상태일 경우 dropdown이 열리지 않아야 함
         2.선택시 class="dropdown-item dropdown-item-selected"클래스 추가
         3.컴포넌트명 select-box -> select로 변경
        -->
    <div class="dropdown" v-show="isShowBox">
      <ul class="dropdown-list">
        <li class="dropdown-item"
            v-for="(option, index) in data"
            :key="index"
            @click="selectItem(option)"
            :class="[{ 'disabled-option': isDisabled(option[valueKey]) }, { 'is-dropdown-item-selected': isActive(option[valueKey]) }]"
        >
          <button class="dropdown-button">
            <svg-icon class="svg-icon" name="setting"></svg-icon>
            <span class="dropdown-text">{{ option[labelKey] }}</span>
          </button>
          <button class="button button-neutral-ghost button-xs">
            <span class="hidden-text">닫기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </li>
        <li class="dropdown-item">
          <div class="checkbox">
            <input type="checkbox" id="checkbox-select" class="checkbox-input" />
            <label for="checkbox-select" class="checkbox-label">
              Checkbox <span class="checkbox-subtext">(Optional)</span>
            </label>
          </div>
        </li>
        <!-- TODO: [개발] 데이터가 없거나 에러가 날 경우 notificaiton으로 메세지 출력   -->
        <li class="dropdown-item">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
          </div>
        </li>
        <li class="dropdown-item dropdown-item-negative">
          <button class="dropdown-button">
            <svg-icon class="svg-icon" name="trash"></svg-icon>
            <span class="dropdown-text">삭제</span>
          </button>
        </li>
      </ul>

    </div>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { SelectBoxProps } from "./SelectBoxProps";
import { SelectBoxComposition } from "./SelectBoxComposition";
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
