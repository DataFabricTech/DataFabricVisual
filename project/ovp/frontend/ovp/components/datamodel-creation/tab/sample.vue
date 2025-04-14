<template>
  <div class="data-summary" v-if="isItemClicked && sampleList.length > 0">
    <span class="data-summary-title">데이터 모델 요약</span>
    <span class="data-summary-name">{{
      selectedDataModel.displayName ?? selectedDataModel.modelNm
    }}</span>
    <span class="data-summary-text"
      >({{
        selectedDataModel.owner?.displayName ??
        selectedDataModel.owner?.name ??
        "-"
      }})</span
    >
  </div>
  <div
    class="data-detail"
    v-if="sampleList.length > 0 && !exceptExtSampleData"
    style="height: calc(100% - 28px)"
  >
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
const { isItemClicked, selectedDataModel } = storeToRefs(creationStore);

const dataModelDetailStore = useDataModelDetailStore();
const { sampleColumns, sampleList, exceptExtSampleData } =
  storeToRefs(dataModelDetailStore);

watch(isItemClicked, (newValue) => {
  console.log(newValue);
});

watch(sampleList, (newValue) => {
  // console.log(sampleColumns);
  console.log(newValue);
});
</script>

<style scoped></style>
