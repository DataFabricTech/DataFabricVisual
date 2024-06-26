<template>
  <ag-grid-vue
    :class="props.class"
    :style="props.style"
    :columnDefs="gridColumnDefs"
    :rowData="props.rowData"
    :rowId="props.rowId"
    :rowSelection="props.useRowCheckBox ? 'multiple' : ''"
    @cellClicked="cellClicked"
    @rowClicked="rowClicked"
    @grid-ready="onGridReady"
    @selectionChanged="selectionChanged"
  ></ag-grid-vue>

  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
    <svg-icon class="button-icon" name="close"></svg-icon>
  </button>
</template>

<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";

import { GridProps } from "@/components/extends/grid/GridProps";
import { GridComposition } from "@/components/extends/grid/GridComposition";

const props = withDefaults(defineProps<GridProps>(), {
  rowId: "id",
  selectedNodes: () => [],
  columnWidthList: () => []
});

const emit = defineEmits<{
  (e: "cellClicked", item: object): void;
  (e: "rowClicked", item: object): void;
  (e: "selectionChanged", item: any[]): void;
}>();

const cellClicked = ({
  rowIndex,
  data,
  column
}: {
  rowIndex: number;
  data: object;
  column: { colDef: { field: any } };
}) => {
  if (column.colDef.field.includes("AG_GRID_")) {
    // field 명에 AG_GRID_ 가 들어가있는 field 는 button 이나 icon 으로 click event 를 처리한 cell 이기 때문에
    // row event 를 실행하지 않는다.
    return;
  }
  emit("cellClicked", {
    rowIndex: rowIndex,
    data: data
  });
};
const rowClicked = ({ rowIndex, data }: { rowIndex: number; data: object }) => {
  emit("rowClicked", {
    rowIndex: rowIndex,
    data: data
  });
};
const selectionChanged = (params: { api: any }) => {
  emit("selectionChanged", params.api.getSelectedRows());
};

const { onGridReady, getDefs } = GridComposition(props);

const gridColumnDefs = ref<any[]>([]);
gridColumnDefs.value = getDefs();
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
