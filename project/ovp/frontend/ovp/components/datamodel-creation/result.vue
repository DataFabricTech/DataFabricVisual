<template>
  <div class="work-page">
    <div class="l-top-bar py-3">
      <div class="h-group gap-2">
        <span class="font-semibold">실행 결과</span>
        <div
          :class="querySuccess ? 'badge badge-green' : 'badge badge-red'"
          v-show="isFirstExcute"
        >
          <svg-icon
            class="badge-icon"
            :name="querySuccess ? 'success' : 'error'"
          ></svg-icon>
          <p class="badge-text">{{ querySuccess ? "success" : "error" }}</p>
        </div>
      </div>
      <div class="result-info">
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시간</p>
            </div>
          </dt>
          <dd v-if="querySuccess && isFirstExcute">
            {{ excuteResult.runTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시각</p>
            </div>
          </dt>
          <dd v-if="querySuccess && isFirstExcute">
            {{ excuteResult.startTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">레코드 수</p>
            </div>
          </dt>
          <dd v-if="querySuccess && isFirstExcute">
            {{ excuteResult.totalRows }}
          </dd>
        </dl>
      </div>
    </div>
    <div class="result" v-if="querySuccess && isFirstExcute">
      <agGrid
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="excuteResult.columnDefs"
        :rowData="excuteResult.rowData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
    <div class="result result-error" v-if="!querySuccess && isFirstExcute">
      <p>
        {{ excuteResultErrMsg }}
      </p>
    </div>
  </div>
</template>
<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";
import { useCreationStore } from "@/store/datamodel-creation/index";

const store = useCreationStore();
const {
  querySuccess,
  excuteResult,
  isExcuteQuery,
  isFirstExcute,
  query,
  excuteResultErrMsg,
} = storeToRefs(store);
</script>
<style lang="scss" scoped></style>
