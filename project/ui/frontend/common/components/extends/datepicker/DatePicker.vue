<template>
  <div>
    <ClientOnly>
      <DatePicker
        :clearable="clearable"
        :confirm="confirm"
        :confirm-text="confirmText"
        :disabled="disabled"
        :disabled-date="disabledDate"
        :disabled-time="disabledTime"
        :editable="editable"
        :format="format"
        :formatter="formatter"
        :hour-options="hourOptions"
        :lang="lang"
        :minute-options="minuteOptions"
        :multiple="multiple"
        :placeholder="placeholder"
        :range="range"
        :second-options="secondOptions"
        :separator="separator"
        :show-time-panel="dateTimePanel"
        :title-format="titleFormat"
        :type="type"
        :value="props.modelValue"
        :value-type="valueType"
        class="datepicker"
        @update:value="emit('update:modelValue', $event)"
      >
        <template v-if="isPanelVisible()" #footer>
          <button class="mx-btn mx-btn-text" @click="switchDateTimePanel">
            {{ dateTimePanel ? "select date" : "select time" }}
          </button>
        </template>
      </DatePicker>
    </ClientOnly>
  </div>
</template>

<script lang="ts" setup>
import DatePicker from "vue-datepicker-next";
import "vue-datepicker-next/index.css";
import { DatepickerComposition } from "./DatepickerComposition";
import { DatePickerPropsWithModelValue } from "./DatepickerProps";

const props = withDefaults(defineProps<DatePickerPropsWithModelValue>(), {
  modelValue: "",
  type: "date",
  range: false,
  format: "YYYY-MM-DD",
  valueType: "format",
  lang: "ko",
  placeholder: null,
  editable: true,
  clearable: true,
  confirm: false,
  confirmText: "확인",
  multiple: false,
  disabled: false,
  disabledDateRange: null,
  disabledTimeRange: null,
  titleFormat: "YYYY-MM-DD",
  separator: " ~ ",
  showTimePanel: false
});
const emit = defineEmits(["update:modelValue"]);

const {
  modelValue,
  type,
  range,
  format,
  formatter,
  valueType,
  lang,
  placeholder,
  editable,
  clearable,
  confirm,
  confirmText,
  multiple,
  disabled,
  disabledDateRange,
  disabledTimeRange,
  titleFormat,
  separator,
  hourOptions,
  minuteOptions,
  secondOptions,
  showTimePanel,
  dateTimePanel,
  disabledDate,
  disabledTime,
  isPanelVisible,
  switchDateTimePanel
} = DatepickerComposition(props);
</script>
