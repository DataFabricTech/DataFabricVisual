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
      <!--  TODO: [퍼블리싱] ag-grid 개발 후 스타일 적용 예정 -->
      <div class="profiling">
        <agGrid
          class="ag-theme-alpine ag-theme-quartz"
          :style="`width: 100%; height: 500px`"
          :columnDefs="COLUMN_DEFS"
          :rowData="rowData"
          rowId="id"
          :column-width-list="[100, 100, 100, 100, 100, 100]"
          :setColumnFit="true"
          :useColumnResize="true"
          :quickFilterText="keyword"
          :column-render="{
            null: {
              type: 'html',
              fn: (val: any) => {
                return `<progress id='bar-gage-02' class='progressbar progressbar-major' value=${val} max='100'></progress>`;
              },
            },
            unique: {
              type: 'html',
              fn: (val: any) => {
                return `<progress id='bar-gage-02' class='progressbar progressbar-major' value=${val} max='100'></progress>`;
              },
            },
            distinct: {
              type: 'html',
              fn: (val: any) => {
                return `<progress id='bar-gage-02' class='progressbar progressbar-major' value=${val} max='100'></progress>`;
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
import agGrid from "@extends/grid/Grid.vue";
import type { ColDef } from "ag-grid-community";

const keyword = ref("");
const clearInput = (): void => {
  keyword.value = "";
};

const COLUMN_DEFS: ColDef[] = [
  { headerName: "NAME", field: "name", sortable: true },
  { headerName: "DATA TYPE", field: "data_type", sortable: true },
  { headerName: "NULL %", field: "null", sortable: true },
  { headerName: "UNIQUE %", field: "unique", sortable: true },
  { headerName: "DISTINCT %", field: "distinct", sortable: true },
  { headerName: "VALUE COUNT", field: "value_count", sortable: true },
];

const rowData = ref([
  {
    name: "요일번호",
    data_type: "integer",
    null: 30,
    unique: 35000,
    distinct: 100,
    value_count: 100,
  },
  {
    name: "연월",
    data_type: "integer",
    null: 20,
    unique: 35,
    distinct: 66,
    value_count: 100,
  },
  {
    name: "역번호",
    data_type: "integer",
    null: 80,
    unique: 87,
    distinct: 45,
    value_count: 100,
  },
  {
    name: "역명",
    data_type: "integer",
    null: 100,
    unique: 23,
    distinct: 9,
    value_count: 100,
  },
  {
    name: "구분",
    data_type: "integer",
    null: 50,
    unique: 76,
    distinct: 45,
    value_count: 100,
  },
  {
    name: "선불카드_어른",
    data_type: "real",
    null: 60,
    unique: 33,
    distinct: 12,
    value_count: 54,
  },
  {
    name: "선불카드_청소년",
    data_type: "real",
    null: 37,
    unique: 78,
    distinct: 100,
    value_count: 100,
  },
  {
    name: "요일번호",
    data_type: "integer",
    null: 30,
    unique: 35000,
    distinct: 100,
    value_count: 100,
  },
  {
    name: "연월",
    data_type: "integer",
    null: 20,
    unique: 35,
    distinct: 66,
    value_count: 100,
  },
  {
    name: "역번호",
    data_type: "integer",
    null: 80,
    unique: 87,
    distinct: 45,
    value_count: 100,
  },
  {
    name: "역명",
    data_type: "integer",
    null: 100,
    unique: 23,
    distinct: 9,
    value_count: 100,
  },
  {
    name: "구분",
    data_type: "integer",
    null: 50,
    unique: 76,
    distinct: 45,
    value_count: 100,
  },
  {
    name: "선불카드_어른",
    data_type: "real",
    null: 60,
    unique: 33,
    distinct: 12,
    value_count: 54,
  },
  {
    name: "선불카드_청소년",
    data_type: "real",
    null: 37,
    unique: 78,
    distinct: 100,
    value_count: 100,
  },
]);
</script>
<style lang="scss" scoped></style>
