<template>
  <div class="data-detail">
    <div style="display: flex; flex-direction: row">
      <div style="overflow: hidden; flex-grow: 1">
        <agGrid
          class="ag-theme-alpine ag-theme-quartz"
          :style="`width: 100%; height: 500px`"
          :columnDefs="columnDefs"
          :rowData="rowData"
          rowId="id"
          :useRowCheckBox="false"
          :setColumnFit="true"
          :useColumnResize="true"
        ></agGrid>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";

const columnDefs = [
  { headerName: "ID", field: "id" },
  { headerName: "Make", field: "make" },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price" },
];

const rowData = [
  { id: 1, make: "Toyota", model: "Celica", price: 35000 },
  { id: 2, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 3, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 4, make: "Toyota", model: "Celica", price: 35000 },
  { id: 5, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 6, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 7, make: "Toyota", model: "Celica", price: 35000 },
  { id: 8, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 9, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 10, make: "Toyota", model: "Celica", price: 35000 },
  { id: 11, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 12, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 13, make: "Toyota", model: "Celica", price: 35000 },
  { id: 14, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 15, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 16, make: "Toyota", model: "Celica", price: 35000 },
  { id: 17, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 18, make: "Porsche", model: "Boxter", price: 72000 },
];
</script>

<style lang="scss" scoped></style>
