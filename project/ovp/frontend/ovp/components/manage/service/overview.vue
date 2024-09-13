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
              id="responseListLoader"
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
              @click="showPrevData"
              :disabled="isPrevDisabled"
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
              @click="showNextData"
              :disabled="isNextDisabled"
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
      <h4 class="overview-title">서비스 상태</h4>
      <agGrid
        :style="'width: 100%; height: 300px'"
        class="ag-theme-alpine ag-theme-quartz"
        :columnDefs="serviceColumnDefs"
        :rowData="statusDetailData"
        rowId="id"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
    </div>
    <div class="v-group gap-5">
      <h4 class="overview-title">수집히스토리</h4>
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
import { onMounted, ref } from "vue";
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";
import Loading from "@base/loading/Loading.vue";
import { useOverviewStore } from "~/store/manage/service/overview";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { ServiceNameRenderer } from "~/store/manage/service/overview/cell-renderer/serviceNameRenderer";
import { HistoryServiceNameRenderer } from "~/store/manage/service/overview/cell-renderer/historyServiceNameRenderer";
import { HistoryServiceStatusRenderer } from "~/store/manage/service/overview/cell-renderer/historyServiceStatusRenderer";
import HeaderTooltip from "~/components/manage/service/ag-grid/header-tooltip.vue";

const overviewStore = useOverviewStore();
const {
  getServiceTypeData,
  getServiceStatusData,
  getServiceResponseData,
  getDataCurrentSituationData,
  getStatusDetailData,
  getHistoryData,
  addServiceResponseData,
} = overviewStore;
const {
  serviceTypeData,
  serviceStatusData,
  serviceResponseData,
  currentSituationData,
  statusDetailData,
  historyData,
} = storeToRefs(overviewStore);

// Common
const setOverviewData = () => {
  console.log("1. setOverviewData 실행");
  getServiceTypeData();
  getServiceStatusData();
  getServiceResponseData();
  getDataCurrentSituationData();
  getStatusDetailData();
  getHistoryData();
};

// Ag-grid
const serviceColumnDefs = ref([
  {
    headerName: "서비스 이름",
    headerClass: "ag-header-center",
    field: "name",
    cellRenderer: ServiceNameRenderer,
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "데이터 저장소 유형",
    headerClass: "ag-header-center",
    field: "type",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "상태",
    headerClass: "ag-header-center",
    field: "status",
    cellStyle: { textAlign: "center" },
  },
]);

const historyColumnDefs = ref([
  {
    headerName: "이벤트 발생 일시",
    headerClass: "ag-header-center",
    field: "date",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "수집 이름",
    headerClass: "ag-header-center",
    field: "collectionName",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "이벤트",
    headerClass: "ag-header-center",
    field: "event",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: HeaderTooltip,
    headerClass: "ag-header-center",
    field: "status",
    cellRenderer: HistoryServiceStatusRenderer,
    cellStyle: {
      textAlign: "center",
    },
  },
  {
    headerName: "서비스 이름",
    headerClass: "ag-header-center",
    field: "name",
    cellRenderer: HistoryServiceNameRenderer,
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "저장소 유형",
    headerClass: "ag-header-center",
    field: "type",
    cellStyle: { textAlign: "center" },
  },
]);

// ECharts
let currentSituationChart: echarts.ECharts | null = null;

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
  currentSituationChart = echarts.init(currentSituationChartDOM);

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
      dimensions: ["data", "전체 데이터", "등록된 데이터 모델"],
      source: currentSituationData.value,
    },
    xAxis: { type: "category" },
    yAxis: {},
    // Declare several bar series, each will be mapped
    // to a column of dataset.source by default.
    series: [{ type: "bar" }, { type: "bar" }],
  });
};

const updateCurrentSituationChart = () => {
  console.log("update! ", currentSituationData.value);
  if (currentSituationChart) {
    currentSituationChart.setOption({
      dataset: {
        source: currentSituationData.value, // 업데이트된 데이터
      },
    });
  }
};

// Data output by number
const DEFAULT_COUNT = 5;
const startStandard: Ref<number> = ref(0);
const isPrevDisabled: Ref<boolean> = ref(true);
const isNextDisabled: Ref<boolean> = ref(false);

const showPrevData = () => {
  startStandard.value -= DEFAULT_COUNT;

  getDataCurrentSituationData(startStandard.value, DEFAULT_COUNT);
  updateCurrentSituationChart();

  if (startStandard.value === 0) {
    isPrevDisabled.value = true;
    return;
  }

  if (currentSituationData.value.length === DEFAULT_COUNT) {
    isNextDisabled.value = false;
  }
};

const showNextData = () => {
  startStandard.value += DEFAULT_COUNT;

  if (startStandard.value === DEFAULT_COUNT) {
    isPrevDisabled.value = false;
  }

  getDataCurrentSituationData(startStandard.value, DEFAULT_COUNT);
  updateCurrentSituationChart();

  if (currentSituationData.value.length < DEFAULT_COUNT) {
    isNextDisabled.value = true;
    return;
  }
};

onMounted(async () => {
  await setOverviewData();
  setECharts();

  // 등록된 데이터 모델 현황의 총 개수가 DEFAULT_COUNT 보다 적을 경우 '다음버튼' 비활성화
  if (currentSituationData.value.length + 1 < DEFAULT_COUNT) {
    isNextDisabled.value = true;
  }
});

const { scrollTrigger } = useIntersectionObserver({
  callback: addServiceResponseData,
  targetId: "responseList",
  loaderId: "responseListLoader",
});
</script>
