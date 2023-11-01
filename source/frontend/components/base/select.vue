<template>
  <!-- 현재 위치에서 클릭시 is-open 클래스 추가 -->
  <div :class="toggle === false ? 'select' : 'select is-open'" @click="openToggle">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true">
      <button class="select-selector-button" type="button" title="열기">
        <slot name="title">
          <span class="select-selector-title">{{ setSelect }}</span>
        </slot>
        <span class="hidden">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
      <div class="select-selector-input" style="display: none">
        <baseTextInput class="text-input" type="text" placeholder="검색어 입력"></baseTextInput>
      </div>
      <baseButton class="select-selector-close-button" style="display: none">
        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
      </baseButton>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox" v-for="(item, index) in props.data">
        <li class="select-container-item" v-if="props.isCheck">
          <baseCheckbox class="checkbox-indeterminate" role="option">전체</baseCheckbox>
        </li>
        <li class="select-container-item" v-if="props.isCheck">
          <baseCheckbox role="option">item 01</baseCheckbox>
        </li>
        <li class="select-container-item" v-if="!props.isCheck">
          <button class="select-container-button" type="button" role="option" @click="clickItem(item)">
            <span class="select-container-text">{{ item[dataKey] }}</span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, defineEmits } from "vue";

interface data {
  [key: string]: any;
  value: any;
}
const props = defineProps({
  data: {
    type: Array,
    default: null,
  },
  dataKey: {
    type: String,
    default: "key",
  },
  dataValue: {
    type: String,
    default: "value",
  },
  title: {
    type: String,
    default: "선택"
  },
  isReset: {
    type: Boolean,
    default: false
  },
  isCheck: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['select']);
const toggle = ref(false);
const select = ref(null);
function openToggle() {
  toggle.value = !toggle.value
}
const setSelect = computed(() => {
  if(props.data !== null) {
    if (select.value !== null) {
      return select.value;
    } else {
      let item : data = props.data[0]
      clickItem(item);
      return item[props.dataKey];
    }
  } else {
    return props.title
  }
});
const clickItem = (item: data) => {
  select.value = item[props.dataKey];
  emit('select', item[props.dataValue]);
};
</script>
