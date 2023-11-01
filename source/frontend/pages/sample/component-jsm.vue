<template>
  <div class="l-common">
    <tab-bar></tab-bar>
    <alert></alert>

    <br>
    <span> 단일 날짜 선택</span><date-picker v-model:modelValue="date"></date-picker>
    <br>
    <span> 기간 날짜 선택</span><date-picker v-model:modelValue="dateRange"></date-picker>
    <br>
    <highcharts style="width: 500px; height: 200px" :options="chartOptions"> </highcharts>

    <ag-grid-vue style="width: 550px; height: 300px" class="ag-theme-alpine" :gridOptions="gridOptions"
                 :columnDefs="columnDefs" :rowData="rowData">
    </ag-grid-vue>
    <p>composables test : {{ sampleData }}</p>
  </div>
</template>

<script lang="ts" setup>
import { sample } from "/composables/sample";
import TabBar from "/components/project/functional/tab-bar/tab-bar.vue";
import Alert from "/components/common/alert.vue";
import DatePicker from "/components/common/date-picker/date-picker.vue";

import { AgGridVue } from "ag-grid-vue3";

definePageMeta({
  layout: "default-layout"
});

const sampleData = sample();

const date = ref("2023-11-22");
const dateRange = ref(["2023-11-11", "2023-11-22"]);

const chartOptions = {
  series: [
    {
      data: [1, 2, 3] // sample data
    }
  ]
};

const gridOptions = {
  headerHeight: 56,
  rowHeight: 56,
  // sort 설정 시 추가할 것
  unSortIcon: true
};

// sort 예시 추가
const columnDefs = [
  { headerName: "Make", field: "make", sortable: true },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price", sortable: true }
];
const rowData = [
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxster", price: 72000 }
];
</script>
