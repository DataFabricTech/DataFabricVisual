<template>
  <div class="data-detail" v-if="schemaList.length > 0" style="height: 500px">
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
  <div class="no-result h-auto" v-else-if="isItemClicked">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 정보가 없습니다.</p>
    </div>
  </div>
  <div class="no-result" v-else>
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import _ from "lodash";

import agGrid from "@extends/grid/Grid.vue";

import { useCreationStore } from "@/store/datamodel-creation";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const creationStore = useCreationStore();
const { isItemClicked } = storeToRefs(creationStore);

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

watch(schemaList, (newValue) => {
  console.log(newValue);
});
</script>

<style scoped></style>
