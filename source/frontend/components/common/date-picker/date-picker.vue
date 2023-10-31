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
      :disabled-date="disabledDate"
      :disabled-time="disabledTime"
      :confirm="confirm"
      :confirm-text="confirmText"
      :disabled="disabled"
      :placeholder="placeholder"
    />
  </div>
</template>

<script lang="ts" setup>
import Picker from "vue-datepicker-next";
import "vue-datepicker-next/index.css";
import "vue-datepicker-next/locale/ko"; // 로케일 설정 파일
import { PropType, computed } from "vue";

import dayjs from "dayjs";
import { DateType, PickerType, DateFormat } from "./date-picker";
import _ from "lodash";

// note: 로케일 변경 시 로케일 파일 impoㄱt 필요
const langString = "ko";
const props = defineProps({
  modelValue: {
    type: [Array, String] as PropType<DateType>,
    default: ""
  },
  format: {
    type: String,
    default: null
  },
  type: {
    type: String as PropType<PickerType>,
    default: PickerType.DATE
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
    type: String,
    default: null
  },
  disableStartValue: {
    type: String,
    default: null
  },
  disableEndValue: {
    type: String,
    default: null
  }
});

//modelValue 변경감지를 위한 computed 사용
const range = computed(() => {
  return Array.isArray(props.modelValue);
});
const dateFormat = computed(() => {
  const formatKey: string = props.type.toUpperCase();
  return props.format || DateFormat[formatKey];
});
const dateValue = computed(() => {
  if (!props.modelValue || _.isEmpty(props.modelValue)) {
    return null;
  }
  return range.value
    ? [setDay(props.modelValue[0] as string), setDay(props.modelValue[1] as string)]
    : setDay(props.modelValue as string);
});

const emit = defineEmits(["update:modelValue"]);

function setDay(value: string | Date | null) {
  return dayjs(value).format(dateFormat.value);
}

/**
 * 날짜 비활성화
 * @param date
 */
function disabledDate(date: Date) {
  const newDate = setDay(date);
  const startValue = setDay(props.disableStartValue);
  const endValue = setDay(props.disableEndValue);

  const isStartValueValid = dayjs(startValue, dateFormat.value).isValid();
  const isEndValueValid = dayjs(endValue, dateFormat.value).isValid();

  if (isStartValueValid && isEndValueValid) {
    return newDate < startValue || newDate > endValue;
  } else if (isStartValueValid && !isEndValueValid) {
    return newDate < startValue;
  } else if (!isStartValueValid && isEndValueValid) {
    return newDate > endValue;
  }

  return false;
}

/**
 * 시간 비활성화
 * @param time
 */
function disabledTime(time: Date) {
  const timeFormat = DateFormat.TIME;

  const newTime = dayjs(time).format(timeFormat);
  const startValue = dayjs(props.disableStartValue).format(timeFormat);
  const endValue = dayjs(props.disableEndValue).format(timeFormat);

  // NOTE: dayjs의 isValid로 유효성 검사 불가.
  const isStartValueValid = !_.isNaN(Date.parse(props.disableStartValue));
  const isEndValueValid = !_.isNaN(Date.parse(props.disableEndValue));

  if (isStartValueValid && isEndValueValid) {
    return newTime < startValue || newTime > endValue;
  } else if (isStartValueValid && !isEndValueValid) {
    return newTime < startValue;
  } else if (!isStartValueValid && isEndValueValid) {
    return newTime > endValue;
  }

  return false;
}
</script>
