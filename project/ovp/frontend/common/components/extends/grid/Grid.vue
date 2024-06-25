<template>
  <ag-grid-vue
    class="ag-theme-alpine"
    style="width: 1500px; height: 500px"
    :columnDefs="gridColumnDefs"
    :rowData="props.rowData"
    @columnHeaderClicked="columnHeaderClicked"
    @cellClicked="cellClicked"
    :frameworkComponents="frameworkComponents"
  ></ag-grid-vue>
</template>

<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";

import { GridProps } from "@/components/extends/grid/GridProps";
import { GridComposition } from "@/components/extends/grid/GridComposition";

import buttonRenderer from "./grid-units/Button.vue";

const clickRenderer = () => {
  console.log("btnClick");
};

const props = withDefaults(defineProps<GridProps>(), {});

let _gridColumnDefs: any[] = [];
if (Object.keys(props.buttons).length > 0) {
  _gridColumnDefs = Object.keys(props.buttons).map((e: string, i: number) => {
    const el: any = props.buttons[e];
    return {
      headerName: el.headerText,
      field: `actions_${i}`,
      cellRenderer: buttonRenderer,
      cellRendererParams: {
        buttonText: el.headerText,
        onClickRenderer: clickRenderer
      }
    };
  });
}
let gridColumnDefs = ref<any[]>([]);
gridColumnDefs.value = props.columnDefs.concat(_gridColumnDefs);

const emit = defineEmits<{
  (e: "cellClicked", item: object): void;
  (e: "columnHeaderClicked", item: object): void;
}>();

const columnHeaderClicked = ({ rowIndex, data }: { rowIndex: number; data: object }) => {
  emit("cellClicked", {
    rowIndex: rowIndex,
    data: data
  });
};
const cellClicked = ({ rowIndex, data }: { rowIndex: number; data: object }) => {
  emit("columnHeaderClicked", {
    rowIndex: rowIndex,
    data: data
  });
};

const {} = GridComposition(props);

const frameworkComponents = {
  cellRenderer: buttonRenderer
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
