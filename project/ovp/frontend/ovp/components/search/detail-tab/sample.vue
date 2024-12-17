<template>
  <div class="data-detail" v-if="sampleList.length > 0 && !exceptExtSampleData">
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :columnDefs="sampleColumns"
      :rowData="sampleList"
      rowId="id"
      :useRowCheckBox="false"
      :setColumnFit="true"
      :useColumnResize="true"
    />
  </div>
  <div class="data-detail" v-else-if="exceptExtSampleData">
    <div class="unstructured-sample">
      <pre>{{ exceptExtSampleData }}</pre>
    </div>
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

const { sampleColumns, sampleList, exceptExtSampleData } =
  storeToRefs(dataModelDetailStore);
</script>

<style lang="scss" scoped></style>
