<template>
  <div :class="toggle === false ? 'select' : 'select is-open'">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true" @click="controlToggle">
      <button class="select-selector-button" type="button" title="열기">
        <slot name="title">
          <span class="select-selector-title">{{ setTitle }}</span>
        </slot>
        <span class="hidden-text">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox" v-if="props.isCheck">
        <li class="select-container-item">
          <BaseCheckbox
            :class="allCheckClass"
            role="option"
            :id="checkBoxId + '_all'"
            :checked="isAllCheck"
            @change="checkAll"
          >전체
          </BaseCheckbox
          >
        </li>
        <li class="select-container-item" v-for="(item, index) in selectData" :key="index">
          <BaseCheckbox
            role="option"
            :id="checkBoxId + '_' + index"
            :name="item[dataName]"
            :value="item[dataValue]"
            :checked="selectedList.includes(item[dataValue])"
            :disabled="false"
            @change="(checked) => checkItem(item, checked)"
          ></BaseCheckbox>
        </li>
      </ul>
      <ul class="select-container-list" role="listbox" v-else>
        <li class="select-container-item" v-for="(item, index) in selectData" :key="index">
          <button class="select-container-button" type="button" role="option" @click="clickItem(item)">
            <span class="select-container-text">{{ item[dataName] }}</span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Select } from "~/components/base/select";
import { computed, defineEmits, defineProps, onMounted, ref } from "vue";

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
   * data의 표시 값
   */
  dataName: {
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
   * 체크박스일 때 타이틀을 지정해줘야 하는 경우
   */
  defaultTitle: {
    type: String,
    default: SELECT
  },
  modelValue: {
    type: Array,
    default: null
  },
  /**
   * checkbox select
   */
  isCheck: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits<{
  selected: [data: any];
  selectedName: [data: any];
  "update:modelValue": [data: any];
}>();

const checkBoxId = ref(Math.random().toString());
const selectData = ref<Select[]>([]);
const toggle = ref(false);
const selectedList = ref<string[]>([]);
const nameList: any = ref([]);

/**
 * 셀렉트 박스 선택 값
 */
const setTitle = computed(() => {
  if (props.isCheck) {
    // 체크 셀렉트 박스일 경우 선택 값
    if (isAllCheck.value) {
      return ALL;
    } else {
      const length = selectedList.value.length;
      switch (length) {
        case 0:
          return props.defaultTitle ? props.defaultTitle : SELECT;
        case 1:
          let item: Select | undefined = findDataValue(selectedList.value.at(0));
          return item ? item[props.dataName] : null;
        default:
          return MULTI_SELECT + `(${length})`;
      }
    }
  } else {
    // 일반 셀렉트 박스일 경우 선택 값
    if (selectedList.value && selectedList.value.length > 0) {
      let item: Select | undefined = findDataValue(selectedList.value.at(0));
      return item ? item[props.dataName] : null;
    } else {
      return props.defaultTitle ? props.defaultTitle : SELECT;
    }
  }
});

const isAllCheck = computed(() => {
  return selectedList.value.length === props.data?.length;
});

const allCheckClass = computed(() => {
  return selectedList.value.length === props.data?.length || selectedList.value.length === 0 ? "" : "checkbox-indeterminate";
});

/**
 * 검색 필터에서 초기화 버튼 클릭시 초기화 처리
 */
watch(
  () => props.modelValue,
  (newVal: any | null, oldVal: any | null) => {
    if (newVal != oldVal) {
      if (!newVal || (<Select[]>newVal).length === 0) {
        setSelect(null);
      }
    }
  }
);

/**
 * 토글  open - close
 */
function controlToggle() {
  toggle.value = !toggle.value;
}

/**
 * 체크 셀렉트 박스 전체 선택 기능
 * @param checked
 */
function checkAll(checked: boolean) {
  selectedList.value = checked ? props.data.map((item) => item[props.dataValue]) : [];
  nameList.value = checked ? props.data.map((item) => item[props.dataName]) : [];
  emitEvent();
  emitEvent("name");
}

/**
 * 검색 있는 셀렉트 박스일 경우 || 그 외 셀렉트 박스일 경우
 * @param item
 */
function clickItem(item: Select) {
  selectedList.value = [item[props.dataValue]];

  toggle.value = false;
  emitEvent();
}

/**
 * 체크 셀렉트 박스 선택 기능
 * @param checked
 * @param item
 */
function checkItem(item: Select, checked: boolean = true) {
  if (checked) {
    selectedList.value.push(item[props.dataValue]);
    nameList.value.push(item[props.dataName]);
  } else {
    let indexToRemove = selectedList.value.indexOf(item[props.dataValue]);
    if (indexToRemove > -1) {
      selectedList.value.splice(indexToRemove, 1);
    }
    let indexToRemoveNames = nameList.value.indexOf(item[props.dataName]);
    if (indexToRemoveNames > -1) {
      nameList.value.splice(indexToRemoveNames, 1);
    }
  }

  emitEvent();
  emitEvent("name");
}

function emitEvent(type: string | null = null) {
  if (props.modelValue === null) {
    if (props.isCheck) {
      if (type === "name") {
        emit("selectedName", nameList.value);
      } else {
        emit("selected", selectedList.value);
      }
    } else {
      emit("selected", selectedList.value.at(0));
    }
  } else {
    let value = [];
    for (let key of selectedList.value) {
      let item: Select | undefined = findDataValue(key);
      if (item) {
        value.push(item);
      }
    }
    emit("update:modelValue", value);
  }
}

function setSelect(value: string | null) {
  let selectValue = value ? value : props.defaultValue;
  let item: Select | undefined = selectValue ? findDataValue(selectValue) : props.data[0];
  if (!item) {
    return;
  }

  if (props.isCheck) {
    selectedList.value = [item[props.dataValue]];
    nameList.value = [item[props.dataName]];
    emitEvent();
    emitEvent("name");
  } else {
    selectedList.value = [item[props.dataValue]];
    emitEvent();
  }
}

function setDefault() {
  setSelect(props.defaultValue);
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
  setDefault();
});
</script>
