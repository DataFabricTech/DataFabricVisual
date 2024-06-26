<template>
  <div>
    <h1>Ag-Grid</h1>
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :style="`width: 1500px; height: 1000px`"
      :columnDefs="columnDefs"
      :rowData="rowData"
      rowId="id"
      :useRowCheckBox="true"
      :selectedNodes="selectedNodes"
      :column-width-list="[50, 100, 'auto', 100, 100, 100, 100]"
      :buttons="[
        {
          rendererKey: 'bookmark',
          headerText: '북마크',
          type: 'icon',
          icon: 'bookmark',
          useOnOffOpt: true,
          selectedData: selectedData,
          order: 2,
          fn: bookmarkClick
        },
        {
          rendererKey: 'edit',
          headerText: '수정',
          type: 'icon',
          icon: 'edit',
          order: 4,
          fn: editClick
        },
        {
          rendererKey: 'delete',
          headerText: '삭제',
          type: 'button',
          icon: 'delete',
          order: 5,
          fn: deleteClick
        }
      ]"
      @cell-clicked="cellClicked"
      @row-clicked="rowClicked"
      @selection-changed="selectionChanged"
    >
    </agGrid>
  </div>
</template>

<script setup lang="ts">
import agGrid from "@/components/extends/grid/Grid.vue";

const columnDefs = [
  { headerName: "ID", field: "id" },
  { headerName: "Make", field: "make" },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price" }
];

const rowData = [
  { id: 1, make: "Toyota", model: "Celica", price: 35000 },
  { id: 2, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 3, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 4, make: "Toyota", model: "Celica", price: 35000 },
  { id: 5, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 6, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 7, make: "Toyota", model: "Celica", price: 35000 },
  { id: 8, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 9, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 10, make: "Toyota", model: "Celica", price: 35000 },
  { id: 11, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 12, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 13, make: "Toyota", model: "Celica", price: 35000 },
  { id: 14, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 15, make: "Porsche", model: "Boxter", price: 72000 },
  { id: 16, make: "Toyota", model: "Celica", price: 35000 },
  { id: 17, make: "Ford", model: "Mondeo", price: 32000 },
  { id: 18, make: "Porsche", model: "Boxter", price: 72000 }
];

const selectedNodes = [10, 11, 12];

const selectedData = ref({});
selectedData.value.bookmark = [1, 4, 5];

// cell click 동작 -> buttons 항목 및 checkbox 항목은 click 이벤트가 동작하지 않음.
const cellClicked = (params: object) => {
  console.log(params);
};
// row click 동작
const rowClicked = (params: object) => {
  console.log(params);
};

// 노드 '북마크' 버튼 클릭시 - buttons : bookmarkClick
const bookmarkClick = ({ data }: { data: object }) => {
  if (selectedData.value.bookmark.includes(data.id)) {
    selectedData.value.bookmark = selectedData.value.bookmark.filter((item: any) => item !== data.id);
  } else {
    selectedData.value.bookmark.push(data.id);
  }
};

// 노드 '수정' 버튼 클릭시 - buttons : editClick
const editClick = (param: object) => {
  console.log(param);
};

// 노드 '삭제' 버튼 클릭시 - buttons : deleteClick
const deleteClick = (param: object) => {
  console.log(param);
};

const selectionChanged = () => {
  // console.log(params);
};
</script>

<style lang="scss" scoped></style>
