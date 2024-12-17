<template>
  <div class="data-detail" v-if="schemaList.length > 0">
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
  <!-- 결과 없을 시 no-result 표시 -->
  <div class="no-result h-auto" v-else>
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 정보가 없습니다.</p>
    </div>
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
