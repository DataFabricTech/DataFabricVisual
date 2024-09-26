<template>
  <div class="no-result" v-if="profileList.length === 0">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 정보가 없습니다.</p>
    </div>
  </div>
  <div class="data-detail" v-else>
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
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";
import type { ColDef } from "ag-grid-community";
import { useDataModelDetailStore } from "@/store/search/detail/index";
import $constants from "~/utils/constant";

const dataModelDetailStore = useDataModelDetailStore();

const { profileList } = storeToRefs(dataModelDetailStore);

const keyword = ref("");
const clearInput = (): void => {
  keyword.value = "";
};

const COLUMN_DEFS: ColDef[] = $constants.COMMON.DATA_PROFILE_COLUMN;
</script>
<style lang="scss" scoped></style>
