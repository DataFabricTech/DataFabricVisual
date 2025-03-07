<template>
  <div>
    <Picker
      v-model="dateValue"
      :type="props.type"
      :range="props.range"
      :format="dateFormat"
      value-type="format"
      :confirm="props.confirm"
      :confirm-text="props.confirmText"
      :disabled="props.disabled"
      :placeholder="props.placeholder"
      :disabledDates="{ predicate: disabledDate }"
      :lang="props.lang"
      :minimumView="props.minimumView"
      @change="emit('change', $event)"
    />
  </div>
</template>

<script setup lang="ts">
import Picker from "vue3-datepicker";
import moment from "moment";
import { onMounted, computed, ComputedRef, ref, watch } from "vue";

const props = defineProps({
  date: String,
  startDate: String,
  endDate: String,
  format: String,
  // date |datetime|year|month|time|week
  minimumView: {
    type: String,
    default: "day",
  },
  type: {
    type: String,
    default: "date",
  },
  range: {
    type: Boolean,
    default: false,
  },
  confirm: {
    type: Boolean,
    default: true,
  },
  confirmText: {
    type: String,
    default: "OK",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
  },
  // 날짜 선택 제한 추가
  disabledMaxDate: {
    //이후날짜 선택제한
    type: String,
  },
  disabledMinDate: {
    //이전날짜 선택제한
    type: String,
  },
  changeFilterColumn: {
    //필터컬럼 변경 여부
    type: Boolean,
  },
  deleteDateColumn_s: {
    // 필터 초기화 여부
    type: Boolean,
  },
  lang: {
    type: Object,
  }
});

const emit = defineEmits(["update:date", "change"]);

interface DateFormat {
  DATE: string;
  DATETIME: string;
  YEAR: string;
  MONTH: string;
  MONTH_DATE: string;
  TIME: string;
}

const DATE_FORMAT = ref<DateFormat>({
  DATE: "YYYY-MM-DD",
  DATETIME: "YYYY-MM-DD HH:mm",
  YEAR: "YYYY",
  MONTH: "YYYY-MM",
  MONTH_DATE: "YYYY-MM-DD",
  TIME: "HH:mm:ss",
});

const dateFormat: ComputedRef<DateFormat> = computed(
  () => props.format || DATE_FORMAT.value[props.type.toUpperCase()]

);

const dateValue = computed({
  get() {
    return props.date ? new Date(props.date) : null
  },
  set(newVal: any) {
    emit('change', newVal == "Invalid date" ? null : moment(newVal).format(dateFormat.value))
    emit("update:date", newVal == "Invalid date" ? null : moment(newVal).format(dateFormat.value));
  },
});

onMounted(() => changeDateFormat());

watch(
  () => props.range,
  (currentVal, oldVal) => {
    changeDateFormat();
  }
);

const changeDateFormat = () => {
  if (props.range) {
    dateValue.value = [
      moment(props.startDate).format(dateFormat.value),
      moment(props.endDate).format(dateFormat.value),
    ];
  } else {
    dateValue.value = moment(props.date).format(dateFormat.value);
  }
};

// 날짜 선택 제한 추가
const disabledDate = (date: any) => {
  return props.disabledMaxDate
    ? date > moment(props.disabledMaxDate)
    : props.disabledMinDate
    ? date < moment(props.disabledMinDate)
    : false;
};

// 다국어 처리
// const locale = $i18n.locale;
// require(`vue3-datepicker/locale/${locale}`);
</script>

<style lang="scss">
@import "date-picker";

.v3dp__input_wrapper input {
  width:200px;
  height:36px;
  line-height:36px;
  padding:0 15px;
  border:1px solid #d6dce2;
  border-radius:8px;
  background:#fff;
}

</style>
