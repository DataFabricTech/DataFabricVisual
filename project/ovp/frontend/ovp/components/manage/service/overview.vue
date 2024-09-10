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
          <span>데이터 모델 추천 순위</span>
          <div id="recommendChart" style="width: 100%; height: 320px"></div>
        </div>
        <div class="overview-summary">
          <span>데이터 모델 등록 현황</span>
          <div id="registerChart" style="width: 100%; height: 320px"></div>
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
  getRecommendApi,
  getResponseApi,
  getRegisterApi,
  getServiceApi,
  getHistoryApi,
} = overviewStore;
const {
  typeData,
  statusData,
  recommendData,
  responseData,
  registerData,
  serviceData,
  historyData,
} = storeToRefs(overviewStore);

// Common
const setOverviewData = () => {
  console.log("1. setOverviewData 실행");
  getTypeApi();
  getStatusApi();
  getRecommendApi();
  getResponseApi();
  getRegisterApi();
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
  const recommendChartDOM = document.getElementById(
    "recommendChart",
  ) as HTMLElement;
  const registerChartDOM = document.getElementById(
    "registerChart",
  ) as HTMLElement;

  // 차트가 이미 초기화되어 있는지 확인하고, 존재하면 dispose()로 기존 인스턴스 파괴 후, 재 생성
  if (echarts.getInstanceByDom(typeChartDOM)) {
    echarts.dispose(typeChartDOM);
  }
  if (echarts.getInstanceByDom(statusChartDOM)) {
    echarts.dispose(statusChartDOM);
  }
  if (echarts.getInstanceByDom(recommendChartDOM)) {
    echarts.dispose(recommendChartDOM);
  }
  if (echarts.getInstanceByDom(registerChartDOM)) {
    echarts.dispose(registerChartDOM);
  }

  const typeChart = echarts.init(typeChartDOM);
  const statusChart = echarts.init(statusChartDOM);
  const recommendChart = echarts.init(recommendChartDOM);
  const registerChart = echarts.init(registerChartDOM);

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
  recommendChart.setOption({
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    xAxis: {
      type: "category",
      data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: recommendData.value,
        type: "bar",
      },
    ],
  });
  registerChart.setOption({
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    legend: {},
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true,
    },
    xAxis: [
      {
        type: "category",
        data: ["A", "B", "C", "D", "E", "F", "G"],
      },
    ],
    yAxis: [
      {
        type: "value",
      },
    ],
    series: [
      {
        name: "전체 데이터",
        type: "bar",
        stack: "Ad",
        emphasis: {
          focus: "series",
        },
        data: [120, 132, 101, 134, 90, 230, 210],
      },
      {
        name: "데이터 모델로 등록된 데이터",
        type: "bar",
        stack: "Ad",
        emphasis: {
          focus: "series",
        },
        data: [220, 182, 191, 234, 290, 330, 310],
      },
    ],
  });
};

onMounted(() => {
  setOverviewData();
  setECharts();
});
</script>
