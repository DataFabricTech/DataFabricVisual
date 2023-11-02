<template>
  <div :class="toggle === false ? 'select' : 'select is-open'" @click="openToggle">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true" @click="openTextInput">
      <button class="select-selector-button" type="button" title="열기" :style="props.isSearch && textInputMode? 'display: none' : ''">
        <slot name="title">
          <span class="select-selector-title">{{ setTitle }}</span>
        </slot>
        <span class="hidden-text">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
      <div class="select-selector-input" v-if="props.isSearch && textInputMode" @click.stop="doNothing">
        <baseTextInput class="text-input" type="text" placeholder="검색어 입력" v-model="keyword" autofocus></baseTextInput>
      </div>
      <baseButton class="select-selector-close-button" :style="props.isSearch && textInputMode? '' : 'display: none'" v-if="textInputMode" @click="controlAllToggle">
        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
      </baseButton>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox">
        <li class="select-container-item" v-if="props.isCheck">
          <baseCheckbox
            class="checkbox-indeterminate"
            role="option"
            @change="checkAll"
            :id="'checkAll'"
            :checked="isAllCheck"
          >전체</baseCheckbox>
        </li>
        <li class="select-container-item" v-if="props.isCheck" v-for="(item, index) in selectData">
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
        <li class="select-container-item" v-if="!props.isCheck" v-for="(item, index) in selectData">
          <button class="select-container-button" type="button" role="option" @click="clickItem(item)">
            <span class="select-container-text">{{ item[dataKey] }}</span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, defineEmits, onMounted } from "vue";
const SELECT = "선택"
const MULTI_SELECT = "다중 선택"
const ALL = "전체"

interface data {
  [key: string]: any;
  value: any;
}
const props = defineProps({
  data: {
    type: Array as () => Array<data>,
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
  defaultValue: {
    default: null
  },
  isReset: {
    type: Boolean,
    default: false
  },
  isCheck: {
    type: Boolean,
    default: false
  },
  isSearch: {
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

const selectData = ref<data[]>([]);
const toggle = ref(false);
const select = ref(null);
const checkList :any = ref([]);
const isAllCheck = ref(false);
const keyword = ref<string | null>(null);
const textInputMode = ref(false)

/**
 * 토글  open - close
 */
function openToggle() {
  toggle.value = !toggle.value
}

/**
 * 검색창 open - close
 */
function openTextInput() {
  textInputMode.value = !textInputMode.value
}
function controlAllToggle() {
  openToggle();
  openTextInput();
}
function doNothing() {}

/**
 * 셀렉트 박스 선택 값
 */
const setTitle = computed(() => {
    if (props.isCheck) {
      return setCheckTitle.value;
    }
    if (props.isSearch) {
      if (keyword.value !== null) {
        return keyword.value;
      }
      return SELECT;
    }
    return setSelectTitle.value;
});

/**
 * 일반 셀렉트 박스일 경우 선택 값
 */
const setSelectTitle = computed(() => {
  if (props.data === null) {
    return null;
  }
  if (!props.isCheck && select.value !== null) {
    return select.value;
  }

  /**
   * 디폴트 값 있을때 title 및 emit 처리
   */
  if(props.defaultValue != null) {
    let select = props.data.filter(item => item[props.dataValue] === props.defaultValue)[0];
    clickItem(select);
    return select[props.dataKey]
  }
  const defaultItem : data = props.data[0];
  clickItem(defaultItem);
  return defaultItem[props.dataKey];
});

/**
 * 체크 셀렉트 박스일 경우 선택 값
 */
const setCheckTitle = computed(() => {
  const length = checkList.value.length;
  if (length === props.data?.length) {
    return ALL;
  } else if (length >= 2 && length < props.data?.length) {
    return MULTI_SELECT + `(${length})`;
  } else if (length === 1) {
    for(let item of props.data) {
      if(item[props.dataValue] === checkList.value[0]) {
        return item[props.dataKey]
      }
    }
  } else if (length === 0) {
    return SELECT;
  }
})

/**
 * 검색 있는 셀렉트 박스일 경우
 * keyword 감지해서 셀렉트박스 데이터 변경
 */
watch(() => keyword.value,
  (newVal: string | null, oldVal: string | null) => {
    if (newVal != oldVal) {
      selectData.value = props.data.filter(item => item[props.dataKey].includes(keyword.value));
    }
  }
);

/**
 * 검색 있는 셀렉트 박스일 경우 || 그 외 셀렉트 박스일 경우
 * @param item
 */
const clickItem = (item: data) => {
  if (props.isSearch) {
    keyword.value = item[props.dataKey];
    textInputMode.value = false;
  } else {
    select.value = item[props.dataKey];
  }
  emit('select', item[props.dataValue]);
};

/**
 * 체크 셀렉트 박스 전체 선택 기능
 * @param checked
 */
function checkAll(checked: boolean) {
  isAllCheck.value = checked;
  checkList.value = checked ? props.data.map(item => item[props.dataValue]) : [];
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

/**
 * props.data = selectData
 */
onMounted(() => {
  if(props.data != null) {
    selectData.value = props.data
  }
  /**
   * 검색 셀렉트 박스일 경우 디폴트 값이 있을때
   */
  if(props.isSearch && props.defaultValue) {
    let select = props.data.filter(item => item[props.dataValue] === props.defaultValue)[0];
    keyword.value = select[props.dataKey]
    emit('select', select[props.dataValue]);
  }
})
</script>
