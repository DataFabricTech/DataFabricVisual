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
      :selectedNodes="[10, 11, 12]"
      :column-width-list="[100, 100, 200, 100, 100, 100, 100]"
      :setColumnFit="true"
      :columnRender="{
        make: {
          type: 'valFunc',
          fn: (val: any) => {
            return `preFix - ${val}`;
          }
        },
        model: {
          type: 'html',
          fn: (val: any) => {
            return `<div style='display: ruby;'><img src='https://flags.fmcdn.net/data/flags/mini/de.png' > - ${val}</img></div>`;
          }
        },
        price: {
          type: 'html',
          fn: (val: any) => {
            // fa, svg 는 안먹음. 변환 된 걸로 넣어야 함.
            return `<div style='display: ruby;'>
                   <svg style='width:20px; height:20px;' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 512 512'>
                   <!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.-->
                   <path d='M62.4 53.9C56.8 37.1 38.6 28.1 21.9 33.6S-3.9 57.4 1.6 74.1L51.6 224H32c-17.7 0-32 14.3-32 32s14.3 32 32 32H72.9l56.7 170.1c4.5 13.5 17.4 22.4 31.6 21.9s26.4-10.4 29.8-24.2L233 288h46L321 455.8c3.4 13.8 15.6 23.7 29.8 24.2s27.1-8.4 31.6-21.9L439.1 288H480c17.7 0 32-14.3 32-32s-14.3-32-32-32H460.4l50-149.9c5.6-16.8-3.5-34.9-20.2-40.5s-34.9 3.5-40.5 20.2L392.9 224H329L287 56.2C283.5 42 270.7 32 256 32s-27.5 10-31 24.2L183 224h-64L62.4 53.9zm78 234.1H167l-11.4 45.6L140.4 288zM249 224l7-28.1 7 28.1H249zm96 64h26.6l-15.2 45.6L345 288z'/>
                   </svg>
                   ${val}</div>`;
          }
        }
      }"
      :buttons="[
        {
          headerText: '북마크',
          rendererType: 'button',
          type: 'onOff',
          icon: {
            on: 'fa-solid fa-bookmark',
            off: 'fa-regular fa-bookmark'
          },
          selectedData: bookmarkSelected,
          order: 2,
          fn: bookmarkClick
        },
        {
          headerText: '수정',
          rendererType: 'button',
          type: 'icon',
          icon: 'fa-regular fa-pen-to-square',
          order: 4,
          fn: editClick
        },
        {
          headerText: '삭제',
          rendererType: 'button',
          type: 'button',
          buttonText: '삭제',
          order: 5,
          fn: editClick
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

const bookmarkSelected = ref<Array<string | number>>([]);
bookmarkSelected.value = [1, 4, 5];

// cell click 동작 -> buttons 항목 및 checkbox 항목은 click 이벤트가 동작하지 않음.
const cellClicked = (params: object) => {
  console.log(params);
};
// row click 동작
const rowClicked = (params: object) => {
  console.log(params);
};

// 노드 '북마크' 버튼 클릭시 - buttons : bookmarkClick
const bookmarkClick = ({ data }: { data: { id: string | number } }) => {
  if (bookmarkSelected.value.includes(data.id)) {
    bookmarkSelected.value = bookmarkSelected.value.filter((item: any) => item !== data.id);
  } else {
    bookmarkSelected.value.push(data.id);
  }
};

// 노드 '수정' 버튼 클릭시 - buttons : editClick
const editClick = (param: object) => {
  console.log(param);
};

// checkbox 선택시 목록 리턴
const selectionChanged = (params: any[]) => {
  console.log(params);
};
</script>

<style lang="scss" scoped></style>
