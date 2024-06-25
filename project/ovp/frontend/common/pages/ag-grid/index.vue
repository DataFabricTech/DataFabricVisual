<template>
  <div>
    <h1>Ag-Grid</h1>

    <agGrid
      class="ag-theme-alpine"
      :columnDefs="columnDefs"
      :rowData="rowData"
      :buttons="{
        edit: {
          headerText: '수정',
          icon: 'edit',
          fn: btnClick
        },
        delete: {
          headerText: '삭제',
          icon: 'delete',
          fn: btnClick
        }
      }"
      @columnHeaderClicked="columnHeaderClicked"
      @cell-clicked="cellClicked"
    >
    </agGrid>
  </div>
</template>

<script setup lang="ts">
import agGrid from "@/components/extends/grid/Grid.vue";

const columnDefs = [
  { headerName: "Make", field: "make" },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price" },
  { headerName: "Test", field: "test" }
];

const rowData = [
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 },
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 },
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 },
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 },
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 },
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxter", price: 72000 }
];

const cellClicked = (params: object) => {
  console.log(params);
};
const columnHeaderClicked = (params: object) => {
  console.log(params);
};

const btnClick = () => {
  console.log("editClick");
};
</script>

<style lang="scss" scoped></style>
