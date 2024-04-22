<template>
  <h2>confirm, confirm-text, placeholder</h2>
  <DatePicker
    v-model="date"
    :confirm="true"
    :confirm-text="'확인'"
    :format="'YYYY-MM-DD'"
    :placeholder="'입력해주세용'"
    value-type="format"
  ></DatePicker>
  <h2>editable = false, clearable = false</h2>
  <p>editable 이 input 에서만 수정 안되는거였음...</p>
  <DatePicker
    v-model:modelValue="date2"
    :clearable="false"
    :editable="false"
    :format="'YY.MM.DD'"
    :type="'date'"
    value-type="YY-MM-DD"
  ></DatePicker>
  <h2>Disabled</h2>
  <DatePicker v-model:modelValue="date" :disabled="true" :format="'YY-MM-DD H:m:s'" :type="'date'"></DatePicker>

  <h2>Range</h2>
  <DatePicker v-model:modelValue="range1" :format="'YYYY-MM-DD'" :range="true"></DatePicker>
  <h2>separator, confirm</h2>
  <DatePicker
    v-model="range2"
    :confirm="true"
    :format="'YY.MM-DD'"
    :range="true"
    :separator="'~~~~~~~~~'"
    value-type="format"
  ></DatePicker>

  <h2>datetime</h2>
  <p>v-model에 null 넣으면 placeholder</p>
  <DatePicker
    v-model="nullData"
    :format="'YYYY-MM-DD HH:mm'"
    :placeholder="'선택해주세요.'"
    :type="'datetime'"
    value-type="YYYY-MM-DD HH:mm"
  ></DatePicker>
  <DatePicker
    v-model="dateTimeRange"
    :confirm="true"
    :format="'YYYY-MM-DD HH:mm:ss'"
    :range="true"
    type="datetime"
    value-type="YYYY-MM-DD HH:mm:ss"
  ></DatePicker>

  <h2>Year</h2>
  <DatePicker
    v-model="year"
    :confirm="true"
    :format="'YYYY'"
    :separator="'~~~~~~~~~'"
    type="year"
    value-type="YYYY"
  ></DatePicker>
  <DatePicker v-model="yearRange" :format="'YYYY'" :range="true" type="year" value-type="YYYY"></DatePicker>

  <h2>month</h2>
  <DatePicker v-model="month" :confirm="true" :format="'MM'" type="month" value-type="MM"></DatePicker>
  <DatePicker v-model="monthRange" :format="'MM'" :range="true" type="month" value-type="format"></DatePicker>

  <h2>time</h2>
  <DatePicker v-model="time" :confirm="true" :format="'HH:mm'" type="time" value-type="format"></DatePicker>
  <DatePicker v-model="timeRange" :format="'HH:mm'" :range="true" type="time" value-type="format"></DatePicker>

  <h2>Multiple</h2>
  <p>multiple 설정할 경우 separator 알맞게(",") 설정해줘야 함!</p>
  <DatePicker
    v-model="multiple"
    :format="'YYYY-MM-DD'"
    :multiple="true"
    :separator="','"
    type="date"
    value-type="format"
  ></DatePicker>

  <h2>Time options (Hour, Minute, Second Options)</h2>
  <DatePicker
    v-model="options"
    :format="'HH:mm:ss'"
    :hour-options="[8, 9, 10, 11, 12, 13]"
    :minute-options="[0, 30]"
    :placeholder="'HH:mm:ss'"
    :second-options="[0, 20, 40]"
    type="time"
    value-type="format"
  ></DatePicker>

  <h2>타입이 datetime 일때만 show-time-panel이 보인다. 아무리 true 줘봤자 안됌!</h2>
  <DatePicker
    v-model="date"
    :format="'YYYY-MM-DD'"
    :show-time-panel="true"
    type="date"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="dateTime"
    :format="'YYYY-MM-DD HH:mm:ss'"
    :show-time-panel="true"
    type="datetime"
    value-type="format"
  ></DatePicker>

  <h2>Disabled</h2>
  <p>datetime 일때 date, time 둘 다 disabled range 적용</p>
  <DatePicker
    v-model="nullData"
    :disabled-date-range="['2024-01-01', '2024-01-11']"
    :disabled-time-range="['09:30:00', '12:00:00']"
    :format="'YYYY-MM-DD HH:mm:ss'"
    :range="true"
    type="datetime"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="nullData"
    :disabled-date-range="['2024-01-01', '2024-01-11']"
    :disabled-time-range="['09:30:00', '12:00:00']"
    :format="'YYYY-MM-DD HH:mm:ss'"
    type="datetime"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="date"
    :disabled-date-range="['2024-01-01', null]"
    :format="'YYYY-MM-DD'"
    type="date"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="time"
    :disabled-time-range="[null, '09:30:00', null]"
    :format="'HH:mm:ss'"
    type="time"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="date"
    :disabled-date-range="['2024-01-01', '2024-01-11']"
    :format="'YYYY-MM-DD'"
    :range="true"
    type="date"
    value-type="format"
  ></DatePicker>
  <DatePicker
    v-model="time"
    :disabled-time-range="[null, '12:00:00']"
    :format="'HH:mm:ss'"
    :range="true"
    type="time"
    value-type="format"
  ></DatePicker>
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
</template>

<script lang="ts" setup>
import DatePicker from "@/components/extends/datepicker/DatePicker.vue";

const nullData = ref(null);
const date = ref("2024-01-26");
const date2 = ref("24.01.26");
const range1 = ref(["2023-12-26", "2024-01-11"]);
const range2 = ref(["24.01-01", "24.01-11"]);
const dateTime = ref("2024-01-29 16:31:30");
const dateTimeRange = ref(["2024-01-29 16:31:30", "2024-01-31 12:31:30"]);
const year = ref("2018");
const yearRange = ref(["2021", "2024"]);
const month = ref("09");
const monthRange = ref(["01", "11"]);
const time = ref("15:00");
const timeRange = ref(["01:13", "20:20"]);
const multiple = ref(["2024-01-02", "2024-01-11"]);
const options = ref("");
</script>

