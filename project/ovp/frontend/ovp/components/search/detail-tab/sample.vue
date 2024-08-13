<template>
  <div class="data-detail">
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :columnDefs="sampleColumns"
      :rowData="sampleList"
      rowId="id"
      :useRowCheckBox="false"
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

const { sampleColumns, sampleList } = storeToRefs(dataModelDetailStore);
</script>

<style lang="scss" scoped></style>
