<template>
  <div class="layout-guide">
    <div class="layout-guide-title">
      <div class="inner">
        <h1>Fabric UI Components</h1>
      </div>
    </div>
    <div class="guide-group">
      <div class="inner">
        <table class="guide-table">
          <tbody>
          <tr>
            <th scope="row">Context</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <div class="h-group gap-12">
                    <baseContext class="context-lg w-[180px]"></baseContext>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <div class="h-group gap-12">
                    <baseContext class="w-[180px]"></baseContext>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <div class="h-group gap-12">
                    <baseContext class="context-sm w-[180px]"></baseContext>
                  </div>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Select</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-4">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px]">size: lg</strong>
                  <baseSelect class="select-lg is-open"></baseSelect>
                  <baseSelect class="select-lg is-disabled"></baseSelect>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px]">size: default</strong>
                  <baseSelect class=""></baseSelect>
                  <baseSelect class="is-disabled"></baseSelect>
                  <baseSelectSearch></baseSelectSearch>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px]">size: sm</strong>
                  <baseSelect class="select-sm"></baseSelect>
                  <baseSelect class="select-sm is-disabled"></baseSelect>
                  <baseSelect class="select-sm is-open"></baseSelect>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px]">select-checkbox</strong>
                  <baseSelectCheckbox class="select-lg is-open"></baseSelectCheckbox>
                  <baseSelectCheckbox class="is-open"></baseSelectCheckbox>
                  <baseSelectCheckbox class="select-sm is-open"></baseSelectCheckbox>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">List</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: Checkable List - title</strong>
                  <baseList class="w-[300px]">
                    <template v-slot:head>
                      <div class="list-head">
                        <div class="list-head-title">
                          title
                        </div>
                      </div>
                    </template>
                  </baseList>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: Checkable List - no title</strong>
                  <baseList class="w-[300px]"> </baseList>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="guide-group">
      <div class="inner">
        <table class="guide-table">
          <tbody>
          <tr>
            <th scope="row">Kebab-menu</th>
          </tr>
          <tr>
            <td>
              <KebabMenu></KebabMenu>
            </td>
          </tr>

          <tr>
            <th scope="row">Search filter</th>
          </tr>
          <tr>
            <td>
              <SearchFilter></SearchFilter>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <modal v-model="show" title="Modal Sample">
    <template v-slot:body>
      ~ Modal is Here! Modal is Here! ~
    </template>
  </modal>
</template>
<script lang="ts" setup>
import { ref } from "@vue/reactivity";
import { useRouter } from "vue-router";
import { sample } from "../composables/sample";
import { AgGridVue } from "ag-grid-vue3";

const sampleData = sample();
const show = ref(false);
const router = useRouter();
const gridOptions = {
  headerHeight: 56,
  rowHeight: 56,
  // sort 설정 시 추가할 것
  unSortIcon: true
};

// sort 예시 추가
const columnDefs = [
  { headerName: "Make", field: "make", sortable: true },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price", sortable: true }
];
const rowData = [
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxster", price: 72000 }
];

function onClickOpen() {
  show.value = !show.value;
}

function onClickMain() {
  router.push("/main");
}

</script>

<style lang="scss">
@import "component-ui";
</style>
