<template>
  <div class="data-detail">
    <div class="data-detail-group">
      <div class="search-input search-input-lg w-96">
        <label class="hidden-text" for="text-input-example-4">label</label>
        <input
          id="text-input-example-4"
          class="text-input"
          placeholder="이름으로 검색하세요."
          v-model="keyword"
        />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
          @click="clearInput"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="profiling">
        <agGrid
          class="ag-theme-alpine ag-theme-quartz"
          :columnDefs="COLUMN_DEFS"
          :rowData="profileList"
          rowId="id"
          :column-width-list="[100, 100, 100, 100, 100, 100]"
          :setColumnFit="true"
          :useColumnResize="true"
          :quickFilterText="keyword"
          :column-render="{
            nullCount: {
              type: 'html',
              fn: (val: any) => {
                return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
              },
            },
            uniqueCount: {
              type: 'html',
              fn: (val: any) => {
                return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
              },
            },
            distinctCount: {
              type: 'html',
              fn: (val: any) => {
                return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
              },
            },
          }"
        >
        </agGrid>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";
import type { ColDef } from "ag-grid-community";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const dataModelDetailStore = useDataModelDetailStore();

const { profileList } = storeToRefs(dataModelDetailStore);

const keyword = ref("");
const clearInput = (): void => {
  keyword.value = "";
};

const COLUMN_DEFS: ColDef[] = [
  { headerName: "NAME", field: "name", sortable: true },
  { headerName: "DATA TYPE", field: "dateTypeDisplay", sortable: true },
  { headerName: "NULL %", field: "nullCount", sortable: true },
  { headerName: "UNIQUE %", field: "uniqueCount", sortable: true },
  { headerName: "DISTINCT %", field: "distinctCount", sortable: true },
  { headerName: "VALUE COUNT", field: "valueCount", sortable: true },
];

// const rowData = ref([
//   {
//     name: "요일번호",
//     dateTypeDisplay: "integer",
//     nullCount: 30,
//     uniqueCount: 35000,
//     distinctCount: 100,
//     valueCount: 100,
//   },
//   {
//     name: "연월",
//     dateTypeDisplay: "integer",
//     nullCount: 20,
//     uniqueCount: 35,
//     distinctCount: 66,
//     valueCount: 100,
//   },
//   {
//     name: "역번호",
//     dateTypeDisplay: "integer",
//     nullCount: 80,
//     uniqueCount: 87,
//     distinctCount: 45,
//     valueCount: 100,
//   },
//   {
//     name: "역명",
//     dateTypeDisplay: "integer",
//     nullCount: 100,
//     uniqueCount: 23,
//     distinctCount: 9,
//     valueCount: 100,
//   },
//   {
//     name: "구분",
//     dateTypeDisplay: "integer",
//     nullCount: 50,
//     uniqueCount: 76,
//     distinctCount: 45,
//     valueCount: 100,
//   },
//   {
//     name: "선불카드_어른",
//     dateTypeDisplay: "real",
//     nullCount: 60,
//     uniqueCount: 33,
//     distinctCount: 12,
//     valueCount: 54,
//   },
//   {
//     name: "선불카드_청소년",
//     dateTypeDisplay: "real",
//     nullCount: 37,
//     uniqueCount: 78,
//     distinctCount: 100,
//     valueCount: 100,
//   },
//   {
//     name: "요일번호",
//     dateTypeDisplay: "integer",
//     nullCount: 30,
//     uniqueCount: 35000,
//     distinctCount: 100,
//     valueCount: 100,
//   },
//   {
//     name: "연월",
//     dateTypeDisplay: "integer",
//     nullCount: 20,
//     uniqueCount: 35,
//     distinctCount: 66,
//     valueCount: 100,
//   },
//   {
//     name: "역번호",
//     dateTypeDisplay: "integer",
//     nullCount: 80,
//     uniqueCount: 87,
//     distinctCount: 45,
//     valueCount: 100,
//   },
//   {
//     name: "역명",
//     dateTypeDisplay: "integer",
//     nullCount: 100,
//     uniqueCount: 23,
//     distinctCount: 9,
//     valueCount: 100,
//   },
//   {
//     name: "구분",
//     dateTypeDisplay: "integer",
//     nullCount: 50,
//     uniqueCount: 76,
//     distinctCount: 45,
//     valueCount: 100,
//   },
//   {
//     name: "선불카드_어른",
//     dateTypeDisplay: "real",
//     nullCount: 60,
//     uniqueCount: 33,
//     distinctCount: 12,
//     valueCount: 54,
//   },
//   {
//     name: "선불카드_청소년",
//     dateTypeDisplay: "real",
//     nullCount: 37,
//     uniqueCount: 78,
//     distinctCount: 100,
//     valueCount: 100,
//   },
// ]);
</script>
<style lang="scss" scoped></style>
