<template>
  <div class="overview">
    <div class="v-group gap-5">
      <h4 class="overview-title">요약 정보</h4>
      <div class="flex w-full gap-5">
        <div class="overview-summary">
          <span>서비스 타입 요약</span>
          <div id="typeChart" style="width: 100%; height: 320px"></div>
        </div>
        <div class="overview-summary">
          <span>서비스 상태 요약</span>
          <div id="statusChart" style="width: 100%; height: 320px"></div>
        </div>
        <div class="overview-summary">
          <span>서비스 응답시간</span>
          <div class="overview-chart p-3">
            <dl v-for="(item, index) in responseData" :key="index">
              <dt>{{ item.name }}</dt>
              <dd>{{ item.value }}</dd>
            </dl>
          </div>
        </div>
      </div>
      <div class="flex w-full gap-5">
        <div class="overview-summary">
          <span>등록된 데이터 모델 현황</span>
          <div class="overview-chart h-group gap-4 px-2">
            <button
              class="button button-neutral-stroke button-lg"
              type="button"
              disabled
            >
              <span class="hidden-text">이전 내용으로 넘기기</span>
              <svg-icon
                class="button-icon"
                name="chevron-left-medium"
              ></svg-icon>
            </button>
            <div
              id="currentSituationChart"
              style="width: 100%; height: 320px"
            ></div>
            <button
              class="button button-neutral-stroke button-lg"
              type="button"
            >
              <span class="hidden-text">다음 내용으로 넘기기</span>
              <svg-icon
                class="button-icon"
                name="chevron-right-medium"
              ></svg-icon>
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="v-group gap-5">
      <h4 class="overview-title">최근 등록/수정된 서비스</h4>
      <agGrid
        :style="'width: 100%; height: 300px'"
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="serviceColumnDefs"
        :rowData="serviceData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
    <div class="v-group gap-5">
      <h4 class="overview-title">히스토리</h4>
      <agGrid
        :style="'width: 100%; height: 300px'"
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="historyColumnDefs"
        :rowData="historyData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
  </div>
</template>
<script setup lang="ts">
import * as echarts from "echarts";
import { onMounted } from "vue";
import { useOverviewStore } from "~/store/manage/service/overview";
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";

const overviewStore = useOverviewStore();
const {
  getTypeApi,
  getStatusApi,
  getResponseApi,
  getCurrentSituationApi,
  getServiceApi,
  getHistoryApi,
} = overviewStore;
const {
  typeData,
  statusData,
  responseData,
  currentSituationData,
  serviceData,
  historyData,
} = storeToRefs(overviewStore);

// Common
const setOverviewData = () => {
  console.log("1. setOverviewData 실행");
  getTypeApi();
  getStatusApi();
  getResponseApi();
  getCurrentSituationApi();
  getServiceApi();
  getHistoryApi();
};

// Ag-grid
const serviceColumnDefs = ref([
  { headerName: "서비스 이름", field: "name" },
  { headerName: "데이터 저장소 유형", field: "type" },
  { headerName: "상태", field: "status" },
  { headerName: "등록일시", field: "register" },
  { headerName: "수정일시", field: "modification" },
  { headerName: "내용", field: "detail" },
]);

const historyColumnDefs = ref([
  { headerName: "이벤트 발생 일시", field: "date" },
  { headerName: "이벤트", field: "event" },
  { headerName: "서비스 이름", field: "name" },
  { headerName: "저장소 유형", field: "type" },
  { headerName: "내용", field: "detail" },
]);

// ECharts
const setECharts = () => {
  console.log("2. setECharts 실행");
  const typeChartDOM = document.getElementById("typeChart") as HTMLElement;
  const statusChartDOM = document.getElementById("statusChart") as HTMLElement;
  const currentSituationChartDOM = document.getElementById(
    "currentSituationChart",
  ) as HTMLElement;

  // 차트가 이미 초기화되어 있는지 확인하고, 존재하면 dispose()로 기존 인스턴스 파괴 후, 재 생성
  if (echarts.getInstanceByDom(typeChartDOM)) {
    echarts.dispose(typeChartDOM);
  }
  if (echarts.getInstanceByDom(statusChartDOM)) {
    echarts.dispose(statusChartDOM);
  }
  if (echarts.getInstanceByDom(currentSituationChartDOM)) {
    echarts.dispose(currentSituationChartDOM);
  }

  const typeChart = echarts.init(typeChartDOM);
  const statusChart = echarts.init(statusChartDOM);
  const currentSituationChart = echarts.init(currentSituationChartDOM);

  typeChart.setOption({
    tooltip: {
      trigger: "item",
    },
    legend: {
      top: "5%",
      left: "center",
    },
    series: [
      {
        name: "Access From",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: "center",
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: typeData.value,
      },
    ],
  });
  statusChart.setOption({
    tooltip: {
      trigger: "item",
    },
    legend: {
      top: "5%",
      left: "center",
    },
    series: [
      {
        name: "Access From",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: "center",
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: statusData.value,
      },
    ],
  });
  currentSituationChart.setOption({
    legend: {},
    tooltip: {},
    dataset: {
      source: [
        ["product", "전체 데이터", "등록된 데이터 모델"],
        ["서비스 A", 43.3, 85.8],
        ["서비스 B", 83.1, 73.4],
        ["서비스 C", 86.4, 65.2],
        ["서비스 D", 72.4, 53.9],
        ["서비스 E", 100, 89],
      ],
    },
    xAxis: { type: "category" },
    yAxis: {},
    // Declare several bar series, each will be mapped
    // to a column of dataset.source by default.
    series: [{ type: "bar" }, { type: "bar" }],
  });
};

onMounted(() => {
  setOverviewData();
  setECharts();
});
</script>
