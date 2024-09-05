<template>
  <div class="data-detail">
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :columnDefs="COLUMN_DEFS"
      :rowData="schemaList"
      rowId="id"
      :column-width-list="[30, 30, 70]"
      :setColumnFit="true"
      :useColumnResize="true"
    ></agGrid>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";
import type { ColDef } from "ag-grid-community";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const dataModelDetailStore = useDataModelDetailStore();

const { schemaList } = storeToRefs(dataModelDetailStore);
const COLUMN_DEFS: ColDef[] = [
  { headerName: "NAME", field: "name" },
  { headerName: "DATA TYPE", field: "dataTypeDisplay" },
  {
    headerName: "Description",
    field: "description",
    cellRenderer: (params: any) => {
      return params.value ? params.value : "No Description";
    },
  },
];
</script>

<style lang="scss" scoped></style>
