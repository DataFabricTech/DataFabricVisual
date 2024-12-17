<template>
  <div class="overview">
    <div
      id="dynamicTooltip"
      class="dynamic-tooltip"
      v-show="isOpenAgHeaderTooltip"
    >
      <div v-if="agHeaderTooltipContents === 'event'">
        <p>등록: 수집 정보가 최초 등록된 경우</p>
        <p>편집: 수집 정보를 수정한 경우</p>
        <p>삭제: 수집 정보를 삭제한 경우</p>
        <p>현황변경: 동기화, 종료 등 기타 이벤트가 발생한 경우</p>
      </div>
      <div v-if="agHeaderTooltipContents === 'status'">
        <p>Queued: 수집 작업이 시작 대기 상태에 있음</p>
        <p>Running: 현재 수집 작업이 진행 중</p>
        <p>Success: 수집 작업이 성공적으로 완료됨</p>
        <p>Failed: 오류가 발생하여 작업이 완료되지 않음</p>
        <p>PartialSuccess: 일부는 성공적으로 수집되었으나, 일부는 실패함</p>
      </div>
    </div>
    <div class="v-group gap-5">
      <div class="h-group w-full">
        <h4 class="overview-title">요약 정보</h4>
        <div class="overview-info">수집 일시 : {{ collectedDateTime }}</div>
      </div>
      <div class="flex w-full gap-5">
        <div class="overview-summary">
          <span>서비스 타입 요약</span>
          <div class="no-result" v-if="_.isEmpty(serviceTypeData)">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 서비스가 없습니다.</p>
            </div>
          </div>
          <div v-else id="typeChart" style="width: 100%; height: 320px"></div>
        </div>
        <div class="overview-summary">
          <span>서비스 상태 요약</span>
          <div class="no-result" v-if="_.isEmpty(serviceStatusData)">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 서비스가 없습니다.</p>
            </div>
          </div>
          <div v-else id="statusChart" style="width: 100%; height: 320px"></div>
        </div>
        <div class="overview-summary" style="position: relative">
          <span>서비스 응답시간</span>
          <div class="no-result" v-show="_.isEmpty(serviceResponseData)">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 서비스가 없습니다.</p>
            </div>
          </div>
          <div
            v-show="serviceResponseData.length > 0"
            class="overview-chart p-3 overflow-y-auto"
            id="responseList"
          >
            <dl v-for="(item, index) in serviceResponseData" :key="index">
              <dt>{{ item.serviceName }}</dt>
              <dd>{{ item.queryExecutionTime }} sec</dd>
            </dl>
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
          <div class="no-result" v-if="_.isEmpty(currentSituationData)">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 서비스가 없습니다.</p>
            </div>
          </div>
          <div v-else class="overview-chart h-group gap-4 px-2">
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
      <div class="no-result" v-if="isEmptyServiceStatus">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 서비스가 없습니다.</p>
        </div>
      </div>
      <div v-else style="position: relative; width: 100%; height: 300px">
        <Loading
          id="collectorHistoryLoading"
          :use-loader-overlay="true"
          class="loader-lg is-loader-inner mt-[42px]"
          v-show="showServiceStatusLoading"
        ></Loading>
        <agGrid
          :style="'width: 100%; height: 300px'"
          class="ag-theme-alpine ag-theme-quartz"
          :columnDefs="serviceColumnDefs"
          :rowModelType="rowModelType"
          :cacheBlockSize="cacheBlockSize"
          :infiniteInitialRowCount="infiniteInitialRowCount"
          @grid-ready="onServiceStatusGrid"
          @cellClicked="onCellClicked"
          :setColumnFit="true"
          :useColumnResize="true"
          rowId="id"
        ></agGrid>
      </div>
    </div>
    <div class="v-group gap-5">
      <h4 class="overview-title">수집 히스토리</h4>
      <div class="no-result" v-if="isEmptyCollectorHistory">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 서비스가 없습니다.</p>
        </div>
      </div>
      <div v-else style="position: relative; width: 100%; height: 300px">
        <Loading
          id="collectorHistoryLoading"
          :use-loader-overlay="true"
          class="loader-lg is-loader-inner mt-[42px]"
          v-show="showCollectorHistoryLoading"
        ></Loading>
        <agGrid
          :style="'width: 100%; height: 300px'"
          class="ag-theme-alpine ag-theme-quartz"
          :columnDefs="historyColumnDefs"
          :rowModelType="rowModelType"
          :cacheBlockSize="cacheBlockSize"
          :infiniteInitialRowCount="infiniteInitialRowCount"
          @grid-ready="onCollectorHistoryGrid"
          @cellClicked="onCellClicked"
          :setColumnFit="true"
          :useColumnResize="true"
        ></agGrid>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import * as echarts from "echarts";
import { useRouter } from "nuxt/app";
import { onMounted, ref, watch, defineEmits } from "vue";
import { storeToRefs } from "pinia";
import agGrid from "@extends/grid/Grid.vue";
import Loading from "@base/loading/Loading.vue";
import { useOverviewStore } from "~/store/manage/service/overview";
import { useServiceStore } from "@/store/manage/service";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import HeaderTooltipStatus from "~/components/manage/service/ag-grid/header-tooltip-status.vue";
import HeaderTooltipEvent from "~/components/manage/service/ag-grid/header-tooltip-event.vue";
import _ from "lodash";

const router = useRouter();

const overviewStore = useOverviewStore();
const {
  getServiceTypeData,
  getServiceStatusData,
  getServiceResponseData,
  getDataCurrentSituationData,
  addServiceResponseData,
  onCollectorHistoryGrid,
  onServiceStatusGrid,
} = overviewStore;
const {
  serviceTypeData,
  serviceStatusData,
  serviceResponseData,
  currentSituationData,
  isOpenAgHeaderTooltip,
  agHeaderTooltipContents,
  agHeaderCoordinates,
  collectedDateTime,
  slicedCurrentSituationData,
  showCollectorHistoryLoading,
  showServiceStatusLoading,
  isEmptyCollectorHistory,
  isEmptyServiceStatus,
} = storeToRefs(overviewStore);

const serviceStore = useServiceStore();
const { changeCurrentService } = serviceStore;
const { serviceList } = storeToRefs(serviceStore);
// Dynamic Tooltip
watch(
  () => agHeaderCoordinates.value,
  async (newVal) => {
    await nextTick();

    const dynamicTooltipDOM = document.getElementById(
      "dynamicTooltip",
    ) as HTMLElement;

    if (dynamicTooltipDOM) {
      dynamicTooltipDOM.style.left = `${agHeaderCoordinates.value.x}px`;
      dynamicTooltipDOM.style.top = `${agHeaderCoordinates.value.y}px`;
    }
  },
  { immediate: true },
);

// Common
const setOverviewData = async () => {
  await getServiceTypeData();
  await getServiceStatusData();
  await getServiceResponseData();
  await getDataCurrentSituationData();
};

// Ag-grid
// 무한 스크롤 방식을 사용하도록 설정
const rowModelType = ref("infinite");
// 서버에서 한 번에 가져올 데이터 블록 크기 설정
const cacheBlockSize = ref(20);
// 무한 스크롤 시 첫 화면에 표시될 행의 초기 개수
const infiniteInitialRowCount = ref(20);

const emit = defineEmits<{ (e: "change", value: string): void }>();

const onCellClicked = (params: any) => {
  const id: string = params.data.serviceId;
  emit("change", "service");
  changeService(id);
  router.push(`/portal/manage/service?id=${id}`);
};

function changeService(id: string) {
  const foundService = _.find(serviceList.value, { id: id });
  changeCurrentService(foundService);
}

const getBadgeClass = (status: string) => {
  let badgeTheme = "";
  switch (status) {
    case "success":
      badgeTheme = "badge-green-lighter";
      break;
    case "running":
      badgeTheme = "badge-blue-lighter";
      break;
    case "queued":
      badgeTheme = "badge-gray-lighter";
      break;
    case "failed":
      badgeTheme = "badge-red-lighter";
      break;
    case "partialSuccess":
      badgeTheme = "badge-yellow-lighter";
      break;
    default:
      badgeTheme = "badge";
  }

  return badgeTheme;
};

const setBadgeStatus = (params: string) => {
  const badgeClass = getBadgeClass(params);

  return `<div class="badge ${badgeClass} ag-cell-badge"><p class="badge-text">${params ? params : ""}</p></div>`;
};

const serviceColumnDefs = ref([
  // [TODO] 개발: 추후 field: serviceNameFormatted 으로 변경 필요 (데이터 상세 이동하는 api 만들어지면 적용할 것)
  {
    headerName: "서비스 이름",
    headerClass: "ag-header-center",
    field: "serviceName",
    cellStyle: {
      textAlign: "center",
    },
  },
  {
    headerName: "데이터 저장소 유형",
    headerClass: "ag-header-center",
    field: "serviceType",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "상태",
    headerClass: "ag-header-center",
    field: "connectionStatus",
    cellStyle: { textAlign: "center" },
  },
]);

const historyColumnDefs = ref([
  {
    headerName: "이벤트 발생 일시",
    headerClass: "ag-header-center",
    field: "eventAtFormatted",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "수집 이름",
    headerClass: "ag-header-center",
    field: "ingestionName",
    cellStyle: { textAlign: "center" },
  },
  {
    headerName: "유형",
    headerClass: "ag-header-center",
    field: "type",
    cellStyle: { textAlign: "center" },
  },
  {
    headerComponent: HeaderTooltipEvent,
    headerClass: "ag-header-center",
    field: "eventFormatted",
    cellStyle: { textAlign: "center" },
  },
  {
    headerComponent: HeaderTooltipStatus,
    headerClass: "ag-header-center",
    field: "statusFormatted",
    cellRenderer: (params: any) => setBadgeStatus(params.value),
    cellStyle: {
      textAlign: "center",
    },
  },
  // [TODO] 개발: 추후 field: serviceNameFormatted 으로 변경 필요 (데이터 상세 이동하는 api 만들어지면 적용할 것)
  {
    headerName: "서비스 이름",
    headerClass: "ag-header-center",
    field: "serviceName",
    cellStyle: {
      textAlign: "center",
    },
  },
  {
    headerName: "저장소 유형",
    headerClass: "ag-header-center",
    field: "dbType",
    cellStyle: { textAlign: "center" },
  },
]);
// ECharts
let currentSituationChart: echarts.ECharts | null = null;

const setECharts = () => {
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
  if (currentSituationChart) {
    currentSituationChart.setOption({
      dataset: {
        source: currentSituationData.value, // 업데이트된 데이터
      },
    });
  }
};

// 등록된 데이터 모델 현황
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

const checkNextDisabled = () => {
  currentSituationData.value = [];
  slicedCurrentSituationData.value = [];

  if (currentSituationData.value.length + 1 < DEFAULT_COUNT) {
    isNextDisabled.value = true;
  } else {
    isNextDisabled.value = false;
  }
};

onMounted(async () => {
  checkNextDisabled();
  await setOverviewData();
  setECharts();
});

const { scrollTrigger } = useIntersectionObserver({
  callback: addServiceResponseData,
  targetId: "responseList",
  loaderId: "responseListLoader",
});
</script>
