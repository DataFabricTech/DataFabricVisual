<template>
  <tab-bar></tab-bar>
  <alert></alert>

  <br>
  <span> 단일 날짜 선택 : </span><date-picker v-model:modelValue="date"></date-picker>
  <br>
  <span> 기간 날짜 선택 : </span><date-picker v-model:modelValue="dateRange"></date-picker>
  <br>
  <highcharts style="width: 500px; height: 200px" :options="chartOptions"> </highcharts>

  <ag-grid-vue style="width: 500px; height: 200px" class="ag-theme-alpine" :columnDefs="columnDefs" :rowData="rowData">
  </ag-grid-vue>
  <p>composables test : {{ sampleData }}</p>
</template>

<script lang="ts" setup>
import { sample } from "../../composables/sample";
import TabBar from "../../components/project/tab-bar.vue";
import Alert from "../../components/common/alert.vue";
import DatePicker from "../../components/common/date-picker/date-picker.vue";

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { AgGridVue } from "ag-grid-vue3";

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

const columnDefs = [
  { headerName: "Make", field: "make" },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price" }
];
const rowData = [
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxster", price: 72000 }
];
</script>
