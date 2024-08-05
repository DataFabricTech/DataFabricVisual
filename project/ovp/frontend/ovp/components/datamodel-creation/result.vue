<template>
  <div class="work-page">
    <div class="l-top-bar py-3">
      <div class="h-group gap-2">
        <span class="font-semibold">실행 결과</span>
        <div
          :class="localQuerySuccess ? 'badge badge-green' : 'badge badge-red'"
          v-show="localIsFirstExecute"
        >
          <svg-icon
            class="badge-icon"
            :name="localQuerySuccess ? 'success' : 'error'"
          ></svg-icon>
          <p class="badge-text">
            {{ localQuerySuccess ? "success" : "error" }}
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
          <dd v-if="localQuerySuccess && localIsFirstExecute">
            {{ localExecuteResult.runTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시각</p>
            </div>
          </dt>
          <dd v-if="localQuerySuccess && localIsFirstExecute">
            {{ localExecuteResult.startTime }}
          </dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">레코드 수</p>
            </div>
          </dt>
          <dd v-if="localQuerySuccess && localIsFirstExecute">
            {{ localExecuteResult.totalRows }}
          </dd>
        </dl>
      </div>
    </div>
    <div class="result" v-if="localQuerySuccess && localIsFirstExecute">
      <agGrid
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="localExecuteResult.columnDefs"
        :rowData="localExecuteResult.rowData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
    <div
      class="result result-error"
      v-if="!localQuerySuccess && localIsFirstExecute"
    >
      <p>
        {{ localExecuteResultErrMsg }}
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

const localQuerySuccess = ref(props.querySuccess);
const localExecuteResult = ref(props.executeResult);
const localIsFirstExecute = ref(props.isFirstExecute);
const localExecuteResultErrMsg = ref(props.executeResultErrMsg);

watch(
  () => ({
    querySuccess: props.querySuccess,
    executeResult: props.executeResult,
    isFirstExecute: props.isFirstExecute,
    executeResultErrMsg: props.executeResultErrMsg,
  }),
  (newProps) => {
    localQuerySuccess.value = newProps.querySuccess;
    localExecuteResult.value = newProps.executeResult;
    localIsFirstExecute.value = newProps.isFirstExecute;
    localExecuteResultErrMsg.value = newProps.executeResultErrMsg;
  },
);
</script>
<style lang="scss" scoped></style>
