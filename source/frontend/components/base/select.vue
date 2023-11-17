<template>
  <div :class="toggle === false ? 'select' : 'select is-open'">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true" @click="controlToggle">
      <button class="select-selector-button" type="button" title="열기" v-if="!(props.isSearch && textInputMode)">
        <slot name="title">
          <span class="select-selector-title">{{ setTitle }}</span>
        </slot>
        <span class="hidden-text">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
      <div class="select-selector-input" v-if="props.isSearch && textInputMode" @click.stop="openAllToggle">
        <baseTextInput
          class="text-input"
          type="text"
          placeholder="검색어 입력"
          v-model="keyword"
          autofocus
        ></baseTextInput>
      </div>
      <baseButton
        class="select-selector-close-button"
        v-if="props.isSearch && textInputMode"
        @click.stop="controlToggle"
      >
        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
      </baseButton>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox">
        <li class="select-container-item" v-if="props.isCheck">
          <baseCheckbox
            class="checkbox-indeterminate"
            role="option"
            :id="'checkAll' + checkBoxId"
            :checked="isAllCheck"
            @change="checkAll"
            >전체</baseCheckbox
          >
        </li>
        <li class="select-container-item" v-if="props.isCheck" v-for="(item, index) in selectData">
          <baseCheckbox
            role="option"
            :id="checkBoxId + index"
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
import type { Select } from "~/components/base/select";
import { ref, computed, defineProps, defineEmits, onMounted } from "vue";
const SELECT = "선택";
const MULTI_SELECT = "다중 선택";
const ALL = "전체";

const props = defineProps({
  /**
   * select 리스트 데이터 값
   */
  data: {
    type: Array as () => Array<Select>,
    default: [
      {
        key: "선택1",
        value: 10
      },
      {
        key: "선택2",
        value: 20
      },
      {
        key: "선택3",
        value: 50
      }
    ]
  },
  /**
   * data의 key 값
   */
  dataKey: {
    type: String,
    default: "key"
  },
  /**
   * data value 값
   */
  dataValue: {
    type: String,
    default: "value"
  },
  /**
   * select 에 지정된 값
   * (selectedValue)
   */
  defaultValue: {
    default: null
  },
  /**
   * checkbox select
   */
  isCheck: {
    type: Boolean,
    default: false
  },
  /**
   * 검색용 select
   */
  isSearch: {
    type: Boolean,
    default: false
  },
  /**
   * 체크박스일 때 타이틀을 지정해줘야 하는 경우
   */
  defaultTitle: {
    type: String,
    default: SELECT
  }
});
const emit = defineEmits<{
  select: [data: any];
  getName: [data: any];
}>();

const checkBoxId = ref(Math.random().toString());
const selectData = ref<Select[]>([]);
const toggle = ref(false);
const select = ref(null);
const checkList: any = ref([]);
const nameList: any = ref([]);
const isAllCheck = ref(false);
const keyword = ref<string | null>(null);
const textInputMode = ref(false);

/**
 * 토글  open - close
 */
function controlToggle() {
  toggle.value = !toggle.value;
  if (props.isSearch) {
    controlTextInput();
  }
}

/**
 * 검색창 open - close
 */
function controlTextInput() {
  textInputMode.value = !textInputMode.value;
}
function openAllToggle() {
  toggle.value = true;
  textInputMode.value = true;
}

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
  if (select.value == null) {
    return SELECT;
  }
  return select.value;
});

/**
 * 체크 셀렉트 박스일 경우 선택 값
 */
const setCheckTitle = computed(() => {
  const length = checkList.value.length;
  if (isAllCheck.value) {
    return ALL;
  } else {
    switch (length) {
      case 0:
        return props.defaultTitle;
      case 1:
        let item: Select | undefined = findDataValue(checkList.value[0]);
        return item != undefined ? item[props.dataKey] : null;
      default:
        return MULTI_SELECT + `(${length})`;
    }
  }
});

/**
 * 검색 있는 셀렉트 박스일 경우
 * keyword 감지해서 셀렉트박스 데이터 변경
 */
watch(
  () => keyword.value,
  (newVal: string | null, oldVal: string | null) => {
    if (newVal != oldVal) {
      selectData.value = props.data.filter((item) => item[props.dataKey].includes(keyword.value));
    }
  }
);

/**
 * 검색 있는 셀렉트 박스일 경우 || 그 외 셀렉트 박스일 경우
 * @param item
 */
function clickItem(item: Select) {
  if (props.isSearch) {
    keyword.value = item[props.dataKey];
  } else {
    select.value = item[props.dataKey];
  }

  if (toggle.value) {
    controlToggle();
  }
  if (props.isSearch) {
    textInputMode.value = false;
  }
  emit("select", item[props.dataValue]);
}

/**
 * 체크 셀렉트 박스 전체 선택 기능
 * @param checked
 */
function checkAll(checked: boolean) {
  isAllCheck.value = checked;
  checkList.value = checked ? props.data.map((item) => item[props.dataValue]) : [];
  nameList.value = checked ? props.data.map((item) => item[props.dataKey]) : [];
  emit("select", checkList.value);
  emit("getName", nameList.value);
}

/**
 * 체크 셀렉트 박스 선택 기능
 * @param checked
 * @param item
 * @param index
 */
function checkData(checked: boolean, item: Select, index: number) {
  if (checked) {
    checkList.value.push(item[props.dataValue]);
    nameList.value.push(item[props.dataValue]);
  } else {
    let indexToRemove = checkList.value.indexOf(item[props.dataValue]);
    if (indexToRemove !== -1) {
      checkList.value.splice(indexToRemove, 1);
    }
    let indexToRemoveNames = nameList.value.indexOf(item[props.dataKey]);
    if (indexToRemoveNames !== -1) {
      nameList.value.splice(indexToRemoveNames, 1);
    }
  }
  isAllCheck.value = checkList.value.length === props.data?.length;
  emit("select", checkList.value);
  emit("getName", nameList.value);
}

const findDataValue = (data: any): Select | undefined => {
  return props.data.find((item) => item[props.dataValue] === data);
};

onMounted(() => {
  if (props.data != null) {
    selectData.value = props.data;
  }
  /**
   * 일반 셀렉트 박스 일 경우 title 및 emit 처리
   */
  if (!props.isSearch && !props.isCheck) {
    let defaultItem: Select | undefined =
      props.defaultValue == null ? props.data[0] : findDataValue(props.defaultValue);
    if (defaultItem !== undefined) {
      clickItem(defaultItem);
    }
  }
  /**
   * 검색 셀렉트 박스일 경우 디폴트 값이 있을때
   */
  if (props.isSearch && props.defaultValue) {
    let defaultItem: Select | undefined = findDataValue(props.defaultValue);
    if (defaultItem !== undefined) {
      clickItem(defaultItem);
    }
  }
  //TODO: 검색 체크박스 일 경우 디폴트 값 있을 때 기능 개발
});
</script>
