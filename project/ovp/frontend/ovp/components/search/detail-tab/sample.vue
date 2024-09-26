<template>
  <div class="no-result" v-if="sampleList.length === 0">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 정보가 없습니다.</p>
    </div>
  </div>
  <div class="data-detail" v-else>
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
