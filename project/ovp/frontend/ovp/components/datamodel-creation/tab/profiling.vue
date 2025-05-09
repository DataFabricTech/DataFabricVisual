<template>
  <div class="data-detail" v-if="profileList.length > 0">
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
          :column-render="$constants.COMMON.DATA_PROFILE_RENDER"
        >
        </agGrid>
      </div>
    </div>
  </div>
  <div class="no-result" v-else-if="isItemClicked">
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
import $constants from "~/utils/constant";
import type { ColDef } from "ag-grid-community";

const creationStore = useCreationStore();
const { isItemClicked } = storeToRefs(creationStore);

const dataModelDetailStore = useDataModelDetailStore();
const { profileList } = storeToRefs(dataModelDetailStore);

const COLUMN_DEFS: ColDef[] = $constants.COMMON.DATA_PROFILE_COLUMN;

const keyword = ref("");
const clearInput = (): void => {
  keyword.value = "";
};
</script>

<style scoped></style>
