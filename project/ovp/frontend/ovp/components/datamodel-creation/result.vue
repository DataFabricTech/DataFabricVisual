<template>
  <div class="work-page">
    <div class="l-top-bar py-3">
      <div class="h-group gap-2">
        <span class="font-semibold">실행 결과</span>
        <div
          :class="props.querySuccess ? 'badge badge-green' : 'badge badge-red'"
          v-show="props.isFirstExecute"
        >
          <svg-icon
            class="badge-icon"
            :name="props.querySuccess ? 'success' : 'error'"
          ></svg-icon>
          <p class="badge-text">
            {{ props.querySuccess ? "success" : "error" }}
          </p>
        </div>
      </div>
      <div class="result-info">
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시간</p>
            </div>
          </dt>
          <dd v-if="props.querySuccess && props.isFirstExecute">
            {{ props.executeResult.runTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시각</p>
            </div>
          </dt>
          <dd v-if="props.querySuccess && props.isFirstExecute">
            {{ props.executeResult.startTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">레코드 수</p>
            </div>
          </dt>
          <dd v-if="props.querySuccess && props.isFirstExecute">
            {{ props.executeResult.totalRows }}
          </dd>
        </dl>
      </div>
    </div>
    <div class="result" v-if="props.querySuccess && props.isFirstExecute">
      <agGrid
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="props.executeResult.columnDefs"
        :rowData="props.executeResult.rowData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
    <div
      class="result result-error"
      v-if="!props.querySuccess && props.isFirstExecute"
    >
      <p>
        {{ props.executeResultErrMsg }}
      </p>
    </div>
  </div>
</template>
<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";

const props = defineProps({
  querySuccess: {
    type: Boolean,
    default: false,
  },
  executeResult: {
    type: Object,
    default: [],
  },
  isFirstExecute: {
    type: Boolean,
    default: false,
  },
  executeResultErrMsg: {
    type: String,
    default: "",
  },
});
</script>
<style lang="scss" scoped></style>
