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
    @grid-size-changed="onGridSizeChanged"
  ></ag-grid-vue>
</template>

<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";

import { GridProps } from "@/components/extends/grid/GridProps";
import { GridComposition } from "./GridComposition";

const BTN_FIELD_CONST = "AG_GRID_";

const props = withDefaults(defineProps<GridProps>(), {
  class: "",
  style: "",
  rowId: "id",
  setColumnFit: false,
  useRowCheckBox: false,
  useColumnResize: false,
  buttons: () => [],
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
  if (column.colDef.field.includes(BTN_FIELD_CONST)) {
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

const { onGridReady, getDefs, onGridSizeChanged, setColumnToFit } = GridComposition(props, BTN_FIELD_CONST);

const gridColumnDefs: Ref<any[]> = ref([]);
gridColumnDefs.value = getDefs();

// Grid 를 가져다 쓰는 페이지에서 columnDefs 를 변경했을경우, 변경됨을 catch 해서 agGrid 에 반영해준다.
watch(
  () => props.columnDefs,
  () => {
    gridColumnDefs.value = getDefs();

    nextTick(() => {
      setColumnToFit();
    });
  },
  { deep: true }
);
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
