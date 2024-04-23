<template>
  <div>
    <ClientOnly>
      <DatePicker
        class="datepicker"
        style="width:300px"
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
        :value="modelValue"
        :value-type="valueType"
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
import "../../../assets/css/main.scss";
import { DatepickerComposition } from "./DatepickerComposition";
import { DatepickerProps } from "./DatepickerProps";

const dayjs = useDayjs();
const props = withDefaults(defineProps<DatepickerProps>(), {
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

<style lang="scss">
.mx-datepicker-main {
  font-family: 'pretandard', 'Segoe UI', 'Open Sans', 'Helvetica Neue';
}

.mx-datepicker-popup {
  box-shadow: 1px 6px 5px 0px rgba(162, 171, 181, 0.15);
}

.mx-btn-current-year,
.mx-btn-current-month,
.mx-calendar-header-label {
  font-weight: 600;
}

.mx-calendar-header-label {
  height: 28px;

  .mx-btn-text {
    color: #525866;

    &:hover {
      color: #525866;
      text-decoration: underline;
    }

    &:not(:first-child) {
      margin-left: 4px;
    }
  }
}


.mx-btn-text {
  padding: 0;
}

.mx-btn-icon {
  &-double-left,
  &-double-right,
  &-left,
  &-right {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 28px;
    width: 28px;
    margin-top: 3px;

    &:hover:not(:disabled) {
      background: #F4F6F9;
    }

    &:active:not(:disabled) {
      background: #BABFC8;
    }


    & > i {
      width: 100%;
      color: #525866;


    }
  }

}

.mx-icon-double-left:before, .mx-icon-left:before {
  left: 12px;
}

.mx-icon-right:before {
  left: 8px;
}

.mx-table-date {
  th {
    font-weight: 600;
    color: #525866;
  }

  .cell.not-current-month {
    color: #DBE0E8;

    &:hover {
      background: #fff;
    }

    &.active,
    &.in-range {
      background: #F4F6F9;
      color: #DBE0E8;
    }
  }
}

.mx-calendar-content .cell {
  color: #525866;

  &:hover {
    background: #F4F6F9;
  }

  &.active {
    background: #188AB0;
    color: #fff;
  }

  &.in-range {
    background: #E4F6FB;
    color: #115F79;
  }
}

.mx-datepicker-footer {
  border-top: 1px solid #DBE0E8;
}

.mx-datepicker-btn-confirm {
  border-color: #188AB0;
  color: #188AB0;

  &:hover {
    background: #E4F6FB;
    color: #188AB0;
    border-color: transparent;
  }

  &:active {
    border-color: #188AB0;
  }
}

.mx-btn-text {
  color: #525866;
  font-size: 14px;
  font-weight: 500;
  &:hover {
    text-decoration: underline;
    color: #525866;
  }
  &:active {
    color: #2B3440;
  }
}

.mx-time-column .mx-time-item {
  color: #525866;

  &:hover {
    background: #F4F6F9;
  }

  &.active {
    color: #188AB0;
    font-weight: 400;
  }
}

</style>