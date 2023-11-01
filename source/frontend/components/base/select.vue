<template>
  <div :class="toggle === false ? 'select' : 'select is-open'" @click="openToggle">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true">
      <button class="select-selector-button" type="button" title="열기">
        <slot name="title">
          <span class="select-selector-title">{{ props.isCheck ? setCheckTitle : setSelectTitle }}</span>
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
      <ul class="select-container-list" role="listbox">
        <li class="select-container-item" v-if="props.isCheck">
          <baseCheckbox class="checkbox-indeterminate" role="option" @change="checkAll" :id="'checkAll'" :checked="isAllCheck">전체</baseCheckbox>
        </li>
        <li class="select-container-item" v-if="props.isCheck" v-for="(item, index) in props.data">
          <baseCheckbox
            role="option"
            :id="index + props.checkBoxId.toString()"
            :name="item[dataKey]"
            :value="item[dataValue]"
            :checked="checkList.includes(item[dataValue])"
            :disabled="false"
            @change="(checked) => checkData(checked, item, index)"
          ></baseCheckbox>
        </li>
        <li class="select-container-item" v-if="!props.isCheck" v-for="(item, index) in props.data">
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
  },
  checkBoxId: {
    type: Number,
    default: Math.random()
  }
});
const emit = defineEmits<{
  select: [data: any]
}>()

const toggle = ref(false);
const select = ref(null);
const checkList :any = ref([]);
const isAllCheck = ref(false);
const checkMsg = ref("선택");
function openToggle() {
  toggle.value = !toggle.value
}

/**
 * 일반 셀렉트 박스일 경우 선택 값
 */
const setSelectTitle = computed(() => {
  if (props.data === null) {
    return props.title;
  }
  if (!props.isCheck && select.value !== null) {
    return select.value;
  }
  const defaultItem = props.data[0];
  clickItem(defaultItem);
  return defaultItem[props.dataKey];

});
/**
 * 체크 셀렉트 박스일 경우 선택 값
 */
const setCheckTitle = computed(() => {
  const length = checkList.value.length;
  if (length === props.data?.length) {
    return "전체";
  } else if (length >= 2 && length < props.data?.length) {
    return `다중 선택(${length})`;
  } else if (length === 1) {
    for(let item of props.data) {
      if(item[props.dataValue] === checkList.value[0]) {
        return item[props.dataKey]
      }
    }
  } else if (length === 0) {
    return "선택";
  }
})
/**
 * 일반 셀렉트 박스 선택 기능
 * @param item
 */
const clickItem = (item: data) => {
  select.value = item[props.dataKey];
  emit('select', item[props.dataValue]);
};

/**
 * 체크 셀렉트 박스 전체 선택 기능
 * @param checked
 */
function checkAll(checked: boolean) {
  isAllCheck.value = checked;
  checkList.value = checked ? props.data.map(item => item[props.dataValue]) : [];
  checkMsg.value = checked ? "전체" : "선택";
}

/**
 * 체크 셀렉트 박스 선택 기능
 * @param checked
 * @param item
 * @param index
 */
function checkData(checked: boolean, item: data, index: number) {
  if(checked) {
    checkList.value.push(item[props.dataValue])
  } else {
    let indexToRemove = checkList.value.indexOf(item[props.dataValue]);
    if (indexToRemove !== -1) {
      checkList.value.splice(indexToRemove, 1);
    }
  }
  isAllCheck.value = checkList.value.length == props.data?.length
  emit('select', checkList.value);
}
</script>
