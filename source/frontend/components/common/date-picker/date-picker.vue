<template>
  <div>
    <Picker
      :value="dateValue"
      @update:value="emit('update:modelValue', $event)"
      :lang="langString"
      :type="type"
      :range="range"
      :format="dateFormat"
      value-type="format"
      :confirm="confirm"
      :confirm-text="confirmText"
      :disabled="disabled"
      :placeholder="placeholder"
    />
  </div>
</template>

<script lang="ts" setup>
import Picker from "vue-datepicker-next";
import "vue-datepicker-next/index.css"
import 'vue-datepicker-next/locale/ko'; // 로케일 설정
import { PropType, defineProps, computed } from "vue";

import dayjs from "dayjs";
import { PickerDateType, PickerType } from "../../../@types/global";
import _ from "lodash";

const DATE_FORMAT = {
  DATE: "YYYY-MM-DD",
  DATETIME: "YYYY-MM-DD HH:mm:ss",
  YEAR: "YYYY",
  MONTH: "YYYY-MM",
  TIME: "HH:mm:ss"
};
;
// note: 로케일 변경 시 로케일 파일 impoㄱt 필요
const langString = "ko";
const props = defineProps({
  modelValue: {
    type: [Array, String] as PropType<PickerDateType>
  },
  format: {
    type: String
  },
  type: {
    type: String as PropType<PickerType>,
    default: "date"
  },
  confirm: {
    type: Boolean,
    default: false
  },
  confirmText: {
    type: String,
    default: "OK"
  },
  disabled: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String
  }
});

//modelValue 변경감지를 위한 computed 사용
const range = computed(() => {
  return Array.isArray(props.modelValue);
});
const dateFormat = computed(() => {
  return props.format || DATE_FORMAT[props.type.toUpperCase()];
});
const dateValue = computed(() => {
  if (!props.modelValue || _.isEmpty(props.modelValue)) {
    return null;
  }
  return range.value
    ? [dayjs(props.modelValue[0] as string).format(dateFormat.value), dayjs(props.modelValue[1] as string).format(dateFormat.value)]
    : dayjs(props.modelValue as string).format(dateFormat.value);
});



const emit = defineEmits(["update:modelValue"]);
</script>
