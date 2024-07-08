<template>
  <div class="data-detail">
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :style="`width: 100%; height: 500px`"
      :columnDefs="COLUMN_DEFS"
      :rowData="rowData"
      rowId="id"
      :column-width-list="[30, 30, 70]"
      :setColumnFit="true"
      :useColumnResize="true"
    ></agGrid>
  </div>
</template>

<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";
import type { ColDef } from "ag-grid-community";
const COLUMN_DEFS: ColDef[] = [
  { headerName: "NAME", field: "name" },
  { headerName: "DATA TYPE", field: "data_type" },
  {
    headerName: "Description",
    field: "description",
    cellRenderer: (params: any) => {
      return params.value ? params.value : "No Description";
    },
  },
];
interface schemaType {
  name: string;
  data_type: string;
  description?: string | null;
}
const rowData: schemaType[] = [
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
  { name: "Toyota", data_type: "integer" },
  { name: "Ford", data_type: "integer", description: "description..." },
  { name: "Porsche", data_type: "integer", description: null },
];
</script>

<style lang="scss" scoped></style>
