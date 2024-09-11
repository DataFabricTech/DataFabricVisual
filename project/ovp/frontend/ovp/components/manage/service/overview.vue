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
        <div class="overview-summary" style="position: relative">
          <span>서비스 응답시간</span>
          <div class="overview-chart p-3 overflow-y-auto" id="responseList">
            <dl v-for="(item, index) in serviceResponseData" :key="index">
              <dt>{{ item.name }}</dt>
              <dd>{{ item.value }}</dd>
            </dl>
            <!-- NOTE "scrollTrigger" -> useIntersectionObserver 가 return 하는 변수병과 동일해야함. -->
            <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
            <Loading
              id="loader"
              :use-loader-overlay="true"
              class="loader-lg is-loader-inner"
              style="display: none"
            ></Loading>
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
        :rowData="recentServiceData"
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
import { storeToRefs } from "pinia";
import { useOverviewStore } from "~/store/manage/service/overview";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import agGrid from "@extends/grid/Grid.vue";
import Loading from "@base/loading/Loading.vue";
import { ServiceNameRenderer } from "~/store/manage/service/overview/cell-renderer/serviceNameRenderer";
import { HistoryServiceNameRenderer } from "~/store/manage/service/overview/cell-renderer/historyServiceNameRenderer";

const overviewStore = useOverviewStore();
const {
  getServiceTypeData,
  getServiceStatusData,
  getServiceResponseData,
  getDataCurrentSituationData,
  getRecentServiceData,
  getHistoryData,
  addServiceResponseData,
} = overviewStore;
const {
  serviceTypeData,
  serviceStatusData,
  serviceResponseData,
  currentSituationData,
  recentServiceData,
  historyData,
} = storeToRefs(overviewStore);

// Common
const setOverviewData = () => {
  console.log("1. setOverviewData 실행");
  getServiceTypeData();
  getServiceStatusData();
  getServiceResponseData();
  getDataCurrentSituationData();
  getRecentServiceData();
  getHistoryData();
};

// Ag-grid
const serviceColumnDefs = ref([
  {
    headerName: "서비스 이름",
    field: "name",
    cellRenderer: ServiceNameRenderer,
  },
  { headerName: "데이터 저장소 유형", field: "type" },
  { headerName: "상태", field: "status" },
  { headerName: "등록일시", field: "register" },
  { headerName: "수정일시", field: "modification" },
  { headerName: "내용", field: "detail" },
]);

const historyColumnDefs = ref([
  { headerName: "이벤트 발생 일시", field: "date" },
  { headerName: "이벤트", field: "event" },
  {
    headerName: "서비스 이름",
    field: "name",
    cellRenderer: HistoryServiceNameRenderer,
  },
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
        data: serviceTypeData.value,
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
        data: serviceStatusData.value,
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

const { scrollTrigger } = useIntersectionObserver(addServiceResponseData);
</script>
