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
import { computed } from "vue";
// NOTE: propType import 시 type으로 지정해야함
import type { PropType } from "vue";
import { DateFormat, PickerType } from "./date-picker";

const dayjs = useDayjs();

// note: 로케일 변경 시 로케일 파일 impoㄱt 필요
const langString = "ko";
const props = defineProps({
  modelValue: {
    type: [Array, String],
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
  enabledStartValue: {
    type: String,
    default: null
  },
  enabledEndValue: {
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
  if (!props.modelValue || _isEmpty(props.modelValue)) {
    return null;
  }
  return range.value
    ? [setDay(props.modelValue[0] as string, dateFormat.value), setDay(props.modelValue[1] as string, dateFormat.value)]
    : setDay(props.modelValue as string, dateFormat.value);
});

const emit = defineEmits(["update:modelValue"]);

function setDay(value: string | Date | null, format: DateFormat) {
  return dayjs(value).format(format);
}

/**
 * 날짜 비활성화
 * @param date
 */
function disabledDate(date: Date) {
  // NOTE : 문제점 datetime 일 경우 해당 날짜가 지정이 되지 않음.
  const newFormat = dateFormat.value === DateFormat.DATETIME ? DateFormat.DATE : dateFormat.value;
  const newDate = setDay(date, newFormat);
  const startValue = setDay(props.enabledStartValue, newFormat);
  const endValue = setDay(props.enabledEndValue, newFormat);

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
  // NOTE:
  const timeFormat = dateFormat.value === DateFormat.TIME ? DateFormat.TIME : DateFormat.DATETIME;
  const newTime = setDay(time, timeFormat);
  const startValue = setDay(props.enabledStartValue, timeFormat);
  const endValue = setDay(props.enabledEndValue, timeFormat);

  // NOTE: dayjs의 isValid로 유효성 검사 불가.
  const isStartValueValid = !_isNaN(Date.parse(props.enabledStartValue));
  const isEndValueValid = !_isNaN(Date.parse(props.enabledEndValue));

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
