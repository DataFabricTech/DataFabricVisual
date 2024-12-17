<template>
  <div class="no-result" v-if="checkEmpty">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">{{ props.noDataMsg }}</p>
    </div>
  </div>
  <template v-else>
    <div class="data-summary">
      <span class="data-summary-title">데이터 모델 요약</span>
      <span class="data-summary-name">{{ props.data.fqn }}</span>
      <span class="data-summary-text">{{ owner }}</span>
    </div>
    <div class="ag-grid">
      <agGrid
        :style="'height: 100%'"
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="props.data.columnDefs"
        :rowData="props.data.rowData"
        :fqn="props.data.fqn"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
        :column-render="props.data.columnOptions"
      ></agGrid>
    </div>
  </template>
</template>

<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";

const props = defineProps({
  data: {
    type: Object,
    default: {
      columnOptions: {},
      columnDefs: [],
      rowData: [],
      fqn: "",
    },
  },
  owner: {
    type: String,
  },
  noDataMsg: {
    type: String,
    default: "데이터가 없습니다.",
  },
});

const owner = computed(() => {
  const nOwner = props.owner ? props.owner : "-";
  return `(${nOwner})`;
});

const checkEmpty = computed(() => {
  return $_isEmpty(props.data);
});
</script>

<style scoped></style>
